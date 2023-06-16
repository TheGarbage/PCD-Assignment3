package es2.actors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.ConfigFactory
import es2.resources.Message

object Receiver {

  final case class Command(i: Int) extends Message
  
  def apply(): Behavior[Command] = Behaviors.setup {
    ctx =>
      ctx.system.receptionist ! Receptionist.register(Sender.Service, ctx.self)
      Behaviors.empty
  }
}