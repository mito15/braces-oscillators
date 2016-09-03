package com.mito.mitomod.BraceBase.Brace;

import com.mito.mitomod.BraceBase.BraceBase;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Junction extends BraceBase {
	
	public Block contain = null;

	public Junction(World world) {
		super(world);
	}
	
	public Junction(World world, Vec3 pos) {
		super(world, pos);
	}

	@Override
	protected void readBraceBaseFromNBT(NBTTagCompound nbt) {

	}

	@Override
	protected void writeBraceBaseToNBT(NBTTagCompound nbt) {

	}

}
