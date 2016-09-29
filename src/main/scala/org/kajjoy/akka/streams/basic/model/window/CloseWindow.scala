package org.kajjoy.akka.streams.basic.model.window

import org.kajjoy.akka.streams.basic.model.window.Window.Window


case class CloseWindow(w: Window) extends WindowCommand

