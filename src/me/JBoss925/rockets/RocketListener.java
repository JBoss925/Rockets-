package me.JBoss925.rockets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by jagger1 on 8/5/16.
 */
public class RocketListener {

    public static Rocket[] rockets;
    public int listenerTaskID;
    Main plugin;
    RocketGame game;

    public RocketListener(Main m, RocketGame rocketGame){
        this.plugin = m;
        this.game = rocketGame;
    }

    public static void registerRocket(Rocket rocket){
        rockets[rockets.length] = rocket;
    }

    public void startRunnable(){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        this.listenerTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                moveRocketsForward();
                for(Rocket r : rockets){
                    for(RocketBlock rb : r.blocks){
                        if(rb.isExternal()){
                            for(Block b : rb.getSurroundingBlocks()){
                                if(b.getType() == game.team1.goalMaterial && r.team.color == game.team2.color){
                                    game.endGame();
                                    break;
                                } else if(b.getType() == game.team2.goalMaterial && r.team.color == game.team1.color){
                                    game.endGame();
                                    break;
                                }
                                if(!(b.getType() == Material.AIR)){
                                    r.onHit();
                                }
                            }
                        }
                    }
                }
            }
        }, 0L, 40L);
    }

    public void moveRocketsForward(){
        for(Rocket r : rockets){
            if(r.direction == Rocket.Direction.NORTH){
                r.removeBlocks();
                for(RocketBlock rb : r.blocks){
                    rb.x = rb.x + Utils.ROCKET_TRAVEL_SPEED;
                    rb.z = rb.z + Utils.ROCKET_TRAVEL_SPEED;
                    new Location(rb.world, rb.x, rb.y, rb.z).getBlock().setType(rb.material);
                }
            }
            //TODO ADD MORE DIRECTIONS AND CHANGE HOW THEY MOVE
        }
    }

}
