package com.ventura11.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        System.out.println("");
        Bukkit.getPluginManager().registerEvents(this, this);
    }


    //HashMap<Player, String> game = new HashMap<>();
    //game.put(player, "teszt");
    List<Player> hunters = new ArrayList<Player>();
    Player speedrunner;
    boolean game;
    int db;

    @EventHandler
    public void onDead(PlayerDeathEvent e){
        if(game == true) {
            if (speedrunner.equals(e.getEntity().getPlayer())) {
                game = false;
                Bukkit.broadcastMessage(""); // A játéknak vége
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        if (cmd.getName().equals("svh")){
            if (sender instanceof Player){
                if(args.length <1) {
                    player.sendMessage("/svh <join|start|reset> <s|h>"); // Ha csak annyit ír be, hogy /svh
                }else if(args[0].equalsIgnoreCase("join")){
                    if(args[1].equalsIgnoreCase("s")){
                        speedrunner = player;
                        player.sendMessage("Sikeresen SpeedRunner lettél."); // Csatlakozott a speedrunner csapatba
                    }else if (args[1].equalsIgnoreCase("h")){
                        hunters.add(player);
                        player.sendMessage("Sikeresen beléptél a Hunter csapatba."); // Csatlakozott a hunter csapatba
                    }
                }else if (args[0].equalsIgnoreCase("start")){
                    if(speedrunner !=null){ //Ezen még tökéletesíteni kell!
                        if(hunters.toArray().length >= 1){
                            Bukkit.broadcastMessage("A játék elindúlt!");
                            for(int i = 0; i < hunters.toArray().length; i++) {
                                Player hunter = hunters.get(i);
                                ItemStack compass = new ItemStack(Material.COMPASS);
                                hunter.getInventory().addItem(compass);
                            }
                            game = true;
                            db = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                                public void run() {
                                    Start();
                                }
                            }, 0L, 100L);
                        }else{
                            player.sendMessage("Nincs elég játékos a Hunter csapatban!");
                        }
                    }else{
                        player.sendMessage("Nincs elég játékos a SpeedRunner csapatban!");
                    }
                }else if(args[0].equalsIgnoreCase("reset")){
                    hunters.clear();
                    speedrunner = null;
                    player.sendMessage("Sikeresen alaphelyzetbe lett állítva.");
                }
            }
        }

        return false;
    }

    public void Start(){
        if (game) {
            for (int i = 0; i < hunters.toArray().length; i++) {
                Player hunter = hunters.get(i);
                hunter.setCompassTarget(speedrunner.getLocation());
            }
        }else{
            Bukkit.getScheduler().cancelTask(db);
            Bukkit.broadcastMessage("A játék vége, mert meghalt a SpeedRunner.");
        }
    }
}
