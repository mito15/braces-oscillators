package com.mito.mitomod.client.render.model;

import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.BraceBase.VBOList;

import net.minecraft.util.Vec3;

public class T_OrdinaryModel extends Polygon3D {

	public Polygon2D[] planes;

	public T_OrdinaryModel(Polygon2D... list) {
		planes = list;
	}

	public void drawQuad(CreateVertexBufferObject c, Vec3 offset, double roll, double pitch, double yaw, double size){
		for(int i = 0; i < planes.length; i++){
			planes[i].drawQuad(c, offset, roll, pitch, yaw, size);
		}
	}

	public void drawTri(CreateVertexBufferObject c, Vec3 offset, double roll, double pitch, double yaw, double size){
		for(int i = 0; i < planes.length; i++){
			planes[i].drawTri(c, offset, roll, pitch, yaw, size);
		}
	}

	public void drawSpecial(VBOList c, Vec3 offset, double roll, double pitch, double yaw, double size){
		for(int i = 0; i < planes.length; i++){
			planes[i].drawSpecial(c, offset, roll, pitch, yaw, size);
		}
	}

	@Override
	public void renderAt(Vec3 offset, double roll, double pitch, double yaw, double size, float partialTickTime) {
		for(int i = 0; i < planes.length; i++){
			planes[i].renderAt(offset, roll, pitch, yaw, size, partialTickTime);
		}
	}

}
