package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class App extends AbstractActor {
    public static void main(String[] args) {
        System.out.println("Netflix Akka Model Started");

        // Create the Akka system
        ActorSystem system = ActorSystem.create("NetflixSystem");

        // Simulated in-memory data storage
        Map<String, String> userDatabase = new HashMap<>(); // Username -> Password
        Set<String> likedVideos = new HashSet<>(); // Liked videos
        Set<String> watchList = new HashSet<>(); // Watch list

        // Create actors
        ActorRef appActor = system.actorOf(Props.create(App.class), "appActor");
        ActorRef playVideoActor = system.actorOf(PlayVideoActor.props(appActor), "playVideoActor");
        ActorRef showDetailActor = system.actorOf(ShowDetailActor.props(playVideoActor, likedVideos, watchList), "showDetailActor");
        ActorRef profileActor = system.actorOf(UserProfileActor.props(userDatabase, appActor), "profileActor");
        ActorRef billingActor = system.actorOf(BillingActor.props(), "billingActor");
        ActorRef settingsActor = system.actorOf(SettingActor.props(profileActor, billingActor), "settingsActor");
        ActorRef homeActor = system.actorOf(HomeActor.props(showDetailActor, settingsActor, billingActor), "homeActor");
        ActorRef loginActor = system.actorOf(LoginActor.props(homeActor, appActor, userDatabase), "loginActor");
        ActorRef signupActor = system.actorOf(SignupActor.props(loginActor, appActor, userDatabase), "signupActor");

        // Start menu
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
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    getContext().actorSelection("/user/loginActor").tell("login", getSelf());
                    exitMenu = true;
                    break;
                case 2:
                    getContext().actorSelection("/user/signupActor").tell("signup", getSelf());
                    exitMenu = true;
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    exitMenu = true;
                    getContext().getSystem().terminate();
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}