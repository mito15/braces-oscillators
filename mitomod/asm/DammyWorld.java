package com.mito.mitomod.asm;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class DammyWorld {

	private ArrayList collidingBoundingBoxes;

	public List getCollidingBoundingBoxes(Entity p_72945_1_, AxisAlignedBB p_72945_2_) {
		
		//BraceCoreHooks.getCollisionHook(this, p_72945_2_, this.collidingBoundingBoxes, p_72945_1_);
		
		this.collidingBoundingBoxes.clear();
		int i = MathHelper.floor_double(p_72945_2_.minX);
		int j = MathHelper.floor_double(p_72945_2_.maxX + 1.0D);
		int k = MathHelper.floor_double(p_72945_2_.minY);
		int l = MathHelper.floor_double(p_72945_2_.maxY + 1.0D);
		int i1 = MathHelper.floor_double(p_72945_2_.minZ);
		int j1 = MathHelper.floor_double(p_72945_2_.maxZ + 1.0D);

		double d0 = 0.25D;

		ArrayList list = collidingBoundingBoxes;
		for (int j2 = 0; j2 < list .size(); ++j2) {
			AxisAlignedBB axisalignedbb1 = ((Entity) list.get(j2)).getBoundingBox();

			if (axisalignedbb1 != null && axisalignedbb1.intersectsWith(p_72945_2_)) {
				this.collidingBoundingBoxes.add(axisalignedbb1);
			}

			axisalignedbb1 = p_72945_1_.getCollisionBox((Entity) list.get(j2));

			if (axisalignedbb1 != null && axisalignedbb1.intersectsWith(p_72945_2_)) {
				this.collidingBoundingBoxes.add(axisalignedbb1);
			}
		}

		return this.collidingBoundingBoxes;
	}

}
