package com.mito.mitomod.utilities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class OrientedBoundingBox extends AxisAlignedBB {

	Vec3 center, side1, side2, side3;

	public OrientedBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		super(minX, minY, minZ, maxX, maxY, maxZ);
		this.center = Vec3.createVectorHelper((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2);
		this.side1 = Vec3.createVectorHelper(maxX - minX, 0, 0);
		this.side2 = Vec3.createVectorHelper(0, maxY - minY, 0);
		this.side3 = Vec3.createVectorHelper(0, 0, maxZ - minZ);
	}

	public OrientedBoundingBox(Vec3 vert, Vec3 side1, Vec3 side2, Vec3 side3) {
		super(0, 0, 0, 0, 0, 0);

		double x1 = vert.xCoord;
		double x2 = vert.xCoord;
		double y1 = vert.yCoord;
		double y2 = vert.yCoord;
		double z1 = vert.zCoord;
		double z2 = vert.zCoord;
		double x3, y3, z3;
		double[] a1 = { -0.5, 0.5, -0.5, -0.5, 0.5, -0.5, 0.5, 0.5 };
		double[] a2 = { -0.5, -0.5, 0.5, -0.5, 0.5, 0.5, -0.5, 0.5 };
		double[] a3 = { -0.5, -0.5, -0.5, 0.5, -0.5, 0.5, 0.5, 0.5 };
		for (int n = 1; n < 8; n++) {
			x3 = vert.xCoord + a1[n] * side1.xCoord + a2[n] * side2.xCoord + a3[n] * side3.xCoord;
			y3 = vert.yCoord + a1[n] * side1.yCoord + a2[n] * side2.yCoord + a3[n] * side3.yCoord;
			z3 = vert.zCoord + a1[n] * side1.zCoord + a2[n] * side2.zCoord + a3[n] * side3.zCoord;
			if (x1 > vert.xCoord) {
				x1 = x3;
			}
			if (y1 > vert.yCoord) {
				y1 = y3;
			}
			if (z1 > vert.zCoord) {
				z1 = z3;
			}
			if (x2 < vert.xCoord) {
				x1 = x3;
			}
			if (y2 < vert.yCoord) {
				y1 = y3;
			}
			if (z2 < vert.zCoord) {
				z1 = z3;
			}
		}

		this.center = vert;
		this.side1 = side1;
		this.side2 = side2;
		this.side3 = side3;
		this.minX = x1;
		this.minY = y1;
		this.minZ = z1;
		this.maxX = x2;
		this.maxY = y2;
		this.maxZ = z2;

	}

	public double[] getLengthOnSeparateAxis(Vec3 v) {
		Vec3 vert0;
		int i = 0;
		double dotm = v.dotProduct(this.getVertex(0));
		for (int n = 1; n < 8; n++) {
			vert0 = this.getVertex(n);
			double dot = v.dotProduct(vert0);
			if (dot < dotm) {
				dotm = dot;
				i = n;
			}
		}
		double[] ret = { dotm, v.dotProduct(this.getVertex(this.getReverse(i))) };

		return ret;
	}

	public double[] getLengthOnSeparateAxis(Vec3 v, AxisAlignedBB aabb) {
		Vec3 vert0;
		int i = 0;
		double dotm = v.dotProduct(this.getVertex(0, aabb));
		for (int n = 1; n < 8; n++) {
			vert0 = this.getVertex(n, aabb);
			double dot = v.dotProduct(vert0);
			if (dot < dotm) {
				dotm = dot;
				i = n;
			}
		}
		double[] ret = { dotm, v.dotProduct(this.getVertex(this.getReverse(i))) };

		return ret;
	}

	public Vec3 getVertex(int n, AxisAlignedBB aabb) {
		boolean[] a1 = { false, true, false, false, true, false, true, true };
		boolean[] a2 = { false, false, true, false, true, true, false, true };
		boolean[] a3 = { false, false, false, true, false, true, true, true };
		double x = a1[n] ? aabb.maxX : aabb.minX;
		double y = a2[n] ? aabb.maxY : aabb.minY;
		double z = a3[n] ? aabb.maxZ : aabb.minZ;
		return Vec3.createVectorHelper(x, y, z);
	}

	public Vec3 getVertex(int n) {
		double[] a1 = { -0.5, 0.5, -0.5, -0.5, 0.5, -0.5, 0.5, 0.5 };
		double[] a2 = { -0.5, -0.5, 0.5, -0.5, 0.5, 0.5, -0.5, 0.5 };
		double[] a3 = { -0.5, -0.5, -0.5, 0.5, -0.5, 0.5, 0.5, 0.5 };
		double x = center.xCoord + a1[n] * side1.xCoord + a2[n] * side2.xCoord + a3[n] * side3.xCoord;
		double y = center.yCoord + a1[n] * side1.yCoord + a2[n] * side2.yCoord + a3[n] * side3.yCoord;
		double z = center.zCoord + a1[n] * side1.zCoord + a2[n] * side2.zCoord + a3[n] * side3.zCoord;
		return Vec3.createVectorHelper(x, y, z);
	}

	public int getReverse(int i) {
		if (i < 0 || i > 7) {
			return -1;
		}
		int[] ref = { 7, 5, 6, 4, 3, 1, 2, 0 };
		return ref[i];
	}

	public static OrientedBoundingBox getBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		return new OrientedBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static OrientedBoundingBox getBoundingBox(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4) {
		return new OrientedBoundingBox(v1, v2, v3, v4);
	}

	/**
	 * Sets the bounds of the bounding box. Args: minX, minY, minZ, maxX, maxY, maxZ
	 */
	public AxisAlignedBB setBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
		this.minX = x1;
		this.minY = y1;
		this.minZ = z1;
		this.maxX = x2;
		this.maxY = y2;
		this.maxZ = z2;
		this.center = Vec3.createVectorHelper((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2);
		this.side1 = Vec3.createVectorHelper(maxX - minX, 0, 0);
		this.side2 = Vec3.createVectorHelper(0, maxY - minY, 0);
		this.side3 = Vec3.createVectorHelper(0, 0, maxZ - minZ);
		return this;
	}

	/**
	 * Adds the coordinates to the bounding box extending it if the point lies outside the current ranges. Args: x, y, z
	 */
	public AxisAlignedBB addCoord(double p_72321_1_, double p_72321_3_, double p_72321_5_) {
		return super.addCoord(p_72321_1_, p_72321_3_, p_72321_5_);
	}

	/**
	 * Returns a bounding box expanded by the specified vector (if negative numbers are given it will shrink). Args: x,
	 * y, z
	 */
	public AxisAlignedBB expand(double p_72314_1_, double p_72314_3_, double p_72314_5_) {
		return super.expand(p_72314_1_, p_72314_3_, p_72314_5_);
	}

	//重なり部分
	public AxisAlignedBB func_111270_a(AxisAlignedBB p_111270_1_) {
		return super.func_111270_a(p_111270_1_);
	}

	/**
	 * Returns a bounding box offseted by the specified vector (if negative numbers are given it will shrink). Args: x,
	 * y, z
	 */
	public AxisAlignedBB getOffsetBoundingBox(double x, double y, double z) {
		/**
		 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
		 */
		return getBoundingBox(this.center.addVector(x, y, z), this.side1, this.side2, this.side3);
	}

	public boolean isCollisionWithOBB(OrientedBoundingBox obb) {

		if (!(obb.maxX > this.minX && obb.minX < this.maxX ? (obb.maxY > this.minY && obb.minY < this.maxY ? obb.maxZ > this.minZ && obb.minZ < this.maxZ : false) : false)) {
			return false;
		}

		Vec3 sepAxis;
		int i = -1;
		sepAxis = MitoMath.unitVector(this.side1);
		double[] a1;
		double[] a2;
		Vec3[] v = { this.side1, this.side2, this.side3, obb.side1, obb.side2, obb.side3 };
		for (int n = 0; n < v.length; n++) {
			sepAxis = MitoMath.unitVector(v[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = obb.getLengthOnSeparateAxis(sepAxis);
			if (a1[0] < a2[1] && a1[1] > a2[0]) {

			} else {
				i = n;
				return false;
			}
		}

		Vec3[] v1 = { this.side1.crossProduct(obb.side1), this.side1.crossProduct(obb.side2), this.side1.crossProduct(obb.side3), this.side2.crossProduct(obb.side1),
				this.side2.crossProduct(obb.side2), this.side2.crossProduct(obb.side3), this.side3.crossProduct(obb.side1), this.side3.crossProduct(obb.side2), this.side3.crossProduct(obb.side3) };
		for (int n = 0; n < v1.length; n++) {
			sepAxis = MitoMath.unitVector(v1[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = obb.getLengthOnSeparateAxis(sepAxis);
			if (a1[0] < a2[1] && a1[1] > a2[0]) {

			} else {
				i = n;
				return false;
			}
		}

		return true;

	}

	public boolean isCollisionWithAABB(AxisAlignedBB aabb) {

		if (!(aabb.maxX > this.minX && aabb.minX < this.maxX ? (aabb.maxY > this.minY && aabb.minY < this.maxY ? aabb.maxZ > this.minZ && aabb.minZ < this.maxZ : false) : false)) {
			return false;
		}

		Vec3 sepAxis;
		int i = -1;
		sepAxis = MitoMath.unitVector(this.side1);
		double[] a1;
		double[] a2;
		Vec3[] v = { this.side1, this.side2, this.side3 };
		for (int n = 0; n < v.length; n++) {
			sepAxis = MitoMath.unitVector(v[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = this.getLengthOnSeparateAxis(sepAxis, aabb);
			if (a1[0] < a2[1] && a1[1] > a2[0]) {

			} else {
				i = n;
				return false;
			}
		}

		Vec3[] v1 = { MitoMath.crossX(this.side1), MitoMath.crossY(this.side1), MitoMath.crossZ(this.side1), MitoMath.crossX(this.side2), MitoMath.crossY(this.side2), MitoMath.crossZ(this.side2), MitoMath.crossX(this.side3),
				MitoMath.crossY(this.side3), MitoMath.crossZ(this.side3) };
		for (int n = 0; n < v1.length; n++) {
			sepAxis = MitoMath.unitVector(v1[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = this.getLengthOnSeparateAxis(sepAxis, aabb);
			if (a1[0] < a2[1] && a1[1] > a2[0]) {

			} else {
				i = n;
				return false;
			}
		}

		return true;
	}

	public List<Vec3> getAxisListWithOBB(OrientedBoundingBox obb) {

		List<Vec3> ret = new ArrayList<Vec3>();
		Vec3 sepAxis;
		int i = -1;
		sepAxis = MitoMath.unitVector(this.side1);
		double[] a1;
		double[] a2;
		Vec3[] v = { this.side1, this.side2, this.side3, obb.side1, obb.side2, obb.side3 };
		for (int n = 0; n < v.length; n++) {
			sepAxis = MitoMath.unitVector(v[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = obb.getLengthOnSeparateAxis(sepAxis);
			if (a1[0] < a2[1] && a1[1] > a2[0]) {
			} else {
				ret.add(sepAxis);
			}
		}

		Vec3[] v1 = { this.side1.crossProduct(obb.side1), this.side1.crossProduct(obb.side2), this.side1.crossProduct(obb.side3), this.side2.crossProduct(obb.side1),
				this.side2.crossProduct(obb.side2), this.side2.crossProduct(obb.side3), this.side3.crossProduct(obb.side1), this.side3.crossProduct(obb.side2), this.side3.crossProduct(obb.side3) };
		for (int n = 0; n < v1.length; n++) {
			sepAxis = MitoMath.unitVector(v1[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = obb.getLengthOnSeparateAxis(sepAxis);
			if (a1[0] < a2[1] && a1[1] > a2[0]) {
			} else {
				ret.add(sepAxis);
			}
		}

		return ret;

	}

	public List<Vec3> getAxisListWithAABB(AxisAlignedBB aabb) {

		List<Vec3> ret = new ArrayList<Vec3>();

		Vec3 sepAxis;
		int i = -1;
		sepAxis = MitoMath.unitVector(this.side1);
		double[] a1;
		double[] a2;
		Vec3[] v = { this.side1, this.side2, this.side3 };
		for (int n = 0; n < v.length; n++) {
			sepAxis = MitoMath.unitVector(v[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = this.getLengthOnSeparateAxis(sepAxis, aabb);
			if (a1[0] < a2[1] && a1[1] > a2[0]) {

			} else {
				ret.add(sepAxis);
			}
		}

		Vec3[] v1 = { MitoMath.crossX(this.side1), MitoMath.crossY(this.side1), MitoMath.crossZ(this.side1), MitoMath.crossX(this.side2), MitoMath.crossY(this.side2), MitoMath.crossZ(this.side2), MitoMath.crossX(this.side3),
				MitoMath.crossY(this.side3), MitoMath.crossZ(this.side3) };
		for (int n = 0; n < v1.length; n++) {
			sepAxis = MitoMath.unitVector(v1[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = this.getLengthOnSeparateAxis(sepAxis, aabb);
			if (a1[0] < a2[1] && a1[1] > a2[0]) {

			} else {
				ret.add(sepAxis);
			}
		}

		return ret;
	}

	/**
	 * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
	 * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
	 * calculated offset.  Otherwise return the calculated offset.
	 */
	public double calculateXOffset(AxisAlignedBB aabb, double motionX) {

		if (this.isCollisionWithAABB(aabb.addCoord(motionX, 0, 0))) {
			List<Vec3> list = this.getAxisListWithAABB(aabb);
			if (!list.isEmpty()) {
				Vec3 sepAxis;
				double[] a1;
				double[] a2;
				double[] a3;
				double d;
				for (int n = 0; n < list.size(); n++) {
					sepAxis = list.get(n);
					a1 = this.getLengthOnSeparateAxis(sepAxis);
					a2 = this.getLengthOnSeparateAxis(sepAxis, aabb);
					a3 = this.getLengthOnSeparateAxis(sepAxis, aabb.addCoord(motionX, 0, 0));
					if(a1[0] < a2[0]){
						d = ((a2[0] - a1[1]) / (a2[0] - a3[0]));
					} else {
						d = ((a1[0] - a2[1]) / (a3[1] - a2[1]));
					}
				}
			}
		}

		return motionX;
	}

	/**
	 * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
	 * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
	 * calculated offset.  Otherwise return the calculated offset.
	 */
	public double calculateYOffset(AxisAlignedBB aabb, double motionY) {
		if (this.isCollisionWithAABB(aabb.addCoord(0, motionY, 0))) {
			List<Vec3> list = this.getAxisListWithAABB(aabb);
			if (!list.isEmpty()) {
				Vec3 sepAxis;
				double[] a1;
				double[] a2;
				double[] a3;
				double d;
				for (int n = 0; n < list.size(); n++) {
					sepAxis = list.get(n);
					a1 = this.getLengthOnSeparateAxis(sepAxis);
					a2 = this.getLengthOnSeparateAxis(sepAxis, aabb);
					a3 = this.getLengthOnSeparateAxis(sepAxis, aabb.addCoord(0, motionY, 0));
					if(a1[0] < a2[0]){
						d = ((a2[0] - a1[1]) / (a2[0] - a3[0]));
					} else {
						d = ((a1[0] - a2[1]) / (a3[1] - a2[1]));
					}
				}
			}
		}

		return motionY;
	}

	/**
	 * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
	 * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
	 * calculated offset.  Otherwise return the calculated offset.
	 */
	public double calculateZOffset(AxisAlignedBB aabb, double motionZ) {
		if (this.isCollisionWithAABB(aabb.addCoord(0, 0, motionZ))) {
			List<Vec3> list = this.getAxisListWithAABB(aabb);
			if (!list.isEmpty()) {
				Vec3 sepAxis;
				double[] a1;
				double[] a2;
				double[] a3;
				double d;
				for (int n = 0; n < list.size(); n++) {
					sepAxis = list.get(n);
					a1 = this.getLengthOnSeparateAxis(sepAxis);
					a2 = this.getLengthOnSeparateAxis(sepAxis, aabb);
					a3 = this.getLengthOnSeparateAxis(sepAxis, aabb.addCoord(0, 0, motionZ));
					if(a1[0] < a2[0]){
						d = ((a2[0] - a1[1]) / (a2[0] - a3[0]));
					} else {
						d = ((a1[0] - a2[1]) / (a3[1] - a2[1]));
					}
				}
			}
		}

		return motionZ;
	}

	/**
	 * Returns whether the given bounding box intersects with this one. Args: axisAlignedBB
	 */
	public boolean intersectsWith(AxisAlignedBB aabb) {
		if(aabb instanceof OrientedBoundingBox){
			return this.isCollisionWithOBB((OrientedBoundingBox)aabb);
		}
		return this.isCollisionWithAABB(aabb);
	}

	/**
	 * Offsets the current bounding box by the specified coordinates. Args: x, y, z
	 */
	public AxisAlignedBB offset(double p_72317_1_, double p_72317_3_, double p_72317_5_) {
		this.minX += p_72317_1_;
		this.minY += p_72317_3_;
		this.minZ += p_72317_5_;
		this.maxX += p_72317_1_;
		this.maxY += p_72317_3_;
		this.maxZ += p_72317_5_;
		return this;
	}

	/**
	 * Returns if the supplied Vec3D is completely inside the bounding box
	 */
	public boolean isVecInside(Vec3 v) {
		if (this.minX > v.xCoord || this.maxX < v.xCoord || this.minY > v.yCoord || this.maxY < v.yCoord || this.minZ > v.zCoord || this.maxZ < v.zCoord) {
			return false;
		}

		Vec3 sepAxis;
		sepAxis = MitoMath.unitVector(this.side1);
		double[] a1;
		double a2;
		Vec3[] v0 = { this.side1, this.side2, this.side3 };
		for (int n = 0; n < v0.length; n++) {
			sepAxis = MitoMath.unitVector(v0[n]);
			a1 = this.getLengthOnSeparateAxis(sepAxis);
			a2 = sepAxis.dotProduct(v);
			if (a1[0] > a2 || a1[1] < a2) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the average length of the edges of the bounding box.
	 */
	public double getAverageEdgeLength() {
		double d0 = this.maxX - this.minX;
		double d1 = this.maxY - this.minY;
		double d2 = this.maxZ - this.minZ;
		return (d0 + d1 + d2) / 3.0D;
	}

	/**
	 * Returns a bounding box that is inset by the specified amounts
	 */
	public AxisAlignedBB contract(double p_72331_1_, double p_72331_3_, double p_72331_5_) {
		double d3 = this.minX + p_72331_1_;
		double d4 = this.minY + p_72331_3_;
		double d5 = this.minZ + p_72331_5_;
		double d6 = this.maxX - p_72331_1_;
		double d7 = this.maxY - p_72331_3_;
		double d8 = this.maxZ - p_72331_5_;
		/**
		 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
		 */
		return getBoundingBox(d3, d4, d5, d6, d7, d8);
	}

	/**
	 * Returns a copy of the bounding box.
	 */
	public AxisAlignedBB copy() {
		/**
		 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
		 */
		return getBoundingBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public MovingObjectPosition calculateIntercept(Vec3 p_72327_1_, Vec3 p_72327_2_) {
		Vec3 vec32 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.minX);
		Vec3 vec33 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.maxX);
		Vec3 vec34 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.minY);
		Vec3 vec35 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.maxY);
		Vec3 vec36 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.minZ);
		Vec3 vec37 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.maxZ);

		if (!this.isVecInYZ(vec32)) {
			vec32 = null;
		}

		if (!this.isVecInYZ(vec33)) {
			vec33 = null;
		}

		if (!this.isVecInXZ(vec34)) {
			vec34 = null;
		}

		if (!this.isVecInXZ(vec35)) {
			vec35 = null;
		}

		if (!this.isVecInXY(vec36)) {
			vec36 = null;
		}

		if (!this.isVecInXY(vec37)) {
			vec37 = null;
		}

		Vec3 vec38 = null;

		if (vec32 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec32) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec32;
		}

		if (vec33 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec33) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec33;
		}

		if (vec34 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec34) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec34;
		}

		if (vec35 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec35) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec35;
		}

		if (vec36 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec36) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec36;
		}

		if (vec37 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec37) < p_72327_1_.squareDistanceTo(vec38))) {
			vec38 = vec37;
		}

		if (vec38 == null) {
			return null;
		} else {
			byte b0 = -1;

			if (vec38 == vec32) {
				b0 = 4;
			}

			if (vec38 == vec33) {
				b0 = 5;
			}

			if (vec38 == vec34) {
				b0 = 0;
			}

			if (vec38 == vec35) {
				b0 = 1;
			}

			if (vec38 == vec36) {
				b0 = 2;
			}

			if (vec38 == vec37) {
				b0 = 3;
			}

			return new MovingObjectPosition(0, 0, 0, b0, vec38);
		}
	}

	/**
	 * Checks if the specified vector is within the YZ dimensions of the bounding box. Args: Vec3D
	 */
	private boolean isVecInYZ(Vec3 p_72333_1_) {
		return p_72333_1_ == null ? false : p_72333_1_.yCoord >= this.minY && p_72333_1_.yCoord <= this.maxY && p_72333_1_.zCoord >= this.minZ && p_72333_1_.zCoord <= this.maxZ;
	}

	/**
	 * Checks if the specified vector is within the XZ dimensions of the bounding box. Args: Vec3D
	 */
	private boolean isVecInXZ(Vec3 p_72315_1_) {
		return p_72315_1_ == null ? false : p_72315_1_.xCoord >= this.minX && p_72315_1_.xCoord <= this.maxX && p_72315_1_.zCoord >= this.minZ && p_72315_1_.zCoord <= this.maxZ;
	}

	/**
	 * Checks if the specified vector is within the XY dimensions of the bounding box. Args: Vec3D
	 */
	private boolean isVecInXY(Vec3 p_72319_1_) {
		return p_72319_1_ == null ? false : p_72319_1_.xCoord >= this.minX && p_72319_1_.xCoord <= this.maxX && p_72319_1_.yCoord >= this.minY && p_72319_1_.yCoord <= this.maxY;
	}

	/**
	 * Sets the bounding box to the same bounds as the bounding box passed in. Args: axisAlignedBB
	 */
	public void setBB(AxisAlignedBB p_72328_1_) {
		this.minX = p_72328_1_.minX;
		this.minY = p_72328_1_.minY;
		this.minZ = p_72328_1_.minZ;
		this.maxX = p_72328_1_.maxX;
		this.maxY = p_72328_1_.maxY;
		this.maxZ = p_72328_1_.maxZ;
	}

	public String toString() {
		return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
	}

}
