package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BillingActor extends AbstractActor {
    private final List<String> transactions = new ArrayList<>();
    private String paymentMethod = "None";

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(String.class, this::processCommand)
            .build();
    }

    private void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "manage":
                manageBilling();
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void manageBilling() {
        while (true) {
            System.out.println("\n--- Billing Menu ---");
            System.out.println("1. View Bills");
            System.out.println("2. Pay Bills");
            System.out.println("3. View Transaction History");
            System.out.println("4. Set Payment Method");
            System.out.println("5. Add Payment Method");
            System.out.println("6. Back to Settings Menu");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline.

            switch (choice) {
                case 1:
                    viewBills();
                    break;
                case 2:
                    payBills(scanner);
                    break;
                case 3:
                    viewTransactionHistory();
                    break;
                case 4:
                    setPaymentMethod(scanner);
                    break;
                case 5:
                    addPaymentMethod(scanner);
                    break;
                    case 6:
                    System.out.println("Returning to Settings Menu...");
                    getSender().tell("open", getSelf()); // Notify SettingActor to redisplay menu
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewBills() {
        System.out.println("\n--- Your Bills ---");
        System.out.println("Bill #1: $10.99 - Due");
        System.out.println("Bill #2: $15.99 - Paid");
    }

    private void payBills(Scanner scanner) {
        System.out.print("Enter the amount to pay: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline.
        System.out.println("Paid $" + amount + " successfully.");
        transactions.add("Paid $" + amount);
    }

    private void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            transactions.forEach(System.out::println);
        }
    }

    private void setPaymentMethod(Scanner scanner) {
        System.out.print("Enter payment method to set (e.g., Credit Card): ");
        paymentMethod = scanner.nextLine();
        System.out.println("Payment method set to: " + paymentMethod);
    }

    private void addPaymentMethod(Scanner scanner) {
        System.out.print("Enter new payment method to add: ");
        String newMethod = scanner.nextLine();
        System.out.println("Added new payment method: " + newMethod);
    }

    public static Props props() {
        return Props.create(BillingActor.class);
    }
}
