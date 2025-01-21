package eu.filtastisch.lunarieBuildserverAdditions;

import com.samjakob.spigui.SpiGUI;
import eu.filtastisch.lunarieBuildserverAdditions.commands.WarpCommand;
import eu.filtastisch.lunarieBuildserverAdditions.commands.WarpUtilCommands;
import eu.filtastisch.lunarieBuildserverAdditions.storage.DefaultConfig;
import eu.filtastisch.lunarieBuildserverAdditions.storage.WarpConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class LunarieBuildserverAdditions extends JavaPlugin {

    @Getter
    private static LunarieBuildserverAdditions instance;
    private DefaultConfig defaultConfig;
    private WarpConfig warpConfig;
    private SpiGUI spiGUI;

    @Override
    public void onEnable() {
        instance = this;
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

}
