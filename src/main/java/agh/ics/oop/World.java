package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static void main(String[] args) {

        System.out.println("---------------lab7---------------");

        List<MoveDirection> directions;
        try {
            directions = OptionsParser.parseDirections(args);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return;
        }

        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        ConsoleMapDisplay consoleDisplay = new ConsoleMapDisplay();
        List<Simulation> simulations = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
//            AbstractWorldMap grassFieldMap = new GrassField(10);
//            grassFieldMap.subscribe(consoleDisplay);
//            Simulation grassFieldSimulation = new Simulation(positions, grassFieldMap);
//            simulations.add(grassFieldSimulation);
            AbstractWorldMap rectangularMap = new RectangularMap(5, 5);
            rectangularMap.subscribe(consoleDisplay);
            Simulation rectangularMapSimulation = new Simulation(positions, rectangularMap);
            simulations.add(rectangularMapSimulation);
        }

        SimulationEngine simulationEngine = new SimulationEngine(simulations);

//        simulationEngine.runSync();
//        simulationEngine.runAsync();
        simulationEngine.runAsyncInThreadPool();

        try {
            simulationEngine.awaitSimulationsEnd();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("System operations ended.");

    }
}
