package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import java.util.Map;
import java.util.Scanner;

public class ProfileActor extends AbstractActor {
    private final Map<String, String> userDatabase; // Simulated user database
    private String currentProfile = "Default"; // Tracks the current active profile

    public ProfileActor(Map<String, String> userDatabase) {
        this.userDatabase = userDatabase;
    }

    public static Props props(Map<String, String> userDatabase) {
        return Props.create(ProfileActor.class, () -> new ProfileActor(userDatabase));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processMessage)
                .build();
    }

    private void processMessage(String message) {
        if (message.equals("manage")) {
            manageProfiles();
        } else {
            System.out.println("Unrecognized message: " + message);
        }
    }

    private void manageProfiles() {
        System.out.println("\n=== Profile Management ===");
        System.out.println("1. Create New Profile");
        System.out.println("2. Delete Profile");
        System.out.println("3. Switch Profile");
        System.out.println("4. Back to Settings Menu");
        System.out.print("Choose an option: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                createProfile(scanner);
                break;
            case 2:
                deleteProfile(scanner);
                break;
            case 3:
                switchProfile(scanner);
                break;
            case 4:
                getSender().tell("open", getSelf()); // Return to Settings Menu
                break;
            default:
                System.out.println("Invalid choice. Returning to Profile Management.");
                manageProfiles();
                break;
        }
    }

    private void createProfile(Scanner scanner) {
        System.out.print("Enter a new profile name: ");
        String profileName = scanner.next();

        if (userDatabase.containsKey(profileName)) {
            System.out.println("Profile already exists. Try a different name.");
        } else {
            userDatabase.put(profileName, "DefaultPassword"); // Simulate adding a profile
            System.out.println("Profile '" + profileName + "' created successfully!");
        }
        manageProfiles();
    }

    private void deleteProfile(Scanner scanner) {
        System.out.print("Enter the profile name to delete: ");
        String profileName = scanner.next();

        if (userDatabase.containsKey(profileName)) {
            userDatabase.remove(profileName);
            System.out.println("Profile '" + profileName + "' deleted successfully!");
        } else {
            System.out.println("Profile '" + profileName + "' not found.");
        }
        manageProfiles();
    }

    private void switchProfile(Scanner scanner) {
        System.out.print("Enter the profile name to switch to: ");
        String profileName = scanner.next();

        if (userDatabase.containsKey(profileName)) {
            currentProfile = profileName;
            System.out.println("Switched to profile '" + profileName + "'.");
        } else {
            System.out.println("Profile '" + profileName + "' not found.");
        }
        manageProfiles();
    }
}