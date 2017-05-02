package GCJ2016.QR

/**
 * Revenge of the Pancakes
 * Solved by myself, not as easy. Takes some time to figured out the optimal strategy for flipping the pancakes so that the number of flips is minimal.
 * 
 * At each step, we take the longest continguous sequence of either + or - from the top and flip them.
 * Do this until the stack contains only + or -
 * If stack consists of only -, than 1 more flip is needed to turn the stack to +
 * 
 * It turns out, it is enough to count the number of "-+" or "+-" in the pancake stack to implement the above algorithm. That gets the minimum number of flips.
 */
object B extends App{
  import scala.io.Source;
  
  val lines = Source.fromFile("src/GCJ2016/QR/B-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  for(t <- 1 to T){ 
    val line: String = lines.next();
    // stack of pancakes, each with two faces, represented as + and -
    val pancStack: List[Char] = line.toList;
    
    // sliding - List(1,2,3,4) => List(List(1,2), List(2,3), List(3,4))
    var flips: Int = pancStack.sliding(2).map {  case List(a, b) if a != b => 1
                                            case _                    => 0
                                            }.toList.sum;
    
    // flip the "-" pancake stack
    if(line.charAt(line.length() - 1) == '-') flips += 1;
    
    println("Case #" + t + ": " + flips); 
  }
}