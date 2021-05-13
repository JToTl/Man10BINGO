package ltotj.minecraft.man10bingo;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class Config {

    private final Plugin plugin;
    private final File configFile;
    private FileConfiguration config=null;

    public Config(Plugin plugin){
        this.plugin=plugin;
        load();
        configFile=new File(plugin.getDataFolder(),"config.yml");
    }

    public void load(){
        plugin.saveDefaultConfig();
        if(config!=null){
            plugin.reloadConfig();
        }
        config=plugin.getConfig();
    }

    public String getString(String string){
        try {
            return config.getString(string);
        }catch(Exception exception){
            System.out.println("コンフィグから"+string+"の値を取るのに失敗しました");
            return "";
        }
    }

    public int getInt(String string){
        try{
            return config.getInt(string);
        }catch(Exception exception){
            System.out.println("コンフィグから"+string+"の値を取るのに失敗しました");
            return 0;
        }
    }

    public double getDouble(String string){
        try{
            return config.getDouble(string);
        }catch(Exception exception){
            System.out.println("コンフィグから"+string+"の値を取るのに失敗しました");
            return 0;
        }
    }

    public void reloadConfig(){
        config= YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream=plugin.getResource("config.yml");
        if(defConfigStream==null){
            return;
        }
        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public FileConfiguration getConfig(){
        if(config==null){
            reloadConfig();
        }
        return config;
    }

    public void saveConfig(){
        if(config==null){
            return;
        }
        try{
            getConfig().save(configFile);
        }catch(IOException ex){
            plugin.getLogger().log(Level.SEVERE,"コンフィグをセーブできませんでした");

        }
    }

}