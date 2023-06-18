package es2.actors

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.ClusterEvent
import com.typesafe.config.ConfigFactory
import es2.example.{BrushManager, PixelGrid}
import es2.resources.Message.{Command, Msg}
import es2.resources.Methods

object Sender {

  val Service: ServiceKey[Msg] = ServiceKey[Msg]("Receiver")

  private def senderBehavior(id: Integer, child: ActorRef[Msg], reveiverList: List[ActorRef[Msg]] = List.empty): Behavior[Msg | Receptionist.Listing | ClusterEvent.MemberRemoved] = Behaviors.receiveMessage {
    case msg: Receptionist.Listing =>
      println( msg.serviceInstances(Service).toList)
      msg.serviceInstances(Service).toList.foreach(receiver =>
        if (!reveiverList.contains(receiver))
          child ! Msg(Command.sendInit, None, None, None, None, None, Some(receiver)))
      senderBehavior(id, child, msg.serviceInstances(Service).toList)
    case msg: Msg =>
      reveiverList.foreach(_ ! msg)
      Behaviors.same
    case event: ClusterEvent.MemberRemoved =>
      println(event.member.address)
      Behaviors.same
  }

  def apply(id: Integer): Behavior[Msg | Receptionist.Listing | ClusterEvent.MemberRemoved] = Behaviors.setup { ctx =>
    ctx.system.receptionist ! Receptionist.subscribe(Service, ctx.self)
    val brushes = new BrushManager()
    val grid = new PixelGrid(40, 40)
    senderBehavior(id, ctx.spawn(Receiver(id, grid, brushes, Methods.initView(ctx.self, id, grid, brushes)), "Receiver"))
  }
}