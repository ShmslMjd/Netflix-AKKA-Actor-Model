package akka.tutorial.first.java;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.PoisonPill;
import akka.routing.RoundRobinPool;

import java.util.concurrent.CountDownLatch;

// Main class
public class Pi {

    public static void main(String[] args) throws Exception {
        Pi pi = new Pi();
        pi.calculate(4, 10000, 10000);
    }

    // ====================
    // ===== Messages =====
    // ====================
    static class Calculate {}

    static class Work {
        private final int start;
        private final int nrOfElements;

        public Work(int start, int nrOfElements) {
            this.start = start;
            this.nrOfElements = nrOfElements;
        }

        public int getStart() { return start; }
        public int getNrOfElements() { return nrOfElements; }
    }

    static class Result {
        private final double value;

        public Result(double value) {
            this.value = value;
        }

        public double getValue() { return value; }
    }

    // ==================
    // ===== Worker =====
    // ==================
    static class Worker extends AbstractActor {

        // Define the work
        private double calculatePiFor(int start, int nrOfElements) {
            double acc = 0.0;
            for (int i = start * nrOfElements; i <= ((start + 1) * nrOfElements - 1); i++) {
                acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
            }
            return acc;
        }

        // Message handler
        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(Work.class, work -> {
                        // Perform the work
                        double result = calculatePiFor(work.getStart(), work.getNrOfElements());

                        // Reply with the result
                        getSender().tell(new Result(result), getSelf());
                    })
                    .build();
        }
    }

    // ==================
    // ===== Master =====
    // ==================
    static class Master extends AbstractActor {
        private final int nrOfMessages;
        private final int nrOfElements;
        private final CountDownLatch latch;

        private double pi;
        private int nrOfResults;
        private long start;

        private ActorRef router;

        public Master(int nrOfWorkers, int nrOfMessages, int nrOfElements, CountDownLatch latch) {
            this.nrOfMessages = nrOfMessages;
            this.nrOfElements = nrOfElements;
            this.latch = latch;

            // Create the workers with a router
            router = getContext().actorOf(new RoundRobinPool(nrOfWorkers).props(Props.create(Worker.class)), "workerRouter");
        }

        // Message handler
        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(Calculate.class, calculate -> {
                        // Schedule work
                        for (int start = 0; start < nrOfMessages; start++) {
                            router.tell(new Work(start, nrOfElements), getSelf());
                        }
                    })
                    .match(Result.class, result -> {
                        // Handle result from the worker
                        pi += result.getValue();
                        nrOfResults += 1;

                        // When all results have been received, stop the master and the router
                        if (nrOfResults == nrOfMessages) {
                            // Send a PoisonPill to the router after all results are collected
                            router.tell(PoisonPill.getInstance(), getSelf());
                            getContext().stop(getSelf());
                        }
                    })
                    .build();
        }

        @Override
        public void preStart() {
            start = System.currentTimeMillis();
        }

        @Override
        public void postStop() {
            // Tell the world that the calculation is complete
            System.out.println(String.format(
                    "\n\tPi estimate: \t\t%s\n\tCalculation time: \t%s millis",
                    pi, (System.currentTimeMillis() - start)));
            latch.countDown();
        }
    }

    // ==================
    // ===== Run it =====
    // ==================
    public void calculate(final int nrOfWorkers, final int nrOfElements, final int nrOfMessages)
            throws Exception {

        // This latch is only plumbing to know when the calculation is completed
        final CountDownLatch latch = new CountDownLatch(1);

        // Create the actor system
        ActorSystem system = ActorSystem.create("PiSystem");

        // Create the master
        ActorRef master = system.actorOf(Props.create(Master.class, nrOfWorkers, nrOfMessages, nrOfElements, latch), "master");

        // Start the calculation
        master.tell(new Calculate(), ActorRef.noSender());

        // Wait for the master to shut down
        latch.await();

        // Shutdown the actor system
        system.terminate();
    }
}
