package com.mito.mitomod.BraceBase.Brace;

import java.util.List;

import com.mito.mitomod.utilities.Line;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ILineBrace {

	public void move(Vec3 motion, int command);

	public void readNBT(NBTTagCompound nbt);

	public void writeNBT(NBTTagCompound nbt);

	public boolean interactWithAABB(AxisAlignedBB boundingBox, double size);

	public Vec3 interactWithLine(Vec3 s, Vec3 e);

	public void rotation(Vec3 cent, double yaw);

	public void resize(Vec3 cent, double i);

	public Line interactWithRay(Vec3 set, Vec3 end, double size);

	public AxisAlignedBB getBoundingBox(double size);

	public double getMinY();

	public double getMaxY();

	public Vec3 getPos();

	public void addCoordinate(double x, double y, double z);

	public void particle();

	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity, double size);

	public void snap(MovingObjectPosition mop, boolean b);

}
