package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;
import java.util.Map;
import java.util.Scanner;

public class LoginActor extends AbstractActor {
    private final Map<String, String> userDatabase; // Simulated user database
    private final ActorRef homeActor;
    private final ActorRef appActor;

    public LoginActor(ActorRef homeActor, ActorRef appActor, Map<String, String> userDatabase) {
        this.homeActor = homeActor;
        this.appActor = appActor;
        this.userDatabase = userDatabase;
    }

    public static Props props(ActorRef homeActor, ActorRef appActor, Map<String, String> userDatabase) {
        return Props.create(LoginActor.class, () -> new LoginActor(homeActor, appActor, userDatabase));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, message -> {
                if (message.equals("login")) {
                    System.out.println("\n=== Login ===");
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
            System.out.println("Login successful! Welcome, " + username);
            homeActor.tell("start", ActorRef.noSender());
        } else {
            System.out.println("Invalid username or password. Please try again.");
            appActor.tell("startMenu", ActorRef.noSender());
        }
    }
}