package com.mito.mitomod.utilities;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.InstObject.BraceBase;
import com.mito.mitomod.InstObject.DataLists;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MitoUtil {

	public static void rotation(int side) {
		if (side == 1) {
			GL11.glRotatef(180, 1, 0, 0);
		} else if (side == 2) {
			GL11.glRotatef(90, 1, 0, 0);
		} else if (side == 3) {
			GL11.glRotatef(270, 1, 0, 0);
		} else if (side == 4) {
			GL11.glRotatef(270, 0, 0, 1);
		} else if (side == 5) {
			GL11.glRotatef(90, 0, 0, 1);
		}
	}

	public static BB_MovingObjectPosition rayTraceIncludeBrace(EntityPlayer player, double distance, float partialtick, boolean cKey) {
		return null;

	}

	public static AxisAlignedBB createAABBByVec3(Vec3 set, Vec3 end) {
		double minX, minY, minZ, maxX, maxY, maxZ;
		if (set.xCoord > end.xCoord) {
			maxX = set.xCoord;
			minX = end.xCoord;
		} else {
			maxX = end.xCoord;
			minX = set.xCoord;
		}
		if (set.yCoord > end.yCoord) {
			maxY = set.yCoord;
			minY = end.yCoord;
		} else {
			maxY = end.yCoord;
			minY = set.yCoord;
		}
		if (set.zCoord > end.zCoord) {
			maxZ = set.zCoord;
			minZ = end.zCoord;
		} else {
			maxZ = end.zCoord;
			minZ = set.zCoord;
		}
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}

	//boolean f1:両端以外も含むかどうか

	public static BB_MovingObjectPosition rayTraceBrace(EntityPlayer player, Vec3 set, Vec3 end, float partialtick) {

		World world = player.worldObj;
		BB_MovingObjectPosition m = null;
		List list = DataLists.getWorldData(world).getBraceBaseWithAABB(MitoUtil.createAABBByVec3(set, end));

		double l = 999.0D;
		for (int n = 0; n < list.size(); n++) {
			if (list.get(n) instanceof BraceBase) {
				BraceBase base = (BraceBase) list.get(n);
				Line line = base.interactWithRay(set, end);
				if (line != null) {
					double l2 = MitoMath.subAbs(line.start, set);
					if (l2 < l) {
						l = l2;
						m = new BB_MovingObjectPosition(base, line.end);
					}
				}
			}
		}

		return m;
	}

	public static Vec3 getPlayerEyePosition(EntityPlayer player, double partialticks) {
		Vec3 set;
		if (partialticks == 1.0F) {
			set = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
		} else {
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * partialticks;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * partialticks;
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialticks;
			set = Vec3.createVectorHelper(d0, d1, d2);
		}
		if (!player.worldObj.isRemote) {
			set = set.addVector(0, 1.62, 0);
		}
		return set;
	}

	public static Vec3 conversionByControlKey(EntityPlayer player, Vec3 set) {

		Vec3 ret = Vec3.createVectorHelper(set.xCoord, set.yCoord, set.zCoord);

		Vec3 vec31 = Vec3.createVectorHelper(Math.floor(ret.xCoord * 2 + 0.5) / 2, Math.floor(ret.yCoord * 2 + 0.5) / 2, Math.floor(ret.zCoord * 2 + 0.5) / 2);

		ret = vec31;

		return ret;

	}

	public static Vec3 conversionByShiftKey(EntityPlayer player, Vec3 end, ItemStack itemstack) {

		Vec3 ret = Vec3.createVectorHelper(end.xCoord, end.yCoord, end.zCoord);

		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt != null) {
			Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));

			if (Math.abs(ret.xCoord - set.xCoord) > Math.abs(ret.yCoord - set.yCoord) && Math.abs(ret.xCoord - set.xCoord) > Math.abs(ret.zCoord - set.zCoord)) {

				ret.yCoord = set.yCoord;
				ret.zCoord = set.zCoord;
			} else if (Math.abs(ret.yCoord - set.yCoord) > Math.abs(ret.zCoord - set.zCoord)) {

				ret.xCoord = set.xCoord;
				ret.zCoord = set.zCoord;
			} else {

				ret.yCoord = set.yCoord;
				ret.xCoord = set.xCoord;
			}
		}

		return ret;
	}

	public static void conversionByShiftKey(BB_MovingObjectPosition mop, Vec3 set) {

		if (Math.abs(mop.hitVec.xCoord - set.xCoord) > Math.abs(mop.hitVec.yCoord - set.yCoord) && Math.abs(mop.hitVec.xCoord - set.xCoord) > Math.abs(mop.hitVec.zCoord - set.zCoord)) {

			mop.hitVec.yCoord = set.yCoord;
			mop.hitVec.zCoord = set.zCoord;
		} else if (Math.abs(mop.hitVec.yCoord - set.yCoord) > Math.abs(mop.hitVec.zCoord - set.zCoord)) {

			mop.hitVec.xCoord = set.xCoord;
			mop.hitVec.zCoord = set.zCoord;
		} else {

			mop.hitVec.yCoord = set.yCoord;
			mop.hitVec.xCoord = set.xCoord;
		}
	}

	public static AxisAlignedBB createAabbBySize(double x, double y, double z, double s) {

		double size = s / 2;
		return AxisAlignedBB.getBoundingBox(x - size, y - size, z - size, x + size, y + size, z + size);
	}

	public static AxisAlignedBB createAabbBySize(Vec3 v, double s) {

		return createAabbBySize(v.xCoord, v.yCoord, v.zCoord, s);
	}

	public static void addVertexWithUV(Vec3 v, double i, double j, Tessellator t) {
		t.addVertexWithUV(v.xCoord, v.yCoord, v.zCoord, i, j);

	}

}
