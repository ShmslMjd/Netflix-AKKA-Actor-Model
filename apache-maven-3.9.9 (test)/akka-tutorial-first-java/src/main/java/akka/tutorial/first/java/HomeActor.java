package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Scanner;

public class HomeActor extends AbstractActor {
    private final ActorRef showDetailActor;
    private final ActorRef settingActor;

    public HomeActor(ActorRef showDetailActor, ActorRef settingActor) {
        this.showDetailActor = showDetailActor;
        this.settingActor = settingActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, this::processMessage)
            .build();
    }

    private void processMessage(String message) {
        if ("start".equalsIgnoreCase(message)) {
            displayHomeMenu();
        } else {
            System.out.println("Unrecognized message in HomeActor: " + message);
        }
    }

    private void displayHomeMenu() {
        System.out.println("\n--- Home Menu ---");
        System.out.println("1. Video Recommendations");
        System.out.println("2. Settings");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                showDetailActor.tell("showRecommendations", getSelf());
                break;
            case 2:
                settingActor.tell("open", getSelf());
                break;
            case 3:
                System.out.println("Exiting... Goodbye!");
                getContext().getSystem().terminate();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayHomeMenu();
        }
    }

    public static Props props(ActorRef showDetailActor, ActorRef settingActor) {
        return Props.create(HomeActor.class, () -> new HomeActor(showDetailActor, settingActor));
    }
}