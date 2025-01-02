package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.Scanner;
import java.util.Set;

public class ShowDetailActor extends AbstractActor {
    private final Set<String> likedVideos;
    private final Set<String> userList;
    private ActorRef playVideoActor;
    private String currentShowName;

    public ShowDetailActor(Set<String> likedVideos, Set<String> userList, ActorRef playVideoActor) {
        this.likedVideos = likedVideos;
        this.userList = userList;
        this.playVideoActor = playVideoActor;
    }

    public static Props props(Set<String> likedVideos, Set<String> userList, ActorRef playVideoActor) {
        return Props.create(ShowDetailActor.class, () -> new ShowDetailActor(likedVideos, userList, playVideoActor));
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(ActorRef.class, actorRef -> this.playVideoActor = actorRef)
            .matchEquals("showDetail", msg -> displayShowDetail())
            .match(String.class, this::processShowDetail)
            .build();
    }

    private void displayShowDetail() {
        // Logic to display show details
        System.out.println("\n--- Details for: " + currentShowName + " ---");
        System.out.println("1. Play");
        System.out.println("2. Like");
        System.out.println("3. Add to Watch List");
        System.out.println("4. Go Back to Home");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                playContent(currentShowName, null);
                break;
            case 2:
                toggleLike(currentShowName);
                break;
            case 3:
                toggleList(currentShowName);
                break;
            case 4:
                getSender().tell("start", getSelf());
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                displayShowDetail();
        }
    }

    private void processShowDetail(String showName) {
        this.currentShowName = showName;
        boolean isTvShow = isTvShow(showName);

        String selectedEpisode = null;
        if (isTvShow) {
            selectedEpisode = selectEpisode(showName);
        }

        while (true) {
            System.out.println("\n--- Details for: " + showName + " ---");
            if (isTvShow && selectedEpisode != null) {
                System.out.println("Selected Episode: " + selectedEpisode);
            }
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
                    playContent(showName, selectedEpisode);
                    return; // Exit loop and return to PlayVideoActor
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

    private boolean isTvShow(String showName) {
        // Identify whether the content is a TV show. Add more TV shows here as needed.
        return "Series B".equalsIgnoreCase(showName);
    }

    private String selectEpisode(String showName) {
        System.out.println("\n--- Select an Episode for: " + showName + " ---");
        System.out.println("1. Episode 1");
        System.out.println("2. Episode 2");
        System.out.println("3. Episode 3");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int episodeChoice = scanner.nextInt();

        switch (episodeChoice) {
            case 1:
                return "Episode 1";
            case 2:
                return "Episode 2";
            case 3:
                return "Episode 3";
            default:
                System.out.println("Invalid episode choice. Defaulting to Episode 1.");
                return "Episode 1";
        }
    }

    private void playContent(String showName, String episode) {
        if (episode != null) {
            System.out.println("Playing: " + showName + " - " + episode);
            playVideoActor.tell(showName + " - " + episode, getSelf());
        } else {
            System.out.println("Playing: " + showName);
            playVideoActor.tell(showName, getSelf());
        }
    }

    private void toggleLike(String showName) {
        if (likedVideos.contains(showName)) {
            likedVideos.remove(showName);
            System.out.println(showName + " has been unliked.");
        } else {
            likedVideos.add(showName);
            System.out.println(showName + " has been liked.");
        }
    }

    private void toggleList(String showName) {
        if (userList.contains(showName)) {
            userList.remove(showName);
            System.out.println(showName + " has been removed from your watch list.");
        } else {
            userList.add(showName);
            System.out.println(showName + " has been added to your watch list.");
        }
    }
}