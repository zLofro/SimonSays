package me.lofro;

import lombok.Getter;
import me.lofro.game.GameManager;
import me.lofro.game.listeners.GameListeners;
import me.lofro.utils.ListenerUtils;
import me.lofro.utils.configuration.YMLConfig;
import me.lofro.utils.falseSpectator.FalseSpectator;
import me.lofro.utils.item.InteractionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends JavaPlugin {

    private static @Getter Main instance;

    private @Getter GameManager gameManager;

    private @Getter Connection connection;

    @Override
    public void onEnable() {
        instance = this;
        ListenerUtils.registerListeners(new InteractionManager(), new FalseSpectator()); // Registra las interacciones.

        var databaseURL = YMLConfig.getString("databaseLink");

        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(databaseURL);
            Bukkit.getLogger().info("&aLa base de datos se ha conectado correctamente.");
        } catch (SQLException | ClassNotFoundException e) {
            Bukkit.getLogger().info(e.getMessage());
            Bukkit.getLogger().info("&cLa base de datos no se ha podido conectar correctamente.");
            throw new RuntimeException(e);
        }

        this.gameManager = new GameManager(new GameListeners());
    }

    @Override
    public void onDisable() {
        if (this.connection != null) {
            try {
                this.connection.close();
                Bukkit.getLogger().info("&aLa base de datos se ha desconectado correctamente.");
            } catch (SQLException e) {
                Bukkit.getLogger().info(e.getMessage());
                Bukkit.getLogger().info("&cLa base de datos no se ha desconectado correctamente.");
                throw new RuntimeException(e);
            }
        }
    }

}
