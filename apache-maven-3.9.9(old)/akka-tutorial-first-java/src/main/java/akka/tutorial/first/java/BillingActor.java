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

    public static Props props() {
        return Props.create(BillingActor.class);
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
                manageBilling();
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void manageBilling() {
        Scanner scanner = new Scanner(System.in);
        boolean exitMenu = false;
        while (!exitMenu) {
            System.out.println("\n--- Billing Menu ---");
            System.out.println("1. View Bills");
            System.out.println("2. Pay Bills");
            System.out.println("3. View Transaction History");
            System.out.println("4. Set Payment Method");
            System.out.println("5. Add Payment Method");
            System.out.println("6. Back to Settings Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline.

            switch (choice) {
                case 1:
                    viewBills();
                    break;
                case 2:
                    payBills();
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
                    exitMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewBills() {
        System.out.println("Viewing bills...");
        // Logic to view bills
    }

    private void payBills() {
        System.out.println("Paying bills...");
        // Logic to pay bills
        transactions.add("Bill paid");
    }

    private void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private void setPaymentMethod(Scanner scanner) {
        System.out.print("Enter new payment method: ");
        paymentMethod = scanner.nextLine();
        System.out.println("Payment method set to " + paymentMethod);
    }

    private void addPaymentMethod(Scanner scanner) {
        System.out.print("Enter payment method to add: ");
        String newPaymentMethod = scanner.nextLine();
        System.out.println("Payment method " + newPaymentMethod + " added.");
        // Logic to add payment method
    }
}