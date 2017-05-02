package functional_programming.ad_hoc.mangoes

object Solution extends App {
  import scala.io.Source;
  
  val input = Source.fromFile("src/functional_programming/ad_hoc/mangoes/input.txt").getLines();
  
  val M: Long = input.next().split(" ")(1).toLong;
  val A: List[Long] = input.next().split(" ").map(_.toLong).toList;
  val H: List[Long] = input.next().split(" ").map(_.toLong).toList;
  val N = A.length;
  
  println(findMaxFriends(N, M, A, H));
    
  def findMaxFriends(k: Int, M: Long, A: List[Long], H: List[Long]): Int = {
    def canInvite(k: Int): Boolean = {
      val zipped = A.zip(H);
      return zipped.map{ case (a, h)  => a + (k-1)*h }.sorted.take(k).sum <= M    
    }
    
    def binSearch(lo: Int, hi: Int): Int = {
      val mid: Int = lo + (hi - lo + 1)/2;
      if(lo == hi)            lo   
      else if(canInvite(mid)) binSearch(mid, hi)
      else                    binSearch(lo, mid - 1)
    }
    
    binSearch(0, N);       
  }
}