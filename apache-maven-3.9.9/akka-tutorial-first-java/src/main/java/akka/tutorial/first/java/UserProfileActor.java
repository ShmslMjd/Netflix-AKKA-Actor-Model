package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class UserProfileActor extends AbstractActor {
    private final Map<String, String> userDatabase;
    private final ActorRef appActor;
    private final Set<String> profiles = new HashSet<>();
    private String currentProfile;

    public UserProfileActor(Map<String, String> userDatabase, ActorRef appActor) {
        this.userDatabase = userDatabase;
        this.appActor = appActor;
    }

    public static Props props(Map<String, String> userDatabase, ActorRef appActor) {
        return Props.create(UserProfileActor.class, () -> new UserProfileActor(userDatabase, appActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::onUserProfile)
                .build();
    }

    private void onUserProfile(String message) {
        Scanner scanner = new Scanner(System.in);
        switch (message) {
            case "createProfile":
                createProfile(scanner);
                break;
            case "deleteProfile":
                deleteProfile(scanner);
                break;
            case "switchProfile":
                switchProfile(scanner);
                break;
            case "viewProfiles":
                viewProfiles();
                break;
            default:
                System.out.println("Unknown command: " + message);
        }
    }

    private void createProfile(Scanner scanner) {
        System.out.print("Enter new profile name: ");
        String profileName = scanner.nextLine();
        if (profiles.add(profileName)) {
            System.out.println("Profile created: " + profileName);
        } else {
            System.out.println("Profile already exists.");
        }
    }

    private void deleteProfile(Scanner scanner) {
        System.out.print("Enter profile name to delete: ");
        String profileName = scanner.nextLine();
        if (profiles.remove(profileName)) {
            System.out.println("Profile deleted: " + profileName);
        } else {
            System.out.println("Profile not found.");
        }
    }

    private void switchProfile(Scanner scanner) {
        viewProfiles(); // Display the available profiles
        System.out.print("Enter the name of the profile to switch to: ");
        String profileName = scanner.nextLine();

        if (profiles.contains(profileName)) {
            currentProfile = profileName;
            System.out.println("Switched to profile: " + profileName);
        } else {
            System.out.println("Profile not found.");
        }
    }

    private void viewProfiles() {
        System.out.println("\n--- Available Profiles ---");
        for (String profile : profiles) {
            System.out.println("- " + profile);
        }
    }
}