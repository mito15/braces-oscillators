package com.mito.mitomod.client.render.TileRenderer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.mito.mitomod.client.render.RenderCore;
import com.mito.mitomod.common.tile.TileOscillator;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderTileOscillator extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {

		TileOscillator tile = (TileOscillator) tileEntity;
		
		Tessellator tessellator = Tessellator.instance;
		World world = tile.getWorldObj();
		
		int tempo = (int)(tile.freqPecRe * 20) * 3;

		this.bindTexture(new ResourceLocation("mitomod", "textures/blocks/oscillator" + tempo + ".png"));

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
		
		FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);float[] lightPositionArray = {100.0F, 20.0F, 100.0F, 1.0F};lightPosition.put( lightPositionArray ).flip(); //光源の位置
		FloatBuffer lightDiffuse = BufferUtils.createFloatBuffer(4);float[] lightDiffuseArray = {0.9F, 0.6F, 0.0F, 1.0F};lightDiffuse.put( lightDiffuseArray ).flip();
		FloatBuffer lightAmbient = BufferUtils.createFloatBuffer(4);float[] lightAmbientArray = {0.4F, 0.4F, 0.4F, 1.0F};lightAmbient.put( lightAmbientArray ).flip();
		FloatBuffer lightSpecular = BufferUtils.createFloatBuffer(4);float[] lightSpecularArray = {1.0F, 1.0F, 1.0F, 1.0F};lightSpecular.put( lightSpecularArray ).flip();
		
		FloatBuffer lightPosition1 = BufferUtils.createFloatBuffer(4);float[] lightPositionArray1 = {-100.0F, -20.0F, -100.0F, 1.0F};lightPosition1.put( lightPositionArray1 ).flip(); //光源の位置
		FloatBuffer lightDiffuse1 = BufferUtils.createFloatBuffer(4);float[] lightDiffuseArray1 = {0.0F, 0.3F, 0.9F, 1.0F};lightDiffuse1.put( lightDiffuseArray1 ).flip();
		
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

		RenderCore.renderCubeOff(size, 4, 0.1);

		GL11.glTranslatef((float) -x, (float) -y, (float) -z);
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_LIGHT2);
		GL11.glDisable(GL11.GL_LIGHT3);

	}
}
