// Import the Slick interface for H2:
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object ExampleBook extends App {

  // Case class representing a row in our table:
  final case class City(
    name:  String,
    postalCode: String,
    countryCode: String)

//  // Helper method for creating test data:
//  def freshTestData = Seq(
//    Message("Dave", "Hello, HAL. Do you read me, HAL?"),
//    Message("HAL",  "Affirmative, Dave. I read you."),
//    Message("Dave", "Open the pod bay doors, HAL."),
//    Message("HAL",  "I'm sorry, Dave. I'm afraid I can't do that.")
//  )

  // Schema for the "message" table:
  final class CitiesTable(tag: Tag)
      extends Table[City](tag, "cities") {

    def name      = column[String]("name", O.PrimaryKey)
    def postalCode  = column[String]("postal_code")
    def countryCode = column[String]("country_code")

    def * = (name, postalCode, countryCode).mapTo[City]
  }

  // Base query for querying the cities table:
  lazy val cities = TableQuery[CitiesTable]
  println(cities.schema.createStatements.mkString)

  // An example query that selects a subset of cities:
  val halSays = cities.filter(_.postalCode === "01488")

  // Create an in-memory H2 database;
  val db = Database.forConfig("book")

  // Helper method for running a query in this example file:
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

//  // Create the "messages" table:
//  println("Creating database table")
//  exec(messages.schema.create)
//
//  // Create and insert the test data:
//  println("\nInserting test data")
//  exec(messages ++= freshTestData)

  // Run the test query and print the results:
  println("\nSelecting all cities:")
  exec( cities.result ) foreach { println }

  println("\nSelecting only right-winged cities:")
  exec( halSays.result ) foreach { println }
}
