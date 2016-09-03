package com.mito.mitomod.client.render.model;

import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;

public class Triangle {
	
	public Vertex[] vertexs = new Vertex[3];
	public Vec3 norm;
	
	public Triangle(Vertex v1, Vertex v2, Vertex v3){
		this.vertexs[0] = v1;
		this.vertexs[1] = v2;
		this.vertexs[2] = v3;
		norm = MitoMath.getNormal(v1, v2, v3);
	}

	public void draw(CreateVertexBufferObject c, double l) {
		c.setNormal(norm);
		c.registVertexWithUV(vertexs[0].addVector(0, 0, l));
		c.registVertexWithUV(vertexs[1].addVector(0, 0, l));
		c.registVertexWithUV(vertexs[2].addVector(0, 0, l));
	}

	public void drawReverse(CreateVertexBufferObject c, double l) {
		c.setNormal(MitoMath.vectorMul(norm, -1));
		c.registVertexWithUV(vertexs[2].addVector(0, 0, l));
		c.registVertexWithUV(vertexs[1].addVector(0, 0, l));
		c.registVertexWithUV(vertexs[0].addVector(0, 0, l));
	}
	
	public void draw(Tessellator c, double l) {
		c.setNormal((float)norm.xCoord, (float)norm.yCoord, (float)norm.zCoord);
		c.addVertexWithUV(vertexs[0].pos.xCoord, vertexs[0].pos.yCoord, vertexs[0].pos.zCoord+l, vertexs[0].textureU, vertexs[0].textureV);
		c.addVertexWithUV(vertexs[1].pos.xCoord, vertexs[1].pos.yCoord, vertexs[1].pos.zCoord+l, vertexs[1].textureU, vertexs[1].textureV);
		c.addVertexWithUV(vertexs[2].pos.xCoord, vertexs[2].pos.yCoord, vertexs[2].pos.zCoord+l, vertexs[2].textureU, vertexs[2].textureV);
	}

	public void drawReverse(Tessellator c, double l) {
		c.setNormal((float)-norm.xCoord, (float)-norm.yCoord, (float)-norm.zCoord);
		c.addVertexWithUV(vertexs[2].pos.xCoord, vertexs[2].pos.yCoord, vertexs[2].pos.zCoord+l, vertexs[2].textureU, vertexs[2].textureV);
		c.addVertexWithUV(vertexs[1].pos.xCoord, vertexs[1].pos.yCoord, vertexs[1].pos.zCoord+l, vertexs[1].textureU, vertexs[1].textureV);
		c.addVertexWithUV(vertexs[0].pos.xCoord, vertexs[0].pos.yCoord, vertexs[0].pos.zCoord+l, vertexs[0].textureU, vertexs[0].textureV);
	}

	public void draw(CreateVertexBufferObject c, Vec3 v, double roll, double pitch, double yaw) {
		c.setNormal(MitoMath.rot(norm, roll, pitch, yaw));
		
		c.registVertexWithUV(vertexs[0].rot(roll, pitch, yaw).addVector(v));
		c.registVertexWithUV(vertexs[1].rot(roll, pitch, yaw).addVector(v));
		c.registVertexWithUV(vertexs[2].rot(roll, pitch, yaw).addVector(v));
	}
	
	public void drawReverse(CreateVertexBufferObject c, Vec3 v, double roll, double pitch, double yaw) {
		c.setNormal(MitoMath.vectorMul(MitoMath.rot(norm, roll, pitch, yaw), -1));
		
		c.registVertexWithUV(vertexs[2].rot(roll, pitch, yaw).addVector(v));
		c.registVertexWithUV(vertexs[1].rot(roll, pitch, yaw).addVector(v));
		c.registVertexWithUV(vertexs[0].rot(roll, pitch, yaw).addVector(v));
	}

}
