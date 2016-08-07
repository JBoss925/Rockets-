package me.JBoss925.rockets;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.*;

/**
 * Created by jagger1 on 8/2/16.
 */
public class Rocket {

    public enum Direction{
        EAST,
        WEST,
        NORTH,
        SOUTH
    }

    public RocketBlock[] blocks;

    Direction direction;

    Team team;

    public Rocket(Team team, Direction direction) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream("rocketBlocks.sav");
        ObjectInputStream save = new ObjectInputStream(file);
        RocketBlock[] blocks = (RocketBlock[]) save.readObject();
        save.close();

        this.blocks = blocks;

        this.team = team;
        this.direction = direction;

        RocketListener.registerRocket(this);
    }

    public void onHit(){
        for(RocketBlock rb : blocks){
            rb.world.getBlockAt((int)rb.x, (int)rb.y, (int)rb.z).setType(Material.AIR);
            rb.world.createExplosion(new Location(rb.world, rb.x, rb.y, rb.z), Utils.ROCKET_EXPLOSION_STRENGTH);
        }
    }

    public void removeBlocks(){
        for(RocketBlock rb : blocks){
            rb.world.getBlockAt((int)rb.x, (int)rb.y, (int)rb.z).setType(Material.AIR);
        }
    }


}
