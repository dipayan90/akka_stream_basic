package org.kajjoy.akka.streams.basic

import org.kajjoy.akka.streams.basic.flow.{Basicflow, BroadcastAndMergeFlow, MultipleSourceFlow}

object Application extends App{

  override def main(args : Array[String]): Unit ={
    System.out.print("Basic Flow")
    Basicflow.start
    System.out.println("Multiple Source Flow")
    MultipleSourceFlow.start
    System.out.println("Broadcast and merge Flow")
    BroadcastAndMergeFlow.start
  }

}
