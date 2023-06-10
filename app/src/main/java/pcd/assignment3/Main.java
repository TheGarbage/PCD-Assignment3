package pcd.assignment3;

import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String... args){
        final ActorSystem<Protocol.Msg> helloWorldActor = ActorSystem.create(Actor.create(),
                "hello-actor");

        helloWorldActor.tell(new Protocol.Msg("Ciao"));
    }
}
