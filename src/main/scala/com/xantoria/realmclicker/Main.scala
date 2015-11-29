package com.xantoria.realmclicker

import scala.concurrent.duration._

case class Config(
  initialDelay: Duration = 2.seconds,
  repeatDelay: Duration = 30.milliseconds,
  printDelay: Option[Duration] = Some(1.second),
  duration: Duration = 20.seconds
)

object Main {
  val optionParser = new scopt.OptionParser[Config]("Realm Clicker") {
    head("Realm Clicker", "0.1")

    opt[String]('i', "initial-delay") action {
      (initialDelay, c) => c.copy(initialDelay = Duration(initialDelay))
    } text("Time to wait before we start clicking; default 2 seconds")

    opt[String]('r', "repeat-delay") action {
      (repeatDelay, c) => c.copy(repeatDelay = Duration(repeatDelay))
    } text("Time to wait between clicks; default 30 milliseconds")

    opt[String]('p', "print-delay") action {
      (printDelay, c) => {
        c.copy(printDelay = if (printDelay == "0") None else Some(Duration(printDelay)))
      }
    } text("Time to wait between messages indicating remaining duration; default 1s. 0 means off")

    opt[String]('d', "duration") action {
      (duration, c) => c.copy(duration = Duration(duration))
    } text("Click repeatedly for this length of time; default 20 seconds")
  }

  def main(args: Array[String]): Unit = {
    optionParser.parse(args, Config()) foreach {
      config: Config => {
        val clicker = new Clicker(config.repeatDelay, config.printDelay)
        clicker.click(config.initialDelay, config.duration)
      }
    }
  }
}
