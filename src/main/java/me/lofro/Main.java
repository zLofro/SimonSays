package me.lofro;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import me.lofro.game.GameManager;
import me.lofro.game.commands.PointsCommand;
import me.lofro.game.commands.StaffCommand;
import me.lofro.game.listeners.GameListeners;
import me.lofro.utils.ChatColorFormatter;
import me.lofro.utils.CommandUtils;
import me.lofro.utils.ListenerUtils;
import me.lofro.utils.configuration.YMLConfig;
import me.lofro.utils.falseSpectator.FalseSpectator;
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

        YMLConfig.load();

        PaperCommandManager paperCommandManager = new PaperCommandManager(this);

        ListenerUtils.registerListener(new FalseSpectator());

        // Sets the location command completion.
        paperCommandManager.getCommandCompletions().registerCompletion("@location", c -> ImmutableList.of("x,y,z"));

        var databaseURL = YMLConfig.getString("databaseLink");

        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(databaseURL);
            Bukkit.getLogger().info(ChatColorFormatter.stringToString("&aLa base de datos se ha conectado correctamente."));
        } catch (SQLException | ClassNotFoundException e) {
            Bukkit.getLogger().info(e.getMessage());
            Bukkit.getLogger().info(ChatColorFormatter.stringToString("&cLa base de datos no se ha podido conectar correctamente."));
            throw new RuntimeException(e);
        }

        this.gameManager = new GameManager(new GameListeners());

        CommandUtils.registerCommands(paperCommandManager, new PointsCommand(gameManager), new StaffCommand(gameManager));
    }

    @Override
    public void onDisable() {
        if (this.connection != null) {
            try {
                this.connection.close();
                Bukkit.getLogger().info(ChatColorFormatter.stringToString("&aLa base de datos se ha desconectado correctamente."));
            } catch (SQLException e) {
                Bukkit.getLogger().info(e.getMessage());
                Bukkit.getLogger().info(ChatColorFormatter.stringToString("&cLa base de datos no se ha desconectado correctamente."));
                throw new RuntimeException(e);
            }
        }
    }

}
