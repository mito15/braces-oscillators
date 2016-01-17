package com.mito.mitomod.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPosition {
	
	public int x;
	public int y;
	public int z;
	public ForgeDirection orientation;
	
	public BlockPosition(TileEntity tile)
	{
		x = tile.xCoord;
		y = tile.yCoord;
		z = tile.zCoord;
		orientation = ForgeDirection.UNKNOWN;
	}
	

}
