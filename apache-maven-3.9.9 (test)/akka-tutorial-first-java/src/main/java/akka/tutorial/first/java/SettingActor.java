package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.Scanner;

public class SettingActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, this::processMessage)
            .build();
    }

    private void processMessage(String command) {
        if ("open".equals(command)) {
            displaySettingsMenu();
        } else {
            System.out.println("Unrecognized command in SettingActor: " + command);
        }
    }

    private void displaySettingsMenu() {
        System.out.println("\n--- Settings Menu ---");
        System.out.println("1. User Profile");
        System.out.println("2. Billing");
        System.out.println("3. Back to Home");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Redirect to UserProfileActor
                getContext().getSystem().actorSelection("/user/userProfileActor").tell("manageProfile", getSelf());
                break;
            case 2:
                // Redirect to BillActor
                getContext().getSystem().actorSelection("/user/billActor").tell("manageBilling", getSelf());
                break;
            case 3:
                // Go back to HomeActor
                getContext().getSystem().actorSelection("/user/homeActor").tell("start", getSelf());
                break;
            default:
                System.out.println("Invalid choice. Returning to Settings Menu...");
                displaySettingsMenu();
        }
    }

    public static Props props() {
        return Props.create(SettingActor.class);
    }
}