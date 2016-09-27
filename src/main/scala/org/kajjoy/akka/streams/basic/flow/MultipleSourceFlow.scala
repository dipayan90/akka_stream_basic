package org.kajjoy.akka.streams.basic.flow

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, SourceShape}
import akka.stream.javadsl.Sink
import akka.stream.scaladsl.{Flow, GraphDSL, Merge, RunnableGraph, Source}
import org.kajjoy.akka.streams.basic.subscriber.MultipleSourceSubscriber

object MultipleSourceFlow {

  def start: Unit = {
    implicit val actorSystem = ActorSystem()
    import actorSystem.dispatcher
    import akka.stream.scaladsl.GraphDSL.Implicits._
    implicit val flowMaterializer = ActorMaterializer()

    RunnableGraph.fromGraph(GraphDSL.create() {
      implicit  b =>
        import GraphDSL.Implicits._

        val source1 = Source(101 to 200)

        val source2 = Source(1 to 100)

        val sink = Sink.actorSubscriber(MultipleSourceSubscriber.pops)

        val transform = Flow[Int].map(_ * 2).to(sink)

        val merge = b.add(Merge[Int]( 2 ))

        source1 ~> merge
        source2 ~> merge  ~> transform

        ClosedShape
    } ).run()

  }



}
