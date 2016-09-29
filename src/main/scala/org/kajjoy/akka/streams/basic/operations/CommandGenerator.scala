package org.kajjoy.akka.streams.basic.operations

import java.time.{Instant, OffsetDateTime, ZoneId}

import org.kajjoy.akka.streams.basic.model.MyEvent
import org.kajjoy.akka.streams.basic.model.window.Window._
import org.kajjoy.akka.streams.basic.model.window._

import scala.concurrent.duration._
import scala.collection.mutable

/**
  * This is the main class that defines when an event should expire
  * and how the window operations need to be defined
  */
class CommandGenerator {

  // This maxDelay specifies that if there is an event that comes in having a timestamp value
  // 5 seconds less than the last event time stamp then expire it
  private val MaxDelay = 5.seconds.toMillis
  private var watermark = 0L
  private val openWindows = mutable.Set[Window]()

  def forEvent(ev: MyEvent): List[WindowCommand] = {
    // check to see if watermark needs to be updated with the current events timestamp
    watermark = math.max(watermark, ev.timestamp - MaxDelay)
    if (ev.timestamp < watermark) {
      println(s"Dropping event with timestamp: ${tsToString(ev.timestamp)}")
      Nil
    } else {
      // create window object here
      val eventWindows = Window.windowsFor(ev.timestamp)

      // define close operation here
      val closeCommands = openWindows.flatMap { ow =>
        // if the event window's end time is less than watermark value , close the window
        if (!eventWindows.contains(ow) && ow._2 < watermark) {
          // remove the window
          openWindows.remove(ow)
          // return a CloseWindow object
          Some(CloseWindow(ow))
        } else None
      }

      val openCommands = eventWindows.flatMap { w =>
        if (!openWindows.contains(w)) {
          // Add the new window to the list
          openWindows.add(w)
          // return OpenWindow object
          Some(OpenWindow(w))
        } else None
      }

      // add event to an already existing window
      val addCommands = eventWindows.map(w => AddToWindow(ev, w))

      openCommands.toList ++ closeCommands.toList ++ addCommands.toList
    }
  }

  def tsToString(ts: Long) = OffsetDateTime
    .ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault())
    .toLocalTime
    .toString

}
