package algorithms.constructive_algorithms.inverse_rmq

object Solution extends App {
  import scala.io.{StdIn, Source}
  import java.util.TreeSet
  
  val input = Source.fromFile("src/algorithms/constructive_algorithms/inverse_rmq/input5.txt").getLines()
  val n: Int = input.next().toInt
  val a: Array[Int] = input.next().split(" ").map(_.toInt)
  
  if(n == 1){
    println("YES")
    println(a.head)
  }
  else{
   getSegTree(a) match {
    case Some(x) => println("YES");println(x.map(_.mkString(" ")).mkString(" "));
    case None => println("NO")
  } 
  }
  
  
  
  def getSegTree(nums: Array[Int]): Option[List[List[Int]]] = { 
    val heads: List[(List[Int], Int)] = listHeads(pack(nums.sorted.toList)).reverse
    
    if(isValidTree(heads)){
      Some(heads.head._1 :: iter(heads.map(_._1)))
    }
    else{
      None
    }
  }
  
  def isValidTree(ls: List[(List[Int], Int)]): Boolean = {
    val lens: List[Int] = ls.map(_._2)
    val shouldbe: List[Int] = List(1,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,65536,131072,262144,524288,1048576)

    lens == shouldbe.take(lens.length)
  }
  
  // copied from http://aperiodic.net/phil/scala/s-99/p09.scala
  def pack[A](ls: List[A]): List[List[A]] = {
		  if (ls.isEmpty) List(List())
		  else {
			  val (packed, next) = ls span { _ == ls.head }
			  if (next == Nil) List(packed)
			  else packed :: pack(next)
		  }
  }

  def listHeads(packed: List[List[Int]]): List[(List[Int], Int)] = {
		  if(packed.isEmpty) Nil
		  else{
		    val (heads: List[Int], n: Int, tails: List[List[Int]]) = packed.foldLeft((List[Int](), 0, List[List[Int]]())){
		      (acc, v) => 
		        if(v.isEmpty) acc
		        else if(v.tail.isEmpty) (v.head :: acc._1, acc._2 + 1, acc._3)
		        else (v.head :: acc._1, acc._2 + 1, v.tail :: acc._3)
		    }   
		    //println(heads)
		    //println(n)
			  if(n > 0) (heads, n) :: listHeads(tails)
			  else listHeads(tails)
		  }
  }

  def combine(cur: List[Int], mins: List[Int]): List[Int] = {
		  val set: Set[Int] = mins.toSet
		  val rightChilds: TreeSet[Int] = new TreeSet()
			cur.filter(!set.contains(_)).foreach(rightChilds.add(_))
			//println(mins)
			mins.flatMap{x => val higher: Int = rightChilds.higher(x); rightChilds.remove(higher); List(x, higher)}    
  }   

  def iter(levls: List[List[Int]]): List[List[Int]] = {
    levls match {
      case x :: y :: Nil => List(combine(y, x))
      case x :: y :: rest => val comb = combine(y, x); comb :: iter(comb :: rest)
    }
  }
}