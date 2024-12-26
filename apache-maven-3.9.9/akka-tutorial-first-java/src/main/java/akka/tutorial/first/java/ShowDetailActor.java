package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShowDetailActor extends AbstractActor {
    private final List<String> userList = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(String.class, this::processShowDetail)
            .build();
    }

    private void processShowDetail(String showName) {
        System.out.println("\n--- Show Details for: " + showName + " ---");
        System.out.println("1. Play Episode");
        System.out.println("2. Like/Unlike");
        System.out.println("3. Add to List");
        System.out.println("4. Go Back to Home");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                playEpisode(showName);
                break;
            case 2:
                likeUnlike(showName);
                break;
            case 3:
                addToList(showName);
                break;
            case 4:
                System.out.println("Returning to Home...");
                getContext().stop(getSelf());
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    private void playEpisode(String showName) {
        System.out.println("Playing: " + showName);
        // Simulate playback controls (others will do it)
    }

    private void likeUnlike(String showName) {
        System.out.println("Liked/Unliked: " + showName);
    }

    private void addToList(String showName) {
        if (!userList.contains(showName)) {
            userList.add(showName);
            System.out.println(showName + " added to your list.");
        } else {
            System.out.println(showName + " is already in your list.");
        }
    }

    public static Props props() {
        return Props.create(ShowDetailActor.class);
    }
}
