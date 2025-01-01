package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.Scanner;

public class SettingActor extends AbstractActor {
    private final ActorRef profileActor;
    private final ActorRef billingActor;

    public SettingActor(ActorRef profileActor, ActorRef billingActor) {
        this.profileActor = profileActor;
        this.billingActor = billingActor;
    }

    public static Props props(ActorRef profileActor, ActorRef billingActor) {
        return Props.create(SettingActor.class, () -> new SettingActor(profileActor, billingActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processCommand)
                .build();
    }

    private void processCommand(String command) {
        if (command.equals("open")) {
            displaySettingsMenu();
        } else {
            System.out.println("Invalid command.");
        }
    }

    private void displaySettingsMenu() {
        System.out.println("\n=== Settings Menu ===");
        System.out.println("1. Manage Profiles");
        System.out.println("2. Billing");
        System.out.println("3. Back to Home Menu");
        System.out.print("Choose an option: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                profileActor.tell("manage", getSelf());
                break;
            case 2:
                billingActor.tell("open", getSelf());
                break;
            case 3:
                getSender().tell("start", getSelf()); // Return to Home Menu
                break;
            default:
                System.out.println("Invalid choice. Returning to Settings Menu.");
                displaySettingsMenu();
                break;
        }
    }
}