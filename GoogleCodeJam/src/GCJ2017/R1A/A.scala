package GCJ2017.R1A

object A extends App {
  import scala.io.Source;
 
  val lines = Source.fromFile("src/GCJ2017/R1A/A-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  for(t <- 1 to T){
    val line: Array[String] = lines.next().split(" ");
    
    val R: Int = line(0).toInt
    
    val cake: List[List[Char]] = lines.take(R).map(_.toList).toList
    
    val res: List[List[Char]] = cake.map(fillRow).transpose.map(fillRow).transpose
    
    println("Case #" + t + ":")
    println(res.map(_.mkString("")).mkString("\n"))   
  }
  
  def fillRow(S: List[Char]): List[Char] = {
      if(S.forall(_ == '?')) S
      else                   S.tail.foldLeft(List[Char](S.find(_!= '?').get)) ( (acc: List[Char], z: Char) => if(z=='?'){(acc.head)::acc} else {z::acc}).reverse
  }  
}