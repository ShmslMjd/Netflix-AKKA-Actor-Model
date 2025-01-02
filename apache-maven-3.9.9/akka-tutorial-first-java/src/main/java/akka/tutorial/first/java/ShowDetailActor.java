package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Scanner;
import java.util.Set;

public class ShowDetailActor extends AbstractActor {
    private final ActorRef playVideoActor;
    private final Set<String> likedVideos;
    private final Set<String> userList;

    public ShowDetailActor(ActorRef playVideoActor, Set<String> likedVideos, Set<String> userList) {
        this.playVideoActor = playVideoActor;
        this.likedVideos = likedVideos;
        this.userList = userList;
    }

    public static Props props(ActorRef playVideoActor, Set<String> likedVideos, Set<String> userList) {
        return Props.create(ShowDetailActor.class, () -> new ShowDetailActor(playVideoActor, likedVideos, userList));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::onShowDetail)
                .build();
    }

    private void onShowDetail(String showName) {
        while (true) {
            System.out.println("1. Play");
            if (likedVideos.contains(showName)) {
                System.out.println("2. Unlike");
            } else {
                System.out.println("2. Like");
            }
            if (userList.contains(showName)) {
                System.out.println("3. Remove from List");
            } else {
                System.out.println("3. Add to Watch List");
            }
            System.out.println("4. Go Back to Home");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    playVideoActor.tell(showName, getSelf());
                    break;
                case 2:
                    toggleLike(showName);
                    break;
                case 3:
                    toggleList(showName);
                    break;
                case 4:
                    getSender().tell("start", getSelf());
                    return; // Exit loop and return to HomeActor
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void toggleLike(String showName) {
        if (likedVideos.contains(showName)) {
            likedVideos.remove(showName);
            System.out.println("Removed from liked videos.");
        } else {
            likedVideos.add(showName);
            System.out.println("Added to liked videos.");
        }
    }

    private void toggleList(String showName) {
        if (userList.contains(showName)) {
            userList.remove(showName);
            System.out.println("Removed from watch list.");
        } else {
            userList.add(showName);
            System.out.println("Added to watch list.");
        }
    }
}