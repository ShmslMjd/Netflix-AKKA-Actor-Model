package akka.tutorial.first.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App {
    public static void main(String[] args) {
        System.out.println("App started");

        ActorSystem akkaSystem = ActorSystem.create("system");

        ActorRef showDetailActor = akkaSystem.actorOf(ShowDetailActor.props(), "showDetailActor");
        ActorRef userProfileActor = akkaSystem.actorOf(UserProfileActor.props(), "userProfileActor");
        ActorRef billingActor = akkaSystem.actorOf(BillingActor.props(), "billingActor");
        ActorRef settingActor = akkaSystem.actorOf(SettingActor.props(userProfileActor, billingActor), "settingActor");
        ActorRef homeActor = akkaSystem.actorOf(HomeActor.props(showDetailActor, settingActor), "homeActor");

        // Start the home menu
        homeActor.tell("start", ActorRef.noSender());
    }
}
