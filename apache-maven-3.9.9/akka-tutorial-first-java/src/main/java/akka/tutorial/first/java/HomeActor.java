package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.Scanner;

public class HomeActor extends AbstractActor {
    private final ActorRef showDetailActor;
    private final ActorRef settingsActor;

    public HomeActor(ActorRef showDetailActor, ActorRef settingsActor) {
        this.showDetailActor = showDetailActor;
        this.settingsActor = settingsActor;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(String.class, this::displayHomeMenu)
            .build();
    }

    private void displayHomeMenu(String message) {
        while (true) {
            System.out.println("\n--- Home Menu ---");
            System.out.println("1. Video Recommendations");
            System.out.println("2. Settings");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayRecommendations();
                    break;
                case 2:
                    displaySettings();
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    getContext().getSystem().terminate();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void displayRecommendations() {
        System.out.println("\n--- Video Recommendations ---");
        System.out.println("1. Movie A");
        System.out.println("2. Series B");
        System.out.println("3. Documentary C");
        System.out.print("Select a video by entering its number: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                showDetailActor.tell("Movie A", getSelf());
                break;
            case 2:
                showDetailActor.tell("Series B", getSelf());
                break;
            case 3:
                showDetailActor.tell("Documentary C", getSelf());
                break;
            default:
                System.out.println("Invalid choice. Returning to Home Menu.");
        }
    }

    private void displaySettings() {
        System.out.println("\n--- Settings ---");
        System.out.println("1. Billing");
        System.out.println("2. User Profiles");
        System.out.println("3. Back to Home Menu");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                settingsActor.tell("billing", getSelf());
                break;
            case 2:
                settingsActor.tell("profiles", getSelf());
                break;
            case 3:
                System.out.println("Returning to Home Menu...");
                return;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    public static Props props(ActorRef showDetailActor, ActorRef settingsActor) {
        return Props.create(HomeActor.class, () -> new HomeActor(showDetailActor, settingsActor));
    }
}
