package com.mito.mitomod.InstObject.Brace;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.mito.mitomod.InstObject.BB_Render;
import com.mito.mitomod.InstObject.BraceBase;
import com.mito.mitomod.InstObject.CreateVertexBufferObject;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class BraceRender extends BB_Render {

	@Override
	public void doRender(BraceBase base, float partialTickTime) {
		super.doRender(base, partialTickTime);

		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/blocks/stone.png"));
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		Brace brace = (Brace) base;
		GL11.glTranslated(brace.rand.xCoord, brace.rand.yCoord, brace.rand.zCoord);
		double l = MitoMath.subAbs(brace.pos, brace.end);
		float yaw = (float)brace.getYaw();
		float pitch = (float)brace.getPitch();

		GL11.glRotatef(yaw, 0.0F, -1.0F, 0.0F);
		GL11.glRotatef(pitch, 0.0F, 0.0F, 1.0F);

		if (base.buffer != null)
			base.buffer.draw();
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		GL11.glScaled(brace.size, brace.size, brace.size);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glScaled(brace.size, brace.size, brace.size);
		GL11.glRotated(90, 0, -1, 0);
		brace.shape.getvbo().draw();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)l, 0, 0);
		GL11.glScaled(brace.size, -brace.size, -brace.size);
		GL11.glRotated(90, 0, 1, 0);
		brace.shape.getvbo().draw();
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();

	}

	@Override
	public void updateRender(BraceBase base, float partialTickTime) {

		Brace brace = (Brace) base;

		Vec3 set = Vec3.createVectorHelper(brace.pos.xCoord, brace.pos.yCoord, brace.pos.zCoord);
		Vec3 end = Vec3.createVectorHelper(brace.end.xCoord, brace.end.yCoord, brace.end.zCoord);
		double l = MitoMath.subAbs(set, end);

		//ここからｘ方向にlの長さの棒を伸ばせばおｋ

		super.updateRender(base, partialTickTime);

		CreateVertexBufferObject c = CreateVertexBufferObject.INSTANCE;

		c.beginRegist(GL15.GL_STATIC_DRAW, GL11.GL_QUADS);
		/*int i = base.getBrightnessForRender(partialTickTime);
		int j = i % 65536;
		int k = i / 65536;
		c.setBrightness(j, k);*/
		c.setColor(1f, 1f, 1f, 1.0f);
		double size = brace.size;

		if (brace.shape == null)
			return;
		for (int n = 0; n < brace.shape.planes.size(); n++) {
			Plane plane = brace.shape.planes.get(n);
			for (int n1 = 0; n1 < plane.line.size(); n1++) {
				Vec3 v1 = n1 == 0 ? plane.line.get(plane.line.size() - 1).pos : plane.line.get(n1 - 1).pos;
				Vec3 v2 = brace.shape.planes.get(n).line.get(n1).pos;
				Vec3 norm = MitoMath.unitVector(Vec3.createVectorHelper(0, (v1.xCoord - v2.xCoord), (v2.yCoord - v1.yCoord)));
				double uOffset = MitoMath.subAbs(v1, v2);
				double vOffset = v1.zCoord - v2.zCoord;
				c.setNormal(norm.xCoord, norm.yCoord, norm.zCoord);
				c.registVertexWithUV(-v1.zCoord * size, v1.yCoord * size, v1.xCoord * size, 0f, 0f);
				c.registVertexWithUV(l - v1.zCoord * size, v1.yCoord * size, v1.xCoord * size, l, 0f);
				c.registVertexWithUV(l - v2.zCoord * size, v2.yCoord * size, v2.xCoord * size, vOffset + l, uOffset * size);
				c.registVertexWithUV(-v2.zCoord * size, v2.yCoord * size, v2.xCoord * size, vOffset, uOffset * size);
			}
		}

		base.buffer = c.end();
	}

}
