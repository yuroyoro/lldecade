package com.yuroyoro

import com.twitter.util.Eval
import twitter4j._
import conf._
import scala.sys.process._

object ReadTwitterStream extends App {

  val listener: StatusListener = new StatusListener {
    def onStatus(status: Status) = {
      val command = """say -v Kyoko "%s %s" """ format(status.getUser.getName, status.getText)
      println(status.getUser.getName + " : " + status.getText)
      Process(command) !
    }

    def onDeletionNotice(s: StatusDeletionNotice) = {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) = {}
    def onException(ex: Exception) = ex.printStackTrace()
    def onScrubGeo(userId: Long, upToStatusId: Long) = {}
  }

  val eval = new Eval()
  val config = eval[TwitterConfig](new java.io.File("config/Config.scala"))
  val configurationBuilder = new ConfigurationBuilder()

  val conf = configurationBuilder.setOAuthConsumerKey(config.consumerKey)
          .setOAuthConsumerSecret(config.consumerSecret)
          .setOAuthAccessToken(config.accessToken)
          .setOAuthAccessTokenSecret(config.accessTokenSecret)
          .build

  val twitterStream: TwitterStream = new TwitterStreamFactory(conf).getInstance

  val filter:FilterQuery  = new FilterQuery()
  filter.track(Array("lldecade", "yuroyoro"))

  val filterStream = twitterStream.getFilterStream(filter)

  while (true) {
    filterStream.next(listener)
  }
}
