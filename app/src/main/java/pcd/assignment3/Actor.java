package pcd.assignment3;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;

public class Actor extends AbstractBehavior<Protocol.Msg> {
    private Actor(ActorContext<Protocol.Msg> context) {
        super(context);
    }

    /* configuring message handlers in this behavior */

    @Override
    public Receive<Protocol.Msg> createReceive() {
        return newReceiveBuilder().onMessage(Protocol.Msg.class, this::onSayHello).build();
    }

    /* message handler for SayHello */

    private Behavior<Protocol.Msg> onSayHello(Protocol.Msg msg) {
        getContext().getLog().info("Hello " + msg.getContent() + " from " + this.getContext().getSelf());
        return this;
    }

    /* factory method */

    public static Behavior<Protocol.Msg> create() {
        return Behaviors.setup(Actor::new);
    }

}
