package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import java.util.Map;
import java.util.Scanner;

public class BillActor extends AbstractActor {
    private final Map<String, String> billingInfo; // Simulated billing information

    public BillActor(Map<String, String> billingInfo) {
        this.billingInfo = billingInfo;
    }

    public static Props props(Map<String, String> billingInfo) {
        return Props.create(BillActor.class, () -> new BillActor(billingInfo));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processMessage)
                .build();
    }

    private void processMessage(String message) {
        if (message.equals("open")) {
            displayBillingMenu();
        } else {
            System.out.println("Unrecognized message: " + message);
        }
    }

    private void displayBillingMenu() {
        System.out.println("\n=== Billing Menu ===");
        System.out.println("1. View Bills");
        System.out.println("2. Pay Bills");
        System.out.println("3. View Transaction History");
        System.out.println("4. Set Payment Method");
        System.out.println("5. Add Payment Method");
        System.out.println("6. Back to Settings Menu");
        System.out.print("Choose an option: ");

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
                viewTransactionHistory();
                break;
            case 4:
                setPaymentMethod(scanner);
                break;
            case 5:
                addPaymentMethod(scanner);
                break;
            case 6:
                getSender().tell("open", getSelf()); // Return to Settings Menu
                break;
            default:
                System.out.println("Invalid choice. Returning to Billing Menu.");
                displayBillingMenu();
                break;
        }
    }

    private void viewBills() {
        System.out.println("\n=== View Bills ===");
        if (billingInfo.containsKey("bills")) {
            System.out.println("Your bills: " + billingInfo.get("bills"));
        } else {
            System.out.println("No bills found.");
        }
        displayBillingMenu();
    }

    private void payBills() {
        System.out.println("\n=== Pay Bills ===");
        if (billingInfo.containsKey("bills")) {
            System.out.println("Paying bills: " + billingInfo.get("bills"));
            billingInfo.remove("bills");
            System.out.println("Bills paid successfully!");
        } else {
            System.out.println("No bills to pay.");
        }
        displayBillingMenu();
    }

    private void viewTransactionHistory() {
        System.out.println("\n=== Transaction History ===");
        if (billingInfo.containsKey("transactions")) {
            System.out.println("Transaction history: " + billingInfo.get("transactions"));
        } else {
            System.out.println("No transaction history found.");
        }
        displayBillingMenu();
    }

    private void setPaymentMethod(Scanner scanner) {
        System.out.println("\n=== Set Payment Method ===");
        System.out.print("Enter your payment method (e.g., Credit Card, PayPal): ");
        String paymentMethod = scanner.next();
        billingInfo.put("paymentMethod", paymentMethod);
        System.out.println("Payment method set to: " + paymentMethod);
        displayBillingMenu();
    }

    private void addPaymentMethod(Scanner scanner) {
        System.out.println("\n=== Add Payment Method ===");
        System.out.print("Enter a new payment method (e.g., Debit Card, Bank Transfer): ");
        String paymentMethod = scanner.next();
        billingInfo.put("additionalPaymentMethod", paymentMethod);
        System.out.println("Added payment method: " + paymentMethod);
        displayBillingMenu();
    }
}
