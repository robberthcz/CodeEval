package GCJ2016.QR

object Temp {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(77); 
  println("Welcome to the Scala worksheet");$skip(73); 
  
  val stream: Stream[Int] = Stream.from(0).map(Math.pow(3, _).toInt);System.out.println("""stream  : Stream[Int] = """ + $show(stream ));$skip(22); ;
  
  println(stream);$skip(40); ;
  
  val ls: List[Int] = List(1,0,1,0);System.out.println("""ls  : List[Int] = """ + $show(ls ));$skip(67); ;
  
  val res: List[Int] = ls.zip(stream).map{ case (a, b) => a*b};System.out.println("""res  : List[Int] = """ + $show(res ));$skip(47); ;
  
  println(res.reduceLeft((x, y) => x + y));}
  
  
}
