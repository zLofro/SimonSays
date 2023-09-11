package vermillion.productions.utils.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vermillion.productions.Main;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YMLConfig {
    private static final Config YMLconfig = create("config.yml");

    public YMLConfig() {
    }

    public static Config create(String fileName) {
        return new Config(fileName);
    }

    public static <T> void set(String key, T value) {
        YMLconfig.set(key, value);
    }

    public static <T> T get(String key, Class<T> type, T default_) {
        return YMLconfig.get(key, type, default_);
    }

    public static <T> T get(String key, Class<T> type) {
        return YMLconfig.get(key, type);
    }

    public static String getString(String key) {
        return YMLconfig.getString(key);
    }

    public static int getInt(String key) {
        return YMLconfig.getInt(key);
    }

    public static long getLong(String key) {
        return YMLconfig.getLong(key);
    }

    public static boolean getBoolean(String key) {
        return YMLconfig.getBoolean(key);
    }

    public static List<String> getStringList(String key) {
        return YMLconfig.getStringList(key);
    }

    public static boolean load() {
        return YMLconfig.load();
    }

    public static boolean save() {
        return YMLconfig.save();
    }

    public static MemorySection getConfig() {
        return YMLconfig.getConfig();
    }

    public static class Config {
        public final String fileName;
        private FileConfiguration customConfig;

        public Config(String fileName) {
            this.fileName = fileName;
        }

        public <T> void set(String key, T value) {
            this.customConfig.set(key, value);
        }

        public <T> T get(String key, Class<T> type, T default_) {
            return this.customConfig.getObject(key, type, default_);
        }

        public <T> T get(String key, Class<T> type) {
            return this.customConfig.getObject(key, type);
        }

        public String getString(String key) {
            return this.customConfig.getString(key);
        }

        public int getInt(String key) {
            return this.customConfig.getInt(key);
        }

        public long getLong(String key) {
            return this.customConfig.getLong(key);
        }

        public boolean getBoolean(String key) {
            return this.customConfig.getBoolean(key);
        }

        public List<String> getStringList(String key) {
            return this.customConfig.getStringList(key);
        }

        public boolean save() {
            try {
                File file = new File(Main.getInstance().getDataFolder(), this.fileName);
                if (!file.exists() && !this.load()) {
                    return false;
                } else {
                    this.customConfig.save(file);
                    return true;
                }
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }

        public boolean load() {
            File ymlFile = new File(Main.getInstance().getDataFolder(), this.fileName);
            if (!ymlFile.exists()) {
                try {
                    if (!ymlFile.getParentFile().exists() && !ymlFile.getParentFile().mkdirs()) {
                        throw new IOException("Hubo un error al crear la carpeta para el archivo " + this.fileName);
                    }

                    try {
                        if (!ymlFile.createNewFile()) {
                            throw new IOException();
                        }
                    } catch (IOException var3) {
                        throw new IOException("Hubo un error al crear el archivo " + this.fileName);
                    }
                } catch (IOException var4) {
                    Bukkit.getLogger().severe(var4.getMessage());
                    var4.printStackTrace();
                    return false;
                }
            }

            this.customConfig = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), this.fileName));
            return true;
        }

        public MemorySection getConfig() {
            return this.customConfig;
        }
    }
}

