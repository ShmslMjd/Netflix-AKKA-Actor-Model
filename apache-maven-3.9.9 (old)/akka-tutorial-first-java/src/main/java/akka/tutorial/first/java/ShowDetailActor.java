package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
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
                .match(String.class, this::processShowDetail)
                .build();
    }

    private void processShowDetail(String showName) {
        System.out.println("\n=== Show Details ===");
        System.out.println("1. Play");
        System.out.println("2. " + (likedVideos.contains(showName) ? "Unlike" : "Like"));
        System.out.println("3. Add to Watchlist");
        
        // Example of using playVideoActor to play a video
        playVideoActor.tell("play " + showName, getSelf());
    }
}