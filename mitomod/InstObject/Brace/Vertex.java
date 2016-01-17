package com.mito.mitomod.InstObject.Brace;

import net.minecraft.util.Vec3;

public class Vertex {

	public Vec3 pos;
	public Vec3 offCurvePoint1;
	public Vec3 offCurvePoint2;
	public double textureU;
	public double textureV;
	public boolean texType = true;
	
	public Vertex(Vec3 position, double u, double v) {
		this.pos = position;
		this.textureU = u;
		this.textureV = v;
	}
	
	public Vertex(double x, double y, double z, double u, double v) {
		this.pos = Vec3.createVectorHelper(x, y, z);
		this.textureU = u;
		this.textureV = v;
	}

}
