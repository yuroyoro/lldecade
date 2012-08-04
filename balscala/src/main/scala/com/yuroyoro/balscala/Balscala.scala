package com.yuroyoro.balscala

object Balscala extends App {
  import com.yuroyoro.balscala._
  import scala.sys.process._
  import dispatch._
  import Http._
  import json.Js._
  import twitter._
  import oauth._

  val server = {
    val props = properties("julius.properties")
    val path   = props.get("julius_path").get
    val config = props.get("julius_conf").get

    Julius.start(path, config)
  }

  Thread.sleep(3000)

  server.connect{ node =>
    node \\ "WHYPO" \\ "@WORD" mkString match {
      case word if isBalse(word) => balse(word)
      case word if word.length > 2 => tweet(word)
      case _ =>
    }
  }

  def isBalse(word:String) = {
    val utf8Word = new String(word.getBytes("shift_jis"), "utf-8")
    println("#" * 80)
    println(utf8Word)
    println("#" * 80)
    List("バルス", "パルス").exists{s => utf8Word.startsWith(s)}
  }

  def balse(word:String) = {
    println(word)
    tweet("バルス")
    Thread.sleep(2000)
    val balse = "shutdown -h now"
    println(balse)
    Process(balse) !
  }

  def tweet(word:String) = {
    val utf8Word = new String(word.getBytes("shift_jis"), "utf-8")
    val req = Status.update(utf8Word + " #lldecade", consumer, token)
    http(req >>> System.out)
  }

  lazy val http = new Http
  lazy val consumer = {
    val props = properties("consumer.properties")
    Consumer(props.get("key").get, props.get("secret").get)
  }

  lazy val token = {
    val props = properties("accesstoken.properties")
    Token(props).get
  }

  def properties(name:String) = {
    import scala.collection.JavaConversions._

    val props = new java.util.Properties
    props.load(new java.io.FileInputStream(name))
    props.collect{case (k,v) => (k.toString, v.toString)}
  }
}

