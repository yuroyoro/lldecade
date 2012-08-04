import sbt._
import sbt.Keys._

object LlDecadeDemoBuild extends Build {

  lazy val llDecadeDemo = Project(
    id = "ll-decade-demo",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "LL DECADE DEMO",
      organization := "com.yuroyoro",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2"
      // add other settings here
    )
  )
}
