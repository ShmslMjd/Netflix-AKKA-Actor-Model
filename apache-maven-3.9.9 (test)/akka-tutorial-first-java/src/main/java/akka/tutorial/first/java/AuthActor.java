package main.java.akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AuthActor extends AbstractActor {
    private final Map<String, String> users = new HashMap<>(); // Store username and password

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, this::processMessage)
            .build();
    }

    private void processMessage(String message) {
        if ("start".equalsIgnoreCase(message)) {
            displayLoginMenu();
        } else {
            System.out.println("Unrecognized command in AuthActor: " + message);
        }
    }

    private void displayLoginMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Login or Signup? (login/signup): ");
        String choice = scanner.nextLine();

        if ("login".equalsIgnoreCase(choice)) {
            login(scanner);
        } else if ("signup".equalsIgnoreCase(choice)) {
            signup(scanner);
        } else {
            System.out.println("Invalid choice. Please try again.");
            displayLoginMenu();
        }
    }

    private void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful!");
            // Redirect to HomeActor
            getContext().getSystem().actorSelection("/user/homeActor").tell("start", getSelf());
        } else {
            System.out.println("Invalid credentials. Please try again.");
            displayLoginMenu();
        }
    }

    private void signup(Scanner scanner) {
        System.out.print("Create a username: ");
        String username = scanner.nextLine();
        System.out.print("Create a password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            displayLoginMenu();
        } else {
            users.put(username, password);
            System.out.println("Signup successful! Redirecting to login...");
            displayLoginMenu();
        }
    }

    public static Props props() {
        return Props.create(AuthActor.class);
    }
}
