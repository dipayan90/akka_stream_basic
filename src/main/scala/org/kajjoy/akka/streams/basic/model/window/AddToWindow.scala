package org.kajjoy.akka.streams.basic.model.window

import org.kajjoy.akka.streams.basic.model.MyEvent
import org.kajjoy.akka.streams.basic.model.window.Window.Window

case class AddToWindow(ev: MyEvent, w: Window) extends WindowCommand
