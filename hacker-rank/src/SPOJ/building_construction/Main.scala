/**
 KOPC12A - K12 - Building Construction
no tags 
Given N buildings of height h1,h2,h3...hn, the objective is to make every building has equal height. This can be done by removing bricks from a building or adding some bricks to a building.Removing a  brick or  adding  a brick is done at certain cost which will be given along with the heights of the buildings.Find the minimal cost at which you can make the buildings look beautiful by re-constructing the buildings such that the N buildings satisfy 

h1=h2=h3=..=hn=k ( k can be any number).

For convenience, all buildings are considered to be vertical piles of bricks, which are of same dimensions. 

 

         Given N buildings of height h1,h2,h3...hn, the objective is to make every building has equal height. This can be done by removing bricks from a building or adding some bricks to a building.Removing a  brick or  adding  a brick is done at certain cost which will be given along with the heights of the buildings.Find the minimal cost at which you can make the buildings look beautiful by re-constructing the buildings such that the N buildings satisfy h1=h2=h3=..=hn=k ( k can be any number).
Input
The first line of input contains an integer T which denotes number of test cases .This will be followed by 3*T lines , 3 lines per test case. The first line of each test case contains an integer n and the  second line contains n integers which denotes the heights of the buildings [h1,h2,h3....hn] and the third line contains n integers [c1,c2,c3...cn] which denotes the cost of adding or removing one unit of brick from the corresponding building.

T<=15;n<=10000;0<=Hi<=10000;0<=Ci<=10000;

Output
The output must contain T lines each line corresponding to a testcase.

Example

Input:
1
3
1 2 3
10 100 1000

Output:
120
 */
package SPOJ.building_construction

object Main extends App {
  import collection._
  
  val input = scala.io.Source.fromFile("src/SPOJ/building_construction/input.txt").getLines();
  
  val T: Int = input.next().toInt;
   
  for(t <- 1 to T){
    input.next();
    
    val heights: List[Int] = input.next().split(" ").map(_.toInt).toList
    val costs: List[Int] = input.next().split(" ").map(_.toInt).toList
    
    def getCost(heights: List[Int], costs: List[Int])(newH: Int): Int = {
      (heights.map(h => Math.abs(h - newH)), costs).zipped.map(_ * _).sum 
    }
    
    def findMinCostNaive(heights: List[Int], costs: List[Int]): Int = {
      heights.map(getCost(heights, costs)).min
    }
    
    def ternarySearch(lo: Int, hi: Int, f: Int => Int): Int = {
      if((hi - lo) < 4){
        (lo to hi).map(f(_)).min
      }
      else{
        val third: Int = (hi - lo) / 3
      
        val m1: Int = lo + third
        val m2: Int = hi - third
      
        if(f(m1) <= f(m2)) ternarySearch(lo, m2, f)
        else ternarySearch(m1, hi, f)
      }     
    }
      
    //println(findMinCostNaive(heights, costs))
    println(ternarySearch( 0, heights.max, getCost(heights, costs)))
    
  }
}