package com.mito.mitomod.common.block;

import com.mito.mitomod.common.tile.TileWhiteAsh;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockWhiteAsh extends BlockSaiBase {

	public BlockWhiteAsh() {
		super();
		setBlockName("BlockWhiteAsh");
		setBlockTextureName("mitomod:white");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int a) {
 
		return new TileWhiteAsh();
	}

}
