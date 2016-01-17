package com.mito.mitomod.utilities;

import com.mito.mitomod.InstObject.BraceBase;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class BB_MovingObjectPosition {

	public BB_MovingObjectPosition.MovingObjectType typeOfHit;
	public int blockX;
	public int blockY;
	public int blockZ;
	public int sideHit;
	public Vec3 hitVec;
	public Entity entityHit;
	public int subHit = -1;
	public Object hitInfo = null;
	public BraceBase braceHit;

	public BB_MovingObjectPosition(int x, int y, int z, int side, Vec3 hitvec) {
		this(x, y, z, side, hitvec, true);
	}

	public BB_MovingObjectPosition(int x, int y, int z, int side, Vec3 hitvec, boolean hitBlock) {
		this.typeOfHit = hitBlock ? BB_MovingObjectPosition.MovingObjectType.BLOCK : BB_MovingObjectPosition.MovingObjectType.MISS;
		this.blockX = x;
		this.blockY = y;
		this.blockZ = z;
		this.sideHit = side;
		this.hitVec = Vec3.createVectorHelper(hitvec.xCoord, hitvec.yCoord, hitvec.zCoord);
	}

	public BB_MovingObjectPosition(Entity entity) {
		this(entity, Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ));
	}

	public BB_MovingObjectPosition(Entity entity, Vec3 hitvec) {
		this.typeOfHit = BB_MovingObjectPosition.MovingObjectType.ENTITY;
		this.entityHit = entity;
		this.hitVec = hitvec;
	}

	public BB_MovingObjectPosition(BraceBase base) {
		this(base, Vec3.createVectorHelper(base.pos.xCoord, base.pos.yCoord, base.pos.zCoord));
	}

	public BB_MovingObjectPosition(BraceBase base, Vec3 hitvec) {
		this.typeOfHit = BB_MovingObjectPosition.MovingObjectType.BRACEBASE;
		this.braceHit = base;
		this.hitVec = hitvec;
	}

	public BB_MovingObjectPosition(MovingObjectPosition mop) {
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			this.typeOfHit = BB_MovingObjectPosition.MovingObjectType.BLOCK;
			this.blockX = mop.blockX;
			this.blockY = mop.blockY;
			this.blockZ = mop.blockZ;
			this.sideHit = mop.sideHit;
			this.hitVec = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord);
		} else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
			this.typeOfHit = BB_MovingObjectPosition.MovingObjectType.MISS;
			this.blockX = mop.blockX;
			this.blockY = mop.blockY;
			this.blockZ = mop.blockZ;
			this.sideHit = mop.sideHit;
			this.hitVec = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord);
		} else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
			this.typeOfHit = BB_MovingObjectPosition.MovingObjectType.ENTITY;
			this.entityHit = mop.entityHit;
			this.hitVec = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord);
		}
	}

	public String toString() {
		return "HitResult{type=" + this.typeOfHit + ", x=" + this.blockX + ", y=" + this.blockY + ", z=" + this.blockZ + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
	}

	public static enum MovingObjectType {
		MISS, BLOCK, ENTITY, BRACEBASE;

		private static final String __OBFID = "CL_00000611";
	}

}
