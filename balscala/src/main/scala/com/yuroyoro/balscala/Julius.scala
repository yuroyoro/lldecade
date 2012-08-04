package com.yuroyoro.balscala

object Julius {

  class Server(path:String, config:String){
    import scala.sys.process._
    import scala.xml.{XML, Node}
    import java.net._
    import java.io._

    def command = "%s/julius -C %s -charconv EUC-JP UTF-8 -module" format(path, config)
    val process = Process(command).run

    def stop = process.destroy

    def connect(f:Node => Unit) = {
      val socket = new Socket("localhost", 10500)
      val in = socket.getInputStream()
      val r = """WORD="([^"]+)CLASSID""".r

      def adjustLine(s:String) =
        r.replaceAllIn(s.replaceAll("\\<\\/?s\\>", "&lt;\\/s&gt;").filterNot{'\u7e32' == }.filterNot{'\ufffd' == },
          m => { """WORD="%s" CLASSID""" format( m.group(1))  })


      val buf = scala.collection.mutable.ArrayBuffer.empty[String]
      consume(in) {
        case "." =>
          try {
            val s= buf.mkString("\n")
            println("-" * 80)
            println(s)
            println("-" * 80)
            val xml = XML.loadString(s)
            buf.clear
            f(xml)
          } catch {
            case e =>
              e.printStackTrace
              buf.clear
          }
        case l   =>  buf += adjustLine(l)
      }
    }

    private def consume(in:InputStream)( f:String => Unit){
      val buf = new Array[Byte](1024)
      var remains:String = ""
      try{
        // InputStreamから1行読んでfにわたす
        for(i <- Stream.continually(in.read(buf)).takeWhile(_ != -1)){
          val str = remains + new String(buf, 0, i, "shift_jis")
          remains = ( "" /: str){ (s,c) =>
            if( c == '\n'){
              f(s)
              ""
            }
            else s + c
          }
        }
     }
     catch{ case e:IOException =>
       e.printStackTrace
     }
     finally{ in.close }
    }

  }

  def start(path:String, config:String) = new Server(path, config)

}

