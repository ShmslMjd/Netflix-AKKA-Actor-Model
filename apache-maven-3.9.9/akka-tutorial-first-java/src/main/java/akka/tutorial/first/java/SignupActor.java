package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.Map;
import java.util.Scanner;

public class SignupActor extends AbstractActor {
    private final Map<String, String> userDatabase; // Simulated user database
    private final ActorRef loginActor;
    private final ActorRef appActor;

    public SignupActor(ActorRef loginActor, ActorRef appActor, Map<String, String> userDatabase) {
        this.loginActor = loginActor;
        this.appActor = appActor;
        this.userDatabase = userDatabase;
    }

    public static Props props(ActorRef loginActor, ActorRef appActor, Map<String, String> userDatabase) {
        return Props.create(SignupActor.class, () -> new SignupActor(loginActor, appActor, userDatabase));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processMessage)
                .build();
    }

    private void processMessage(String message) {
        if (message.equals("signup")) {
            System.out.println("\n=== signup ===");
            handleSignup();
        }
    }

    private void handleSignup() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userDatabase.containsKey(username)) {
            System.out.println("Username already exists. Please try again.");
            appActor.tell("startMenu", ActorRef.noSender());
        } else {
            userDatabase.put(username, password);
            System.out.println("Signup successful! Please login.");
            loginActor.tell("login", ActorRef.noSender());
        }
    }
}