package org.kajjoy.akka.streams.basic.subscriber

import akka.actor.{ActorLogging, Props}
import akka.stream.actor.ActorSubscriberMessage.{OnComplete, OnError, OnNext}
import akka.stream.actor.{ActorSubscriber, RequestStrategy, WatermarkRequestStrategy}
import org.kajjoy.akka.streams.basic.model.OutputCustomer

/**
  * Created by dipayan on 9/24/16.
  */

object BasicFlowSubscriber {
  def pops: Props = Props(new BasicFlowSubscriber())
}

class BasicFlowSubscriber extends ActorSubscriber with ActorLogging{
  override protected def requestStrategy: RequestStrategy = new RequestStrategy {
    val underlying = WatermarkRequestStrategy(5)
    override def requestDemand(remainingRequested: Int): Int = {
      val result = underlying.requestDemand(remainingRequested)
      result
    }
  }

  override def receive: Receive = {
    case OnNext(customer: OutputCustomer) => {
      System.out.println("First name is: "+ customer.firstname + " and last name is: "+ customer.lastname)
    }
    case OnError(err : Exception) =>
      context.stop(self)
    case OnComplete =>
      context.stop(self)
    case _ =>
  }
}
