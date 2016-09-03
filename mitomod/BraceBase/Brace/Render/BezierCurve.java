package com.mito.mitomod.BraceBase.Brace.Render;

import java.util.List;

import com.mito.mitomod.BraceBase.Brace.ILineBrace;
import com.mito.mitomod.utilities.Line;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BezierCurve implements ILineBrace {

	Vec3[] points;

	public BezierCurve(Vec3... points){
	this.points = points;
}

public Vec3 getPoint(double t) {
		Vec3 ret = processBezier(points, t);
		return ret;
	}

	public Vec3 getNormal(double t) {
		if (points.length == 3) {
			return MitoMath.normalBezier(points[2], points[1], points[0], t);
		} else if (points.length == 4) {
			return MitoMath.normalBezier(points[3], points[2], points[1], points[0], t);
		} else if (points.length == 2) {
			return MitoMath.vectorSub(points[1], points[0]).normalize();
		}
		return null;
	}

	public Vec3 processBezier(Vec3[] points, double t) {
		if (points.length > 1) {
			Vec3[] ps = new Vec3[points.length - 1];
			for (int n = 0; n < points.length - 1; n++) {
				ps[n] = MitoMath.vectorRatio(points[n], points[n + 1], t);
			}
			return processBezier(ps, t);
		} else if (points.length == 1) {
			return points[0];
		}
		return null;
	}

	@Override
	public void move(Vec3 motion, int command) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void readNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void writeNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public Vec3 interactWithLine(Vec3 s, Vec3 e) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void rotation(Vec3 cent, double yaw) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void resize(Vec3 cent, double i) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public AxisAlignedBB getBoundingBox(double size) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public double getMinY() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public double getMaxY() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public Vec3 getPos() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void addCoordinate(double x, double y, double z) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void particle() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean interactWithAABB(AxisAlignedBB boundingBox, double size) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public Line interactWithRay(Vec3 set, Vec3 end, double size) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity, double size) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void snap(MovingObjectPosition mop, boolean b) {
		if (points == null || points.length == 0) {
			return;
		}
		Vec3 end = points[points.length - 1];
		if (b) {
			double leng = MitoMath.subAbs(points[0], end);
			if (leng < 1.5) {
				double r = MitoMath.subAbs(points[0], mop.hitVec) / leng;
				//absは絶対値なので厳密ではない
				if (r < 0.3333) {
					mop.hitVec = points[0];
				} else if (r > 0.6666) {
					mop.hitVec = end;
				} else {
					mop.hitVec = MitoMath.vectorRatio(points[0], end, 0.5);
				}
			} else {
				if (MitoMath.subAbs(points[0], mop.hitVec) < 0.5) {
					mop.hitVec = points[0];
				} else if (MitoMath.subAbs(end, mop.hitVec) < 0.5) {
					mop.hitVec = end;
				} else if (MitoMath.subAbs(MitoMath.vectorRatio(points[0], end, 0.5), mop.hitVec) < 0.25) {
					mop.hitVec = MitoMath.vectorRatio(points[0], end, 0.5);
				}
			}
		} else {
			double r = MitoMath.subAbs(points[0], mop.hitVec) / MitoMath.subAbs(points[0], end);
			if (r < 0.5) {
				mop.hitVec = points[0];
			} else if (r > 0.5) {
				mop.hitVec = end;
			}
		}
	}

}
