package es2.actors

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.ClusterEvent
import com.typesafe.config.ConfigFactory
import es2.resources.Message

object Sender {
  final case class Msg() extends Message

  val Service: ServiceKey[Receiver.Command] = ServiceKey[Receiver.Command]("Receiver")

  private def senderBehavior(reveiverList: List[ActorRef[Receiver.Command]] = List.empty): Behavior[Msg | Receptionist.Listing] = Behaviors.receive {
    (ctx, msg) => msg match
      case msg: Receptionist.Listing =>
        println("nuovo actor " + msg.serviceInstances(Service).toList.size)
        senderBehavior(msg.serviceInstances(Service).toList)
      case _ =>
        reveiverList.foreach(_ ! Receiver.Command(1))
        Behaviors.same
  }

  def apply(): Behavior[Msg | Receptionist.Listing] = Behaviors.setup { ctx =>
    ctx.system.receptionist ! Receptionist.subscribe(Service, ctx.self)
    ctx.spawn(Receiver(), "Receiver")
    senderBehavior()
  }
}