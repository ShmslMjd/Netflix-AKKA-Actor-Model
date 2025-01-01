package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import java.util.Scanner;

public class PlayVideoActor extends AbstractActor {

    public static Props props() {
        return Props.create(PlayVideoActor.class, PlayVideoActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processVideoControl)
                .build();
    }

    private void processVideoControl(String message) {
        if (message.equals("play")) {
            playVideo();
        } else {
            System.out.println("Unrecognized message: " + message);
        }
    }

    private void playVideo() {
        System.out.println("\n=== Video Playback Controls ===");
        System.out.println("1. Play");
        System.out.println("2. Pause");
        System.out.println("3. Stop");
        System.out.println("4. Rewind");
        System.out.println("5. Forward");
        System.out.println("6. Adjust Volume");
        System.out.println("7. Adjust Brightness");
        System.out.println("8. Adjust Speed");
        System.out.println("9. Select Subtitles");
        System.out.println("10. Next Episode");
        System.out.println("11. Exit to Home Menu");
        System.out.print("Choose an option: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Playing the video...");
                break;
            case 2:
                System.out.println("Paused the video.");
                break;
            case 3:
                System.out.println("Stopped the video.");
                getSender().tell("start", getSelf()); // Return to Home Menu
                break;
            case 4:
                System.out.println("Rewinding the video...");
                break;
            case 5:
                System.out.println("Forwarding the video...");
                break;
            case 6:
                System.out.print("Enter volume level (0-100): ");
                int volume = scanner.nextInt();
                System.out.println("Volume set to " + volume);
                break;
            case 7:
                System.out.print("Enter brightness level (0-100): ");
                int brightness = scanner.nextInt();
                System.out.println("Brightness set to " + brightness);
                break;
            case 8:
                System.out.print("Enter playback speed (0.5x, 1x, 1.5x, 2x): ");
                double speed = scanner.nextDouble();
                System.out.println("Playback speed set to " + speed + "x");
                break;
            case 9:
                System.out.println("Selecting subtitles...");
                System.out.println("1. English");
                System.out.println("2. Chinese");
                System.out.println("3. Bahasa Malaysia");
                int subtitleChoice = scanner.nextInt();
                System.out.println("Subtitles set to " + getSubtitleLanguage(subtitleChoice));
                break;
            case 10:
                System.out.println("Playing the next episode...");
                break;
            case 11:
                System.out.println("Exiting to Home Menu...");
                getSender().tell("start", getSelf()); // Return to Home Menu
                break;
            default:
                System.out.println("Invalid choice. Returning to Playback Controls.");
                playVideo();
                break;
        }
    }

    private String getSubtitleLanguage(int choice) {
        switch (choice) {
            case 1:
                return "English";
            case 2:
                return "Chinese";
            case 3:
                return "Bahasa Malaysia";
            default:
                return "English (default)";
        }
    }
}
