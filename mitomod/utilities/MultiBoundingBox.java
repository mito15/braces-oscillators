package com.mito.mitomod.utilities;

import com.mito.mitomod.common.mitoLogger;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class MultiBoundingBox extends AxisAlignedBB {

	AxisAlignedBB[] aabbs;

	public MultiBoundingBox(AxisAlignedBB[] aabbs) {
		super(0, 0, 0, 0, 0, 0);
		this.aabbs = aabbs.clone();
		this.minX = aabbs[0].minX;
		this.minY = aabbs[0].minY;
		this.minZ = aabbs[0].minZ;
		this.maxX = aabbs[0].maxX;
		this.maxY = aabbs[0].maxY;
		this.maxZ = aabbs[0].maxZ;
		for (int n = 0; n < aabbs.length; n++) {
			this.minX = Math.min(minX, aabbs[n].minX);
			this.minY = Math.min(minY, aabbs[n].minY);
			this.minZ = Math.min(minZ, aabbs[n].minZ);
			this.maxX = Math.max(maxX, aabbs[n].maxX);
			this.maxY = Math.max(maxY, aabbs[n].maxY);
			this.maxZ = Math.max(maxZ, aabbs[n].maxZ);
		}
	}

	public AxisAlignedBB setBounds(double p_72324_1_, double p_72324_3_, double p_72324_5_, double p_72324_7_, double p_72324_9_, double p_72324_11_) {
		mitoLogger.info("mbb");
		this.minX = p_72324_1_;
		this.minY = p_72324_3_;
		this.minZ = p_72324_5_;
		this.maxX = p_72324_7_;
		this.maxY = p_72324_9_;
		this.maxZ = p_72324_11_;
		return this;
	}

	/**
	 * Adds the coordinates to the bounding box extending it if the point lies outside the current ranges. Args: x, y, z
	 */
	public AxisAlignedBB addCoord(double p_72321_1_, double p_72321_3_, double p_72321_5_) {
		mitoLogger.info("mbb");
		double d3 = this.minX;
		double d4 = this.minY;
		double d5 = this.minZ;
		double d6 = this.maxX;
		double d7 = this.maxY;
		double d8 = this.maxZ;

		if (p_72321_1_ < 0.0D) {
			d3 += p_72321_1_;
		}

		if (p_72321_1_ > 0.0D) {
			d6 += p_72321_1_;
		}

		if (p_72321_3_ < 0.0D) {
			d4 += p_72321_3_;
		}

		if (p_72321_3_ > 0.0D) {
			d7 += p_72321_3_;
		}

		if (p_72321_5_ < 0.0D) {
			d5 += p_72321_5_;
		}

		if (p_72321_5_ > 0.0D) {
			d8 += p_72321_5_;
		}

		/**
		 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
		 */
		return getBoundingBox(d3, d4, d5, d6, d7, d8);
	}

	/**
	 * Returns a bounding box expanded by the specified vector (if negative numbers are given it will shrink). Args: x,
	 * y, z
	 */
	public AxisAlignedBB expand(double p_72314_1_, double p_72314_3_, double p_72314_5_) {
		mitoLogger.info("mbb");
		double d3 = this.minX - p_72314_1_;
		double d4 = this.minY - p_72314_3_;
		double d5 = this.minZ - p_72314_5_;
		double d6 = this.maxX + p_72314_1_;
		double d7 = this.maxY + p_72314_3_;
		double d8 = this.maxZ + p_72314_5_;
		/**
		 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
		 */
		return getBoundingBox(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB func_111270_a(AxisAlignedBB p_111270_1_) {
		mitoLogger.info("mbb");
		double d0 = Math.min(this.minX, p_111270_1_.minX);
		double d1 = Math.min(this.minY, p_111270_1_.minY);
		double d2 = Math.min(this.minZ, p_111270_1_.minZ);
		double d3 = Math.max(this.maxX, p_111270_1_.maxX);
		double d4 = Math.max(this.maxY, p_111270_1_.maxY);
		double d5 = Math.max(this.maxZ, p_111270_1_.maxZ);
		/**
		 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
		 */
		return getBoundingBox(d0, d1, d2, d3, d4, d5);
	}

	/**
	 * Returns a bounding box offseted by the specified vector (if negative numbers are given it will shrink). Args: x,
	 * y, z
	 */
	public AxisAlignedBB getOffsetBoundingBox(double p_72325_1_, double p_72325_3_, double p_72325_5_) {
		mitoLogger.info("mbb");
		/**
		 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
		 */
		return getBoundingBox(this.minX + p_72325_1_, this.minY + p_72325_3_, this.minZ + p_72325_5_, this.maxX + p_72325_1_, this.maxY + p_72325_3_, this.maxZ + p_72325_5_);
	}

	public double calculateXOffset(AxisAlignedBB aabb, double moveX) {
		double ret = moveX;
		for (int n = 0; n < aabbs.length; n++) {
			//if (aabbs[n].intersectsWith(aabb))
				ret = aabbs[n].calculateXOffset(aabb, moveX);
		}
		return ret;
	}

	public double calculateYOffset(AxisAlignedBB aabb, double moveY) {
		double ret = moveY;
		for (int n = 0; n < aabbs.length; n++) {
			//if (aabbs[n].intersectsWith(aabb))
				ret = aabbs[n].calculateYOffset(aabb, moveY);
		}
		return ret;
	}

	public double calculateZOffset(AxisAlignedBB aabb, double moveZ) {
		double ret = moveZ;
		for (int n = 0; n < aabbs.length; n++) {
			//if (aabbs[n].intersectsWith(aabb))
				ret = aabbs[n].calculateZOffset(aabb, moveZ);
		}
		return ret;
	}

	public boolean intersectsWith(AxisAlignedBB p_72326_1_) {
		mitoLogger.info("mbb");
		return p_72326_1_.maxX > this.minX && p_72326_1_.minX < this.maxX ? (p_72326_1_.maxY > this.minY && p_72326_1_.minY < this.maxY ? p_72326_1_.maxZ > this.minZ && p_72326_1_.minZ < this.maxZ : false) : false;
	}

	/**
	 * Offsets the current bounding box by the specified coordinates. Args: x, y, z
	 */
	public AxisAlignedBB offset(double p_72317_1_, double p_72317_3_, double p_72317_5_) {
		mitoLogger.info("mbb");
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
	public boolean isVecInside(Vec3 p_72318_1_) {
		mitoLogger.info("mbb");
		return p_72318_1_.xCoord > this.minX && p_72318_1_.xCoord < this.maxX ? (p_72318_1_.yCoord > this.minY && p_72318_1_.yCoord < this.maxY ? p_72318_1_.zCoord > this.minZ && p_72318_1_.zCoord < this.maxZ : false) : false;
	}

	/**
	 * Returns the average length of the edges of the bounding box.
	 */
	public double getAverageEdgeLength() {
		mitoLogger.info("mbb");
		double d0 = this.maxX - this.minX;
		double d1 = this.maxY - this.minY;
		double d2 = this.maxZ - this.minZ;
		return (d0 + d1 + d2) / 3.0D;
	}

	/**
	 * Returns a bounding box that is inset by the specified amounts
	 */
	public AxisAlignedBB contract(double p_72331_1_, double p_72331_3_, double p_72331_5_) {
		mitoLogger.info("mbb");
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
		mitoLogger.info("mbb");
		/**
		 * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
		 */
		return getBoundingBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public MovingObjectPosition calculateIntercept(Vec3 p_72327_1_, Vec3 p_72327_2_) {
		mitoLogger.info("mbb");
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
		mitoLogger.info("mbb");
		return p_72333_1_ == null ? false : p_72333_1_.yCoord >= this.minY && p_72333_1_.yCoord <= this.maxY && p_72333_1_.zCoord >= this.minZ && p_72333_1_.zCoord <= this.maxZ;
	}

	/**
	 * Checks if the specified vector is within the XZ dimensions of the bounding box. Args: Vec3D
	 */
	private boolean isVecInXZ(Vec3 p_72315_1_) {
		mitoLogger.info("mbb");
		return p_72315_1_ == null ? false : p_72315_1_.xCoord >= this.minX && p_72315_1_.xCoord <= this.maxX && p_72315_1_.zCoord >= this.minZ && p_72315_1_.zCoord <= this.maxZ;
	}

	/**
	 * Checks if the specified vector is within the XY dimensions of the bounding box. Args: Vec3D
	 */
	private boolean isVecInXY(Vec3 p_72319_1_) {
		mitoLogger.info("mbb");
		return p_72319_1_ == null ? false : p_72319_1_.xCoord >= this.minX && p_72319_1_.xCoord <= this.maxX && p_72319_1_.yCoord >= this.minY && p_72319_1_.yCoord <= this.maxY;
	}

	/**
	 * Sets the bounding box to the same bounds as the bounding box passed in. Args: axisAlignedBB
	 */
	public void setBB(AxisAlignedBB p_72328_1_) {
		mitoLogger.info("mbb");
		this.minX = p_72328_1_.minX;
		this.minY = p_72328_1_.minY;
		this.minZ = p_72328_1_.minZ;
		this.maxX = p_72328_1_.maxX;
		this.maxY = p_72328_1_.maxY;
		this.maxZ = p_72328_1_.maxZ;
	}

}
