package com.mito.mitomod.client.render.model;

import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.BraceBase.VBOList;
import com.mito.mitomod.BraceBase.Brace.Brace;

public class Pattern implements IDrawBrace {

	public T_Group model;
	public double length;

	public Pattern(double length, T_Group model) {
		this.model = model;
		this.length = length;
	}

	public Pattern(double length, Polygon3D... model) {
		this.model = new T_Group(model);
		this.length = length;
	}

	@Override
	public void renderBraceAt(Brace brace, float partialTickTime) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void drawBrace(VBOList buffer, Brace brace) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void drawBraceSquare(CreateVertexBufferObject c, Brace brace) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void drawBraceTriangle(CreateVertexBufferObject buffer, Brace brace) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/*public void renderBraceAt(Brace brace, float partialTickTime) {
		if (brace.size == 0 || this.length == 0) {
			return;
		}
		double roll = brace.getRoll();
		double yaw = MitoMath.getYaw(brace.pos, brace.end);
		double pitch = MitoMath.getPitch(brace.pos, brace.end);
		Vec3 part = MitoMath.rot(Vec3.createVectorHelper(0, 0, this.length * brace.size), 0, pitch, yaw);
		for (int i = 0; i < MitoMath.subAbs(brace.pos, brace.end) / (this.length * brace.size); i++) {
			Vec3 offset = MitoMath.vectorMul(part, (double) i);
			model.renderAt(offset, roll, pitch, yaw, brace.size, partialTickTime);
		}
	}

	@Override
	public void drawBrace(VBOList vbolist, Brace brace) {
		if (brace.size == 0 || this.length == 0) {
			return;
		}
		double roll = brace.getRoll();
		double yaw = MitoMath.getYaw(brace.pos, brace.end);
		double pitch = MitoMath.getPitch(brace.pos, brace.end);
		Vec3 part = MitoMath.rot(Vec3.createVectorHelper(0, 0, this.length * brace.size), 0, pitch, yaw);
		for (int i = 0; i < MitoMath.subAbs(brace.pos, brace.end) / (this.length * brace.size); i++) {
			Vec3 offset = MitoMath.vectorMul(part, (double) i);
			model.drawSpecial(vbolist, offset, roll, pitch, yaw, brace.size);
		}
	}

	@Override
	public void drawBraceSquare(CreateVertexBufferObject c, Brace brace) {
		if (brace.size == 0 || this.length == 0) {
			return;
		}
		double roll = brace.getRoll();
		double yaw = MitoMath.getYaw(brace.pos, brace.end);
		double pitch = MitoMath.getPitch(brace.pos, brace.end);
		Vec3 part = MitoMath.rot(Vec3.createVectorHelper(0, 0, this.length * brace.size), 0, pitch, yaw);
		for (int i = 0; i < MitoMath.subAbs(brace.pos, brace.end) / (this.length * brace.size); i++) {
			Vec3 offset = MitoMath.vectorMul(part, (double) i);
			model.drawQuad(c, offset, roll, pitch, yaw, brace.size);
		}
	}

	@Override
	public void drawBraceTriangle(CreateVertexBufferObject c, Brace brace) {
		if (brace.size == 0 || this.length == 0) {
			return;
		}
		double roll = brace.getRoll();
		double yaw = MitoMath.getYaw(brace.pos, brace.end);
		double pitch = MitoMath.getPitch(brace.pos, brace.end);
		Vec3 part = MitoMath.rot(Vec3.createVectorHelper(0, 0, this.length * brace.size), 0, pitch, yaw);
		for (int i = 0; i < MitoMath.subAbs(brace.pos, brace.end) / (this.length * brace.size); i++) {
			Vec3 offset = MitoMath.vectorMul(part, (double) i);
			model.drawTri(c, offset, roll, pitch, yaw, brace.size);
		}
	}*/

}
