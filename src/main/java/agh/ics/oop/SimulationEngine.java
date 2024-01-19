package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private List<Simulation> simulationList;
    private List<Thread> threads;
    private ExecutorService executorService;

    public SimulationEngine(List<Simulation> simulationList) {
        this.simulationList = simulationList;
        this.threads = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public List<Simulation> getSimulationList() {
        return this.simulationList;
    }

    public void runSync() {
        simulationList.forEach(Simulation::run);
    }

    public void runAsync() {
        for (Simulation simulation : simulationList) {
            Thread thread = new Thread(simulation);
            threads.add(thread);
            thread.start();
        }
    }

    public void runAsyncInThreadPool() {
        for (Simulation simulation : simulationList) {
            executorService.submit(simulation);
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("Failed to terminate.");
        }
    }

}
