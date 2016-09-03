package com.mito.mitomod.client.render.model;

import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.util.Vec3;

public class Vertex {

	public Vec3 pos;
	public double textureU;
	public double textureV;
	public boolean onCurve = true;
	
	public Vertex(double x, double y) {
		this(x, y, 0);
	}
	
	public Vertex(double x, double y, double z) {
		this(x, y, z, x, y);
	}
	
	public Vertex(Vec3 p, double u, double v) {
		this(p.xCoord, p.yCoord, p.zCoord, u, v);
	}
	
	public Vertex(double x, double y, double z, double u, double v) {
		this.pos = Vec3.createVectorHelper(x, y, z);
		this.textureU = u;
		this.textureV = v;
	}
	
	public Vertex resize(double size){
		return new Vertex(MitoMath.vectorMul(this.pos, size), this.textureU * size, this.textureV * size);
	}

	public Vertex addVector(double i, double j, double l) {
		return new Vertex(this.pos.addVector(i, j, l), this.textureU + i, this.textureV + j);
	}

	public Vertex addVector(Vec3 v) {
		return this.addVector(v.xCoord, v.yCoord, v.zCoord);
	}

	public Vertex rot(double roll, double pitch, double yaw) {
		// TODO 自動生成されたメソッド・スタブ
		return new Vertex(MitoMath.rot(this.pos, roll, pitch, yaw), this.textureU, this.textureV);
	}

}
