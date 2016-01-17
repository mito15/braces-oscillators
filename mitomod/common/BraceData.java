package com.mito.mitomod.common;

import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class BraceData {

	public Vec3 rand = Vec3.createVectorHelper(Math.random() * 0.002 - 0.001, Math.random() * 0.002 - 0.001, Math.random() * 0.002 - 0.001);
	private Vec3 pos = Vec3.createVectorHelper(0.0, 0.0, 0.0);
	private Vec3 set = Vec3.createVectorHelper(0.0, 0.0, 0.0);
	private Vec3 end = Vec3.createVectorHelper(0.0, 0.0, 0.0);
	private Vec3 setCP = Vec3.createVectorHelper(0.0, 0.0, 0.0);
	private Vec3 endCP = Vec3.createVectorHelper(0.0, 0.0, 0.0);
	private int tex = 0;
	private int type = 0;
	private int size = 1;
	public Entity bindEntity;
	//　普通のブレース　ベジェブレース　面　ねじれ面　ベジェ曲面　ベジェ平面
	public int drawMode = 0;

	public BraceData() {
	}

	public BraceData(Entity entity) {
		this.pos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
		this.bindEntity = entity;
	}

	//Brace
	public BraceData(Entity entity, Vec3 v1, Vec3 v2, int tex, int type, int size) {
		this(entity);
		this.set = v1;
		this.setCP = MitoMath.copyVec3(v1);
		this.end = v2;
		this.endCP = MitoMath.copyVec3(v2);
		this.tex = tex;
		this.type = type;
		this.size = size;
	}

	public BraceData(Entity entity, Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, int tex, int type, int size) {
		this(entity);
		this.set = MitoMath.copyVec3(v1);
		this.setCP = MitoMath.copyVec3(v2);
		this.end = MitoMath.copyVec3(v4);
		this.endCP = MitoMath.copyVec3(v3);
		this.tex = tex;
		this.type = type;
		this.size = size;
	}

	//Wall
	public BraceData(Entity entity, Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, int tex, int type) {
		this(entity);
	}

	public BraceData(Entity entity, Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, Vec3 v5, Vec3 v6, Vec3 v7, Vec3 v8, int tex, int type) {
		this(entity);
	}

	public void generateModel() {

	}

	public boolean equals(BraceData data) {
		if(this.pos.equals(data.getPos())){
			if(this.set.equals(data.getSet())){
				if(this.end.equals(data.getEnd())){
					if(this.setCP.equals(data.getSetCP())){
						if(this.endCP.equals(data.getEndCP())){
							if(this.size == data.getSize() && this.tex == data.getTexture() && this.type == data.getType()){
								return true;
							}
						}
					}
				}
			}
		}
		return false;

	}
	
	public boolean isBent(){
		return MitoMath.subAbs2(this.setCP, this.set) < 0.001 && MitoMath.subAbs2(this.endCP, this.end) < 0.001;
	}

	public Vec3 getPos() {
		return this.pos;
	}

	public void setPos(Vec3 v) {
		this.pos = v;
	}

	public Vec3 getSet() {
		return this.set;
	}

	public void setSet(Vec3 v) {
		this.set = v;
	}

	public Vec3 getEnd() {
		return this.end;
	}

	public void setEnd(Vec3 v) {
		this.end = v;
	}

	public Vec3 getSetCP() {
		return this.setCP;
	}

	public void setSetCP(Vec3 v) {
		this.setCP = Vec3.createVectorHelper(v.xCoord, v.yCoord, v.zCoord);
	}

	public Vec3 getEndCP() {
		return this.endCP;
	}

	public void setEndCP(Vec3 v) {
		this.endCP = Vec3.createVectorHelper(v.xCoord, v.yCoord, v.zCoord);
	}

	public int getTexture() {
		return this.tex;
	}

	public int getcolor() {
		return this.tex & 15;
	}

	public int getMaterial() {
		return this.tex >> 4;
	}

	public void setTexture(int i, int color) {
		this.tex = (i << 4) + (color & 15);
	}

	public void setTexture(int i) {
		this.tex = i;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int i) {
		this.type = i;
	}

	public int getSize() {
		return this.size;
	}

	public double getDSize() {
		return (double) this.size * 0.05;
	}

	public void setSize(int i) {
		this.size = i;
	}

}
