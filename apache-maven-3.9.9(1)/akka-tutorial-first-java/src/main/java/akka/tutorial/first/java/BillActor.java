package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import java.util.Scanner;

public class BillActor extends AbstractActor {

    public static Props props() {
        return Props.create(BillActor.class, BillActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("billing", msg -> displayBillingMenu())
                .build();
    }

    private void displayBillingMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exitBillingMenu = false;
        while (!exitBillingMenu) {
            System.out.println("\n=== Billing Menu ===");
            System.out.println("1. View Bills");
            System.out.println("2. Pay Bills");
            System.out.println("3. View Transaction History");
            System.out.println("4. Set Payment Method");
            System.out.println("5. Add Payment Method");
            System.out.println("6. Back to Settings Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                    exitBillingMenu = true;
                    getSender().tell("open", getSelf());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
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
    }

    private void viewTransactionHistory() {
        System.out.println("Viewing transaction history...");
        // Logic to view transaction history
    }

    private void setPaymentMethod(Scanner scanner) {
        System.out.print("Enter new payment method: ");
        String paymentMethod = scanner.nextLine();
        // Logic to set payment method
        System.out.println("Payment method set to " + paymentMethod);
    }

    private void addPaymentMethod(Scanner scanner) {
        System.out.print("Enter new payment method: ");
        String paymentMethod = scanner.nextLine();
        // Logic to add payment method
        System.out.println("Payment method " + paymentMethod + " added.");
    }
}