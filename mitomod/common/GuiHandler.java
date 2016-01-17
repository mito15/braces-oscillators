package com.mito.mitomod.common;

import com.mito.mitomod.client.gui.GuiOChest;
import com.mito.mitomod.common.tile.TileOscillatorChest;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	 @Override
	    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	        if (ID == mitomain.GUI_ID) {
	            return new ContainerOscillatorChest(player, (TileOscillatorChest) world.getTileEntity(x, y, z));
	        }
	        return null;
	    }
	 
	    /*クライアント側の処理*/
	    @Override
	    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	        if (ID == mitomain.GUI_ID) {
	            return new GuiOChest(player, (TileOscillatorChest) world.getTileEntity(x, y, z));
	        }
	        return null;
	    }

}
