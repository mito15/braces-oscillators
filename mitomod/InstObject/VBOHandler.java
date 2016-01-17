package com.mito.mitomod.InstObject;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import net.minecraft.client.renderer.OpenGlHelper;

public class VBOHandler {

	public int buffer;
	public int brightness;
	public int usage;
	public int mode;
	public int size;

	public void draw() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 48, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 48, 12);
		GL11.glNormalPointer(GL11.GL_FLOAT, 48, 20);
		GL11.glColorPointer(4, GL11.GL_FLOAT, 48, 32);
		if (brightness != 0) {
			OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, brightness);
			GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
			OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
		}

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

		GL11.glDrawArrays(mode, 0, size);

		if (brightness != 0) {
			OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
		}
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	}

	public void updateBrightness(BraceBase base, float partick) {
		int i = base.getBrightnessForRender(partick);
		int j = i % 65536;
		int k = i / 65536;
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.brightness);
		ByteBuffer buffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_READ_WRITE, null);
		FloatBuffer floatBuffer = buffer.asFloatBuffer();
		for (int n = 0; n < this.size; n++) {
			floatBuffer.put((float) j / 1.0F);
			floatBuffer.put((float) k / 1.0F);
		}
		GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

}
