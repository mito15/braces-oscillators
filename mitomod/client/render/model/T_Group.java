package com.mito.mitomod.client.render.model;

import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.BraceBase.VBOList;

import net.minecraft.util.Vec3;

public class T_Group extends Polygon3D {

	public Polygon3D[] models;

	public T_Group(Polygon3D... ma) {
		this.models = ma;
	}

	public void drawQuad(CreateVertexBufferObject c, Vec3 offset, double roll, double pitch, double yaw, double size){
		for(int i = 0; i < models.length; i++){
			models[i].drawQuad(c, offset, roll, pitch, yaw, size);
		}
	}

	public void drawTri(CreateVertexBufferObject c, Vec3 offset, double roll, double pitch, double yaw, double size){
		for(int i = 0; i < models.length; i++){
			models[i].drawTri(c, offset, roll, pitch, yaw, size);
		}
	}

	public void drawSpecial(VBOList c, Vec3 offset, double roll, double pitch, double yaw, double size){
		for(int i = 0; i < models.length; i++){
			models[i].drawSpecial(c, offset, roll, pitch, yaw, size);
		}
	}

	@Override
	public void renderAt(Vec3 offset, double roll, double pitch, double yaw, double size, float partialTickTime) {
		for(int i = 0; i < models.length; i++){
			models[i].renderAt(offset, roll, pitch, yaw, size, partialTickTime);
		}
	}

}
