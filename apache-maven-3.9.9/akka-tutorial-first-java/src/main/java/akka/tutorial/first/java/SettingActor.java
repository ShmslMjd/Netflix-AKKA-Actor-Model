package akka.tutorial.first.java;

import java.util.Scanner;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class SettingActor extends AbstractActor {
    private final ActorRef userProfileActor;
    private final ActorRef billingActor;
    private ActorRef homeActor;

    public SettingActor(ActorRef userProfileActor, ActorRef billingActor, ActorRef homeActor) {
        this.userProfileActor = userProfileActor;
        this.billingActor = billingActor;
        this.homeActor = homeActor;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
            .match(String.class, this::processCommand)
            .match(ActorRef.class, this::updateHomeActor) // Handle dynamic HomeActor updates
            .build();
    }

    private void updateHomeActor(ActorRef homeActor) {
        this.homeActor = homeActor;
        System.out.println("HomeActor reference updated in SettingActor.");
    }

    private void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "open":
                displaySettingsMenu();
                break;
            case "back":
                if (homeActor != null) {
                    homeActor.tell("start", getSelf()); // Notify HomeActor to return to home menu
                } else {
                    System.out.println("HomeActor reference not set. Cannot return to home menu.");
                }
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void displaySettingsMenu() {
        System.out.println("\n--- Settings ---");
        System.out.println("1. Billing");
        System.out.println("2. User Profiles");
        System.out.println("3. Back to Home Menu");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                billingActor.tell("manage", getSelf());
                break;
            case 2:
                userProfileActor.tell("manage", getSelf());
                break;
            case 3:
                System.out.println("Returning to Home Menu...");
                processCommand("back"); // Return to Home Menu
                break;
            default:
                System.out.println("Invalid choice. Try again.");
                displaySettingsMenu();
        }
    }

    public static Props props(ActorRef userProfileActor, ActorRef billingActor, ActorRef homeActor) {
        return Props.create(SettingActor.class, () -> new SettingActor(userProfileActor, billingActor, homeActor));
    }
}
