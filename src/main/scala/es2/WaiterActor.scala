package es2

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import com.typesafe.config.ConfigFactory

import java.io.File

object WaiterActor {
  def startup[X](port: Int)(root: => Behavior[X]): ActorSystem[X] = {
    val config = ConfigFactory
      .parseString(s"""akka.remote.artery.canonical.port=$port""")
      .withFallback(ConfigFactory.load("cluster"))

    ActorSystem(root, "ClusterSystem", config)
  }

  def apply(): Behavior[Receptionist.Listing] =
    Behaviors.setup[Receptionist.Listing] { ctx =>
      ctx.system.receptionist ! Receptionist.Subscribe(ServiceKey[Message]("prova"), ctx.self) // register to new frontend
      Behaviors.receive{ (_,msg) =>
        println("nuovo actor")
        Behaviors.same
      }
    }
}