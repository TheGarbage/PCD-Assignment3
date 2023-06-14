package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import resources.View

object BootActor{
  def apply(view: View) : Behavior[MainActor.Msg] = Behaviors.setup { ctx =>
    MainActor(view, ctx.spawn(RenderActor(view), "RenderActor"))
  }
}