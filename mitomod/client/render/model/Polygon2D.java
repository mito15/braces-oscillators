package com.mito.mitomod.client.render.model;

import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.BraceBase.VBOList;
import com.mito.mitomod.BraceBase.Brace.Brace;

import net.minecraft.util.Vec3;

public abstract class Polygon2D implements IDrawable, IDrawBrace{

	public void drawQuad(CreateVertexBufferObject c, Vec3 offset, double roll, double pitch, double yaw, double size){}

	public void drawTri(CreateVertexBufferObject c, Vec3 offset, double roll, double pitch, double yaw, double size){}

	public void drawSpecial(VBOList c, Vec3 offset, double roll, double pitch, double yaw, double size){}

	public void drawBrace(VBOList buffer, Brace brace) {}

	public void renderAt(Vec3 offset, double roll, double pitch, double yaw, double size, float partialTickTime) {}

	public void renderBraceAt(Brace brace, float partialTickTime) {}

}
