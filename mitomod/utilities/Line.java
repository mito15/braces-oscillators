package com.mito.mitomod.utilities;

import java.util.ArrayList;
import java.util.List;

import com.mito.mitomod.BraceBase.Brace.ILineBrace;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Line implements ILineBrace{

	public Vec3 start;
	public Vec3 end;

	public Line (Vec3 s, Vec3 e){
		this.start = s;
		this.end = e;
	}

	public double getAbs(){
		return MitoMath.subAbs(this.start, this.end);
	}

	@Override
	public void move(Vec3 motion, int command) {
		this.start = MitoMath.vectorPul(this.start, motion);
		this.end = MitoMath.vectorPul(this.end, motion);
	}

	@Override
	public void readNBT(NBTTagCompound nbt) {
		this.start = getVec3(nbt, "start");
		this.end = getVec3(nbt, "end");
	}

	private void setVec3(NBTTagCompound nbt, String name, Vec3 vec) {
		nbt.setDouble(name + "X", vec.xCoord);
		nbt.setDouble(name + "Y", vec.yCoord);
		nbt.setDouble(name + "Z", vec.zCoord);
	}

	private Vec3 getVec3(NBTTagCompound nbt, String name) {
		return Vec3.createVectorHelper(nbt.getDouble(name + "X"), nbt.getDouble(name + "Y"), nbt.getDouble(name + "Z"));
	}

	@Override
	public void writeNBT(NBTTagCompound nbt) {
		setVec3(nbt, "start", start);
		setVec3(nbt, "end", end);
	}

	@Override
	public boolean interactWithAABB(AxisAlignedBB boundingBox, double size) {
		boolean ret = false;
		if (boundingBox.expand(size, size, size).calculateIntercept(start, this.end) != null
				|| (boundingBox.expand(size, size, size).isVecInside(start) && boundingBox.expand(size, size, size).isVecInside(this.end))) {
			ret = true;
		}
		return ret;
	}

	@Override
	public Vec3 interactWithLine(Vec3 s, Vec3 e) {
		Line line = MitoMath.getDistanceLine(s, e, this.start, this.end);
		return line.end;
	}

	@Override
	public void rotation(Vec3 cent, double yaw) {
		start = MitoMath.vectorPul(MitoMath.rotY(MitoMath.vectorSub(start, cent), yaw), cent);
		end = MitoMath.vectorPul(MitoMath.rotY(MitoMath.vectorSub(end, cent), yaw), cent);

	}

	@Override
	public void resize(Vec3 cent, double i) {
		start = MitoMath.vectorPul(MitoMath.vectorMul(MitoMath.vectorSub(start, cent), i), cent);
		end = MitoMath.vectorPul(MitoMath.vectorMul(MitoMath.vectorSub(end, cent), i), cent);

	}

	@Override
	public Line interactWithRay(Vec3 set, Vec3 end, double size) {
		if (this.start.distanceTo(this.end) < 0.01) {
			Vec3 ve = MitoMath.getNearPoint(set, end, this.start);
			if (ve.distanceTo(this.start) < size / 1.5) {
				return new Line(ve, this.start);
			}
		}
		Line line = MitoMath.getDistanceLine(set, end, this.start, this.end);
		if (line.getAbs() < size / 1.5 && !(MitoUtil.isVecEqual(line.end, this.start) || MitoUtil.isVecEqual(line.end, this.end))) {
			return line;
		}
		return null;
	}

	@Override
	public AxisAlignedBB getBoundingBox(double size) {
		double maxX = Math.max(this.start.xCoord, this.end.xCoord);
		double maxY = Math.max(this.start.yCoord, this.end.yCoord);
		double maxZ = Math.max(this.start.zCoord, this.end.zCoord);
		double minX = Math.min(this.start.xCoord, this.end.xCoord);
		double minY = Math.min(this.start.yCoord, this.end.yCoord);
		double minZ = Math.min(this.start.zCoord, this.end.zCoord);
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ).expand(size, size, size);
	}

	@Override
	public double getMinY() {
		return Math.min(start.yCoord, end.yCoord);
	}

	@Override
	public double getMaxY() {
		return Math.max(start.yCoord, end.yCoord);
	}

	@Override
	public Vec3 getPos() {
		return MitoMath.vectorRatio(start, end, 0.5);
	}

	@Override
	public void addCoordinate(double x, double y, double z) {
		this.start = this.start.addVector(x, y, z);
		this.end = this.end.addVector(x, y, z);

	}

	@Override
	public void particle() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity, double size) {
		Vec3 v3 = MitoMath.vectorSub(this.end, this.start);
		int div = size > 0 ? (int) Math.floor(MitoMath.abs(v3) / size) + 1 : 1;
		Vec3 part = MitoMath.vectorDiv(MitoMath.vectorSub(v3, MitoMath.vectorMul(v3.normalize(), size)), div);
		Vec3 offset = MitoMath.vectorMul(v3.normalize(), size / 2);
		List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
		for (int n = 0; n <= div; n++) {
			Vec3 v = MitoMath.vectorPul(this.start, offset, MitoMath.vectorMul(part, (double) n));
			AxisAlignedBB aabb1 = MitoUtil.createAabbBySize(v, size);
			if (aabb1 != null && aabb1.intersectsWith(aabb)) {
				//list.add(aabb1);
				collidingBoundingBoxes.add(aabb1);
			}
		}
	}

	@Override
	public void snap(MovingObjectPosition mop, boolean b) {
		if (b) {
			double leng = MitoMath.subAbs(start, end);
			if (leng < 1.5) {
				double r = MitoMath.subAbs(start, mop.hitVec) / leng;
				//absは絶対値なので厳密ではない
				if (r < 0.3333) {
					mop.hitVec = start;
				} else if (r > 0.6666) {
					mop.hitVec = end;
				} else {
					mop.hitVec = MitoMath.vectorRatio(start, end, 0.5);
				}
			} else {
				if (MitoMath.subAbs(start, mop.hitVec) < 0.5) {
					mop.hitVec = start;
				} else if (MitoMath.subAbs(end, mop.hitVec) < 0.5) {
					mop.hitVec = end;
				} else if (MitoMath.subAbs(MitoMath.vectorRatio(start, end, 0.5), mop.hitVec) < 0.25) {
					mop.hitVec = MitoMath.vectorRatio(start, end, 0.5);
				}
			}
		} else {
			double r = MitoMath.subAbs(start, mop.hitVec) / MitoMath.subAbs(start, end);
			if (r < 0.5) {
				mop.hitVec = start;
			} else if (r > 0.5) {
				mop.hitVec = end;
			}
		}
	}

}
