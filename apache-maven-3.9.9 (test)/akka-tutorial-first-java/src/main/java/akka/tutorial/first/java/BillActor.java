package main.java.akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BillActor extends AbstractActor {
    private final Map<String, Double> bills = new HashMap<>(); // Store bills with user profile names as keys

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, this::processMessage)
            .build();
    }

    private void processMessage(String command) {
        if ("manageBilling".equals(command)) {
            displayBillingMenu();
        } else {
            System.out.println("Unrecognized command in BillActor: " + command);
        }
    }

    private void displayBillingMenu() {
        System.out.println("\n--- Billing Menu ---");
        System.out.println("1. View Bills");
        System.out.println("2. Pay Bill");
        System.out.println("3. Add Bill");
        System.out.println("4. Back to Settings");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                viewBills();
                break;
            case 2:
                payBill(scanner);
                break;
            case 3:
                addBill(scanner);
                break;
            case 4:
                // Go back to SettingActor
                getContext().getSystem().actorSelection("/user/settingActor").tell("open", getSelf());
                break;
            default:
                System.out.println("Invalid choice. Returning to Billing Menu...");
                displayBillingMenu();
        }
    }

    private void viewBills() {
        if (bills.isEmpty()) {
            System.out.println("No bills available.");
        } else {
            System.out.println("Current Bills:");
            for (Map.Entry<String, Double> entry : bills.entrySet()) {
                System.out.println("Profile: " + entry.getKey() + ", Amount Due: $" + entry.getValue());
            }
        }
        displayBillingMenu();
    }

    private void payBill(Scanner scanner) {
        System.out.print("Enter profile name to pay bill: ");
        String profileName = scanner.next();
        if (bills.containsKey(profileName)) {
            System.out.print("Enter amount to pay: ");
            double amount = scanner.nextDouble();
            double currentBill = bills.get(profileName);
            if (amount >= currentBill) {
                bills.remove(profileName);
                System.out.println("Bill for " + profileName + " paid in full.");
            } else {
                bills.put(profileName, currentBill - amount);
                System.out.println("Partial payment of $" + amount + " made for " + profileName + ". Remaining balance: $" + (currentBill - amount));
            }
        } else {
            System.out.println("Profile not found or no bills available.");
        }
        displayBillingMenu();
    }

    private void addBill(Scanner scanner) {
        System.out.print("Enter profile name to add bill: ");
        String profileName = scanner.next();
        System.out.print("Enter amount for the bill: ");
        double amount = scanner.nextDouble();
        bills.put(profileName, bills.getOrDefault(profileName, 0.0) + amount);
        System.out.println("Bill of $" + amount + " added for " + profileName + ".");
        displayBillingMenu();
    }

    public static Props props() {
        return Props.create(BillActor.class);
    }
}