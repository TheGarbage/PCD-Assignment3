package es2.actors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.ConfigFactory
import es2.example.{BrushManager, PixelGrid, PixelGridView}
import es2.resources.Message.{Command, Msg}
import es2.resources.Methods

import scala.util.Random

object Receiver {

  def receiverBehavior(grid: PixelGrid, brushes: BrushManager): Behavior[Msg] = Behaviors.receive{ (ctx, msg) =>
    msg.command match
      case Command.brushColorChange =>
        brushes.getBrush(msg.id.get).setColor(msg.color.get)
      case Command.gridColorChange =>
        grid.set(msg.x.get, msg.y.get, brushes.getBrush(msg.id.get).getColor)
      case Command.mousePositionChange =>
        brushes.getBrush(msg.id.get).updatePosition(msg.x.get, msg.y.get)
      case Command.init =>
        brushes.addBrush(msg.id.get,  new BrushManager.Brush(msg.x.get, msg.y.get, msg.color.get, true))
        Methods.updateGrid(grid, msg.grid.get)
      case Command.sendInit =>
        val id = Methods.getReceiverId(ctx.self)
        val brush = brushes.getBrush(id)
        msg.receiver.get ! Msg(Command.init, Some(brush.getX), Some(brush.getY), Some(id), Some(brush.getColor), Some(grid), None)
      case Command.removeBrush =>
        brushes.removeBrush(msg.id.get)
      case _ =>
    Behaviors.same
  }
  
  def apply(grid: PixelGrid, brushes: BrushManager): Behavior[Msg] = Behaviors.setup {
    ctx =>
      ctx.system.receptionist ! Receptionist.register(Sender.Service, ctx.self)
      brushes.addBrush(Methods.getReceiverId(ctx.self), new BrushManager.Brush(0, 0, new Random().nextInt(256 * 256 * 256), false))
      receiverBehavior(grid, brushes)
  }
}