package org.kajjoy.akka.streams.basic

import org.kajjoy.akka.streams.basic.flow.Basicflow

object Application extends App{

  override def main(args : Array[String]): Unit ={
    System.out.print("Dipayan")
    Basicflow.start
  }

}