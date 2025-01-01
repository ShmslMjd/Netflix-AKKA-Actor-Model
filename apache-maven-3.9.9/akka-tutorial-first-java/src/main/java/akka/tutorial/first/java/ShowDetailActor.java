package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ShowDetailActor extends AbstractActor {
    private final Set<String> likedVideos;
    private final Set<String> watchList;
    private final ActorRef playVideoActor;

    public ShowDetailActor(Set<String> likedVideos, Set<String> watchList, ActorRef playVideoActor) {
        this.likedVideos = likedVideos;
        this.watchList = watchList;
        this.playVideoActor = playVideoActor;
    }

    public static Props props(Set<String> likedVideos, Set<String> watchList, ActorRef playVideoActor) {
        return Props.create(ShowDetailActor.class, () -> new ShowDetailActor(likedVideos, watchList, playVideoActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processMessage)
                .build();
    }

    private void processMessage(String message) {
        System.out.println("Showing details for: " + message);
        displayShowDetails(message);
    }

    private void displayShowDetails(String showName) {
        Scanner scanner = new Scanner(System.in);
        boolean exitDetailMenu = false;
        while (!exitDetailMenu) {
            System.out.println("\n=== Show Details: " + showName + " ===");
            System.out.println("1. Select Episode");
            System.out.println("2. Play Episode");
            System.out.println("3. Like/Unlike Content");
            System.out.println("4. Add to Watch List");
            System.out.println("5. Back to Home Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    selectEpisode(showName);
                    break;
                case 2:
                    playEpisode(showName);
                    break;
                case 3:
                    likeUnlikeContent(showName);
                    break;
                case 4:
                    addToWatchList(showName);
                    break;
                case 5:
                    exitDetailMenu = true;
                    getSender().tell("home", getSelf());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void selectEpisode(String showName) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter episode number: ");
        int episodeNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Selected episode " + episodeNumber + " of " + showName);
    }

    private void playEpisode(String showName) {
        System.out.println("Playing episode of " + showName);
        playVideoActor.tell(showName, getSelf());
    }

    private void likeUnlikeContent(String showName) {
        if (likedVideos.contains(showName)) {
            likedVideos.remove(showName);
            System.out.println("You unliked " + showName);
        } else {
            likedVideos.add(showName);
            System.out.println("You liked " + showName);
        }
    }

    private void addToWatchList(String showName) {
        if (watchList.contains(showName)) {
            System.out.println(showName + " is already in your watch list.");
        } else {
            watchList.add(showName);
            System.out.println(showName + " added to your watch list.");
        }
    }
}