package algorithms.constructive_algorithms.bonetrousle

object Solution extends App {
  import scala.io.Source
  
  val input = Source.fromFile("src/algorithms/constructive_algorithms/bonetrousle/input3.txt").getLines()
  
  val T: Int = input.next().toInt
  
  for(t <- 1 to T){
    val trip: Array[Long] = input.next().split(" ").map(_.toLong).toArray
    
    //println(trip.mkString(" "))
    
    val n: Long = trip(0)
    val k: Long = trip(1)
    val b: Int = trip(2).toInt
    
    val a: Array[Long] = (1 to b).map(_.toLong).toArray
    
    val min: Long = a.sum
    val max: Long = min + (k - a.last)*(b.toLong)
    
    if(n < min || n > max)
      println(-1)
    else{
      val c: Long = k - a.last
      println(iter(a, n - min, c, b - 1).mkString(" "))
    }
    
  }
  
  def iter(a: Array[Long], n: Long, c: Long, i: Int): Array[Long] = {
    if(c < n){
      a(i) += c
      iter(a, n - c, c, i - 1)
    }
    else{
      a(i) += n
      a
    }
  }
}