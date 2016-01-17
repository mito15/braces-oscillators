package com.mito.mitomod.client.render;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.common.BraceData;
import com.mito.mitomod.common.entity.BlockEntity;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderBrace {

	private static final ResourceLocation[] tex = new ResourceLocation[32];
	private static final double[] s = new double[40];
	private static final double[] c = new double[40];
	private static final String[] plankName = { "oak", "spruce", "birch", "jungle", "acacia", "big_oak" };
	private static final String[] color_name = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };

	public RenderBrace() {

		for (int n = 0; n < tex.length; n++) {

			tex[n] = new ResourceLocation("mitomod", "textures/blocks/brace_" + (n) + ".png");
		}
		for (int n = 0; n < s.length; n++) {

			s[n] = Math.sin((double) n / (double) s.length * Math.PI * 2);
		}

		for (int n = 0; n < c.length; n++) {

			c[n] = Math.cos((double) n / (double) s.length * Math.PI * 2);
		}
	}

	public float dpx;
	public float dpy;
	public float dpz;

	public static final double WireLength = 10;

	public void doRender(BraceData data) {

		Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		this.bindTexture(this.tex[data.getTexture()]);
		renderBrace(data, data.getSet(), data.getEnd(), data.getDSize(), data.getPos().xCoord, data.getPos().yCoord, data.getPos().zCoord);

	}

	protected void bindTexture(ResourceLocation p_110776_1_) {
		Minecraft.getMinecraft().renderEngine.bindTexture(p_110776_1_);
	}

	public void renderBrace(BraceData data, Vec3 set, Vec3 end, double size, double x, double y, double z) {

		float alpha = 1.0f;
		double l = MitoMath.subAbs(set, end);

		double x1, y1, z1;
		float a1, a2;

		double xabs = set.xCoord - end.xCoord;
		double xzabs = Math.sqrt(Math.pow(set.xCoord - end.xCoord, 2) + Math.pow(set.zCoord - end.zCoord, 2));
		xzabs = set.xCoord - end.xCoord >= 0 ? -xzabs : xzabs;

		a1 = (float) (Math.atan((set.yCoord - end.yCoord) / xzabs) / Math.PI * 180);
		a2 = (float) (Math.atan((set.zCoord - end.zCoord) / xabs) / Math.PI * 180);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTranslatef((float) data.rand.xCoord, (float) data.rand.yCoord, (float) data.rand.zCoord);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorRGBA_F(1, 1, 1, 1.0f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glPushMatrix();

		GL11.glTranslatef((float) x, (float) y, (float) z);
		if (data.isBent()) {
			GL11.glTranslated(-data.getPos().xCoord, -data.getPos().yCoord, -data.getPos().zCoord);
			if (data.getType() == 1) {
				RenderCore.renderSimpleBezier(20, data.getSet(), data.getSetCP(), data.getEndCP(), data.getEnd(), data.getSize(), alpha);
			} else if (data.getType() == 3) {
				RenderCore.renderSimpleBezier(20, data.getSet(), data.getSetCP(), data.getEndCP(), data.getEnd(), data.getSize(), alpha);
			} else if (data.getType() == 4) {
				RenderCore.renderSimpleBezier(20, data.getSet(), data.getSetCP(), data.getEndCP(), data.getEnd(), data.getSize(), alpha);
			} else if (data.getType() == 5 || data.getType() == 7) {
				RenderCore.renderQuadBezier(20, data.getSet(), data.getSetCP(), data.getEndCP(), data.getEnd(), size, size / 5, alpha);
			} else if (data.getType() == 6 || data.getType() == 8) {
				RenderCore.renderQuadBezier(20, data.getSet(), data.getSetCP(), data.getEndCP(), data.getEnd(), size / 5, size, alpha);
			} else {
				RenderCore.renderQuadBezier(20, data.getSet(), data.getSetCP(), data.getEndCP(), data.getEnd(), size, size, alpha);
			}
		} else {

			GL11.glRotatef(-a2, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(a1, 0.0F, 0.0F, -1.0F);

			if (data.getType() == 1) {
				renderCyl(l, (float) size, alpha, false);
			} else if (data.getType() == 3) {
				renderCyl(l, (float) size, alpha, true);
			} else if (data.getType() == 4) {
				renderBox(l, size, size / 5, alpha);
				GL11.glTranslatef(0.0f, (float) size / 2, 0.0f);
				renderBox(l, size / 5, size, alpha);
				GL11.glTranslatef(0.0f, (float) -size, 0.0f);
				renderBox(l, size / 5, size, alpha);
			} else if (data.getType() == 5 || data.getType() == 7) {
				renderBox(l, size, size / 5, alpha);
			} else if (data.getType() == 6 || data.getType() == 8) {
				renderBox(l, size / 5, size, alpha);
			} else {
				renderBox(l, size, size, alpha);
			}
		}

		GL11.glPopMatrix();

		if (data.getType() != 2 && data.getType() != 3 && data.getType() != 4 && data.getType() != 7 && data.getType() != 8) {
			GL11.glPushMatrix();

			GL11.glTranslated(-data.getPos().xCoord, -data.getPos().yCoord, -data.getPos().zCoord);
			GL11.glTranslatef((float) x, (float) y, (float) z);
			GL11.glTranslatef((float) data.getSet().xCoord, (float) data.getSet().yCoord, (float) data.getSet().zCoord);
			double d = size >= 0.8 ? 1 : 1.5D - size;
			renderBox(size * d, size * d, size * d, alpha);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glTranslated(-data.getPos().xCoord, -data.getPos().yCoord, -data.getPos().zCoord);
			GL11.glTranslatef((float) x, (float) y, (float) z);
			GL11.glTranslatef((float) data.getEnd().xCoord, (float) data.getEnd().yCoord, (float) data.getEnd().zCoord);
			renderBox(size * d, size * d, size * d, alpha);
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
	}

	private void renderCyl(double l, float r, float alpha, boolean b) {

		Tessellator tessellator = Tessellator.instance;

		tessellator.setColorOpaque_I(1);

		tessellator.startDrawingQuads();

		tessellator.setColorRGBA_F(1, 1, 1, alpha);

		//if(alpha != 1.0)GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		if (alpha != 1.0)
			GL11.glDepthMask(false);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		double part = r / s.length * 2 * Math.PI;

		for (int n = 0; n < s.length; n++) {

			double y3 = s[n] * r / 2;
			double z3 = c[n] * r / 2;
			double y2 = n == s.length - 1 ? s[0] : s[n + 1];
			double z2 = n == s.length - 1 ? c[0] : c[n + 1];
			double y4 = y2 * r / 2;
			double z4 = z2 * r / 2;

			tessellator.setNormal((float) 0, (float) s[n], (float) c[n]);
			tessellator.addVertexWithUV(-l / 2, y3, z3, 0, part * n);
			tessellator.setNormal((float) 0, (float) s[n], (float) c[n]);
			tessellator.addVertexWithUV(l / 2, y3, z3, l, part * n);
			tessellator.setNormal((float) 0, (float) y2, (float) z2);
			tessellator.addVertexWithUV(l / 2, y4, z4, l, part * (n + 1));
			tessellator.setNormal((float) 0, (float) y2, (float) z2);
			tessellator.addVertexWithUV(-l / 2, y4, z4, 0, part * (n + 1));

		}
		tessellator.draw();
		if (b) {

			tessellator.startDrawing(6);

			tessellator.setNormal(1.0f, 0.0f, 0.0f);
			tessellator.addVertexWithUV(l / 2, 0, 0, 0, 0);

			for (int n = 0; n < s.length; n++) {

				double y3 = s[n] * r / 2;
				double z3 = c[n] * r / 2;
				tessellator.addVertexWithUV(l / 2, -y3, z3, 0, 0);
			}

			tessellator.addVertexWithUV(l / 2, 0, r / 2, 0, 0);

			tessellator.draw();

			tessellator.startDrawing(6);

			tessellator.setNormal(-1.0f, 0.0f, 0.0f);
			tessellator.addVertexWithUV(-l / 2, 0, 0, 0, 0);

			for (int n = 0; n < s.length; n++) {

				double y3 = s[n] * r / 2;
				double z3 = c[n] * r / 2;
				tessellator.addVertexWithUV(-l / 2, y3, z3, 0, 0);
			}

			tessellator.addVertexWithUV(-l / 2, 0, r / 2, 0, 0);

			tessellator.draw();

		}

		GL11.glDepthMask(true);
		GL11.glShadeModel(GL11.GL_FLAT);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void renderBox(double x, double y, double z, float alpha) {
		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();

		tessellator.setColorRGBA_F(1, 1, 1, alpha);
		//if(alpha != 1.0)GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		if (alpha != 1.0)
			GL11.glDepthMask(false);

		tessellator.setNormal(0, 0, -1);
		tessellator.addVertexWithUV(-x / 2, -y / 2, -z / 2, 0, 0);
		tessellator.addVertexWithUV(-x / 2, y / 2, -z / 2, 0, y);
		tessellator.addVertexWithUV(x / 2, y / 2, -z / 2, x, y);
		tessellator.addVertexWithUV(x / 2, -y / 2, -z / 2, x, 0);

		tessellator.setNormal(0, 0, 1);
		tessellator.addVertexWithUV(-x / 2, -y / 2, z / 2, 0, 0);
		tessellator.addVertexWithUV(x / 2, -y / 2, z / 2, x, 0);
		tessellator.addVertexWithUV(x / 2, y / 2, z / 2, x, y);
		tessellator.addVertexWithUV(-x / 2, y / 2, z / 2, 0, y);

		tessellator.setNormal(-1, 0, 0);
		tessellator.addVertexWithUV(-x / 2, -y / 2, -z / 2, 0, 0);
		tessellator.addVertexWithUV(-x / 2, -y / 2, z / 2, 0, z);
		tessellator.addVertexWithUV(-x / 2, y / 2, z / 2, y, z);
		tessellator.addVertexWithUV(-x / 2, y / 2, -z / 2, y, 0);

		tessellator.setNormal(1, 0, 0);
		tessellator.addVertexWithUV(x / 2, -y / 2, -z / 2, 0, 0);
		tessellator.addVertexWithUV(x / 2, y / 2, -z / 2, y, 0);
		tessellator.addVertexWithUV(x / 2, y / 2, z / 2, y, z);
		tessellator.addVertexWithUV(x / 2, -y / 2, z / 2, 0, z);

		tessellator.setNormal(0, 1, 0);
		tessellator.addVertexWithUV(-x / 2, y / 2, -z / 2, 0, 0);
		tessellator.addVertexWithUV(-x / 2, y / 2, z / 2, 0, z);
		tessellator.addVertexWithUV(x / 2, y / 2, z / 2, x, z);
		tessellator.addVertexWithUV(x / 2, y / 2, -z / 2, x, 0);

		tessellator.setNormal(0, -1, 0);
		tessellator.addVertexWithUV(-x / 2, -y / 2, -z / 2, 0, 0);
		tessellator.addVertexWithUV(x / 2, -y / 2, -z / 2, x, 0);
		tessellator.addVertexWithUV(x / 2, -y / 2, z / 2, x, z);
		tessellator.addVertexWithUV(-x / 2, -y / 2, z / 2, 0, z);

		tessellator.draw();

		GL11.glDepthMask(true);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	}

	protected ResourceLocation getArrowTextures(BlockEntity par1EntityArrow) {
		return tex[0];
	}

	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.getArrowTextures((BlockEntity) par1Entity);
	}

}
