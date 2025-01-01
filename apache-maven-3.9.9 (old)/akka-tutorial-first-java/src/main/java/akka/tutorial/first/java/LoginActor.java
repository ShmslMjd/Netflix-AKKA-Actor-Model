package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.Map;
import java.util.Scanner;

public class LoginActor extends AbstractActor {
    private final ActorRef homeActor;
    private final ActorRef signupActor;
    private final Map<String, String> userDatabase; // Simulated user database

    public LoginActor(ActorRef homeActor, ActorRef signupActor, Map<String, String> userDatabase) {
        this.homeActor = homeActor;
        this.signupActor = signupActor;
        this.userDatabase = userDatabase;
    }

    public static Props props(ActorRef homeActor, ActorRef signupActor, Map<String, String> userDatabase) {
        return Props.create(LoginActor.class, () -> new LoginActor(homeActor, signupActor, userDatabase));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, message -> {
                if (message.equals("login")) {
                    handleLogin();
                }
            })
            .build();
    }

    private void handleLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            System.out.println("Login successful!");
            homeActor.tell("showHome", getSelf());
        } else {
            System.out.println("Invalid credentials. Would you like to signup instead? (yes/no)");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                signupActor.tell("signup", getSelf());
            } else {
                System.out.println("Exiting...");
                getContext().getSystem().terminate();
            }
        }
    }
}