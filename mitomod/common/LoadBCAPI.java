package com.mito.mitomod.common;

import com.mito.mitomod.common.tile.TileOscillatorPipe;

import buildcraft.api.transport.IPipeTile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraftforge.common.util.ForgeDirection;

public class LoadBCAPI {

	public static boolean isPipe(Object object) {
		return object instanceof IPipeTile;
	}

	public static boolean sendItemforPipe(TileOscillatorPipe tile) {

		if (tile.sideObj[tile.sendSide] != null && tile.sideObj[tile.sendSide] instanceof IPipeTile) {
			IPipeTile ipipetile = (IPipeTile) tile.sideObj[tile.sendSide];
			int i = Facing.oppositeSide[tile.sendSide];

			for (int j = 0; j < tile.getSizeInventory(); ++j) {
				if (tile.getStackInSlot(j) != null) {
					ItemStack itemstack = tile.getStackInSlot(j).copy();
					int ins = insertItemToPipe(tile.sideObj[tile.sendSide], tile.decrStackSize(j, 16), ForgeDirection.getOrientation(i));

					if (ins != 0) {
						return true;
					}

					tile.setInventorySlotContents(j, itemstack);

				}
			}
		}
		return false;
	}

	public static int insertItemToPipe(Object into, ItemStack item, ForgeDirection side) {
		if (into instanceof IPipeTile) {
			return ((IPipeTile) into).injectItem(item, true, side, null);
		}
		return 0;
	}

	private static IPipeTile getSidePipe() {
		return null;
	}

}
