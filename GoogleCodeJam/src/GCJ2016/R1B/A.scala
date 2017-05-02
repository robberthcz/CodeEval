package GCJ2016.R1B

object A extends App {
  import scala.io.Source;
  
  val lines = Source.fromFile("src/GCJ2016/R1B/A-small-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  println(subtractFreqMaps(Map('l' -> 2, 'k' -> 3, 'm' -> 4), Map('l' -> 1, 'k' -> 2)));
  
  val nums: List[String] = List("ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE");
  val numsFreqMaps: List[Map[Char, Int]] = nums.map(str2freqMap(_));
  
  for(t <- 1 to T){
    val line: String = lines.next();
    
    
  }
  
  def str2freqMap(str: String): Map[Char, Int] = {
    str.toList.groupBy(identity).mapValues(_.size);
  }
  
  def subtractFreqMaps(thisMap: Map[Char, Int], thatMap: Map[Char, Int]): Map[Char, Int] = {
    thisMap ++ thatMap.map{case (k, v) => (k, thisMap.getOrElse(k, 0) - v)}
  }
}