package agh.ics.oop.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class MapCVSLogger implements MapChangeListener {

    private final String filename;
    private final File saveFolder;

    public MapCVSLogger(UUID mapId, File saveFolder) {
        this.filename = "map_" + mapId + ".csv";
        this.saveFolder = saveFolder;
    }

    @Override
    public void onMapChange(WorldMap map, String message) {
        String filename = this.saveFolder.getAbsolutePath() + "/" + this.filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            if (new File(filename).length() == 0) {
                writer.write("Day;Animals;Plants;Unoccupied Positions;Most Common Genome;Average Lifespan of Dead;Average Kids Number");
                writer.newLine();
            }
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the log file: " + e.getMessage());
        }
    }
}
