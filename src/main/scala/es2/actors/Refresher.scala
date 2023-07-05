package es2.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import es2.example.PixelGridView
import concurrent.duration.DurationInt

object Refresher{
  private val REFRESH_TIME = 10.millis

  def apply(view: PixelGridView) : Behavior[String] = Behaviors.withTimers { timers =>
    timers.startSingleTimer("Refresh", REFRESH_TIME)
    Behaviors.receiveMessage { msg =>
      view.refresh()
      Refresher(view)
    }
  }
}