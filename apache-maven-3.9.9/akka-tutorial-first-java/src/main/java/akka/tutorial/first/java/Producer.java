package akka.tutorial.first.java;

import akka.tutorial.first.java.Consumer;

import java.util.concurrent.TimeUnit;
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Producer {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("generate-multiple-calculations");
        ActorRef printNumbersConsumer = system.actorOf(Props.create(Consumer.class));
        int rand_int1, rand_int2, operatorand;
        Random rand = new Random();
        for (int i = 1; i <= 10; i++) {
            operatorand = rand.nextInt(4) + 1;
            if(operatorand == 1) {
                rand_int1 = rand.nextInt(1000) + 1;
                rand_int2 = rand.nextInt(1000) + 1;
                System.out.println(">>> Producing & sending 2 numbers " +  rand_int1 + " and " + rand_int2);
                System.out.println("Producer plans to do addition!");
                int[] numbers = {1, rand_int1, rand_int2};
                printNumbersConsumer.tell(numbers, ActorRef.noSender());
                TimeUnit.SECONDS.sleep(1); // sleep for 1 second before sending the next number
            }
            if(operatorand == 2) {
                rand_int1 = rand.nextInt(1000) + 1;
                rand_int2 = rand.nextInt(1000) + 1;
                System.out.println(">>> Producing & sending 2 numbers " +  rand_int1 + " and " + rand_int2);
                System.out.println("Producer plans to do subtraction!");
                int[] numbers = {2, rand_int1, rand_int2};
                printNumbersConsumer.tell(numbers, ActorRef.noSender());
                TimeUnit.SECONDS.sleep(1); // sleep for 1 second before sending the next number
            }
            if(operatorand == 3) {
                rand_int1 = rand.nextInt(1000) + 1;
                rand_int2 = rand.nextInt(20) + 1;
                System.out.println(">>> Producing & sending 2 numbers " +  rand_int1 + " and " + rand_int2);
                System.out.println("Producer plans to do multiplication!");
                int[] numbers = {3, rand_int1, rand_int2};
                printNumbersConsumer.tell(numbers, ActorRef.noSender());
                TimeUnit.SECONDS.sleep(1); // sleep for 1 second before sending the next number
            }
            if(operatorand == 4) {
                rand_int1 = rand.nextInt(1000) + 1;
                rand_int2 = rand.nextInt(20) + 1;
                System.out.println(">>> Producing & sending 2 numbers " +  rand_int1 + " and " + rand_int2);
                System.out.println("Producer plans to do division!");
                int[] numbers = {4, rand_int1, rand_int2};
                printNumbersConsumer.tell(numbers, ActorRef.noSender());
                TimeUnit.SECONDS.sleep(1); // sleep for 1 second before sending the next number
            }
        }
        
        system.terminate();
        System.out.println("===== Finished producing & sending numbers 1 to 10");
        
    }
}

