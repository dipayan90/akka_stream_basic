package org.kajjoy.akka.streams.basic.flow

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.javadsl.Sink
import akka.stream.scaladsl.{Flow, Source}
import org.kajjoy.akka.streams.basic.model.{InputCustomer, OutputCustomer}
import org.kajjoy.akka.streams.basic.subscriber.FlowSubscriber

object Basicflow {

  def start: Unit = {
    implicit val actorSystem = ActorSystem()
    import actorSystem.dispatcher
    implicit val flowMaterializer = ActorMaterializer()

    val source = Source(1 to 100).map(_=> InputCustomer.random())

    val processor : Flow[InputCustomer,OutputCustomer,NotUsed] = Flow[InputCustomer]
      .map(e => e.name.split(" "))
      .map(e => OutputCustomer(e(0),e(1)))

    val flow = source
        .via(Flow[InputCustomer].map(e =>{
          System.out.println("Input is: " + e)
          e
        }))
      .via(processor)
      .runWith(Sink.actorSubscriber(FlowSubscriber.pops))

  }

}
