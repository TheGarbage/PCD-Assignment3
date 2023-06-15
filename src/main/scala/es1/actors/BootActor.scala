package es1.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import es1.resources.View

object BootActor{
  def apply(view: View) : Behavior[MainActor.Msg] = Behaviors.setup { ctx =>
    MainActor(view, ctx.spawn(RenderActor(view), "RenderActor"))
  }
}