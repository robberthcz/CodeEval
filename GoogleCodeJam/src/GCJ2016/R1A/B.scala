package GCJ2016.R1A

/**
 * Rank and file
 * 
 * I could come up with solution, but it would be too involved and too complicated to implement.
 * 
 * The analysis shows this insight: every element of a matrix is at the same time a member of 1 column and 1 row.
 * Which means if we write out all columns and all rows, each element would repeated even number of times.
 * 
 * In our case, the numbers are strictly increasing for each row and column, so that if there is one row or column missing, 
 * the elements of that will be repeated odd number of times among the available rows and columns.
 */
object B extends App {
  import scala.io.Source;
  
  val lines = Source.fromFile("src/GCJ2016/R1A/B-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  for(t <- 1 to T){
    // N-dim matrix
    val N: Int = lines.next().toInt;
    val numOfLists: Int = 2*N - 1;
    
    val allLists: List[String] = (1 to numOfLists).foldLeft(List[String]()) ((acc, n) => lines.next() :: acc);
    val height2freq: Map[Int, Int] = allLists.mkString(" ").split(" ").groupBy(_.toInt).mapValues(_.length);
    val missingColOrRow: String = height2freq.filter(_._2 % 2 != 0).keys.toList.sortBy(identity).mkString(" ");

    println("Case #" + t + ": " + missingColOrRow);
  }
}