package akka.tutorial.first.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        System.out.println("Netflix Akka Model Started");

        // Create the Akka system
        ActorSystem system = ActorSystem.create("NetflixSystem");

        // Simulated in-memory data storage
        Map<String, String> userDatabase = new HashMap<>(); // Username -> Password
        Set<String> likedVideos = new HashSet<>(); // Liked videos
        Set<String> watchList = new HashSet<>(); // Watch list
        Map<String, String> billingInfo = new HashMap<>(); // Billing info (username -> payment method)

        // Create actors
        ActorRef playVideoActor = system.actorOf(PlayVideoActor.props(), "playVideoActor");
        ActorRef showDetailActor = system.actorOf(ShowDetailActor.props(likedVideos, watchList, playVideoActor), "showDetailActor");
        ActorRef profileActor = system.actorOf(ProfileActor.props(userDatabase), "profileActor");
        ActorRef billingActor = system.actorOf(BillActor.props(billingInfo), "billingActor");
        ActorRef settingsActor = system.actorOf(SettingActor.props(profileActor, billingActor), "settingsActor");
        ActorRef homeActor = system.actorOf(HomeActor.props(showDetailActor, settingsActor), "homeActor");
        ActorRef signupActor = system.actorOf(SignupActor.props(homeActor, userDatabase), "signupActor");
        ActorRef loginActor = system.actorOf(LoginActor.props(homeActor, signupActor, userDatabase), "loginActor");

        // Start menu
        startMenu(loginActor, signupActor);
    }

    private static void startMenu(ActorRef loginActor, ActorRef signupActor) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Start Menu ===");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    loginActor.tell("login", ActorRef.noSender());
                    break;
                case 2:
                    signupActor.tell("signup", ActorRef.noSender());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}