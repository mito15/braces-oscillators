package com.mito.mitomod.BraceBase.Brace;

import com.mito.mitomod.BraceBase.BraceBase;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Wall extends BraceBase {

	public Wall(World world) {
		super(world);
	}
	
	public Wall(World world, Vec3 pos) {
		super(world, pos);
	}

	@Override
	protected void readBraceBaseFromNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void writeBraceBaseToNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
