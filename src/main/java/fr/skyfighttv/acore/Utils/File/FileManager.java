package fr.skyfighttv.acore.Utils.File;

import fr.skyfighttv.acore.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;

public class FileManager {
    private static HashMap<Files, YamlConfiguration> values;

    public static void init() {
        values = new HashMap<>();

        for (Files files : Files.values()) {
            try {
                File file = new File(Main.getInstances().getDataFolder() + "/" + files.getName() + ".yml");
                if (!file.exists()) {
                    InputStream fileStream = Main.getInstances().getResource(files.getName() + ".yml");
                    byte[] buffer = new byte[fileStream.available()];
                    fileStream.read(buffer);

                    file.createNewFile();
                    OutputStream outStream = new FileOutputStream(file);
                    outStream.write(buffer);
                }
                values.put(files, YamlConfiguration.loadConfiguration(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save(Files files) {
        File file = new File(Main.getInstances().getDataFolder() + "/" + files.getName() + ".yml");
        if (!file.exists()) {
            System.out.println("ERROR : File " + files.getName() + " not found !");
            return;
        }
        try {
            values.get(files).save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.put(files, YamlConfiguration.loadConfiguration(file));
    }

    public static void reload(Files files) {
        File file = new File(Main.getInstances().getDataFolder() + "/" + files.getName() + ".yml");

        if (!file.exists()) {
            System.out.println("ERROR : File " + files.getName() + " not found !");
            return;
        }

        values.put(files, YamlConfiguration.loadConfiguration(file));
    }

    public static HashMap<Files, YamlConfiguration> getValues() {
        return values;
    }
}
