package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class VideoPlayerActor extends AbstractActor {
    private boolean isPlaying = false;
    private int currentEpisode = 1;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, this::processMessage)
            .build();
    }

    private void processMessage(String command) {
        if (command.startsWith("play")) {
            String videoTitle = command.substring(5); // Extract video title
            playVideo(videoTitle);
        } else if (command.equals("pause")) {
            pauseVideo();
        } else if (command.equals("stop")) {
            stopVideo();
        } else if (command.equals("next")) {
            goToNextEpisode();
        } else if (command.equals("exit")) {
            exitVideo();
        } else {
            System.out.println("Unrecognized command in VideoPlayerActor: " + command);
        }
    }

    private void playVideo(String videoTitle) {
        if (!isPlaying) {
            isPlaying = true;
            System.out.println("Playing " + videoTitle + " (Episode " + currentEpisode + ")...");
        } else {
            System.out.println("Video is already playing.");
        }
    }

    private void pauseVideo() {
        if (isPlaying) {
            isPlaying = false;
            System.out.println("Video paused.");
        } else {
            System.out.println("Video is not currently playing.");
        }
    }

    private void stopVideo() {
        if (isPlaying) {
            isPlaying = false;
            System.out.println("Video stopped.");
        } else {
            System.out.println("Video is already stopped.");
        }
    }

    private void goToNextEpisode() {
        currentEpisode++;
        System.out.println("Now playing episode: " + currentEpisode);
        if (isPlaying) {
            System.out.println("Continuing playback...");
        }
    }

    private void exitVideo() {
        stopVideo();
        System.out.println("Exiting video player...");
        // Optionally, you can send a message back to the HomeActor to return to the home menu
        getContext().getSystem().actorSelection("/user/homeActor").tell("start", getSelf());
    }

    public static Props props() {
        return Props.create(VideoPlayerActor.class);
    }
}