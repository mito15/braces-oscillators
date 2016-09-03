package com.mito.mitomod.client.render.model;

import net.minecraft.util.Vec3;

public class Mat4 {

	public double[] val;

	public Mat4() {
		this.val = new double[] {
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1,
		};
	}

	public Mat4(double[] val) {
		this.val = val;
	}

	public static Mat4 createMat4(double... da) {
		if (da.length == 16) {
			return new Mat4(da);
		} else if (da.length == 9) {
			return new Mat4(new double[] {
					da[0], da[1], da[2], 0,
					da[3], da[4], da[5], 0,
					da[6], da[7], da[8], 0,
					0, 0, 0, 1,
			});
		} else {
			return new Mat4();
		}
	}

	public Mat4 matrixProduct(Mat4 mat) {
		double[] a1 = this.val;
		double[] a2 = mat.val;
		if (a1.length != 16 || a2.length != 16) {
			return null;
		}
		return createMat4(
				a1[0] * a2[0] + a1[1] * a2[4] + a1[2] * a2[8] + a1[3] * a2[12],
				a1[0] * a2[1] + a1[1] * a2[5] + a1[2] * a2[9] + a1[3] * a2[13],
				a1[0] * a2[2] + a1[1] * a2[6] + a1[2] * a2[10] + a1[3] * a2[14],
				a1[0] * a2[3] + a1[1] * a2[7] + a1[2] * a2[11] + a1[3] * a2[15],

		a1[4] * a2[0] + a1[5] * a2[4] + a1[6] * a2[8] + a1[7] * a2[12],
				a1[4] * a2[1] + a1[5] * a2[5] + a1[6] * a2[9] + a1[7] * a2[13],
				a1[4] * a2[2] + a1[5] * a2[6] + a1[6] * a2[10] + a1[7] * a2[14],
				a1[4] * a2[3] + a1[5] * a2[7] + a1[6] * a2[11] + a1[7] * a2[15],

		a1[8] * a2[0] + a1[9] * a2[4] + a1[10] * a2[8] + a1[11] * a2[12],
				a1[8] * a2[1] + a1[9] * a2[5] + a1[10] * a2[9] + a1[11] * a2[13],
				a1[8] * a2[2] + a1[9] * a2[6] + a1[10] * a2[10] + a1[11] * a2[14],
				a1[8] * a2[3] + a1[9] * a2[7] + a1[10] * a2[11] + a1[11] * a2[15],
				0, 0, 0, 1);
		/*a1[12] * a2[0] + a1[13] * a2[4] + a1[14] * a2[8] + a1[15] * a2[12],
		a1[12] * a2[1] + a1[13] * a2[5] + a1[14] * a2[9] + a1[15] * a2[13],
		a1[12] * a2[2] + a1[13] * a2[6] + a1[14] * a2[10] + a1[15] * a2[14],
		a1[12] * a2[3] + a1[13] * a2[7] + a1[14] * a2[11] + a1[15] * a2[15]
		);*/
	}

	public Vec3 transformVec3(Vec3 vec) {
		double[] a1 = this.val;
		return Vec3.createVectorHelper(
				a1[0] * vec.xCoord + a1[1] * vec.yCoord + a1[2] * vec.zCoord + a1[3],
				a1[4] * vec.xCoord + a1[5] * vec.yCoord + a1[6] * vec.zCoord + a1[7],
				a1[8] * vec.xCoord + a1[9] * vec.yCoord + a1[10] * vec.zCoord + a1[11]);
	}

	public Mat4 addCoord(Vec3 vec) {
		if (val.length != 16) {
			return null;
		} else {
			val[3] += vec.xCoord;
			val[7] += vec.yCoord;
			val[11] += vec.zCoord;
		}
		return this;
	}

	public Mat4 add(Mat4 mat) {
		if (val.length != 16) {
			return null;
		} else {
			for (int n = 0; n < 16; n++) {
				val[n] += mat.val[n];
			}
		}
		return this;
	}
	
	public Mat4 mul(double d) {
		if (val.length != 16) {
			return null;
		} else {
			for (int n = 0; n < 16; n++) {
				val[n] *= d;
			}
		}
		return this;
	}
	
	public Mat4 copy() {
		return createMat4(val.clone());
	}

}
