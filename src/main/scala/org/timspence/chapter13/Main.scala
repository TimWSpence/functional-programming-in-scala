package org.timspence.chapter13

import cats.Monad

object Main {

}

sealed trait Free[F[_],A] {

  def flatMap[B](f: A => Free[F, B]): Free[F, B] = FlatMap(this, f)

  def map[B](f:A => B): Free[F, B] = flatMap(x => Return(f(x)))

}
case class Return[F[_],A](a: A) extends Free[F,A]
case class Suspend[F[_],A](s: F[A]) extends Free[F,A]
case class FlatMap[F[_],A,B](s: Free[F,A], f: A => Free[F,B]) extends Free[F,B]

object Free {

  def freeMonad[F[_]]: Monad[({type f[a] = Free[F,a]})#f] = ???

  @annotation.tailrec
  def runTrampoline[A](a: Free[Function0,A]): A = a match {
    case Return(a) => a
    case Suspend(fa) => fa()
    case FlatMap(s, f) => s match {
      case Return(a) => runTrampoline(f(a))
      case Suspend(fa) => runTrampoline(f(fa()))
      case FlatMap(y, g) => runTrampoline(y flatMap(t => g(t).flatMap(f)))
    }
  }

  def run[F[_],A](a: Free[F,A])(implicit F: Monad[F]): F[A] = step(a) match {
    case Return(a) => F.pure(a)
    case Suspend(fa) => fa
    case FlatMap(Suspend(fa), f) => F.flatMap(fa)(a => run(f(a)))
    case _ => throw new RuntimeException("Impossible")
  }

  @annotation.tailrec
  def step[F[_], A](a: Free[F, A]): Free[F, A] = a match {
    case FlatMap(FlatMap(y, g), f) => step(g(y) flatMap(f))
    case FlatMap(Return(x), f) => step(f(x))
    case _ => a
  }

}

