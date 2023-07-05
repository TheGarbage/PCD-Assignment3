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

  private def senderBehavior(childReceiver: ActorRef[Msg], receiverList: List[ActorRef[Msg]] = List.empty): Behavior[Msg | Receptionist.Listing] = Behaviors.receive { (ctx, msg) =>
    msg match
      case msg: Receptionist.Listing =>
        val updateReveiverList = msg.serviceInstances(Service).toList
        if (updateReveiverList.size > receiverList.size)
          updateReveiverList.foreach(receiver =>
            if (!receiverList.contains(receiver) && receiver != childReceiver)
              childReceiver ! Msg(Command.sendInit, None, None, None, None, None, Some(receiver)))
        else
          receiverList.foreach(receiver =>
            if (!updateReveiverList.contains(receiver))
              childReceiver ! Msg(Command.removeBrush, None, None, Some(Methods.getReceiverId(receiver)), None, None, None)
          )
        senderBehavior(childReceiver, updateReveiverList)
      case msg: Msg =>
        receiverList.foreach(_ ! msg)
        Behaviors.same
  }

  def apply(): Behavior[Msg | Receptionist.Listing] = Behaviors.setup { ctx =>
    ctx.system.receptionist ! Receptionist.subscribe(Service, ctx.self)
    val brushes = new BrushManager()
    val grid = new PixelGrid(40, 40)
    val childReceiver = ctx.spawn(Receiver(grid, brushes), "Receiver")
    ctx.spawn(Refresher(Methods.initView(ctx.self, Methods.getReceiverId(childReceiver), grid, brushes)), "Refresher")
    senderBehavior(childReceiver)
  }
}