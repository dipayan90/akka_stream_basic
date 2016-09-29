package org.kajjoy.akka.streams.basic

import org.kajjoy.akka.streams.basic.flow._

/**
  * Main application runner, comment out flows to run individual ones.
  * Will have to work on it to make the execution of this project
  * little more user friendly
  */
object Application extends App{

  override def main(args : Array[String]): Unit ={
     /* System.out.print("Basic Flow")
    Basicflow.start
    System.out.println("Multiple Source Flow")
    MultipleSourceFlow.start
    System.out.println("Broadcast and merge Flow")
    BroadcastAndMergeFlow.start
    System.out.println("Variable time flow")
    VariableTimeFlow.start */
    System.out.println("Time window flow")
    TimeWindowFlow.start
  }

}
