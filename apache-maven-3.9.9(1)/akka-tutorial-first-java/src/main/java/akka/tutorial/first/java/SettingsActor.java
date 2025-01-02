package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.Scanner;

public class SettingsActor extends AbstractActor {
    private final ActorRef profileActor;
    private final ActorRef billingActor;

    public SettingsActor(ActorRef profileActor, ActorRef billingActor) {
        this.profileActor = profileActor;
        this.billingActor = billingActor;
    }

    public static Props props(ActorRef profileActor, ActorRef billingActor) {
        return Props.create(SettingsActor.class, () -> new SettingsActor(profileActor, billingActor));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("open", msg -> displaySettingsMenu())
                .build();
    }

    private void displaySettingsMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exitSettingsMenu = false;
        while (!exitSettingsMenu) {
            System.out.println("\n=== Settings Menu ===");
            System.out.println("1. Profile Settings");
            System.out.println("2. Billing");
            System.out.println("3. Back to Home Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    profileActor.tell("profileSettings", getSelf());
                    break;
                case 2:
                    billingActor.tell("billing", getSelf());
                    break;
                case 3:
                    exitSettingsMenu = true;
                    getSender().tell("home", getSelf());
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}