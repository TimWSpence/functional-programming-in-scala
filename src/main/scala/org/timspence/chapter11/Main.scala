package org.timspence.chapter11

object Main {

}

trait State[S, A] {

}

trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  def flatMap[A,B](a: F[A])(f: A => F[B]): F[B]
  def sequence[A](lma: List[F[A]]): F[List[A]]
  def traverse[A,B](la: List[A])(f: A => F[B]): F[List[B]]
}
