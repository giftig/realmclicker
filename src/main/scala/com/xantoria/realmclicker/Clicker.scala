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
          r.mousePress(InputEvent.BUTTON1_DOWN_MASK)
          r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
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
