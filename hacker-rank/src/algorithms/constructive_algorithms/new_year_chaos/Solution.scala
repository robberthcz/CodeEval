package algorithms.constructive_algorithms.new_year_chaos

object Solution extends App {
  import scala.io.Source
  
  val input = Source.fromFile("src/algorithms/constructive_algorithms/new_year_chaos/input2.txt").getLines()
  
  val T: Int = input.next().toInt
  
  for(t <- 1 to T){
    input.next()
    val queue: Stream[Int] = input.next().split(" ").map(_.toInt).toStream
    
    val qMoved: Stream[Int] = queue.zip(1 to queue.size).map{case(pos, init) => pos - init}
 
    
    if(qMoved.exists(_ > 2)){
      println("Too chaotic")
    }
    else{
      println(queue.tails.map(ls => ls.filter(x => ls.head > x).size).sum)
    }
  } 
}