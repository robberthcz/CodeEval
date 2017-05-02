package algorithms.constructive_algorithms.flipping_the_matrix

object Solution extends App {
  import scala.io.Source
  
  val input = Source.fromFile("src/algorithms/constructive_algorithms/flipping_the_matrix/input.txt").getLines()
  
  val T: Int = input.next().toInt
  
  for(t <- 1 to T){
    val n: Int = input.next().toInt
    val len: Int = 2*n
    
    val a: Array[Array[Int]] = Array.ofDim[Int](2*n, 2*n)
    
    for(i <- 0 until len){
      a(i) = input.next().split(" ").map(_.toInt).toArray
    }
    
    
    
    val ls: List[Int] = 
      for(i <- List.range(0, n);
          j <- List.range(0, n)
        )
    yield{
      val iOff: Int = len - i - 1;
      val jOff: Int = len - j - 1;
      val possible: List[Int] = List(a(i)(j),a(iOff)(j),a(i)(jOff),a(iOff)(jOff));
      possible.max
    }
    
    println(ls.sum)
  }
}