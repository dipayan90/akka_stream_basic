package org.kajjoy.akka.streams.basic.model.window

import org.kajjoy.akka.streams.basic.model.window.Window.Window

trait WindowCommand {
  def w: Window
}
