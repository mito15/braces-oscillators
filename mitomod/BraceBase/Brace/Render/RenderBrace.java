package com.mito.mitomod.BraceBase.Brace.Render;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.BraceBase.BB_Render;
import com.mito.mitomod.BraceBase.BB_RenderHandler;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.BraceBase.VBOHandler;
import com.mito.mitomod.BraceBase.VBOList;
import com.mito.mitomod.BraceBase.Brace.Brace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class RenderBrace extends BB_Render {

	public void staticRender(BraceBase base) {
		Tessellator t = Tessellator.instance;

		int i = base.getBrightnessForRender(0);
		int j = i % 65536;
		int k = i / 65536;
		Brace brace = (Brace) base;
		Minecraft.getMinecraft().renderEngine.bindTexture(brace.texture.getResourceLocation(brace.color));
		if (brace.shape == null)
			return;

		brace.shape.renderBraceAt(brace, 0);
		
		
	}

	public void doRender(BraceBase base, float partialTickTime) {
		BB_RenderHandler.enableClient();
		Brace brace = (Brace) base;
		Minecraft.getMinecraft().renderEngine.bindTexture(brace.texture.getResourceLocation(brace.color));
		GL11.glTranslated(brace.rand.xCoord, brace.rand.yCoord, brace.rand.zCoord);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		if (base.buffer != null) {
			base.buffer.draw();
		}
	}

	public void updateRender(BraceBase base, float partialticks) {

		int i = base.getBrightnessForRender(partialticks);
		int j = i % 65536;
		int k = i / 65536;

		base.shouldUpdateRender = false;
		Brace brace = (Brace) base;
		if (brace.shape == null)
			return;
		base.buffer = new VBOList(new VBOHandler[0]);
		brace.shape.drawBrace(base.buffer, brace);
		CreateVertexBufferObject c = CreateVertexBufferObject.INSTANCE;
		c.beginRegist(35044, 7);
		c.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		c.setBrightness(j, k);
		brace.shape.drawBraceSquare(c, brace);
		VBOHandler vbo1 = c.end();
		c.beginRegist(35044, 4);
		c.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		c.setBrightness(j, k);
		brace.shape.drawBraceTriangle(c, brace);
		VBOHandler vbo2 = c.end();
		base.buffer.add(vbo1);
		base.buffer.add(vbo2);
	}
}
