package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SettingActor extends AbstractActor {
    private final Map<String, String> billingInfo = new HashMap<>();
    private String currentProfile = "Default";

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(String.class, this::processCommand)
            .build();
    }

    private void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "open":
                displaySettingsMenu();
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void displaySettingsMenu() {
        System.out.println("\n--- Settings ---");
        System.out.println("1. Billing");
        System.out.println("2. User Profiles");
        System.out.println("3. Back to Home");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                handleBilling();
                break;
            case 2:
                manageProfiles();
                break;
            case 3:
                System.out.println("Returning to Home...");
                getContext().stop(getSelf());
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    private void handleBilling() {
        System.out.println("\n--- Billing Options ---");
        System.out.println("1. View Bills");
        System.out.println("2. Pay Bills");
        System.out.println("3. Add Payment Method");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                viewBills();
                break;
            case 2:
                payBills();
                break;
            case 3:
                addPaymentMethod();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewBills() {
        System.out.println("Viewing bills...");
        // Display bills (others will do it).
    }

    private void payBills() {
        System.out.println("Paying bills...");
        // Simulate bill payment (others will do it).
    }

    private void addPaymentMethod() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter payment method: ");
        String method = scanner.nextLine();
        billingInfo.put(currentProfile, method);
        System.out.println("Payment method added for profile: " + currentProfile);
    }

    private void manageProfiles() {
        System.out.println("\n--- User Profiles ---");
        System.out.println("1. Create New Profile");
        System.out.println("2. Delete Profile");
        System.out.println("3. Switch Profile");
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
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void createProfile(Scanner scanner) {
        System.out.print("Enter new profile name: ");
        String profileName = scanner.nextLine();
        billingInfo.put(profileName, "No payment method added.");
        System.out.println("Profile created: " + profileName);
    }

    private void deleteProfile(Scanner scanner) {
        System.out.print("Enter profile name to delete: ");
        String profileName = scanner.nextLine();
        if (billingInfo.containsKey(profileName)) {
            billingInfo.remove(profileName);
            System.out.println("Profile deleted: " + profileName);
        } else {
            System.out.println("Profile not found.");
        }
    }

    private void switchProfile(Scanner scanner) {
        System.out.print("Enter profile name to switch to: ");
        String profileName = scanner.nextLine();
        if (billingInfo.containsKey(profileName)) {
            currentProfile = profileName;
            System.out.println("Switched to profile: " + profileName);
        } else {
            System.out.println("Profile not found.");
        }
    }

    public static Props props() {
        return Props.create(SettingActor.class);
    }
}
