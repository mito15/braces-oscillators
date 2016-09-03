package com.mito.mitomod.BraceBase;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BB_Chunk extends Chunk {

	public BB_Chunk(World world, Block[] blocks, byte[] bytes, int x, int z) {
		super(world, blocks, bytes, x, z);
	}

}
