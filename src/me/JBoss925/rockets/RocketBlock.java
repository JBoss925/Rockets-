package me.JBoss925.rockets;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;

/**
 * Created by jagger1 on 8/5/16.
 */
public class RocketBlock {

    double x, y, z;

    World world;

    Material material;

    public RocketBlock(double x, double y, double z, World w, Material m){
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = m;
        this.world = w;
    }

    public boolean isExternal(){
        for(Block b : getSurroundingBlocks()){
            if(b.getType() == Material.AIR){
                return true;
            }
        }
        return false;
    }

    public Block[] getSurroundingBlocks(){
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(world.getBlockAt((int)x+1, (int)y, (int)z));
        blocks.add(world.getBlockAt((int)x-1, (int)y, (int)z));
        blocks.add(world.getBlockAt((int)x, (int)y+1, (int)z));
        blocks.add(world.getBlockAt((int)x, (int)y-1, (int)z));
        blocks.add(world.getBlockAt((int)x, (int)y, (int)z+1));
        blocks.add(world.getBlockAt((int)x, (int)y, (int)z-1));
        return (Block[])blocks.toArray();
    }


}
