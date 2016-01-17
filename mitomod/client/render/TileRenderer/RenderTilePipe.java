package com.mito.mitomod.client.render.TileRenderer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.mito.mitomod.client.render.RenderCore;
import com.mito.mitomod.common.tile.TileOscillatorPipe;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderTilePipe extends TileEntitySpecialRenderer {

	ItemStack itemStack;
	RenderItem itemRenderer = new RenderItem();

	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {

		TileOscillatorPipe tile = (TileOscillatorPipe) tileEntity;

		Tessellator tessellator = Tessellator.instance;
		World world = tile.getWorldObj();

		this.bindTexture(new ResourceLocation("mitomod", "textures/blocks/oscillator" + tile.tex + ".png"));

		float f = world.getBlockLightValue(tile.xCoord, tile.yCoord, tile.zCoord);
		int l = world.getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord, tile.zCoord, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f, f, f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);

		double size = tile.beatSize;

		GL11.glDisable(GL11.GL_LIGHT0);
		GL11.glDisable(GL11.GL_LIGHT1);

		GL11.glShadeModel(GL11.GL_SMOOTH);

		FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
		float[] lightPositionArray = { 100.0F, 20.0F, 100.0F, 1.0F };
		lightPosition.put(lightPositionArray).flip(); //光源の位置
		FloatBuffer lightDiffuse = BufferUtils.createFloatBuffer(4);
		float[] lightDiffuseArray = { 0.9F, 0.6F, 0.0F, 1.0F };
		lightDiffuse.put(lightDiffuseArray).flip();
		FloatBuffer lightAmbient = BufferUtils.createFloatBuffer(4);
		float[] lightAmbientArray = { 0.4F, 0.4F, 0.4F, 1.0F };
		lightAmbient.put(lightAmbientArray).flip();
		FloatBuffer lightSpecular = BufferUtils.createFloatBuffer(4);
		float[] lightSpecularArray = { 1.0F, 1.0F, 1.0F, 1.0F };
		lightSpecular.put(lightSpecularArray).flip();

		FloatBuffer lightPosition1 = BufferUtils.createFloatBuffer(4);
		float[] lightPositionArray1 = { -100.0F, -20.0F, -100.0F, 1.0F };
		lightPosition1.put(lightPositionArray1).flip(); //光源の位置
		FloatBuffer lightDiffuse1 = BufferUtils.createFloatBuffer(4);
		float[] lightDiffuseArray1 = { 0.0F, 0.3F, 0.9F, 1.0F };
		lightDiffuse1.put(lightDiffuseArray1).flip();

		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_POSITION, lightPosition);
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_DIFFUSE, lightDiffuse);
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_AMBIENT, lightAmbient);
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_SPECULAR, lightSpecular);

		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_POSITION, lightPosition1);
		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_DIFFUSE, lightDiffuse1);

		GL11.glEnable(GL11.GL_LIGHT2);
		GL11.glEnable(GL11.GL_LIGHT3);

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);

		RenderCore.renderCubeOff(size / 2, 4, 0.1);

		/**
		
		this.bindTexture(new ResourceLocation("mitomod", "textures/misc/ya.png"));

		//GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        //GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
		if (tile.sendSide != -1) {
			
			float angle = 0.0F;
			
			switch (tile.sendSide) {
			case 2:
				angle = 0.0F;
				break;
			case 3:
				angle = 180F;
				break;
			case 4:
				angle = 90F;
				break;
			case 5:
				angle = 270F;
				break;
			}
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(angle, 0F, 1F, 0F);

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			tessellator.addVertexWithUV(0.5, 0.4, -0.5, 1, 0);
			tessellator.addVertexWithUV(-0.5, 0.4, -0.5, 0, 0);
			tessellator.addVertexWithUV(-0.5, 0.4, 0.5, 0, 1);
			tessellator.addVertexWithUV(0.5, 0.4, 0.5, 1, 1);
			tessellator.draw();
			
			GL11.glRotatef(-angle, 0F, 1F, 0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		}
		
		**/

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		this.itemStack = tile.getStackInSlotOnClosing(0);

		if (tile.hasItem) {

			/*
			
			IIcon iicon = this.itemStack.getIconIndex();
			
			float minU = iicon.getMinU();
			float maxU = iicon.getMaxU();
			float minV = iicon.getMinV();
			float maxV = iicon.getMaxV();
			
			int side = tile.sendSide;
			int time = tile.getCooldownTime();
			
			double x1 = 0.5 + Facing.offsetsXForSide[side] * 0.5;
			double y1 = 0.5 + Facing.offsetsYForSide[side] * 0.5;
			double z1 = 0.5 + Facing.offsetsZForSide[side] * 0.5;
			
			this.bindTexture(TextureMap.locationBlocksTexture);
			*/

			//renderCube(0.35, 0.5, 0.7, 0.5, 0, 1, 0, 1);

		}

		GL11.glDisable(GL11.GL_BLEND);

		GL11.glTranslatef((float) -x, (float) -y, (float) -z);
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_LIGHT2);
		GL11.glDisable(GL11.GL_LIGHT3);

	}

	public void renderCube(double size, double x, double y, double z, float minU, float maxU, float minV, float maxV) {

		if (size < 0) {
			return;
		}

		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();

		double xmin = x - size / 2;
		double xmax = x + size / 2;
		double ymin = y - size / 2;
		double ymax = y + size / 2;
		double zmin = z - size / 2;
		double zmax = z + size / 2;

		tessellator.setColorRGBA_F(1.0F, 0.6F, 0.6F, 0.8F);

		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator.addVertexWithUV(xmin, ymin, zmin, (double) maxU, (double) minV);
		tessellator.addVertexWithUV(xmin, ymax, zmin, (double) minU, (double) minV);
		tessellator.addVertexWithUV(xmax, ymax, zmin, (double) minU, (double) maxV);
		tessellator.addVertexWithUV(xmax, ymin, zmin, (double) maxU, (double) maxV);

		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertexWithUV(xmin, ymax, zmax, (double) maxU, (double) minV);
		tessellator.addVertexWithUV(xmin, ymin, zmax, (double) minU, (double) minV);
		tessellator.addVertexWithUV(xmax, ymin, zmax, (double) minU, (double) maxV);
		tessellator.addVertexWithUV(xmax, ymax, zmax, (double) maxU, (double) maxV);

		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		tessellator.addVertexWithUV(xmin, ymin, zmin, (double) maxU, (double) minV);
		tessellator.addVertexWithUV(xmax, ymin, zmin, (double) minU, (double) minV);
		tessellator.addVertexWithUV(xmax, ymin, zmax, (double) minU, (double) maxV);
		tessellator.addVertexWithUV(xmin, ymin, zmax, (double) maxU, (double) maxV);

		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(xmin, ymax, zmin, (double) maxU, (double) minV);
		tessellator.addVertexWithUV(xmin, ymax, zmax, (double) minU, (double) minV);
		tessellator.addVertexWithUV(xmax, ymax, zmax, (double) minU, (double) maxV);
		tessellator.addVertexWithUV(xmax, ymax, zmin, (double) maxU, (double) maxV);

		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(xmin, ymin, zmin, (double) maxU, (double) minV);
		tessellator.addVertexWithUV(xmin, ymin, zmax, (double) minU, (double) minV);
		tessellator.addVertexWithUV(xmin, ymax, zmax, (double) minU, (double) maxV);
		tessellator.addVertexWithUV(xmin, ymax, zmin, (double) maxU, (double) maxV);

		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(xmax, ymin, zmax, (double) maxU, (double) minV);
		tessellator.addVertexWithUV(xmax, ymin, zmin, (double) minU, (double) minV);
		tessellator.addVertexWithUV(xmax, ymax, zmin, (double) minU, (double) maxV);
		tessellator.addVertexWithUV(xmax, ymax, zmax, (double) maxU, (double) maxV);

		tessellator.draw();
	}
}
