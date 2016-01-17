package com.mito.mitomod.client.render;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.common.entity.BlockEntity;
import com.mito.mitomod.common.entity.EntityWall;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderWall extends Render {

	private static final String[] plankName = { "oak", "spruce", "birch", "jungle", "acacia", "big_oak" };
	private static final String[] color_name = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };
	private static final ResourceLocation[] tex = new ResourceLocation[32];

	public RenderWall() {
		super();
		this.shadowSize = 0.0F;
		int n;
		for (n = 0; n < this.color_name.length; n++) {
			tex[n] = new ResourceLocation("textures/blocks/wool_colored_" + this.color_name[n] + ".png");
		}
		for (n = this.color_name.length; n < this.color_name.length + this.plankName.length; n++) {
			tex[n] = new ResourceLocation("textures/blocks/planks_" + this.plankName[n - this.color_name.length] + ".png");
		}
		tex[22] = new ResourceLocation("textures/blocks/stone.png");
		tex[23] = new ResourceLocation("textures/blocks/cobblestone.png");
		tex[24] = new ResourceLocation("textures/blocks/stonebrick.png");
		tex[25] = new ResourceLocation("textures/blocks/quartz_block_top.png");
		tex[26] = new ResourceLocation("textures/blocks/end_stone.png");
	}

	public float dpx;
	public float dpy;
	public float dpz;

	private EntityWall ent;

	public static final double WireLength = 10;

	public void doRender(Entity entity, double x, double y, double z, float par8, float par9) {

		this.ent = (EntityWall) entity;
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		//tessellator.setBrightness(100000);

		this.bindTexture(this.tex[ent.tex]);

		renderWall(ent, x, y, z);
	}

	public void renderWall(EntityWall ent, double x, double y, double z) {

		float alpha = 1.0f;
		if (ent.flags != 0) {
			if ((ent.flags & 1) == 1) {
				alpha = 0.7f;
				ent.flags = (byte) 0;
			} else if ((ent.flags & 2) == 2) {
				alpha = 0.3f;
				ent.flags = (byte) 0;
			}
		}

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTranslatef((float) ent.rand.xCoord, (float) ent.rand.yCoord, (float) ent.rand.zCoord);
		Tessellator tessellator = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glPushMatrix();

		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glTranslatef((float) -ent.posX, (float) -ent.posY, (float) -ent.posZ);

		if (ent.shouldDivine) {

			this.renderSurface(alpha);
		} else {

			this.renderPlane(alpha);
		}

		GL11.glDepthMask(true);

		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
	}

	public void mapping(Vec3 norm, Vec3 co) {

	}

	public void renderPlane(float alpha) {

		Tessellator tessellator = Tessellator.instance;

		tessellator.setColorOpaque_I(1);
		tessellator.startDrawingQuads();

		tessellator.setColorRGBA_F(1, 1, 1, alpha);
		//if(alpha != 1.0)GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		if (alpha != 1.0)
			GL11.glDepthMask(false);

		if (ent.v1 != null) {

			Vec3 v2co = MitoMath.vectorSub(ent.v2, ent.v1);
			Vec3 v3co = MitoMath.vectorSub(ent.v3, ent.v1);
			Vec3 v4co = MitoMath.vectorSub(ent.v4, ent.v1);
			Vec3 norm;
			if (MitoMath.abs(v2co) != 0) {
				if (MitoMath.abs(v4co) != 0) {
					norm = MitoMath.unitVector(v2co.crossProduct(v4co));
				} else {
					norm = MitoMath.unitVector(v2co.crossProduct(v3co));
				}
			} else {
				norm = MitoMath.unitVector(v3co.crossProduct(v4co));
			}
			Vec3 uCoord;
			Vec3 vCoord;
			double v2v;
			double v2u;
			double v3v;
			double v3u;
			double v4v;
			double v4u;
			if (Math.abs(norm.yCoord) > 0.99) {
				v2u = v2co.xCoord;
				v2v = v2co.zCoord;
				v3u = v3co.xCoord;
				v3v = v3co.zCoord;
				v4u = v4co.xCoord;
				v4v = v4co.zCoord;
			} else {
				uCoord = MitoMath.unitVector(Vec3.createVectorHelper(-norm.zCoord, 0, norm.xCoord));
				vCoord = MitoMath.unitVector(Vec3.createVectorHelper(norm.xCoord * norm.yCoord, -(Math.pow(norm.xCoord, 2) + Math.pow(norm.zCoord, 2)), norm.zCoord * norm.yCoord));
				v2v = v2co.yCoord / vCoord.yCoord;
				v3v = v3co.yCoord / vCoord.yCoord;
				v4v = v4co.yCoord / vCoord.yCoord;
				if (Math.abs(uCoord.xCoord) > 0.1) {
					v2u = (v2co.xCoord - v2v * vCoord.xCoord) / uCoord.xCoord;
					v3u = (v3co.xCoord - v3v * vCoord.xCoord) / uCoord.xCoord;
					v4u = (v4co.xCoord - v4v * vCoord.xCoord) / uCoord.xCoord;
				} else if (uCoord.zCoord != 0) {
					v2u = (v2co.zCoord - v2v * vCoord.zCoord) / uCoord.zCoord;
					v3u = (v3co.zCoord - v3v * vCoord.zCoord) / uCoord.zCoord;
					v4u = (v4co.zCoord - v4v * vCoord.zCoord) / uCoord.zCoord;
				} else {
					v2u = 0;
					v3u = 0;
					v4u = 0;
				}
			}

			tessellator.setNormal((float) norm.xCoord, (float) norm.yCoord, (float) norm.zCoord);
			MitoUtil.addVertexWithUV(ent.v1, 0, 0, tessellator);
			MitoUtil.addVertexWithUV(ent.v2, v2u, v2v, tessellator);
			MitoUtil.addVertexWithUV(ent.v3, v3u, v3v, tessellator);
			MitoUtil.addVertexWithUV(ent.v4, v4u, v4v, tessellator);

			tessellator.setNormal((float) -norm.xCoord, (float) -norm.yCoord, (float) -norm.zCoord);
			MitoUtil.addVertexWithUV(ent.v1, 0, 0, tessellator);
			MitoUtil.addVertexWithUV(ent.v4, v4u, v4v, tessellator);
			MitoUtil.addVertexWithUV(ent.v3, v3u, v3v, tessellator);
			MitoUtil.addVertexWithUV(ent.v2, v2u, v2v, tessellator);
		}

		tessellator.draw();

		GL11.glDepthMask(true);
	}

	public void renderSurface(float alpha) {

		Tessellator tessellator = Tessellator.instance;

		tessellator.setColorOpaque_I(1);
		tessellator.startDrawingQuads();

		tessellator.setColorRGBA_F(1, 1, 1, alpha);
		//if(alpha != 1.0)GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		if (alpha != 1.0)
			GL11.glDepthMask(false);
		Vec3 v1 = Vec3.createVectorHelper(0, 0, 0);
		Vec3 v2 = Vec3.createVectorHelper(ent.v1.xCoord, ent.v1.yCoord, ent.v1.zCoord);
		Vec3 v3 = Vec3.createVectorHelper(ent.v4.xCoord, ent.v4.yCoord, ent.v4.zCoord);
		Vec3 v4 = Vec3.createVectorHelper(0, 0, 0);
		Vec3 sub1 = MitoMath.vectorDiv(MitoMath.vectorSub(ent.v2, ent.v1), 20);
		Vec3 sub2 = MitoMath.vectorDiv(MitoMath.vectorSub(ent.v3, ent.v4), 20);
		double vCo2 = 0, uCo2 = 0, vCo3 = 0, uCo3 = 0;
		double v1v = 0, v1u = 0, v2v = 0, v2u = 0, v3v = 0, v3u = 0, v4v = 0, v4u = 0;

		if (ent.setEntity != null && ent.setEntity != null) {

			v1 = Vec3.createVectorHelper(v2.xCoord, v2.yCoord, v2.zCoord);
			v4 = Vec3.createVectorHelper(v3.xCoord, v3.yCoord, v3.zCoord);

//			if (ent.setEntity.isBent) {
//				v2 = MitoMath.vectorBezier(ent.setEntity.set, ent.setEntity.setCP, ent.setEntity.endCP, ent.setEntity.end, (double) 1 / 20D);
//			} else {
//				v2 = ent.v1.addVector(sub1.xCoord * 1, sub1.yCoord * 1, sub1.zCoord * 1);
//			}
//			if (ent.endEntity.isBent) {
//				if (ent.ref) {
//					v3 = MitoMath.vectorBezier(ent.endEntity.set, ent.endEntity.setCP, ent.endEntity.endCP, ent.endEntity.end, (double) (21 - 1) / 20D);
//				} else {
//					v3 = MitoMath.vectorBezier(ent.endEntity.set, ent.endEntity.setCP, ent.endEntity.endCP, ent.endEntity.end, (double) 1 / 20D);
//				}
//			} else {
//				v3 = ent.v4.addVector(sub2.xCoord * 1, sub2.yCoord * 1, sub2.zCoord * 1);
//			}

			Vec3 v2co = MitoMath.vectorSub(v2, v1);
			Vec3 v3co = MitoMath.vectorSub(v3, v1);
			Vec3 v4co = MitoMath.vectorSub(v4, v1);

			Vec3 norm123 = MitoMath.unitVector(v2co.crossProduct(v3co));
			Vec3 norm134 = MitoMath.unitVector(v4co.crossProduct(v3co));

			Vec3 uCoord;
			Vec3 vCoord;

			if (Math.abs(norm123.yCoord) > 0.99) {
				uCo2 = v2co.xCoord;
				vCo2 = v2co.zCoord;
				uCo3 = v3co.xCoord;
				vCo3 = v3co.zCoord;
			} else {
				uCoord = MitoMath.unitVector(Vec3.createVectorHelper(-norm123.zCoord, 0, norm123.xCoord));
				vCoord = MitoMath.unitVector(Vec3.createVectorHelper(norm123.xCoord * norm123.yCoord, -(Math.pow(norm123.xCoord, 2) + Math.pow(norm123.zCoord, 2)), norm123.zCoord * norm123.yCoord));
				vCo2 = v2co.yCoord / vCoord.yCoord;
				vCo3 = v3co.yCoord / vCoord.yCoord;
				if (Math.abs(uCoord.xCoord) > 0.1) {
					uCo2 = (v2co.xCoord - vCo2 * vCoord.xCoord) / uCoord.xCoord;
					uCo3 = (v3co.xCoord - vCo3 * vCoord.xCoord) / uCoord.xCoord;
				} else if (uCoord.zCoord != 0) {
					uCo2 = (v2co.zCoord - vCo2 * vCoord.zCoord) / uCoord.zCoord;
					uCo3 = (v3co.zCoord - vCo3 * vCoord.zCoord) / uCoord.zCoord;
				} else {
					uCo2 = 0;
					uCo3 = 0;
				}
			}

			v3u = uCo3 - uCo2;
			v3v = vCo3 - vCo2;

			for (int n = 1; n <= 20; n++) {

				v1v = v2v;
				v1u = v2u;
				v4v = v3v;
				v4u = v3u;
				v2v = v1v + vCo2;
				v2u = v1u + uCo2;
				v3v = v1v + vCo3;
				v3u = v1u + uCo3;

				v1 = Vec3.createVectorHelper(v2.xCoord, v2.yCoord, v2.zCoord);
				v4 = Vec3.createVectorHelper(v3.xCoord, v3.yCoord, v3.zCoord);

//				if (ent.setEntity.isBent) {
//					v2 = MitoMath.vectorBezier(ent.setEntity.set, ent.setEntity.setCP, ent.setEntity.endCP, ent.setEntity.end, (double) n / 20D);
//				} else {
//					v2 = ent.v1.addVector(sub1.xCoord * n, sub1.yCoord * n, sub1.zCoord * n);
//				}
//				if (ent.endEntity.isBent) {
//					if (ent.ref) {
//						v3 = MitoMath.vectorBezier(ent.endEntity.set, ent.endEntity.setCP, ent.endEntity.endCP, ent.endEntity.end, (double) (21 - n) / 20D);
//					} else {
//						v3 = MitoMath.vectorBezier(ent.endEntity.set, ent.endEntity.setCP, ent.endEntity.endCP, ent.endEntity.end, (double) n / 20D);
//					}
//				} else {
//					v3 = ent.v4.addVector(sub2.xCoord * n, sub2.yCoord * n, sub2.zCoord * n);
//				}

				v2co = MitoMath.vectorSub(v2, v1);
				v3co = MitoMath.vectorSub(v3, v1);
				v4co = MitoMath.vectorSub(v4, v1);

				norm123 = MitoMath.unitVector(v2co.crossProduct(v3co));
				norm134 = MitoMath.unitVector(v3co.crossProduct(v4co));

				tessellator.setNormal((float) norm123.xCoord, (float) norm123.yCoord, (float) norm123.zCoord);
				MitoUtil.addVertexWithUV(v1, v1u, v1v, tessellator);
				MitoUtil.addVertexWithUV(v2, v2u, v2v, tessellator);
				tessellator.setNormal((float) norm134.xCoord, (float) norm134.yCoord, (float) norm134.zCoord);
				MitoUtil.addVertexWithUV(v3, v3u, v3v, tessellator);
				MitoUtil.addVertexWithUV(v4, v4u, v4v, tessellator);

				tessellator.setNormal((float) -norm123.xCoord, (float) -norm123.yCoord, (float) -norm123.zCoord);
				MitoUtil.addVertexWithUV(v1, v1u, v1v, tessellator);
				MitoUtil.addVertexWithUV(v4, v4u, v4v, tessellator);
				tessellator.setNormal((float) -norm134.xCoord, (float) -norm134.yCoord, (float) -norm134.zCoord);
				MitoUtil.addVertexWithUV(v3, v3u, v3v, tessellator);
				MitoUtil.addVertexWithUV(v2, v2u, v2v, tessellator);
			}
		}

		tessellator.draw();

		GL11.glDepthMask(true);
	}

	protected ResourceLocation getArrowTextures(BlockEntity par1EntityArrow) {
		return tex[0];
	}

	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.getArrowTextures((BlockEntity) par1Entity);
	}

}
