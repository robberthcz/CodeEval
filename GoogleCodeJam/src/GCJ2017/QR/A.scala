package GCJ2017.QR

/**
 * Quadratic solution, could be optimized to linear, but it is not needed
 */
object A extends App {
  import scala.io.Source;
 
  val lines = Source.fromFile("src/GCJ2017/QR/A-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  for(t <- 1 to T){
    val line: Array[String] = lines.next().split(" ");
    
    val S: String = line(0)
    val K: Int = line(1).toInt
    val N: Int = S.length()
    
    val pancs: Array[Boolean] =  S.map(_ == '+').toArray
    var minCount: Int = 0
    
    for(i <- 0 until (N-K+1)){
      if(!pancs(i)){
        minCount += 1
        for(j <- (i until (i+K)))
          pancs(j) = !pancs(j)     
      }
    }
    
    if(pancs.contains(false)){
      println("Case #" + t + ": IMPOSSIBLE") 
    }
    else{
      println("Case #" + t + ": " + minCount) 
    }
  }
}