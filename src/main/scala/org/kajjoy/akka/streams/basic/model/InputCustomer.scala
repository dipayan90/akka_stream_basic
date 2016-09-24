package org.kajjoy.akka.streams.basic.model

import scala.util.Random

/**
  * Created by dipayan on 9/24/16.
  */
case class InputCustomer (name: String) {

}

object  InputCustomer{
  def random():InputCustomer = {
    InputCustomer(s"FirstName${Random.nextInt(1000)} LastName${Random.nextInt(1000)}")
  }
}
