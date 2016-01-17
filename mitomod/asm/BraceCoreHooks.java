package com.mito.mitomod.asm;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BraceCoreHooks {

	public static void getCollisionHook(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity) {
		double d0 = 0.25D;
		Chunk chunk = world.getChunkFromBlockCoords(0, 0);
		/*List list = LoadWorldHandler.INSTANCE.data.getBraceData(aabb.expand(10.0, 10.0, 10.0));

		for (int j2 = 0; j2 < list.size(); ++j2) {
			if (((BraceData)list.get(j2)).bindEntity instanceof EntityBrace && entity instanceof EntityPlayer) {
				BraceData data = (BraceData)list.get(j2);
				double s = data.getDSize();
				Vec3 v3 = MitoMath.vectorSub(data.getEnd(), data.getSet());
				int div = s > 0 ? (int) Math.floor(MitoMath.abs(v3) / s) + 1 : 1;
				Vec3 v1 = MitoMath.vectorDiv(v3, div);
				for (int n = 1; n <= div; n++) {
					Vec3 v2 = MitoMath.vectorMul(v1, (double) n);
					Vec3 v = data.getSet().addVector(v2.xCoord, v2.yCoord, v2.zCoord);
					AxisAlignedBB aabb1 = MitoUtil.createAabbBySize(v, s);
					if (aabb1 != null && aabb1.intersectsWith(aabb)) {
						collidingBoundingBoxes.add(aabb1);
					}
				}
			}
		}*/
//		double d0 = 0.25D;
//		List list = world.getEntitiesWithinAABBExcludingEntity(entity, aabb.expand(d0, d0, d0));
//
//		for (int j2 = 0; j2 < list.size(); ++j2) {
//			if (list.get(j2) instanceof EntityBrace && entity instanceof EntityPlayer) {
//				EntityBrace ent = (EntityBrace)list.get(j2);
//				double s;
//				if (ent.getData() == null) {
//					s = 0;
//				} else {
//					s = ent.getData().getDSize();
//				}
//				Vec3 v3 = MitoMath.vectorSub(ent.getEnd(), ent.getSet());
//				int div = s > 0 ? (int) Math.floor(MitoMath.abs(v3) / s) + 1 : 1;
//				Vec3 v1 = MitoMath.vectorDiv(v3, div);
//				for (int n = 1; n <= div; n++) {
//					Vec3 v2 = MitoMath.vectorMul(v1, (double) n);
//					Vec3 v = ent.getSet().addVector(v2.xCoord, v2.yCoord, v2.zCoord);
//					AxisAlignedBB aabb1 = MitoUtil.createAabbBySize(v, s);
//					if (aabb1 != null && aabb1.intersectsWith(aabb)) {
//						collidingBoundingBoxes.add(aabb1);
//					}
//				}
//			}
//		}
	}

}
