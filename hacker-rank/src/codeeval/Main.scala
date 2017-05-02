/**
 EFFICIENT DELIVERY
CHALLENGE DESCRIPTION:

A shipping company is providing oil delivery between two continents using tankers. They’re trying to increase their efficiency by keeping their ships in port to wait for additional oil to prevent setting to sea only partially loaded. 
As a logistician, the challenge for you will be to determine all variations of efficient delivery based on the available tankers and the total amount of oil in barrels needed to achieve maximum efficiency. 
A tanker's carrying capacity is expressed in barrels of oil that it can take on board. 
E.g. the company has only two kind of tankers with the capacity of 2 and 5 barrels and the amount of oil to be transferred is 12 barrels. In this case there are two options of efficient delivery: 
1. load 6 tankers with the capacity of 2. [6,0] 
2. load 1 tanker with the capacity of 2 and 2 tankers with the capacity of 5. [1,2] 
In case the company had three kind of tankers with the capacity of 6, 7, 8 barrels and 10 barrels of oil to be transferred then there would be no option for efficient delivery and the minimum amount of oil needed would be 12 [2,0,0] so the answer in this case is 2. 
So you see that if there is no option to load the tankers effectively, you need to find out the minimum amount of oil which needs to be added to the given quantity to make the efficient delivery possible.

INPUT SAMPLE:

Your program should accept as its first argument a path to a filename containing rows with available tankers in brackets in sorted order, and the amount of oil after a comma. E.g.
(2,5), 12
(6,9,20), 44
(197,8170), 155862
(2,4,8), 8
OUTPUT SAMPLE:

Print out all possible variations of efficient delivery in sorted order if the efficient delivery exists. In another case print out the amount of oil to be added. E.g.
[1,2][6,0]
[1,2,1][4,0,1]
3
[0,0,1][0,2,0][2,1,0][4,0,0]
Constrains: Number of test cases is in range [20, 40] 
The number of tankers is in range [2, 5] 
A tanker's capacity is in range of [2, 10000] barrels 
Oil amount is in range of [1, 200000] barrels
 */
package codeeval

/*
 * This problem reduces to coin change problem solved by dynamic programming
 * 
 * Sometimes not as efficient as it could be
 */
object Main extends App {
  import scala.math.Ordering.Int.compare
  
  //val source = scala.io.Source.fromFile(args(0))
  val source = scala.io.Source.fromFile("src/codeeval/input_large.txt")
  val lines = source.getLines.filter(_.length > 0)
  for (l <- lines) {
    val nums: Array[Int] = l.split(",").map(_.replaceAll("[^0-9.]", "")).map(_.toInt)
    //println(nums.mkString(" "))
    
    val oil: Int = nums.last
    val tankers: Array[Int] = nums.dropRight(1)
    
    val res = loadTankers(oil, tankers)
    
    if(res(tankers.size - 1)(oil).length > 0){
      println(res(tankers.size - 1)(oil).sortWith(cmp).map(_.mkString("[", ",", "]")).mkString(""))
    }
    else{
      val min: Int = (1 to oil).filter(x => res(tankers.size - 1)(x).length > 0).flatMap(x => tankers.map(x + _)).filter(_ > oil).min
      println(min - oil)      
    }
    
    //println(tankers.mkString(" "))
  }
  
  def cmp(a1: Array[Int], a2: Array[Int]): Boolean = {
    val cmps = (0 until a1.length).map(i => compare(a1(i), a2(i))).dropWhile(_ == 0).map(_ == -1)
    
    if(cmps.isEmpty) false
    else cmps.head
  }
  
  
  def loadTankers(oil: Int, tankers: Array[Int]): Array[Array[List[Array[Int]]]] = {
    val table: Array[Array[List[Array[Int]]]] = Array.ofDim(tankers.size, oil + 1)
      
    for(r <- 0 until tankers.size){
      table(r)(0) = List(Array.fill(tankers.size)(0))
    }
     
    for(i <- 1 until (oil+1)){
      for(j <- 0 until tankers.size){
        table(j)(i) = List()
        // include tankers(j)
        if(i - tankers(j) >= 0){
          table(j)(i) = table(j)(i - tankers(j)).map(_.clone())
          table(j)(i).foreach(a => a(j) += 1)
        }       
        // exclude tankers(j)
        if(j - 1 >= 0){
          table(j)(i) = table(j-1)(i) ::: table(j)(i)
        }
      }
    }
   
      
    table
  }
}