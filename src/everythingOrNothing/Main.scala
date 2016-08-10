package everythingOrNothing

object Main extends App {
  val source = scala.io.Source.fromFile("test-cases/everythingOrNothing.txt")
  val lines = source.getLines.filter(_.length > 0)
  for (l <- lines) {
    System.out.println(l)
    if(parseEvents(l.split(" ").toList, acRightsTable).dropWhile(x => x).isEmpty)
      System.out.println("True")
    else
      System.out.println("False")
  }
  
  val acRightsTable: Vector[Vector[Int]] = Vector(Vector(7, 3, 0), Vector(6, 2, 4), Vector(5, 1, 5), Vector(3, 7, 1), Vector(6, 0, 2), Vector(4, 2, 6))
  
  def isAllowed(user1: Int, file1: Int, action: Char, vec: Vector[Vector[Int]]): Boolean = {
    val right: Int = vec(user1 - 1)(file1 - 1)
    action match{
      case 'w' => 4 <= right
      case 'r' => right == 2 || right == 3 || right == 6 || right == 7
      case 'g' => right % 2 == 1
    }      
  }
  
  def grantRight(grantUser: Int, file1: Int, grantAction: Char, vec: Vector[Vector[Int]]): Vector[Vector[Int]] = {
    val right: Int = vec(grantUser - 1)(file1 - 1)
    grantAction match {
      case 'w' if (right == 0 || right == 1 || right == 4 || right == 5) => vec.updated(grantUser - 1, vec(grantUser - 1).updated(file1 - 1, right + 2))
      case 'r' if right < 4 => vec.updated(grantUser - 1, vec(grantUser - 1).updated(file1 - 1, right + 4))
      case 'g' if right % 2 == 0 => vec.updated(grantUser - 1, vec(grantUser - 1).updated(file1 - 1, right + 1))
    }
  }
  
  def parseEvents(events: List[String], vec: Vector[Vector[Int]]): Stream[Boolean] = {
    if(events.isEmpty)
      return Stream.Empty
    val event = events.head
    val user1 : Int = event.charAt(5).toInt
    val file1: Int = event.charAt(13).toInt
    val action: Char = event.charAt(16)
    if(action == 'g'){
      val grantAction: Char = event.charAt(23)
      val grantUser: Int = event.charAt(event.length() - 1).toInt
      val updatedVec = grantRight(grantUser, file1, grantAction, vec)
      return isAllowed(user1, file1, action, updatedVec) #:: parseEvents(events.tail, updatedVec)
    }
    else
      return isAllowed(user1, file1, action, vec) #:: parseEvents(events.tail, vec)
  }
}