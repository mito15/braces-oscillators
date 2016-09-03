package com.mito.mitomod.client.render.model;

import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.util.Vec3;

public class D_Ellipse extends D_Face {
	Vec3 pos;
	double axisX;
	double axisY;
	Vec3 normalRotation;

	public D_Ellipse(Vec3 p, Vec3 nr, double min, double maj) {
		this.pos = p;
		this.normalRotation = nr;
		this.axisX = min;
		this.axisY = maj;
	}

	public int getSize(double size) {
		return 20;
	}

	public Vec3 getVec3(int n, double size) {
		return Vec3.createVectorHelper(0.5 * size * axisX * Math.cos((double) n / (double) this.getSize(size) * 2 * Math.PI), 0.5 * size * axisX * Math.sin((double) n / (double) this.getSize(size) * 2 * Math.PI), 0);
	}

	private Vec3 getNorm1(Vec3 v1, Vec3 v2) {
		return MitoMath.unitVector(v1);
	}

	private Vec3 getNorm2(Vec3 v1, Vec3 v2) {
		return MitoMath.unitVector(v2);
	}

	public void drawPlane(CreateVertexBufferObject c) {
		Vec3 v1 = this.getVec3(1, 1.0);
		for (int n1 = 0; n1 < 18; n1++) {
			Vec3 v2 = this.getVec3(n1 + 1, 1.0);
			Vec3 v3 = this.getVec3(n1 + 2, 1.0);
			c.registVertexWithUV(v1, v1.xCoord, v1.yCoord);
			c.registVertexWithUV(v2, v2.xCoord, v2.yCoord);
			c.registVertexWithUV(v3, v3.xCoord, v3.yCoord);
		}
	}

	public Vertex getVertex(int n, double size) {
		Vec3 vec3 = this.getVec3(n, size);
		return new Vertex(vec3, vec3.xCoord, vec3.yCoord);
	}
}
