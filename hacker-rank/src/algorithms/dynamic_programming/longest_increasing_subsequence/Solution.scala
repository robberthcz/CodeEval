package algorithms.dynamic_programming.longest_increasing_subsequence

object Solution extends App {
  import scala.io.Source;
  import scala.io.StdIn
  //import scala.collection.mutable.TreeSet
  import java.util.Scanner
  import java.io.FileReader
  import java.util.TreeSet
  
  //val input = Source.fromFile("src/algorithms/dynamic_programming/longest_increasing_subsequence/input.txt").getLines
  //val nums: List[Int] = input.toList.map(_.toInt).drop(1)
  //println(nums.foldLeft(TreeSet.empty[Int])(addNum2Set).size)
  
  /*val T: Int = readInt
  val set: TreeSet[Int] = TreeSet.empty[Int]
  for(t <- 1 to T){
    addNum2Set(set, readInt)          
  }
  println(set.size)*/
  
  /*val textScan = new Scanner(new FileReader("src/algorithms/dynamic_programming/longest_increasing_subsequence/input.txt"))
  val set: TreeSet[Int] = new TreeSet()
  textScan.nextLine() // skip line count
  while(textScan.hasNextLine()){
    val nextLine = textScan.nextLine()
    addNum2Set(set, nextLine.toInt)
  }
  println(set.size)
  
  def addNum2Set(set: TreeSet[Int], num: Int): TreeSet[Int] = {
    val higher = set.ceiling(num)
    
    if(higher == null){
      set.add(num)
      return set
    }
    else{
      set.remove(higher)
      set.add(num)
      return set
    }
  }*/
  
  
 
  val set: TreeSet[Int] = new TreeSet()
  val T: Int = readInt
  for(t <- 1 to T){
    addNum2Set(set, readInt)
  }
  println(set.size)
  
  def addNum2Set(set: TreeSet[Int], num: Int): TreeSet[Int] = {
    val higher = set.ceiling(num)
    
    if(higher == null){
      set.add(num)
      return set
    }
    else{
      set.remove(higher)
      set.add(num)
      return set
    }
  }
   
  
  
  /*def addNum2Set(set: TreeSet[Int], num: Int): TreeSet[Int] = {
    val higher = set.from(num)
    
    if(higher.isEmpty) set += num
    else{
      val fstHigher: Int = higher.min
      (set -= fstHigher) += num
    }
  }*/
  
  def time[R](block: => R): R = {  
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/100000000 + "ns")
    result
}
}