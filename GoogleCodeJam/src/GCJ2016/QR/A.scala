package GCJ2016.QR

/**
 * Counting Sheep
 * Solved by myself, easy.
 */
object A extends App{
  import scala.io.Source;
  import scala.collection.mutable.HashSet;
  
  val lines = Source.fromFile("src/GCJ2016/QR/A-large-practice.in").getLines(); 
  val T: Int = lines.next().toInt;
  
  for(t <- 1 to T){
    val n: Int = lines.next().toInt;
    
    // will count to forever, since N * 0 = 0, for any N >= 1.
    if(n == 0) println("Case #" + t + ": INSOMNIA");
    // it takes finite amount of times to multiply n so that all digits are covered
    else{
      val set = new HashSet[Char]();
      
      val res: Int = Stream.from(1).map(_ * n).dropWhile{ x => x.toString().map(set.add(_)); set.size < 10}.head;
      
      println("Case #" + t + ": " + res);
    }
  }
}