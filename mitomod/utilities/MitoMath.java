package com.mito.mitomod.utilities;

import net.minecraft.util.Vec3;

public final class MitoMath {

	public static double[] vectorRatio(double[] set, double[] end, double r) {
		double[] ret = vectorPul(vectorMul(end, r), vectorMul(set, 1 - r));
		return ret;
	}

	public static Vec3 vectorRatio(Vec3 set, Vec3 end, double r) {
		Vec3 ret = vectorPul(vectorMul(end, r), vectorMul(set, 1 - r));
		return ret;
	}

	public static double[] crossProduct(double[] d, double[] m) {
		double[] ret = { d[1] * m[2] - d[2] * m[1], d[2] * m[0] - d[0] * m[2], d[0] * m[1] - d[1] * m[0] };
		return ret;
	}

	public static double subAbs(double[] v1, double[] v2) {
		double ret = MitoMath.abs(v1[0] - v2[0], v1[1] - v2[1], v1[2] - v2[2]);
		return ret;
	}

	public static double subAbs(double[] v1, Vec3 v2) {
		double ret = MitoMath.abs(v1[0] - v2.xCoord, v1[1] - v2.yCoord, v1[2] - v2.zCoord);
		return ret;
	}

	public static double subAbs(Vec3 v1, Vec3 v2) {
		double ret = MitoMath.abs(v1.xCoord - v2.xCoord, v1.yCoord - v2.yCoord, v1.zCoord - v2.zCoord);
		return ret;
	}

	public static double subAbs2(Vec3 v1, Vec3 v2) {
		double ret = MitoMath.abs2(v1.xCoord - v2.xCoord, v1.yCoord - v2.yCoord, v1.zCoord - v2.zCoord);
		return ret;
	}

	public static double subAbs2(double[] v1, double[] v2) {
		double ret = MitoMath.abs2(v1[0] - v2[0], v1[1] - v2[1], v1[2] - v2[2]);
		return ret;
	}

	public static double dotProduct(double[] d, double[] m) {
		double ret = d[0] * m[0] + d[1] * m[1] + d[2] * m[2];
		return ret;
	}

	public static double[] vectorMul(double[] d, double m) {
		double[] ret = { d[0] * m, d[1] * m, d[2] * m };
		return ret;
	}

	public static Vec3 vectorMul(Vec3 d, double m) {
		Vec3 ret = Vec3.createVectorHelper(d.xCoord * m, d.yCoord * m, d.zCoord * m);
		return ret;
	}

	public static double[] vectorCul(double[] d1, double n1, double[] d2, double n2) {
		double[] ret = vectorPul(vectorMul(d1, n1), vectorMul(d2, n2));
		return ret;
	}

	public static double[] vectorCul(double[] d1, double[] d2, double n2) {
		double[] ret = vectorPul(d1, vectorMul(d2, n2));
		return ret;
	}

	public static double[] vectorDiv(double[] d, double m) {

		if (m == 0) {
			return d;
		}

		double[] ret = { d[0] / m, d[1] / m, d[2] / m };
		return ret;
	}

	public static Vec3 vectorDiv(Vec3 d, double m) {

		if (m == 0) {
			return d;
		}

		Vec3 ret = Vec3.createVectorHelper(d.xCoord / m, d.yCoord / m, d.zCoord / m);
		return ret;
	}

	public static double[] vecSetL(double[] d, double l) {
		double[] ret = vectorMul(unitVector(d), l);
		return ret;
	}

	public static double[] vectorPul(double[] d1, double[] d2) {
		double[] ret = { d1[0] + d2[0], d1[1] + d2[1], d1[2] + d2[2] };
		return ret;
	}

	public static double[] vectorPul(double[] d1, Vec3 d2) {
		double[] ret = { d1[0] + d2.xCoord, d1[1] + d2.yCoord, d1[2] + d2.zCoord };
		return ret;
	}

	public static Vec3 vectorPul(Vec3 d1, Vec3 d2) {
		Vec3 ret = Vec3.createVectorHelper(d1.xCoord + d2.xCoord, d1.yCoord + d2.yCoord, d1.zCoord + d2.zCoord);
		return ret;
	}

	public static Vec3 vectorSub(Vec3 d1, Vec3 d2) {
		if (d1 == null || d2 == null) {
			return Vec3.createVectorHelper(0, 0, 0);
		}
		Vec3 ret = Vec3.createVectorHelper(d1.xCoord - d2.xCoord, d1.yCoord - d2.yCoord, d1.zCoord - d2.zCoord);
		return ret;
	}

	public static double[] vectorBezier(double[] d1, double[] d2, double[] d3, double[] d4, double ratio) {
		double[] ret = vectorRatio(vectorRatio(vectorRatio(d1, d2, ratio), vectorRatio(d2, d3, ratio), ratio),
				vectorRatio(vectorRatio(d2, d3, ratio), vectorRatio(d3, d4, ratio), ratio), ratio);
		return ret;
	}

	public static Vec3 vectorBezier(Vec3 d1, Vec3 d2, Vec3 d3, Vec3 d4, double ratio) {
		Vec3 ret = vectorRatio(vectorRatio(vectorRatio(d1, d2, ratio), vectorRatio(d2, d3, ratio), ratio),
				vectorRatio(vectorRatio(d2, d3, ratio), vectorRatio(d3, d4, ratio), ratio), ratio);
		return ret;
	}

	public static double abs(double x, double y, double z) {
		double ret = Math.sqrt(x * x + y * y + z * z);

		return ret;
	}

	public static double abs2(double x, double y, double z) {
		double ret = x * x + y * y + z * z;

		return ret;
	}

	public static double abs(double[] v) {
		double ret = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);

		return ret;
	}

	public static Vec3 copyVec3(Vec3 v) {
		return Vec3.createVectorHelper(v.xCoord, v.yCoord, v.zCoord);
	}

	public static double abs(Vec3 v) {
		double ret = Math.sqrt(v.xCoord * v.xCoord + v.yCoord * v.yCoord + v.zCoord * v.zCoord);

		return ret;
	}

	public static double abs2(double[] v) {
		double ret = Math.pow(v[0], 2) + Math.pow(v[1], 2) + Math.pow(v[2], 2);

		return ret;
	}

	public static double abs2(Vec3 v) {
		double ret = Math.pow(v.xCoord, 2) + Math.pow(v.yCoord, 2) + Math.pow(v.zCoord, 2);

		return ret;
	}

	public static double[] unitVector(double[] v) {
		double[] ret = vectorDiv(v, abs(v));

		return ret;
	}

	public static Vec3 unitVector(Vec3 v) {
		Vec3 ret = vectorDiv(v, abs(v));

		return ret;
	}

	public static double[] send(double x, double y, double z) {
		double[] ret = { x, y, z };

		return ret;
	}

	public static void assign(double[] v, double x, double y, double z) {
		v[0] = x;
		v[1] = y;
		v[2] = z;
	}

	public static void assign(double[] v1, double[] v2) {
		v1[0] = v2[0];
		v1[1] = v2[1];
		v1[2] = v2[2];
	}

	public static double setLimExp(double x, double max) {
		double ret;

		if (x > 0) {

			ret = max - max * Math.exp(-x / max);

		} else {

			ret = -max + max * Math.exp(x / max);

		}

		return ret;
	}

	public static double[][] equalize(double[] s, double[] e, int a) {

		double[][] ret = new double[a + 1][3];
		double partX = (e[0] - s[0]) / a;
		double partY = (e[1] - s[1]) / a;
		double partZ = (e[2] - s[2]) / a;

		for (int i = 0; i < a + 1; i++) {

			ret[i][0] = s[0] + partX * i;
			ret[i][1] = s[1] + partY * i;
			ret[i][2] = s[2] + partZ * i;

		}

		return ret;
	}

	public static Vec3 getNearPoint(Vec3 s, Vec3 e, Vec3 p) {
		Vec3 ret;

		double d1 = abs2(vectorSub(s, p));
		double d2 = abs2(vectorSub(e, p));
		double l = abs2(vectorSub(s, e));

		double k = (d1 - d2 + l) / (2 * l);
		k = k >= 1 ? 1 : (k <= 0 ? 0 : k);
		ret = vectorPul(vectorMul(vectorSub(e, s), k), s);

		return ret;
	}

	public static Line getDistanceLine(Vec3 s1, Vec3 e1, Vec3 s2, Vec3 e2) {

		Vec3 v1 = MitoMath.vectorSub(e1, s1);
		Vec3 v2 = MitoMath.vectorSub(e2, s2);
		double l1 = v1.lengthVector();
		double l2 = v2.lengthVector();
		Vec3 u1 = v1.normalize();
		Vec3 u2 = v2.normalize();
		Vec3 ds = MitoMath.vectorSub(s2, s1);
		double dot = u1.dotProduct(u2);

		double k1 = ds.dotProduct(vectorSub(u1, vectorMul(u2, dot))) / (1 - dot * dot);
		double k2 = ds.dotProduct(vectorSub(vectorMul(u1, dot), u2)) / (1 - dot * dot);

		k1 = k1 < 0 ? 0 : (k1 > l1 ? l1 : k1);
		k2 = k2 < 0 ? 0 : (k2 > l2 ? l2 : k2);

		return new Line(vectorPul(s1, vectorMul(u1, k1)), vectorPul(s2, vectorMul(u2, k2)));
	}

	public static double distancePointPlane(Vec3 plane, Vec3 normal, Vec3 p) {
		return normal.dotProduct(vectorSub(plane, p));
	}

	public static double distancePointPlane(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4) {
		return distancePointPlane(v1, v2.crossProduct(v3).normalize(), v4);
	}

	public static Vec3 getIntersectPlaneLine(Vec3 plane, Vec3 normal, Vec3 s, Vec3 e) {
		double ls = normal.dotProduct(vectorSub(s, plane));
		double le = normal.dotProduct(vectorSub(e, plane));
		double r = ls / (ls + le);
		if (r < 0 || r > 1) {
			return null;
		}
		Vec3 l = vectorSub(e, s);
		Vec3 ret = vectorPul(vectorMul(l, r), s);

		return ret;
	}

	public static boolean onLine(Vec3 p, Vec3 set, Vec3 end) {
		return subAbs2(getNearPoint(set, end, p), p) < 0.001;
	}

	public static Vec3 crossX(Vec3 side1) {
		return Vec3.createVectorHelper(0, side1.zCoord, -side1.yCoord);
	}

	public static Vec3 crossY(Vec3 side1) {
		return Vec3.createVectorHelper(-side1.zCoord, 0, side1.xCoord);
	}

	public static Vec3 crossZ(Vec3 side1) {
		return Vec3.createVectorHelper(side1.yCoord, -side1.xCoord, 0);
	}

}
