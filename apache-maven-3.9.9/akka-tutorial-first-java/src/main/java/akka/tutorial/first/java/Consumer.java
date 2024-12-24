package akka.tutorial.first.java;

import akka.actor.AbstractActor;

public class Consumer extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(int[].class, msg -> {
                int operator = msg[0]; // Extract operator type (1, 2, 3, or 4)
                int num1 = msg[1];
                int num2 = msg[2];
                if (operator == 1) {
                    System.out.println("<<< Receiving & printing { addition, " + msg[1] + ", " + msg[2] + " }");
                    System.out.println("Consumer doing addition : " + num1 + " + " + num2 + " = " + (num1 + num2) + "\n");
                }
                if (operator == 2) {
                    System.out.println("<<< Receiving & printing { subtraction, " + msg[1] + ", " + msg[2] + " }");
                    System.out.println("Consumer doing subtraction : " + num1 + " - " + num2 + " = " + (num1 - num2) + "\n");
                }
                if (operator == 3) {
                    System.out.println("<<< Receiving & printing { multiplication, " + msg[1] + ", " + msg[2] + " }");
                    System.out.println("Consumer doing multiplication : " + num1 + " x " + num2 + " = " + (num1 * num2) + "\n");
                }
                if (operator == 4) {
                    System.out.println("<<< Receiving & printing { division, " + msg[1] + ", " + msg[2] + " }");
                    System.out.println("Consumer doing division : " + num1 + " / " + num2 + " = " + (num1 / num2) + "\n");
                }
            })
            .build();
    }
}
