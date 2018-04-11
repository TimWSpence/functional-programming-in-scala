package org.timspence.chapter5

object Main extends App {
}

sealed trait Stream[+A] {
  import Stream._

  def toList: List[A] = this match {
    case Empty => Nil
    case Cons(h,t) => h() :: t().toList
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    case Cons(h,t) => f(h(), t().foldRight(z)(f))
    case _ => z
  }

  def take(n: Int): Stream[A] = {
    if(n == 0) Empty else this match {
      case Empty => empty
      case Cons(h, t) => cons(h(), t().take(n-1))
    }
  }

  def takeWhile(p: A => Boolean): Stream[A] = this match {
    case Empty => empty
    case Cons(h,t) => if(p(h())) cons(h(), t().takeWhile(p)) else empty
  }

  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h,_) => Option(h())
  }
}
case object Empty extends Stream[Nothing]
case class Cons[A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {

  def empty[A]: Stream[A] = Empty
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val h = hd
    lazy val t = tl
    Cons(() => h, () => t)
  }

  def unfold[A,S](z: S)(f: S => Option[(A,S)]): Stream[A] = {
    f(z) match {
      case None => Empty
      case Some((value, state)) => Cons(() => value, () => unfold(state)(f))
    }
  }

  def constant[A](a: A): Stream[A] = cons(a, constant(a))

}
