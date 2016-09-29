package org.kajjoy.akka.streams.basic.model.window

import scala.concurrent.duration._

/**
  *  Define a window type that takes in (startTime, endTime)
  */
object Window {

  type Window = (Long, Long)

  val WindowLength = 10.seconds.toMillis
  val WindowStep = 1.second.toMillis
  val WindowsPerEvent = (WindowLength / WindowStep).toInt

  // Decide the window start and end times, currently the window size is
  // 10 secs and this sliding window slides after every one sec
  def windowsFor(ts: Long): Set[Window] = {
    val firstWindowStart = ts - ts % WindowStep - WindowLength + WindowStep
    (for (i <- 0 until WindowsPerEvent) yield
      (firstWindowStart + i * WindowStep,
        firstWindowStart + i * WindowStep + WindowLength)
      ).toSet
  }

}