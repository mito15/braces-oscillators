package com.mito.mitomod.common.block;

import com.mito.mitomod.common.tile.TileAurora;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAurora extends BlockSaiBase {

	public BlockAurora() {
		super();
		setBlockName("BlockAurora");
		setBlockTextureName("mitomod:aurora");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int a) {
 
		return new TileAurora();
	}

}
