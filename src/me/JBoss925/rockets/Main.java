package me.JBoss925.rockets;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by jagger1 on 8/5/16.
 */
public class Main extends JavaPlugin{


    public static Main instance;

    public static RocketGameManager manager;

    HashMap<UUID, RocketGame> editingCopy = new HashMap<UUID, RocketGame>();

    public HashMap<UUID, RocketGame> currentWorkingCopy = new HashMap<UUID, RocketGame>();

    public HashMap<UUID, CreationState> currentWorkingState = new HashMap<UUID, CreationState>();

    @Override
    public void onEnable(){
        instance = this;
        manager = new RocketGameManager();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) throws IOException, ClassNotFoundException {
        if(e.getPlayer().getItemInHand().getType() == Material.DIAMOND_HOE){
            for(RocketGame game : RocketGameManager.rocketGames){
                if(game.team1.players.contains(e.getPlayer().getUniqueId())){
                    Rocket r = new Rocket(game.team1, Rocket.Direction.EAST);
                    Utils.spawnRocket(r);
                    //TODO GET THE PLAYER'S DIRECTION FROM THE YAW
                }
                if(game.team2.players.contains(e.getPlayer().getUniqueId())){
                    Rocket r = new Rocket(game.team2, Rocket.Direction.EAST);
                    Utils.spawnRocket(r);
                    //TODO GET THE PLAYER'S DIRECTION FROM THE YAW
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command!");
        }
        if(command.getName().equalsIgnoreCase("rockets") && sender instanceof Player){
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("init") && args.length == 2 && !currentWorkingState.containsKey(player.getUniqueId())){
                currentWorkingState.put(player.getUniqueId(), CreationState.SETTING_SPAWN_ONE);

                Team team1 = new Team(Team.Color.RED, null, Material.OBSIDIAN);
                Team team2 = new Team(Team.Color.BLUE, null, Material.DIAMOND);
                String tempRocketGameName = args[1];
                Location lobby = ((Player)sender).getLocation();
                RocketGame game = new RocketGame(team1, team2, tempRocketGameName, lobby);
                currentWorkingCopy.put(player.getUniqueId(), game);
                player.sendMessage("Your current working copy \"" + tempRocketGameName + "\" has been created.\n" +
                        "The lobby location is where you are currently standing.\n" +
                        "Now type \"/rockets setRedTeamSpawn\" to set the red team's spawn.");
                return true;
                //TODO ADD IMPLEMENTATION FOR SUPPLYING INFO TO THE GAME
            }
            if(args[0].equalsIgnoreCase("setTeamRedSpawn") && currentWorkingState.containsKey(player.getUniqueId())
                    && currentWorkingCopy.containsKey(player.getUniqueId())){
                if(currentWorkingState.get(player.getUniqueId()) == CreationState.SETTING_SPAWN_ONE){
                    currentWorkingCopy.get(player.getUniqueId()).team1.spawn = player.getLocation();
                    currentWorkingState.put(player.getUniqueId(), CreationState.SETTING_SPAWN_TWO);
                    player.sendMessage("You have set the red team spawn for the working copy \"" + currentWorkingCopy.get(player.getUniqueId()).name
                    + "\".\n" + "Now type \"/rockets setBlueTeamSpawn\" to set the blue team's spawn.");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("setBlueTeamSpawn") && currentWorkingState.containsKey(player.getUniqueId())
                    && currentWorkingCopy.containsKey(player.getUniqueId())){
                if(currentWorkingState.get(player.getUniqueId()) == CreationState.SETTING_SPAWN_TWO){
                    currentWorkingCopy.get(player.getUniqueId()).team2.spawn = player.getLocation();
                    currentWorkingState.put(player.getUniqueId(), CreationState.GAME_READY);
                    player.sendMessage("You have set the blue team spawn for the working copy " + currentWorkingCopy.get(player.getUniqueId()).name
                            + "\". The game is now ready.\n" + "Now type \"/rockets start\" to start the game.");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("start") && currentWorkingState.containsKey(player.getUniqueId())
                    && currentWorkingCopy.containsKey(player.getUniqueId())){
                if(currentWorkingState.get(player.getUniqueId()) == CreationState.GAME_READY){
                    currentWorkingCopy.get(player.getUniqueId()).queueStart();
                    currentWorkingCopy.remove(player.getUniqueId());
                    currentWorkingState.remove(player.getUniqueId());
                    player.sendMessage("The game has now been registered. If you want to edit a game, type \"/rockets edit <game name>\"");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("edit") && args.length == 2){
                player.sendMessage("Not implemented yet.");
                return true;
            }
            if(args[0].equalsIgnoreCase("join") && args.length == 2){
                manager.addPlayerToRocketGame(player, args[1]);
                player.sendMessage("Added to game \"" + args[1] + "\"");
                return true;
            }
            if(args[0].equalsIgnoreCase("getInstructions")){
                if(currentWorkingState.containsKey(player.getUniqueId())){
                    if(currentWorkingState.get(player.getUniqueId()) == CreationState.SETTING_SPAWN_ONE){
                        player.sendMessage("Your current working copy \"" + currentWorkingCopy.get(player.getUniqueId()).name + "\" has been created.\n" +
                                "The lobby location is where you are currently standing.\n" +
                                "Now type \"/rockets setRedTeamSpawn\" to set the red team's spawn.");
                    }
                    if(currentWorkingState.get(player.getUniqueId()) == CreationState.SETTING_SPAWN_TWO){
                        player.sendMessage("You have set the red team spawn for the working copy \"" + currentWorkingCopy.get(player.getUniqueId()).name
                                + "\".\n" + "Now type \"/rockets setBlueTeamSpawn\" to set the blue team's spawn.");
                        return true;
                    }
                    if(currentWorkingState.get(player.getUniqueId()) == CreationState.GAME_READY){
                        player.sendMessage("You have set the blue team spawn for the working copy " + currentWorkingCopy.get(player.getUniqueId()).name
                                + "\". The game is now ready.\n" + "Now type \"/rockets start\" to start the game.");
                        return true;
                    }
                    return true;
                }else{
                    player.sendMessage("You haven't set your working copy yet! Type \"/rockets init\" to create a new one " +
                            "or type \"/rockets edit <game name>\" to edit a current game.");
                    return true;
                }
            }
        }
        ((Player) sender).sendMessage("Something was invalid with that command. Type \"/rockets getInstructions\" to get your next needed command.");
        return true;
    }

    public enum CreationState{
        SETTING_SPAWN_ONE,
        SETTING_SPAWN_TWO,
        GAME_READY
    }

}
