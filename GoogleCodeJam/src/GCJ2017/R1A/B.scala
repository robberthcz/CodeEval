package GCJ2017.R1A

/**
 * Not the most efficient implementation, but it is not needed even for the large input
 */
object B extends App {
  import scala.io.Source;
 
  val lines = Source.fromFile("src/GCJ2017/R1A/B-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;

  
  for(t <- 1 to T){
    val line: Array[String] = lines.next().split(" ");
    val N: Int = line(0).toInt
    val P: Int = line(1).toInt
    
    val Rs: Array[Int] = lines.next().split(" ").map(_.toInt)
    
    val Qs: Array[Array[Int]] = Array.ofDim(N, P)
    val rngs: Array[Array[Range]] = Array.ofDim(N, P)
    
    for(n <- 0 until N)
      Qs(n) = lines.next().split(" ").map(_.toInt)
      
    //println(Qs.map(_.mkString("\t")).mkString("\n"))
           
    for(i <- 0 until N; j <- 0 until P){
      for(j <- 0 until P){
        rngs(i)(j) = getRange(Qs(i)(j), Rs(i))
      }
    }
       
    
    
    val rngsLs: List[List[Range]] = rngs.map(_.sortBy(x => x).toList).toList
    //println(rngsLs.map(_.mkString("\t")).mkString("\n"))
    
    val kits: List[Range] = iter(rngsLs)
    
    
    println("Case #" + t + ": " + kits.length) 
    
    //println()
  }
  
  type Range = (Int, Int)
  
  def getRange(Q: Int, R: Int): Range = {
    val upper: Int = (Q*10)/(R*9)
    val lower: Int = if((Q*10) % (R*11) == 0) (Q*10)/(R*11)
                     else                     (Q*10)/(R*11) + 1
                     
   if(lower <= upper) (lower, upper)
   else (-1, -1)
  }
  
  def intersection(r1: Range, r2: Range): Range = {
    val lower: Int = Math.max(r1._1, r2._1)
    val upper: Int = Math.min(r1._2, r2._2)

    if(r1._1 == -1 || r2._1 == -1) (-1, -1)
    else if(lower <= upper)        (lower, upper)
    else                           (-1, -1)
  }
  
  def iter(rngs: List[List[Range]]): List[Range] = {
    if(rngs.contains(Nil)){
      Nil
    }
    else{
      val heads: List[Range] = rngs.map(_.head)
      val isection: Range = heads.reduceLeft(intersection)
      if(isection._1 == -1){
        val sorted = rngs.sortBy(_.head._2)
        iter(sorted.head.tail :: sorted.tail)
      }
      else{
        isection :: iter(rngs.map(_.tail))
      }
    }
  }
  
  
}