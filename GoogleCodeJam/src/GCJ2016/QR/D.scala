package GCJ2016.QR

/**
 * Fractiles
 * Very hard, solved after reading the analysis.
 * 
 * Tile at position (C=1) 1, generates tiles at positions 1 to K (when going from C=1 to C=2), etc.
 * If that tile is G, then all positions (C=2) 1 to K will be also G
 * Else if the tile is L, then positions (C=2) 1 to K will be equal to initial artwork
 *  => then we know how positions (C=2) 1 to K are covered, position 1 will be again L, but position 2 might not be L
 *  => we should look at descendants of position (C=2) 2, which is already descendant of position (C=1) 1
 *  
 *  The contest analysis explains this better, I do not think it is possible to provide more succint explanation than that.
 */
object D extends App {
  import scala.io.Source;
  import scala.collection.mutable.HashSet;
  
  val lines = Source.fromFile("src/GCJ2016/QR/D-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  for(t <- 1 to T){
    val args: Array[String] = lines.next().split(" ");
    
    // K, C, S
    val K: Int = args(0).toInt;
    val C: Int = args(1).toInt;
    val S: Int = args(2).toInt;
    
    // picking S tiles is not enough to determine the artwork with all L
    if(C*S < K)
      println("Case #" + t + ": IMPOSSIBLE");
    else{
      val one: Long = 1;
      val tiles: List[Long] = (0 to K by C).map(i => (1 to C).foldLeft(one)((a, v) => (a - 1)*K + Math.min(v + i, K))).toList;  
      println("Case #" + t + ": " + tiles.take(S).mkString(" "));
    }
  }
}