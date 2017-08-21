package scrape

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._

class DisneyScrape {

  def getSeaWaitTimes(): List[(String, String)] = {
    getWaitTimes("sea")
  }

  def getLandWaitTimes(): List[(String, String)] = {
    getWaitTimes("land")
  }

  private def getWaitTimes(kind: String): List[(String, String)] = {
    val doc = JsoupBrowser().get(s"http://disneyreal.asumirai.info/realtime/disney$kind-wait-today.html")

    val waitTimesPair = for {
      waitTimeDiv <- doc >> elements(".wait-time > li > a > .area > .about")
      attraction <- waitTimeDiv >> elements("h4")
      waitTime <- waitTimeDiv >> elements(".run")
    } yield {
      (attraction >> text("h4"), (waitTime >> text(".run")).diff(" （アトラクション詳細・混雑予想はこちら）"))
    }

    return waitTimesPair.toList
  }
}

