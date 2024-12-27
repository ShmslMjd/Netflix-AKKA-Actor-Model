package akka.tutorial.first.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("VideoAppSystem");

        ActorRef authActor = system.actorOf(AuthActor.props(), "authActor");
        ActorRef homeActor = system.actorOf(HomeActor.props(), "homeActor");
        ActorRef showDetailActor = system.actorOf(ShowDetailActor.props(), "showDetailActor");
        ActorRef videoPlayerActor = system.actorOf(VideoPlayerActor.props(), "videoPlayerActor");
        ActorRef settingActor = system.actorOf(SettingActor.props(), "settingActor");
        ActorRef userProfileActor = system.actorOf(UserProfileActor.props(), "userProfileActor");
        ActorRef billActor = system.actorOf(BillActor.props(), "billActor");

        // Start the application by sending a message to the AuthActor
        authActor.tell("start", ActorRef.noSender());
    }
}
