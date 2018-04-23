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
  def runTrampoline[A](a: Free[Function0,A]): A = runTrampoline(a)

}

