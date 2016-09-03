package com.mito.mitomod.common;

import com.mito.mitomod.client.gui.GuiOChest;
import com.mito.mitomod.common.item.itemGUI.ContainerItemBlockSetter;
import com.mito.mitomod.common.item.itemGUI.ContainerItemSelectTool;
import com.mito.mitomod.common.item.itemGUI.GuiItemBlockSetter;
import com.mito.mitomod.common.item.itemGUI.GuiItemSelectTool;
import com.mito.mitomod.common.tile.TileOscillatorChest;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == BAO_main.GUI_ID_OCHEST) {
			return new ContainerOscillatorChest(player, (TileOscillatorChest) world.getTileEntity(x, y, z));
		} else if (ID == BAO_main.GUI_ID_BBSetter) {
			return new ContainerItemBlockSetter(player.inventory);
		} else if (ID == BAO_main.GUI_ID_BBSelect) {
			return new ContainerItemSelectTool(player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == BAO_main.GUI_ID_OCHEST) {
			return new GuiOChest(player, (TileOscillatorChest) world.getTileEntity(x, y, z));
		} else if (ID == BAO_main.GUI_ID_BBSetter) {
			return new GuiItemBlockSetter(player.inventory);
		} else if (ID == BAO_main.GUI_ID_BBSelect) {
			return new GuiItemSelectTool();
		}
		return null;
	}

}
