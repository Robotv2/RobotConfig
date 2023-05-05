package fr.robotv2.robotconfig;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RobotConfigFile {

    private final String name;
    private final Plugin plugin;

    private File file;
    private YamlConfiguration configuration;

    public RobotConfigFile(@NotNull Plugin plugin, @NotNull String name) {
        this(plugin, name, false);
    }

    public RobotConfigFile(@NotNull Plugin plugin, @NotNull String name, boolean setupOnInit) {
        this.plugin = plugin;
        this.name = name;

        if(setupOnInit) {
            setup();
        }
    }

    private void setupFile() {
        this.file = new File(plugin.getDataFolder(), name.endsWith(".yml") ? name : name.concat(".yml"));
    }

    public void setup() {

        if(this.file == null) {
            this.setupFile();
        }

        if(!file.exists()) {

            if(file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            plugin.saveResource(name + ".yml", false);
        }
    }

    public void reload() {

        if(this.file == null) {
            this.setupFile();
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
        final InputStream defaultStream = plugin.getResource(name + ".yml");

        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.configuration.setDefaults(defaultConfig);
        }
    }

    public YamlConfiguration getConfiguration() {

        if(configuration == null) {
            reload();
        }

        return configuration;
    }

    public void save() {

        if(file == null || configuration == null) {
            return;
        }

        try {
            getConfiguration().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
}
