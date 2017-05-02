package GCJ2017.R1A

object C extends App{
  import scala.io.Source;
 
  val lines = Source.fromFile("src/GCJ2017/R1A/C-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;

  
  for(t <- 1 to T){
    val line: Array[Int] = lines.next().split(" ").map(_.toInt);
    
    val Hd: Int = line(0)
    val Ad: Int = line(1)
    val Hk: Int = line(2)
    val Ak: Int = line(3)
    val B: Int = line(4)
    val D: Int = line(5)
    
    val dBound: Int = if(D != 0) (Ak/D) + 1 else 0
    val bBound: Int = if(B != 0) ((Hk-Ad)/B) + 1  else 0
    var minCount: Int = Int.MaxValue
    
    for(d <- 0 to dBound){
      for(b <- 0 to bBound){
        minCount = Math.min(minCount, fight(d, b))
      }
    }
    if(minCount == Int.MaxValue)
      println("Case #" + t + ": IMPOSSIBLE")
    else
      println("Case #" + t + ": " + minCount)
    
    //println(fight(4,3))  
      
    def fight(d: Int, b : Int): Int = {
      //println("\nnew fight " + d + ", " + b)
      var count: Int = 0
      var curHd: Int = Hd
      var curHk: Int = Hk
      var curAd: Int = Ad
      var curAk: Int = Ak
      
      if(d > 0){
        for(i <- 1 to d){
          //cure
          if(curHd - (curAk - D) <= 0){
            //println("D cure")
            count += 1
            curHd = Hd - curAk
            // twice cure
            if(curHd - (curAk - D) <= 0)
              return Int.MaxValue
          }
          //println("D")
          curAk -= D
          curHd -= curAk
          count += 1
         
          //println("curHd: " + curHd + ", curAd: " + curAd + ", curHk: " + curHk + ", curAk: " + curAk)
        }
      }
      if(b > 0){
        for(i <- 1 to b){
          // cure
          if(curHd - curAk <= 0){
            //println("B cure")
            count += 1
            curHd = Hd - curAk
            // twice cure
            if(curHd - curAk <= 0)
              return Int.MaxValue
          }
          //println("B")
          curAd += B
          curHd -= curAk
          count += 1
          
          //println("curHd: " + curHd + ", curAd: " + curAd + ", curHk: " + curHk + ", curAk: " + curAk)
        }
      }
      
      curAk = Math.max(curAk, 0)
      
      while(curHk > 0){
        // cure
        if(curHd - curAk <= 0 && curAd < curHk){
          //println("A cure")
          count += 1
          curHd = Hd - curAk
          // twice cure
          if(curHd - curAk <= 0 && curAd < curHk)
            return Int.MaxValue
        }
        else{
          //println("A")
          curHd -= curAk
          curHk -= curAd
          count += 1
          if(curHk <= 0)
            return count
        }
        //println("curHd: " + curHd + ", curAd: " + curAd + ", curHk: " + curHk + ", curAk: " + curAk)
      }
      return count
    }
    
    
  }
}