package GCJ2016.R1C

object C extends App {
  import scala.io.Source;
 
  val lines = Source.fromFile("src/GCJ2016/R1C/C-small-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  for(t <- 1 to T){
    val line: Array[Int] = lines.next().split(" ").map(_.toInt);
    
    val J: Int = line(0);
    val P: Int = line(1);
    val S: Int = line(2);
    val K: Int = line(3);
  }
}