package akka.tutorial.first.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.AbstractActor;
import akka.actor.Props;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

public class App extends AbstractActor {
    public static void main(String[] args) {
        System.out.println("App started");

        ActorSystem akkaSystem = ActorSystem.create("system");

        // Simulated in-memory user database
        Map<String, String> userDatabase = new HashMap<>();
        Set<String> likedVideos = new HashSet<>();
        Set<String> userList = new HashSet<>();

        // Initialize dependent actors first
        ActorRef userProfileActor = akkaSystem.actorOf(UserProfileActor.props(), "userProfileActor");
        ActorRef billingActor = akkaSystem.actorOf(BillingActor.props(), "billingActor");

        // Create SettingActor without HomeActor reference
        ActorRef settingActor = akkaSystem.actorOf(SettingActor.props(userProfileActor, billingActor, null), "settingActor");

        // Create HomeActor with SettingActor and ShowDetailActor references
        ActorRef showDetailActor = akkaSystem.actorOf(ShowDetailActor.props(likedVideos, userList, null), "showDetailActor");
        ActorRef homeActor = akkaSystem.actorOf(HomeActor.props(showDetailActor, settingActor), "homeActor");

        // Create PlayVideoActor with HomeActor reference
        ActorRef playVideoActor = akkaSystem.actorOf(PlayVideoActor.props(homeActor), "playVideoActor");

        // Update ShowDetailActor with PlayVideoActor reference
        showDetailActor.tell(playVideoActor, ActorRef.noSender());

        // Create LoginActor and SignupActor
        ActorRef appActor = akkaSystem.actorOf(Props.create(App.class), "appActor");
        ActorRef loginActor = akkaSystem.actorOf(LoginActor.props(homeActor, appActor, userDatabase), "loginActor");
        ActorRef signupActor = akkaSystem.actorOf(SignupActor.props(loginActor, appActor, userDatabase), "signupActor");

        // Update SettingActor with HomeActor reference
        settingActor.tell(homeActor, ActorRef.noSender());

        // Start the application menu
        appActor.tell("startMenu", ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("startMenu", msg -> startMenu())
                .build();
    }

    private void startMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exitMenu = false;
        while (!exitMenu) {
            System.out.println("\n=== Start Menu ===");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    getContext().actorSelection("/user/loginActor").tell("login", ActorRef.noSender());
                    exitMenu = true;
                    break;
                case 2:
                    getContext().actorSelection("/user/signupActor").tell("signup", ActorRef.noSender());
                    exitMenu = true;
                    break;
                case 3:
                    System.out.println("Exiting application...");
                    exitMenu = true;
                    getContext().getSystem().terminate();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}