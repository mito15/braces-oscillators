package com.mito.mitomod.utilities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.render.model.Triangle;
import com.mito.mitomod.client.render.model.Vertex;
import com.mito.mitomod.common.Direction26;
import com.mito.mitomod.common.entity.EntityWrapperBB;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MitoUtil {

	public static List<Triangle> decomposeTexture(Triangle tri) {
		Vertex v1 = tri.vertexs[0];
		Vertex v2 = tri.vertexs[1];
		Vertex v3 = tri.vertexs[2];
		List<Triangle> list = new ArrayList<Triangle>();
		double maxU = Math.max(Math.max(v1.textureU, v2.textureU), v3.textureU);
		double minU = Math.min(Math.min(v1.textureU, v2.textureU), v3.textureU);
		int lineU = (int) Math.floor(minU) + 1;
		int numU = (int) Math.floor(maxU) - lineU + 1;
		for(int n = 0; n < numU; n++){
			Triangle[] tris = decomposeLine(tri, lineU);
			lineU++;
		}

		return list;
	}

	public static Triangle[] decomposeLine(Triangle tri, double d) {
		return null;
	}

	public static Triangle[] decomposePolygon(List<Vertex> list) {
		if (list.size() < 3) {
			return null;
		}
		Triangle[] ret = new Triangle[list.size() - 2];
		int n = 0;
		Vec3 curcross = null;
		int curVer = getfar(list);

		while (!list.isEmpty()) {
			if (list.size() == 3) {
				ret[n] = new Triangle(list.get(0), list.get(1), list.get(2));
				break;
			}
			int ne = curVer == list.size() - 1 ? 0 : curVer + 1;
			int pr = curVer == 0 ? list.size() - 1 : curVer - 1;
			Vertex curVec = list.get(curVer);
			Vertex prVec = list.get(pr);
			Vertex neVec = list.get(ne);
			if (curcross == null) {
				curcross = MitoMath.getNormal(curVec, neVec, prVec);
			} else {
				Vec3 cross = MitoMath.getNormal(curVec, neVec, prVec);
				if (cross.dotProduct(curcross) < 0) {
					curVer = curVer == list.size() - 1 ? 0 : curVer + 1;
					continue;
				}
			}
			if (!includePoint(curVec.pos, neVec.pos, prVec.pos, list)) {
				ret[n] = new Triangle(curVec, neVec, prVec);
				list.remove(curVer);
				curVer = getfar(list);
				n++;
			} else {
				curVer = curVer == list.size() - 1 ? 0 : curVer + 1;
			}
		}

		return ret;
	}

	public static boolean includePoint(Vec3 curVec, Vec3 neVec, Vec3 prVec, List<Vertex> list) {
		int curVer = getfar(list);
		int ne = curVer == list.size() - 1 ? 0 : curVer + 1;
		int pr = curVer == 0 ? list.size() - 1 : curVer - 1;
		Vec3 curcross = MitoMath.getNormal(curVec, neVec, prVec);
		boolean flag = false;

		for (int n = 0; n < list.size(); n++) {
			if (n != curVer && n != ne && n != pr) {
				Vec3 d1 = list.get(n).pos;
				//if (Math.abs(curcross.dotProduct(MitoMath.vectorSub(d1, curVec))) < 1.0) {
				Vec3 v12 = MitoMath.vectorSub(neVec, curVec);
				Vec3 v13 = MitoMath.vectorSub(prVec, curVec);
				Vec3 vn1 = MitoMath.vectorSub(v12, MitoMath.vectorMul(v13.normalize(), v13.normalize().dotProduct(v12)));
				if (vn1.dotProduct(MitoMath.vectorSub(d1, curVec)) > 0) {
					Vec3 vn2 = MitoMath.vectorSub(v13, MitoMath.vectorMul(v12.normalize(), v12.normalize().dotProduct(v13)));
					if (vn2.dotProduct(MitoMath.vectorSub(d1, curVec)) > 0) {
						Vec3 v32 = MitoMath.vectorSub(neVec, prVec);
						Vec3 v31 = MitoMath.vectorSub(curVec, prVec);
						Vec3 vn3 = MitoMath.vectorSub(v31, MitoMath.vectorMul(v32.normalize(), v32.normalize().dotProduct(v31)));
						if (vn3.dotProduct(MitoMath.vectorSub(d1, prVec)) > 0) {
							flag = true;
							break;
						}
					}
				}
				//}
			}
		}
		return flag;
	}

	public static int getfar(List<Vertex> line) {
		double d = 0;
		int ret = 0;
		for (int n = 0; n < line.size(); n++) {
			double d1 = MitoMath.abs2(line.get(n).pos);
			if (d1 > d) {
				d = d1;
				ret = n;
			}
		}
		return ret;
	}

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

	/*public static BB_MovingObjectPosition rayTraceBrace(EntityLivingBase player, Vec3 set, Vec3 end, double partialticks) {

		World world = player.worldObj;
		BB_MovingObjectPosition m = null;
		List list = BB_DataLists.getWorldData(world).getBraceBaseWithAABB(MitoUtil.createAABBByVec3(set, end));

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

	public static MovingObjectPosition rayTraceIncl(EntityLivingBase player, double distance, double partialticks) {

		Vec3 start = MitoUtil.getPlayerEyePosition(player, partialticks);
		Vec3 vec31 = player.getLook((float) partialticks);
		Vec3 end = start.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);

		MovingObjectPosition pre = player.worldObj.func_147447_a(MitoMath.copyVec3(start), end, false, false, true);
		MovingObjectPosition m1 = pre == null ? null : new BB_MovingObjectPosition(pre);
		MovingObjectPosition m2 = MitoUtil.rayTraceBrace(player, start, end, (float) partialticks);

		MovingObjectPosition mop;

		if (m1 == null && m2 == null) {
			return null;
		} else if (m1 == null) {
			mop = m2;
		} else if (m2 == null) {
			mop = m1;
		} else {
			if (MitoMath.subAbs(start, m1.hitVec) + 0.05 < MitoMath.subAbs(start, m2.hitVec) && !(player.worldObj.isAirBlock(m1.blockX, m1.blockY, m1.blockZ))) {
				mop = m1;
			} else {
				mop = m2;
			}
		}

		return mop;
	}*/

	public static boolean canClick(World world, BB_Key key, MovingObjectPosition mop) {
		switch (mop.typeOfHit) {
		case BLOCK:
			return !world.isAirBlock(mop.blockX, mop.blockY, mop.blockZ) || key.isAltPressed();
		case ENTITY:
			return true;
		case MISS:
			return key.isAltPressed();
		default:
			return true;
		}
	}

	public static Vec3 getPlayerEyePosition(EntityLivingBase player, double partialticks) {
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

	public static Vec3 snapByShiftKey(EntityPlayer player, Vec3 end, ItemStack itemstack) {

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

	public static void snapByShiftKey(MovingObjectPosition mop, Vec3 set) {

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

	public static boolean isVecEqual(Vec3 v1, Vec3 v2) {
		return MitoMath.subAbs2(v1, v2) < 0.0001;
	}

	public static void snapBlock(MovingObjectPosition mop) {
		mop.hitVec = Vec3.createVectorHelper(Math.floor(mop.hitVec.xCoord * 2 + 0.5) / 2, Math.floor(mop.hitVec.yCoord * 2 + 0.5) / 2, Math.floor(mop.hitVec.zCoord * 2 + 0.5) / 2);
	}

	public static List<int[]> getBlocksOnLine(World world, Vec3 set, Vec3 end, ItemStack itemStack, double size) {
		return getBlocksOnLine(world, set, end, itemStack, size, false);
	}

	public static List<int[]> getBlocksOnLine(World world, Vec3 vec1, Vec3 vec2, ItemStack itemStack, double size, boolean replace) {

		List<int[]> ret = new ArrayList<int[]>();

		Vec3 set = MitoMath.copyVec3(vec1);
		Vec3 end = MitoMath.copyVec3(vec2);

		int i = MathHelper.floor_double(end.xCoord);
		int j = MathHelper.floor_double(end.yCoord);
		int k = MathHelper.floor_double(end.zCoord);
		int l = MathHelper.floor_double(set.xCoord);
		int i1 = MathHelper.floor_double(set.yCoord);
		int j1 = MathHelper.floor_double(set.zCoord);

		if (world.getBlock(l, i1, j1).canReplace(world, l, i1, j1, world.getBlockMetadata(l, i1, j1), itemStack)) {
			ret.add(new int[] { l, i1, j1 });
		}

		int k1 = 200;

		while (k1-- >= 0) {
			if (Double.isNaN(set.xCoord) || Double.isNaN(set.yCoord) || Double.isNaN(set.zCoord)) {
				return null;
			}

			if (l == i && i1 == j && j1 == k) {
				return ret;
			}

			boolean flag6 = true;
			boolean flag3 = true;
			boolean flag4 = true;
			double d0 = 999.0D;
			double d1 = 999.0D;
			double d2 = 999.0D;

			if (i > l) {
				d0 = (double) l + 1.0D;
			} else if (i < l) {
				d0 = (double) l + 0.0D;
			} else {
				flag6 = false;
			}

			if (j > i1) {
				d1 = (double) i1 + 1.0D;
			} else if (j < i1) {
				d1 = (double) i1 + 0.0D;
			} else {
				flag3 = false;
			}

			if (k > j1) {
				d2 = (double) j1 + 1.0D;
			} else if (k < j1) {
				d2 = (double) j1 + 0.0D;
			} else {
				flag4 = false;
			}

			double d3 = 999.0D;
			double d4 = 999.0D;
			double d5 = 999.0D;
			double d6 = end.xCoord - set.xCoord;
			double d7 = end.yCoord - set.yCoord;
			double d8 = end.zCoord - set.zCoord;

			if (flag6) {
				d3 = (d0 - set.xCoord) / d6;
			}

			if (flag3) {
				d4 = (d1 - set.yCoord) / d7;
			}

			if (flag4) {
				d5 = (d2 - set.zCoord) / d8;
			}

			boolean flag5 = false;
			byte b0;

			if (d3 < d4 && d3 < d5) {
				if (i > l) {
					b0 = 4;
				} else {
					b0 = 5;
				}

				set.xCoord = d0;
				set.yCoord += d7 * d3;
				set.zCoord += d8 * d3;
			} else if (d4 < d5) {
				if (j > i1) {
					b0 = 0;
				} else {
					b0 = 1;
				}

				set.xCoord += d6 * d4;
				set.yCoord = d1;
				set.zCoord += d8 * d4;
			} else {
				if (k > j1) {
					b0 = 2;
				} else {
					b0 = 3;
				}

				set.xCoord += d6 * d5;
				set.yCoord += d7 * d5;
				set.zCoord = d2;
			}

			Vec3 vec32 = Vec3.createVectorHelper(set.xCoord, set.yCoord, set.zCoord);
			l = (int) (vec32.xCoord = (double) MathHelper.floor_double(set.xCoord));

			if (b0 == 5) {
				--l;
				++vec32.xCoord;
			}

			i1 = (int) (vec32.yCoord = (double) MathHelper.floor_double(set.yCoord));

			if (b0 == 1) {
				--i1;
				++vec32.yCoord;
			}

			j1 = (int) (vec32.zCoord = (double) MathHelper.floor_double(set.zCoord));

			if (b0 == 3) {
				--j1;
				++vec32.zCoord;
			}

			if (replace || world.getBlock(l, i1, j1).canReplace(world, l, i1, j1, world.getBlockMetadata(l, i1, j1), itemStack)) {
				double res = size / 2;
				if (AxisAlignedBB.getBoundingBox(l + 0.5 - res, i1 + 0.5 - res, j1 + 0.5 - res, l + 0.5 + res, i1 + 0.5 + res, j1 + 0.5 + res).calculateIntercept(set, end) != null)
					ret.add(new int[] { l, i1, j1 });
			}
		}

		return ret;
	}

	public static boolean isBrace(MovingObjectPosition movingOP) {
		return movingOP.typeOfHit == MovingObjectType.ENTITY && movingOP.entityHit != null && movingOP.entityHit instanceof EntityWrapperBB;
	}

	public static BraceBase getBrace(MovingObjectPosition target) {
		if(target.entityHit != null && target.entityHit instanceof EntityWrapperBB)
		return((EntityWrapperBB)target.entityHit).base;
		return null;
	}

	public static void snapBlockOffset(MovingObjectPosition mop) {
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			int x = Direction26.offsetsXForSide[mop.sideHit];
			int y = Direction26.offsetsYForSide[mop.sideHit];
			int z = Direction26.offsetsZForSide[mop.sideHit];
			if (x != 0) {
				mop.hitVec = Vec3.createVectorHelper(0.5 + (double) mop.blockX + (double) x, mop.hitVec.yCoord, mop.hitVec.zCoord);
			} else if (y != 0) {
				mop.hitVec = Vec3.createVectorHelper(mop.hitVec.xCoord, 0.5 + (double) mop.blockY + (double) y, mop.hitVec.zCoord);
			} else {
				mop.hitVec = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, 0.5 + (double) mop.blockZ + (double) z);
			}
		}
	}

}
