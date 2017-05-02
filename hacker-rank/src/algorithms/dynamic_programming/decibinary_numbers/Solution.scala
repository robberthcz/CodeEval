package algorithms.dynamic_programming.decibinary_numbers

object Solution extends App {
   val input = scala.io.Source.fromFile("src/algorithms/dynamic_programming/decibinary_numbers/input2.txt").getLines()
  
  val T: Int = input.next().toInt
  
  
  val size = 2000000
  val a: Array[Long] = Array.ofDim(size+10)
  val s: Array[Long] = Array.ofDim(size+10)
  
  a(0) = 1
  s(0) = 1
  a(10) = -1
  
  for(i <- 1 until size){
    a(i) += a(i-1)
    if(i % 2 == 0){
      a(i) += a(i/2)
      a(i+10) = -a(i/2)
    }
      
    s(i) = a(i) + s(i-1)
  }
   
  println((0 to 10).mkString("\t"))
  println(a.take(10).mkString("\t"))
  println(s.take(10).mkString("\t"))
  
  
 
  
  for(t <- 1 to T){
    val x: Long = input.next().toLong
    
    val id = s.indexWhere(x <= _)
    val th = x - s(Math.max(id-1, 0))
    
    //println(id)
    //println(th)
    
    println(rec(id,th))
  }
  
  
  
  def rec(id: Int, th: Long): Int = {
    if(id <= 0)
      0
    else if(id % 2 == 0 && th > a(id-1)){
      rec(id/2, th - a(id-1))*10
    }
    else{
      rec(id-1, th) + 1
    }
  }
}