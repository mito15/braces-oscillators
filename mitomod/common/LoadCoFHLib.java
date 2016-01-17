package com.mito.mitomod.common;

import com.mito.mitomod.common.tile.TileOscillatorPipe;

import cofh.api.transport.IItemDuct;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraftforge.common.util.ForgeDirection;

public class LoadCoFHLib {

	public static boolean isDuct(Object object) {
		return object instanceof IItemDuct;
	}

	public static boolean sendItemforPipe(TileOscillatorPipe tile) {
		if (tile.sideObj[tile.sendSide] != null && tile.sideObj[tile.sendSide] instanceof IItemDuct){
			IItemDuct ipipetile = (IItemDuct) tile.sideObj[tile.sendSide];

			int i = Facing.oppositeSide[tile.sendSide];

			for (int j = 0; j < tile.getSizeInventory(); ++j) {
				if (tile.getStackInSlot(j) != null) {
					ItemStack itemstack = tile.getStackInSlot(j).copy();
					ItemStack itemstack2 = tile.decrStackSize(j, 16);
					int stacksize = 0;if(itemstack2 != null){stacksize = itemstack2.stackSize;}
					ItemStack itemstack1 = insertItemToDuct(ipipetile, itemstack2, ForgeDirection.getOrientation(i));

					tile.setInventorySlotContents(j, itemstack1);
					if (stacksize > itemstack1.stackSize) {
						return true;
					}

					tile.setInventorySlotContents(j, itemstack);
				}
			}
		}
		return false;
	}

	public static ItemStack insertItemToDuct(IItemDuct into, ItemStack item, ForgeDirection side) {
		if (into instanceof IItemDuct) {
			return into.insertItem(side, item);
		}
		return item;
	}

	private static IItemDuct getSidePipe() {
		return null;
	}

}
