package org.timspence.chapter9

import scala.util.matching.Regex

object Main {

}

trait JSON
object JSON {
  case object JNull extends JSON
  case class JNumber(get: Double) extends JSON
  case class JString(get: String) extends JSON
  case class JBool(get: Boolean) extends JSON
  case class JArray(get: IndexedSeq[JSON]) extends JSON
  case class JObject(get: Map[String, JSON]) extends JSON
}

trait Parsers[ParseError, Parser[+_]] { self =>
  def or[A](s1: Parser[A], s2: => Parser[A]): Parser[A]
  def slice[A](p: Parser[A]): Parser[String]
  def flatMap[A,B](p: Parser[A])(f: A => Parser[B]): Parser[B]
  def succeed[A](a: A): Parser[A]

  def many[A](p: Parser[A]): Parser[List[A]] = many1(p) or succeed(List())
  def map[A,B](a: Parser[A])(f: A => B): Parser[B] = a flatMap(a => succeed(f(a)))
  def many1[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p))(_ :: _)
  def product[A,B](p: Parser[A], p2: => Parser[B]): Parser[(A,B)] = p flatMap(p => p2 map(p2 => (p,p2)))
  def map2[A,B,C](p: Parser[A], p2: => Parser[B])(f: (A,B) => C): Parser[C] = product(p, p2) map (f.tupled)
  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] = n match {
    case 0 => succeed(List())
    case n => map2(p, listOfN(n-1, p)) (_ :: _)
  }

  def char(c: Char): Parser[Char] = string(c.toString) map (_.charAt(0))
  implicit def string(s: String): Parser[String]
  implicit def regex(r: Regex): Parser[String]
  implicit def operators[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)
  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  case class ParserOps[A](p: Parser[A]) {
    def |[B>:A](p2: => Parser[B]): Parser[B] = self.or(p,p2)
    def or[B>:A](p2: => Parser[B]): Parser[B] = self.or(p,p2)
    def many: Parser[List[A]] = self.many(p)
    def many1: Parser[List[A]] = self.many1(p)
    def map[B](f: A=>B): Parser[B] = self.map(p)(f)
    def flatMap[B](f: A=> Parser[B]): Parser[B] = self.flatMap(p)(f)
    def product[B](p2: => Parser[B]): Parser[(A,B)] = self.product(p, p2)
    def **[B](p2: Parser[B]) = product(p2)
  }
}

