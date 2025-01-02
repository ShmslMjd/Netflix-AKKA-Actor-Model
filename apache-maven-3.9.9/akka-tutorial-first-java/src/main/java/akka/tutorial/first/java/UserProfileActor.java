package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserProfileActor extends AbstractActor {
    private final List<String> profiles = new ArrayList<>();
    private String currentProfile = "Default";

    public UserProfileActor() {
        // Add some initial profiles
        profiles.add("Profile 1");
        profiles.add("Profile 2");
        profiles.add("Profile 3");
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(String.class, this::processCommand)
            .build();
    }

    private void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "manage":
                manageProfiles();
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void manageProfiles() {
        while (true) {
            System.out.println("\n--- User Profiles ---");
            System.out.println("Current Profile: " + currentProfile);
            System.out.println("1. Create New Profile");
            System.out.println("2. Delete Profile");
            System.out.println("3. Switch Profile");
            System.out.println("4. View Profiles");
            System.out.println("5. Back to Settings Menu");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline.

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
                    viewProfiles();
                    break;
                case 5:
                    System.out.println("Returning to Settings Menu...");
                    getSender().tell("open", getSelf()); // Notify SettingActor to redisplay the settings menu
                    return;                
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void createProfile(Scanner scanner) {
        System.out.print("Enter new profile name: ");
        String profileName = scanner.nextLine();
        profiles.add(profileName);
        System.out.println("Profile created: " + profileName);
    }

    private void deleteProfile(Scanner scanner) {
        viewProfiles(); // Display the available profiles
        System.out.print("Enter the name of the profile to delete: ");
        String profileName = scanner.nextLine();

        if (profiles.contains(profileName)) {
            if (profileName.equals(currentProfile)) {
                System.out.println("Cannot delete the currently active profile.");
            } else {
                profiles.remove(profileName);
                System.out.println("Profile deleted: " + profileName);
            }
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

    public static Props props() {
        return Props.create(UserProfileActor.class);
    }
}
