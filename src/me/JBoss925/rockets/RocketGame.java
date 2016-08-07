package me.JBoss925.rockets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by jagger1 on 8/5/16.
 */
public class RocketGame {

    public static RocketListener rocketListener;

    Team team1, team2;

    String name;

    Location lobby;

    public int queueID;

    public int playersPerTeamNeededToStart = Utils.PLAYERS_PER_TEAM_NEEDED_TO_START;

    public RocketGame(Team team1, Team team2, String name, Location lobby){
        this.team1 = team1;
        this.team2 = team2;
        this.name = name;
        this.lobby = lobby;
    }

    public void queueStart(){
        queueID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
            @Override
            public void run() {
                if(team1.players.size() >= playersPerTeamNeededToStart && team2.players.size() >= playersPerTeamNeededToStart){
                    startGame();
                    Bukkit.getServer().getScheduler().cancelTask(queueID);
                }
            }
        }, 0l, 20l);
    }

    public void startGame(){
        RocketGameManager.registerGame(this);
        for(UUID uuid : team1.players){
            Bukkit.getServer().getPlayer(uuid).teleport(team1.spawn);
        }
        for(UUID uuid : team2.players){
            Bukkit.getServer().getPlayer(uuid).teleport(team2.spawn);
        }
        rocketListener = new RocketListener(Main.instance, this);
        rocketListener.startRunnable();
    }

    public void endGame(){
        for(Rocket r : rocketListener.rockets){
            r.removeBlocks();
            rocketListener.rockets = new Rocket[]{};
        }

        RocketGameManager.unregisterGame(this);
    }

    public void join(Player p){
        p.teleport(lobby);
        if(team1.players.size() == team2.players.size()){
            double rand = Math.random()%2;
            if(rand == 0){
                team1.players.add(p.getUniqueId());
            }
            if(rand == 1){
                team2.players.add(p.getUniqueId());
            }
        }else if(team1.players.size() > team2.players.size()){
            team2.players.add(p.getUniqueId());
        }else if(team1.players.size() < team2.players.size()){
            team1.players.add(p.getUniqueId());
        }
    }

}
