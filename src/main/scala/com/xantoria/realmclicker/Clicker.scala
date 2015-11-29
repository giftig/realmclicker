package com.xantoria.realmclicker

import java.awt.Robot
import java.awt.event.InputEvent

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future


/**
 * Click every few ms, printing the time elapsed / remaining if desired
 */
class Clicker(repeatDelay: Duration, printTime: Option[Duration] = None) {
  private implicit val ec = global
  private val repeatDelayMillis: Long = repeatDelay.toMillis

  /**
   * Click for `duration` ms after waiting `delay` ms to allow the user time to position
   */
  def click(delay: Duration, duration: Duration): Unit = {
    val durationMillis = duration.toMillis
    Thread.sleep(delay.toMillis)
    var r = new Robot()

    /*
     * Print every `printTime` if provided
     * Note that since this is completely asynchronous and doesn't factor in the length of time
     * taken for the parts of the main loop which aren't sleeps, this may not be accurate over
     * a long duration.
     */
    printTime foreach {
      printDelay: Duration  => Future {
        val printDelayMillis = printDelay.toMillis
        (0 to (durationMillis / printDelayMillis).toInt) foreach {
          i: Int => {
            val elapsed = i * printDelay
            val remaining = duration - elapsed
            println(s"$elapsed elapsed, $remaining remaining")
            Thread.sleep(printDelayMillis)
          }
        }
      }
    }

    (0 to (duration / repeatDelay).toInt) foreach {
      _ => {
        r.mousePress(InputEvent.BUTTON1_DOWN_MASK)
        r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        Thread.sleep(repeatDelayMillis)
      }
    }
  }
}
