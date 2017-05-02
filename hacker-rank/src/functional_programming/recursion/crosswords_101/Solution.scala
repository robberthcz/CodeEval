package functional_programming.recursion.crosswords_101

object Solution extends App {
  import scala.io.Source;
  
  val input = Source.fromFile("src/functional_programming/recursion/crosswords_101/input.txt").getLines();
  
  val crossword: Array[String] = (1 to 10).map(x => input.next()).toArray
  val cities: List[String] = input.next().split(";").toList
  
  
  type Cell = (Int, Int)
  type Word = (Cell, Cell)
  
  val emptyCells: List[Cell] = for(r <- List.range(0,9); 
                                   c <- List.range(0,9);
                                   if(crossword(r).charAt(c) == '-') )
                                     yield (r,c);
  
  println(emptyCells)
  println(cities)
  
  
  emptyCells.sortBy(_._1)
  emptyCells.sortBy(_._2)
  
  println(List(1,2,3,4).sliding(2).toList)
  
                                   
  
}