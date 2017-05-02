package GCJ2017.R1C

object C extends App {
  import scala.io.Source;
  import java.math.BigDecimal
  import java.math.RoundingMode
 
  val lines = Source.fromFile("src/GCJ2017/R1C/C-small-practice-1.in").getLines(); 
  val T: Int = lines.next().toInt;

  
  for(t <- 1 to T){
    val fstLine: Array[Int] = lines.next().split(" ").map(_.toInt)
    val N: Int = fstLine(0)
    val K: Int = fstLine(1)
    val U: Int = (lines.next().toDouble*10000).toInt
        
    val cores: List[Int] = lines.next().split(" ").map(x => (x.toDouble*10000d).toInt).toList
    
    val trainedCores: List[Int] = iter(cores, U)
    //val res: Double = round(trainedCores.map(x => x.toDouble/10000).reduceLeft(_*_), 10)
    val res: Double = trainedCores.map(x => (x.toDouble)/(10000d)).reduceLeft(_*_)
    
    println("Case #" + t + ": " + "%.9f".format(res))  
  }
    
  def iter(ls: List[Int], U: Int): List[Int] = {
      if(U == 0) ls
      else{
        val (head::rest): List[Int] = ls.sorted
        
        iter((head+1)::rest, U-1)
      }
    }
  
  def round(value: Double, places: Int): Double = {
    if (places < 0) throw new IllegalArgumentException();

    val bd: BigDecimal = new BigDecimal(value);
    bd.setScale(places, RoundingMode.HALF_UP).doubleValue()
}
}