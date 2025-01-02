package akka.tutorial.first.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App {
    public static void main(String[] args) {
        System.out.println("App started");

        ActorSystem akkaSystem = ActorSystem.create("system");

        // Initialize dependent actors first
        ActorRef showDetailActor = akkaSystem.actorOf(ShowDetailActor.props(), "showDetailActor");
        ActorRef userProfileActor = akkaSystem.actorOf(UserProfileActor.props(), "userProfileActor");
        ActorRef billingActor = akkaSystem.actorOf(BillingActor.props(), "billingActor");

        // Create SettingActor without HomeActor reference
        ActorRef settingActor = akkaSystem.actorOf(SettingActor.props(userProfileActor, billingActor, null), "settingActor");

        // Create HomeActor with SettingActor and ShowDetailActor references
        ActorRef homeActor = akkaSystem.actorOf(HomeActor.props(showDetailActor, settingActor), "homeActor");

        // Update SettingActor with HomeActor reference
        settingActor.tell(homeActor, ActorRef.noSender());

        // Start the home menu
        homeActor.tell("start", ActorRef.noSender());
    }
}
