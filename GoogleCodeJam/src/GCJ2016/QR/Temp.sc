package GCJ2016.QR

object Temp {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val stream: Stream[Int] = Stream.from(0).map(Math.pow(3, _).toInt);
                                                  //> stream  : Stream[Int] = Stream(1, ?)
  
  println(stream);                                //> Stream(1, ?)
  
  val ls: List[Int] = List(1,0,1,0);              //> ls  : List[Int] = List(1, 0, 1, 0)
  
  val res: List[Int] = ls.zip(stream).map{ case (a, b) => a*b};
                                                  //> res  : List[Int] = List(1, 0, 9, 0)
  
  println(res.reduceLeft((x, y) => x + y));       //> 10
  
  
}