package me.JBoss925.rockets;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by jagger1 on 8/6/16.
 */
public class RocketGameManager {

    public static ArrayList<RocketGame> rocketGames = new ArrayList<RocketGame>();

    public RocketGameManager(){}

    public static void registerGame(RocketGame game){
        rocketGames.add(game);
    }

    public static void unregisterGame(RocketGame game){
        rocketGames.remove(game);
    }

    public static RocketGame getRocketGame(String name){
        for(RocketGame rg : rocketGames){
            if(rg.name.equalsIgnoreCase(name)){
                return rg;
            }
        }
        return null;
        //TODO Throw an exception perhaps?
    }

    public void addPlayerToRocketGame(Player player, String rocketGameName){
        for(RocketGame rg : rocketGames){
            if(rg.name.equalsIgnoreCase(rocketGameName)){
                rg.join(player);
            }
        }
        //TODO Add exception (Rocket Game Not Found)?
    }

}
