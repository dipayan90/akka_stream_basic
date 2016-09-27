package org.kajjoy.akka.streams.basic.subscriber

import akka.actor.{ActorLogging, Props}
import akka.stream.actor.ActorSubscriberMessage.{OnComplete, OnError, OnNext}
import akka.stream.actor.{ActorSubscriber, RequestStrategy, WatermarkRequestStrategy}

object MultipleSourceSubscriber {
  def pops: Props = Props(new MultipleSourceSubscriber())
}

class MultipleSourceSubscriber extends ActorSubscriber with ActorLogging{
  override protected def requestStrategy: RequestStrategy = new RequestStrategy {
    val underlying = WatermarkRequestStrategy(5)
    override def requestDemand(remainingRequested: Int): Int = {
      val result = underlying.requestDemand(remainingRequested)
      result
    }
  }

  override def receive: Receive = {
    case OnNext(value : Int) => {
      System.out.println("Output is: "+ value)
    }
    case OnError(err : Exception) =>
      context.stop(self)
    case OnComplete =>
      context.stop(self)
    case _ =>
  }
}
