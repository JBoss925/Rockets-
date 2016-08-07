package me.JBoss925.rockets;

import org.bukkit.Material;
import org.bukkit.World;

/**
 * Created by jagger1 on 8/5/16.
 */
public class Utils {

    public static final float ROCKET_EXPLOSION_STRENGTH = 10.0f;
    public static final double ROCKET_TRAVEL_SPEED = 2.0;
    public static final int PLAYERS_PER_TEAM_NEEDED_TO_START = 1;

    public static void spawnRocket(Rocket rocket){
        for(RocketBlock rb : rocket.blocks){
            rb.world.getBlockAt((int)rb.x, (int)rb.y, (int)rb.z).setType(rb.material);
        }
    }

}
