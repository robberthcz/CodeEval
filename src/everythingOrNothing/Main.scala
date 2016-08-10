package everythingOrNothing

object Main extends App {
  val source = scala.io.Source.fromFile("test-cases/everythingOrNothing.txt")
  val lines = source.getLines.filter(_.length > 0)
  for (l <- lines) {
    System.out.println(l)
  }
}