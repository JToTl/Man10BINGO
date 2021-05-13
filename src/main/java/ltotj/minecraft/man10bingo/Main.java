package ltotj.minecraft.man10bingo;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        GlobalClass.config=new Config(this);
        new EventList(this);
        getCommand("bingo").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
