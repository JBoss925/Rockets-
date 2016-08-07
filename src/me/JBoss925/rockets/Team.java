package me.JBoss925.rockets;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jagger1 on 8/2/16.
 */
public class Team {

    public enum Color{
        RED,
        BLUE
    }

    Color color;

    ArrayList<UUID> players = new ArrayList<UUID>();

    Location spawn;

    Material goalMaterial;

    public Team(Color color, Location spawn, Material goalMaterial){
        this.color = color;
        this.spawn = spawn;
        this.goalMaterial = goalMaterial;
    }

    public void addPlayer(Player p){
        players.add(p.getUniqueId());
    }

    public void addPlayers(Player... p){
        for(Player player : p){
            players.add(player.getUniqueId());
        }
    }

}
