package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.Scanner;

public class HomeActor extends AbstractActor {
    private final ActorRef showDetailActor;
    private final ActorRef settingsActor;
    private final ActorRef billingActor;

    public HomeActor(ActorRef showDetailActor, ActorRef settingsActor, ActorRef billingActor) {
        this.showDetailActor = showDetailActor;
        this.settingsActor = settingsActor;
        this.billingActor = billingActor;
    }

    public static Props props(ActorRef showDetailActor, ActorRef settingsActor, ActorRef billingActor) {
        return Props.create(HomeActor.class, () -> new HomeActor(showDetailActor, settingsActor, billingActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processMessage)
                .build();
    }

    private void processMessage(String message) {
        if (message.equals("home")) {
            displayHomeMenu();
        } else {
            System.out.println("Unrecognized message: " + message);
        }
    }

    private void displayHomeMenu() {
        System.out.println("\n=== Home Menu ===");
        System.out.println("1. Browse Video Recommendations");
        System.out.println("2. Settings");
        System.out.println("3. Billing");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                browseVideos();
                break;
            case 2:
                settingsActor.tell("open", getSelf());
                break;
            case 3:
                billingActor.tell("billing", getSelf());
                break;
            case 4:
                System.out.println("Exiting...");
                getContext().getSystem().terminate();
                break;
            default:
                System.out.println("Invalid choice. Returning to Home Menu.");
                self().tell("home", getSelf());
                break;
        }
    }

    private void browseVideos() {
        System.out.println("\n=== Video Recommendations ===");
        System.out.println("1. Movie A");
        System.out.println("2. Series B");
        System.out.println("3. Documentary C");
        System.out.println("4. Back to Home Menu");
        System.out.print("Choose a video to watch: ");

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
            case 4:
                self().tell("home", getSelf());
                break;
            default:
                System.out.println("Invalid choice. Returning to Video Recommendations.");
                browseVideos();
                break;
        }
    }
}