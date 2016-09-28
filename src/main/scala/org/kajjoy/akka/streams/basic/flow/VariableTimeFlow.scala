package org.kajjoy.akka.streams.basic.flow

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.javadsl.Sink
import akka.stream.scaladsl.{Flow, GraphDSL, Merge, RunnableGraph, Source, ZipWith}
import org.kajjoy.akka.streams.basic.subscriber.MultipleSourceSubscriber

import scala.concurrent.duration._


object VariableTimeFlow {

  def start: Unit = {
    implicit val actorSystem = ActorSystem()
    import actorSystem.dispatcher
    import akka.stream.scaladsl.GraphDSL.Implicits._
    implicit val flowMaterializer = ActorMaterializer()

    RunnableGraph.fromGraph(GraphDSL.create() {
      implicit  b =>
        import GraphDSL.Implicits._

        val source1 = Source(1 to 100)

        val source2 = Source.tick(initialDelay = 1 second, interval = 1 second, ())

        val sink = Sink.actorSubscriber(MultipleSourceSubscriber.pops)

        val zip = b.add(ZipWith((s1: Int, s2: Unit) => s1))

        source1 ~> zip.in0

        source2 ~> zip.in1

        zip.out ~> sink

        ClosedShape

    } ).run()

  }

}
