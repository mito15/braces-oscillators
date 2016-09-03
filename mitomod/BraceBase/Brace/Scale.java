package com.mito.mitomod.BraceBase.Brace;

import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.utilities.Line;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Scale extends BraceBase {
	

	public Scale(World world) {
		super(world);
	}

	public Scale(World world, double x, double y, double z) {
		super(world, Vec3.createVectorHelper(x, y, z));
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isDead && !worldObj.isRemote) {
		}
	}

	@Override
	protected void readBraceBaseFromNBT(NBTTagCompound nbt) {

	}

	@Override
	protected void writeBraceBaseToNBT(NBTTagCompound nbt) {

	}
	
	@Override
	public Line interactWithRay(Vec3 set, Vec3 end) {
		Line line = MitoMath.getLineNearPoint(set, end, this.pos);
		if (line.getAbs() < 0.1) {
			return line;
		}
		return null;
	}

}
