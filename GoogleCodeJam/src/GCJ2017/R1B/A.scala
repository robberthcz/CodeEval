package GCJ2017.R1B

object A extends App {
  import scala.io.Source;
 
  val lines = Source.fromFile("src/GCJ2017/R1B/A-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;

  
  for(t <- 1 to T){
    val line: Array[Int] = lines.next().split(" ").map(_.toInt);
    
    val D: Double = line(0).toDouble
    val N: Int = line(1)
    
    val horses: List[Double] = 
      for(i <- List.range(1,N+1))
      yield{
        val ith = lines.next().split(" ").map(_.toDouble)
        ((D-ith(0))/ith(1))}   
    
    val res: Double = D/horses.max
    
    println("Case #" + t + ": " + res)
  }
}