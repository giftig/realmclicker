package com.xantoria.realmclicker

import java.awt.Robot
import java.awt.event.InputEvent

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future


/**
 * Click every few ms, printing the time elapsed / remaining if desired
 */
class Clicker(repeatDelay: Duration, printTime: Option[Duration] = None) {
  private implicit val ec = global
  private val repeatDelayMillis: Long = repeatDelay.toMillis
  private val printTimeMillis: Option[Long] = printTime map { _.toMillis }

  val buttonCode = {
    val Version6 = """^1\.6.*$""".r
    val EarlierVersion = """^1\.[1-5].*$""".r

    System.getProperty("java.version") match {
      case Version6() => InputEvent.BUTTON1_MASK
      case EarlierVersion(v) => throw new RuntimeException(s"Unsupported java version: $v")
      case _ => InputEvent.BUTTON1_DOWN_MASK
    }
  }

  /**
   * Click for `duration` ms after waiting `delay` ms to allow the user time to position
   */
  def click(delay: Duration, duration: Duration): Unit = {
    val durationMillis = duration.toMillis
    Thread.sleep(delay.toMillis)

    var r = new Robot()

    (0 to durationMillis.toInt) foreach {
      tick: Int => {
        if (tick % repeatDelayMillis == 0) {
          r.mousePress(buttonCode)
          r.mouseRelease(buttonCode)
        }

        printTimeMillis foreach {
          case i: Long if tick % i == 0 => {
            val elapsed = tick / 1000
            val remaining = (durationMillis - tick) / 1000
            print(s"${elapsed}s elapsed, ${remaining}s remaining          \r")
          }
          case _ => ()
        }
        Thread.sleep(1)
      }
    }
    println("\nDone!")
  }
}
