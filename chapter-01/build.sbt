name := "essential-slick-chapter-01"

version := "3.2"

scalaVersion := "2.12.3"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Xfatal-warnings"
)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

initialCommands in console := """
  |import slick.jdbc.H2Profile.api._
  |import Example._
  |import scala.concurrent.duration._
  |import scala.concurrent.Await
  |import scala.concurrent.ExecutionContext.Implicits.global
  |val db = Database.forConfig("chapter01")
  |def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)
  |exec(messages.schema.create)
  |exec(messages ++= freshTestData)
""".trim.stripMargin
