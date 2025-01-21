package eu.filtastisch.stupidwarps;

import com.samjakob.spigui.SpiGUI;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import eu.filtastisch.stupidwarps.commands.WarpCommand;
import eu.filtastisch.stupidwarps.commands.WarpUtilCommands;
import eu.filtastisch.stupidwarps.storage.DefaultConfig;
import eu.filtastisch.stupidwarps.storage.WarpConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class StupidWarps extends JavaPlugin {

    @Getter
    private static StupidWarps instance;
    private DefaultConfig defaultConfig;
    private WarpConfig warpConfig;
    private SpiGUI spiGUI;

    @Override
    public void onEnable() {
        instance = this;

        this.loadCommandApi();

        this.registerApis();
        this.loadConfig();
        this.registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerApis(){
        this.spiGUI = new SpiGUI(this);
    }

    private void loadConfig() {
        this.defaultConfig = new DefaultConfig();
        this.warpConfig = new WarpConfig();
    }

    private void registerCommands() {
        new WarpCommand();
        new WarpUtilCommands();
    }

    private void loadCommandApi(){
        if (Bukkit.getPluginManager().getPlugin("CommandAPI") != null) {
            CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                    .silentLogs(true)
                    .setNamespace("stupidwarps")
                    .shouldHookPaperReload(true)
            );
        }
    }

}
