package crafttweaker.mc1120.events.handling;

import crafttweaker.api.block.*;
import crafttweaker.api.event.PlayerInteractEvent;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.*;

/**
 * @author Stan
 */
public class MCPlayerInteractEvent implements PlayerInteractEvent {
    
    private final net.minecraftforge.event.entity.player.PlayerInteractEvent event;
    
    public MCPlayerInteractEvent(net.minecraftforge.event.entity.player.PlayerInteractEvent event) {
        this.event = event;
        
    }
    
    @Override
    public void damageItem(int amount) {
        event.getItemStack().damageItem(amount, event.getEntityPlayer());
    }
    
    @Override
    public IPlayer getPlayer() {
        return CraftTweakerMC.getIPlayer(event.getEntityPlayer());
    }
    
    @Override
    public IWorld getWorld() {
        return CraftTweakerMC.getIWorld(event.getWorld());
    }
    
    @Override
    public int getX() {
        return event.getPos().getX();
    }
    
    @Override
    public int getY() {
        return event.getPos().getY();
    }
    
    @Override
    public int getZ() {
        return event.getPos().getZ();
    }
    
    @Override
    public IBlock getBlock() {
        return getBlockState().getBlock();
    }
    
    @Override
    public IBlockState getBlockState() {
        return CraftTweakerMC.getBlockState(event.getWorld().getBlockState(event.getPos()));
    }
    
    @Override
    public int getDimension() {
        return getWorld().getDimension();
    }
    
    @Override
    public IBlockPos getPosition() {
        return CraftTweakerMC.getIBlockPos(event.getPos());
    }
    
    @Override
    public boolean isCanceled() {
        return event.isCanceled();
    }
    
    @Override
    public void cancel() {
        setCanceled(true);
    }
    
    @Override
    public void setCanceled(boolean canceled) {
        event.setCanceled(canceled);
    }
}
