package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.Scanner;

public class ShowDetailActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, this::processMessage)
            .build();
    }

    private void processMessage(String command) {
        if ("showRecommendations".equals(command)) {
            displayRecommendations();
        } else {
            System.out.println("Unrecognized command in ShowDetailActor: " + command);
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
                System.out.println("You selected Movie A.");
                // Redirect to VideoPlayerActor to play Movie A
                getContext().getSystem().actorSelection("/user/videoPlayerActor").tell("play Movie A", getSelf());
                break;
            case 2:
                System.out.println("You selected Series B.");
                // Redirect to VideoPlayerActor to play Series B
                getContext().getSystem().actorSelection("/user/videoPlayerActor").tell("play Series B", getSelf());
                break;
            case 3:
                System.out.println("You selected Documentary C.");
                // Redirect to VideoPlayerActor to play Documentary C
                getContext().getSystem().actorSelection("/user/videoPlayerActor").tell("play Documentary C", getSelf());
                break;
            default:
                System.out.println("Invalid choice. Returning to recommendations...");
                displayRecommendations();
        }
    }

    public static Props props() {
        return Props.create(ShowDetailActor.class);
    }
}