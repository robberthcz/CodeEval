package GCJ2017.R1B

object B extends App {
  import scala.io.Source;
 
  val lines = Source.fromFile("src/GCJ2017/R1B/B-small-practice.in").getLines(); 
  val T: Int = lines.next().toInt;

  
  for(t <- 1 to T){
    val line: Array[Int] = lines.next().split(" ").map(_.toInt)
    val R: Int = line(0)
    val O: Int = line(1)
    val Y: Int = line(2)
    val G: Int = line(3)
    val B: Int = line(4)
    val V: Int = line(5)
    
    type colCount = (Char, Int)
    
    val ls: List[colCount] = List(('R',R), ('Y',Y), ('B',B))
    
    def cmp(fstChar: Char)(cc1: colCount, cc2: colCount): Boolean = {
      if(cc1._2 > cc2._2 || (cc1._2 == cc2._2 && cc1._1 == fstChar)) true
      else false
    }
    
    val fstChar: Char = {
      if(R>=B && R>=Y) 'R'
      else if(B>=R && B>=Y) 'B'
      else 'Y'}
    
    def iter(ls: List[colCount], pat: List[Char]): List[Char] = {
      if(ls.isEmpty) pat
      
      val sorted = ls.sortWith(cmp(fstChar))
      
      if(pat.isEmpty){
        val (col, count) = sorted.head
        if(count > 1) iter( (col, (count-1))::sorted.tail, List(col))
        else iter(sorted.tail, List(col))
      }
      else{
        val (col, count) = sorted.head
        if(count != pat.head){
          if(count > 1) iter( (col, (count-1))::sorted.tail, col::pat)
          else iter(sorted.tail, col::pat)
        }
        else{
          if(sorted.size == 1) Nil
          else{
            val (col2, count2) = sorted.tail.head
            if(count2 > 1) iter((col, count-1)::sorted.tail, col::pat)
            else iter(sorted.tail, col::pat)
          }
        }
      }
    }
    val res: List[Char] = iter(ls, Nil)
    if(res.isEmpty)
      println("Case #" + t + ": IMPOSSIBLE")
    else
      println("Case #" + t + ": " + res.mkString(""))
   

  }
  
}