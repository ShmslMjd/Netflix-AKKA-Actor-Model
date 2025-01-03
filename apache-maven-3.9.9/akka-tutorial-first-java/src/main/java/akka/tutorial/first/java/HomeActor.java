package akka.tutorial.first.java;

import java.util.Scanner;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class HomeActor extends AbstractActor {
    private final ActorRef showDetailActor;
    private final ActorRef settingActor;

    public HomeActor(ActorRef showDetailActor, ActorRef settingActor) {
        this.showDetailActor = showDetailActor;
        this.settingActor = settingActor;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(String.class, this::processMessage)
            .matchEquals("toHomeMenu", msg -> displayHomeMenu())
            .matchEquals("start", msg -> displayHomeMenu())
            .build();
    }

    private void processMessage(String message) {
        if ("start".equalsIgnoreCase(message)) {
            displayHomeMenu();
        } else {
            System.out.println("Unrecognized message: " + message);
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
                displayRecommendations();
                break;
            case 2:
                settingActor.tell("open", getSelf());
                break;
            case 3:
                System.out.println("Exiting... Goodbye!");
                getContext().getSystem().terminate();
                break;
            default:
                System.out.println("Invalid choice. Returning to Home Menu...");
                selfTellStart();
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
                System.out.println("Invalid choice. Returning to Home Menu...");
                selfTellStart();
        }
    }

    private void selfTellStart() {
        getSelf().tell("start", getSelf());
    }

    public static Props props(ActorRef showDetailActor, ActorRef settingActor) {
        return Props.create(HomeActor.class, () -> new HomeActor(showDetailActor, settingActor));
    }
}
