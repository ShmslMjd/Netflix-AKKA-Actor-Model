package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.Scanner;

public class PlayVideoActor extends AbstractActor {
    private final ActorRef homeActor;

    public PlayVideoActor(ActorRef homeActor) {
        this.homeActor = homeActor;
    }

    public static Props props(ActorRef homeActor) {
        return Props.create(PlayVideoActor.class, () -> new PlayVideoActor(homeActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::playVideo)
                .build();
    }

    private void playVideo(String showName) {
        Scanner scanner = new Scanner(System.in);
        boolean exitVideo = false;
        while (!exitVideo) {
            System.out.println("\n=== Video Player ===");
            System.out.println("Now Playing: " + showName);
            System.out.println("1. Stop");
            System.out.println("2. Rewind");
            System.out.println("3. Forward");
            System.out.println("4. Exit");
            System.out.println("5. Modify Volume");
            System.out.println("6. Adjust Brightness");
            System.out.println("7. Adjust Speed");
            System.out.println("8. Select Subtitles");
            System.out.println("9. Go to Next Episode");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Stopping video...");
                    break;
                case 2:
                    System.out.println("Rewinding video...");
                    break;
                case 3:
                    System.out.println("Forwarding video...");
                    break;
                case 4:
                    System.out.println("Exiting video player...");
                    homeActor.tell("home", getSelf());
                    break;
                case 5:
                    modifyVolume();
                    break;
                case 6:
                    adjustBrightness();
                    break;
                case 7:
                    adjustSpeed();
                    break;
                case 8:
                    selectSubtitles();
                    break;
                case 9:
                    goToNextEpisode();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void modifyVolume() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter volume level (0-100): ");
        int volume = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Volume set to " + volume);
    }

    private void adjustBrightness() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter brightness level (0-100): ");
        int brightness = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Brightness set to " + brightness);
    }

    private void adjustSpeed() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter playback speed (0.5-2.0): ");
        double speed = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.println("Playback speed set to " + speed);
    }

    private void selectSubtitles() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter subtitle language: ");
        String language = scanner.nextLine();
        System.out.println("Subtitles set to " + language);
    }

    private void goToNextEpisode() {
        System.out.println("Going to next episode...");
        // Logic to go to the next episode
    }
}