package org.kajjoy.akka.streams.basic.flow

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.javadsl.Sink
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Source}
import org.kajjoy.akka.streams.basic.subscriber.MultipleSourceSubscriber

object BroadcastAndMergeFlow {

  def start: Unit = {
    implicit val actorSystem = ActorSystem()
    import actorSystem.dispatcher
    import akka.stream.scaladsl.GraphDSL.Implicits._
    implicit val flowMaterializer = ActorMaterializer()

    RunnableGraph.fromGraph(GraphDSL.create() {
      implicit b =>
        import GraphDSL.Implicits._

        val source = Source(1 to 100)

        val sink = Sink.actorSubscriber(MultipleSourceSubscriber.pops)

        val rule1 = Flow[Int].map(_ * 2)

        val rule2 = Flow[Int].map(_/2)

        val rule3 = Flow[Int].map(_+2)

        val rule4 = Flow[Int].map(_-2)

        val merge = b.add(Merge[Int](4))

        val broadcast = b.add(Broadcast[Int](4))

        source ~> broadcast ~> rule1 ~> merge
                  broadcast ~> rule2 ~> merge
                  broadcast ~> rule3 ~> merge
                  broadcast ~> rule4 ~> merge ~> sink

        ClosedShape
    }).run()

  }
}
