package es2.resources

import akka.actor.AddressFromURIString
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.cluster.typed.{Cluster, Join}
import akka.remote.RemoteTransportException
import akka.util.Helpers
import com.typesafe.config.ConfigFactory

import scala.util.Random
import es2.actors.Sender
import es2.example.{BrushManager, PixelGrid, PixelGridView}
import es2.resources.Message.{Command, Msg}

import scala.annotation.tailrec


object Methods:
  private val DEFAULT_PORT = 2551

  @tailrec
  def startup(port: Int = DEFAULT_PORT, clusterPort: Int = DEFAULT_PORT): Unit = {
    try {
      val config = ConfigFactory
        .parseString(s"""akka.remote.artery.canonical.port=$port""")
        .withFallback(ConfigFactory.load("cluster"))
      val system = ActorSystem(Sender(), "ClusterSystem", config)
      val seed = AddressFromURIString.parse(s"akka://ClusterSystem@127.0.0.1:$DEFAULT_PORT")
      Cluster(system).manager ! Join(seed)
    } catch {
      case _: RemoteTransportException => startup(new Random().nextInt(65535))
    }
  }

  def initView(actorRef: ActorRef[Msg | Receptionist.Listing], id: Int, grid: PixelGrid, brushManager: BrushManager) : PixelGridView = {
    val view = new PixelGridView(grid, brushManager, 600, 600)
    view.addMouseMovedListener((x: Int, y: Int) => {
      actorRef ! Msg(Command.mousePositionChange, Some(x), Some(y), Some(id), None, None, None)
    })
    view.addPixelGridEventListener((x: Int, y: Int) => {
      actorRef ! Msg(Command.gridColorChange, Some(x), Some(y), Some(id), None, None, None)
    })
    view.addColorChangedListener(color => {
      actorRef ! Msg(Command.brushColorChange, None, None, Some(id), Some(color), None, None)
    })
    view.display()
    view
  }

  def updateGrid(myGrid: PixelGrid, otherGrid: PixelGrid): Unit = {
    for (i <- 0 until myGrid.getNumRows)
      for (j <- 0 until myGrid.getNumColumns)
        if (myGrid.get(i, j) != otherGrid.get(i, j) && myGrid.get(i, j) == 0)
          myGrid.set(i, j, otherGrid.get(i, j))
  }

  def getReceiverId(actorRef: ActorRef[Msg]): Int = actorRef.toString.split("#")(1).split("]")(0).toInt
