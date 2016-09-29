package org.kajjoy.akka.streams.basic.flow


import java.time._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.kajjoy.akka.streams.basic.model.MyEvent

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random
import org.kajjoy.akka.streams.basic.model.window.Window.Window
import org.kajjoy.akka.streams.basic.model.window._
import org.kajjoy.akka.streams.basic.operations.CommandGenerator

object TimeWindowFlow {

  def start : Unit = {

    implicit val as = ActorSystem()
    implicit val mat = ActorMaterializer()

    val random = new Random()

    val f = Source
      //Every second generate a value
      .tick(initialDelay =  0.seconds, interval = 1.second, "")
      .map { _ =>
        val now = System.currentTimeMillis()
        val delay = random.nextInt(8)
        // Create a my event object that takes the random timestamp created.
        MyEvent(now - delay * 1000L)
      }
      /*
      The no-argument function provided to statefulMapConcat will be called each time the stream will be materialized.
      This allows us to enclose over mutable state safely - it won’t be shared by multiple threads.
      The result of the no-arg function should be a mapping function,
      translating each event to a list of elements to emit downstream - here we will generate commands from events.
       */
      .statefulMapConcat { () =>
        val generator = new CommandGenerator()
        ev => generator.forEvent(ev)
      }
      // Group-by will produce sub-streams so
      // 64 here is the max number of those sub-streams that will be supported
      .groupBy(64, command => command.w)
      // keep sending events until the command result is not closedWindow
      .takeWhile(!_.isInstanceOf[CloseWindow])
      // do aggregation operation on the events added to the window
      .fold(AggregateEventData((0L, 0L), 0)) {
        case (agg, OpenWindow(window)) => agg.copy(w = window)
        // always filtered out by takeWhile, so may not be required
        case (agg, CloseWindow(_)) => agg
          // add up the events that come in this window
        case (agg, AddToWindow(ev, _)) => agg.copy(eventCount = agg.eventCount + 1)
      }
      .async
      // merge all the sub-streams generated by groupby
      .mergeSubstreams
      // Add sink and print the results
      .runForeach { agg =>
        println(agg.toString)
      }

    // do this thing till 60 minutes and then finally terminate the flow
    try Await.result(f, 60.minutes)
    finally as.terminate()
  }

  case class AggregateEventData(w: Window, eventCount: Int) {
    override def toString =
      s"Between ${tsToString(w._1)} and ${tsToString(w._2)}, there were $eventCount events."
  }

  def tsToString(ts: Long) = OffsetDateTime
    .ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault())
    .toLocalTime
    .toString


}