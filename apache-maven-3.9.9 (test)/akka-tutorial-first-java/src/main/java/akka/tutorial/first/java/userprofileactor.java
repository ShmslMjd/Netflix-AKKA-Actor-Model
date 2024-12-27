package main.java.akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserProfileActor extends AbstractActor {
    private final Map<String, String> profiles = new HashMap<>(); // Store user profiles

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, this::processMessage)
            .build();
    }

    private void processMessage(String command) {
        if ("manageProfile".equals(command)) {
            displayProfileMenu();
        } else {
            System.out.println("Unrecognized command in UserProfileActor: " + command);
        }
    }

    private void displayProfileMenu() {
        System.out.println("\n--- User Profile Menu ---");
        System.out.println("1. Create Profile");
        System.out.println("2. Delete Profile");
        System.out.println("3. Change Profile");
        System.out.println("4. List Profiles");
        System.out.println("5. Back to Settings");
        System.out.print("Enter your choice: ");

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
                changeProfile(scanner);
                break;
            case 4:
                listProfiles();
                break;
            case 5:
                // Go back to SettingActor
                getContext().getSystem().actorSelection("/user/settingActor").tell("open", getSelf());
                break;
            default:
                System.out.println("Invalid choice. Returning to Profile Menu...");
                displayProfileMenu();
        }
    }

    private void createProfile(Scanner scanner) {
        System.out.print("Enter profile name: ");
        String profileName = scanner.next();
        if (profiles.containsKey(profileName)) {
            System.out.println("Profile already exists. Please choose a different name.");
        } else {
            profiles.put(profileName, "Profile Data"); // Store profile data (you can expand this)
            System.out.println("Profile " + profileName + " created.");
        }
        displayProfileMenu();
    }

    private void deleteProfile(Scanner scanner) {
        System.out.print("Enter profile name to delete: ");
        String profileName = scanner.next();
        if (profiles.remove(profileName) != null) {
            System.out.println("Profile " + profileName + " deleted.");
        } else {
            System.out.println("Profile not found.");
        }
        displayProfileMenu();
    }

    private void changeProfile(Scanner scanner) {
        System.out.print("Enter current profile name: ");
        String currentProfileName = scanner.next();
        if (profiles.containsKey(currentProfileName)) {
            System.out.print("Enter new profile name: ");
            String newProfileName = scanner.next();
            if (profiles.containsKey(newProfileName)) {
                System.out.println("Profile name already exists. Please choose a different name.");
            } else {
                profiles.put(newProfileName, profiles.remove(currentProfileName)); // Change profile name
                System.out.println("Profile changed from " + currentProfileName + " to " + newProfileName + ".");
            }
        } else {
            System.out.println("Profile not found.");
        }
        displayProfileMenu();
    }

    private void listProfiles() {
        if (profiles.isEmpty()) {
            System.out.println("No profiles available.");
        } else {
            System.out.println("Available Profiles:");
            for (String profileName : profiles.keySet()) {
                System.out.println("- " + profileName);
            }
        }
        displayProfileMenu();
    }

    public static Props props() {
        return Props.create(UserProfileActor.class);
    }
}