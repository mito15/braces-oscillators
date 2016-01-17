package com.mito.mitomod.InstObject.Brace;

import com.mito.mitomod.InstObject.BraceBase;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LinearMotor extends BraceBase{

	public Brace bindBrace;

	public LinearMotor(World world, Vec3 pos) {
		super(world, pos);
	}

	public LinearMotor(World world, Vec3 pos, Brace brace) {
		this(world, pos);
		this.bindBrace = brace;
	}

	@Override
	protected void readBraceBaseFromNBT(NBTTagCompound p_70037_1_) {

	}

	@Override
	protected void writeBraceBaseToNBT(NBTTagCompound p_70014_1_) {

	}

}
