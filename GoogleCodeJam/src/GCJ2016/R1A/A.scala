package GCJ2016.R1A

/**
 * The Last Word
 * 
 * Easy, but requires a little trick so that a solution is functional.
 * 
 * Straightforward solution would be to use doubly-linked list, but this structure is not functional. Since appending to both ends of list, produces data structures which is not immutable.
 * 
 * Sufficient is to use 2 lists.
 * 
 * List "more" in the beginning contains only the first char of the word, list "less" is empty.
 * Then we scan the remaining chars. If current char is bigger than current head of the list "more", then current char is the new head of this list. Else we add the current char to list "less".
 * 
 */
object A extends App {
  import scala.io.Source;
  import scala.collection.mutable.HashSet;
  
  val lines = Source.fromFile("src/GCJ2016/R1A/A-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  for(t <- 1 to T){
    val S: String = lines.next();
    
    val (more, less): (List[Char], List[Char]) = S.foldLeft((List[Char](), List[Char]()))((z, curC) => 
      if(z._1.isEmpty || curC >= z._1.head) (curC :: z._1, z._2) else (z._1, curC :: z._2));
    
    val lastWord: List[Char] = more ++ less.reverse;
    
    println("Case #" + t + ": " + lastWord.mkString);
  }
}