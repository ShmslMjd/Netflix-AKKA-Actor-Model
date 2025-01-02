package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.Map;
import java.util.Scanner;

public class ProfileActor extends AbstractActor {
    private final Map<String, String> userDatabase; // Simulated user database
    private final ActorRef appActor;

    public ProfileActor(Map<String, String> userDatabase, ActorRef appActor) {
        this.userDatabase = userDatabase;
        this.appActor = appActor;
    }

    public static Props props(Map<String, String> userDatabase, ActorRef appActor) {
        return Props.create(ProfileActor.class, () -> new ProfileActor(userDatabase, appActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("profileSettings", msg -> manageProfiles())
                .build();
    }

    private void manageProfiles() {
        Scanner scanner = new Scanner(System.in);
        boolean exitProfileMenu = false;
        while (!exitProfileMenu) {
            System.out.println("\n=== Profile Settings ===");
            System.out.println("1. Create New Profile");
            System.out.println("2. Delete Profile");
            System.out.println("3. Change Profile");
            System.out.println("4. Logout");
            System.out.println("5. Back to Settings Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                    logout();
                    exitProfileMenu = true;
                    break;
                case 5:
                    exitProfileMenu = true;
                    getSender().tell("open", getSelf());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createProfile(Scanner scanner) {
        System.out.print("Enter new profile name: ");
        String profileName = scanner.nextLine();
        // Logic to create a new profile
        System.out.println("Profile " + profileName + " created.");
    }

    private void deleteProfile(Scanner scanner) {
        System.out.print("Enter profile name to delete: ");
        String profileName = scanner.nextLine();
        // Logic to delete a profile
        System.out.println("Profile " + profileName + " deleted.");
    }

    private void changeProfile(Scanner scanner) {
        System.out.print("Enter profile name to switch to: ");
        String profileName = scanner.nextLine();
        // Logic to switch profile
        System.out.println("Switched to profile " + profileName + ".");
    }

    private void logout() {
        System.out.println("Logged out successfully.");
        appActor.tell("startMenu", ActorRef.noSender());
    }
}