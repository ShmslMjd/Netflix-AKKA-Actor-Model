package akka.tutorial.first.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App {
    public static void main(String[] args) {
        System.out.println("App started");

        ActorSystem akkaSystem = ActorSystem.create("system");

        ActorRef showDetailActor = akkaSystem.actorOf(ShowDetailActor.props(), "showDetailActor");
        ActorRef settingsActor = akkaSystem.actorOf(SettingActor.props(), "settingsActor");
        ActorRef homeActor = akkaSystem.actorOf(HomeActor.props(showDetailActor, settingsActor), "homeActor");

        // Start the home menu
        homeActor.tell("start", ActorRef.noSender());
    }
}
