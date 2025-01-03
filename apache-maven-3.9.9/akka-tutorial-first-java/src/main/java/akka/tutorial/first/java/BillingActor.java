package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BillingActor extends AbstractActor {
    private final List<Bill> bills = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private String paymentMethod = "None";

    public BillingActor() {
        // Initialize with some sample bills
        bills.add(new Bill("1", 10.99));
        bills.add(new Bill("2", 15.99));
    }

    public static Props props() {
        return Props.create(BillingActor.class, BillingActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("manage", msg -> manageBilling())
                .build();
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
            scanner.nextLine(); // Consume newline

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
                    exitMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewBills() {
        System.out.println("\n--- Your Bills ---");
        if (bills.isEmpty()) {
            System.out.println("No bills available.");
        } else {
            for (Bill bill : bills) {
                System.out.println(bill);
            }
        }
    }

    private void payBills(Scanner scanner) {
        System.out.println("\n--- Pay Bills ---");
        viewBills();
        System.out.print("Enter Bill ID to pay: ");
        String billId = scanner.nextLine();
        Bill billToPay = null;
        for (Bill bill : bills) {
            if (bill.getId().equals(billId)) {
                billToPay = bill;
                break;
            }
        }
        if (billToPay != null) {
            if ("None".equals(paymentMethod)) {
                System.out.println("No payment method set. Please set a payment method first.");
            } else {
                billToPay.setPaid(true);
                transactions.add(new Transaction(billToPay.getId(), billToPay.getAmount(), paymentMethod));
                System.out.println("Bill " + billId + " has been paid using " + paymentMethod + ".");
            }
        } else {
            System.out.println("Bill ID not found.");
        }
    }

    private void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    private void setPaymentMethod(Scanner scanner) {
        System.out.println("\n--- Set Payment Method ---");
        System.out.println("1. Credit Card");
        System.out.println("2. Bank Transfer");
        System.out.print("Choose a payment method: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                paymentMethod = "Credit Card";
                break;
            case 2:
                paymentMethod = "Bank Transfer";
                break;
            default:
                System.out.println("Invalid choice. Payment method not set.");
                return;
        }
        System.out.println("Payment method set to " + paymentMethod);
    }

    private void addPaymentMethod(Scanner scanner) {
        System.out.println("\n--- Add Payment Method ---");
        System.out.println("1. Credit Card");
        System.out.println("2. Bank Transfer");
        System.out.print("Choose a payment method to add: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter Credit Card details: ");
                String creditCardDetails = scanner.nextLine();
                System.out.println("Credit Card " + creditCardDetails + " has been added.");
                break;
            case 2:
                System.out.print("Enter Bank Transfer details: ");
                String bankTransferDetails = scanner.nextLine();
                System.out.println("Bank Transfer " + bankTransferDetails + " has been added.");
                break;
            default:
                System.out.println("Invalid choice. Payment method not added.");
        }
    }

    // Inner classes for Bill and Transaction
    private static class Bill {
        private final String id;
        private final double amount;
        private boolean isPaid;

        public Bill(String id, double amount) {
            this.id = id;
            this.amount = amount;
            this.isPaid = false;
        }

        public String getId() {
            return id;
        }

        public double getAmount() {
            return amount;
        }

        public boolean isPaid() {
            return isPaid;
        }

        public void setPaid(boolean paid) {
            isPaid = paid;
        }

        @Override
        public String toString() {
            return "Bill ID: " + id + ", Amount: $" + amount + ", Status: " + (isPaid ? "Paid" : "Due");
        }
    }

    private static class Transaction {
        private final String billId;
        private final double amount;
        private final String paymentMethod;

        public Transaction(String billId, double amount, String paymentMethod) {
            this.billId = billId;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
        }

        @Override
        public String toString() {
            return "Transaction for Bill ID: " + billId + ", Amount: $" + amount + ", Payment Method: " + paymentMethod;
        }
    }
}