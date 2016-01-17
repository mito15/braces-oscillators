package com.mito.mitomod.InstObject;

import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.ChunkCache;
import net.minecraftforge.client.event.RenderWorldEvent;

public class RenderBraceHandler {

	BB_Render renderer;
	LoadClientWorldHandler lcw;

	public RenderBraceHandler() {
	}

	@SubscribeEvent
	public void onRenderWorld(RenderWorldEvent.Post e) {
		int i = e.renderer.posX;
		int j = e.renderer.posY;
		int k = e.renderer.posZ;
		ChunkCache cache = e.chunkCache;
		/*List<InstObj> list = LoadClientWorldHandler.INSTANCE.data.getBraceData(i, j, k);
		
		for (int n = 0; n < list.size(); n++) {
			if(list.get(n).renderOnWorldRender()){
				
			}
		}*/

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
	}

	public static void onRenderEntities(EntityLivingBase entity, ICamera camera, float partialtick) {

		double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialtick;
		double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialtick;
		double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialtick;

		GL11.glTranslated(-d0, -d1, -d2);

		List<BraceBase> list = LoadClientWorldHandler.INSTANCE.data.braceBaseList;

		Minecraft.getMinecraft().entityRenderer.enableLightmap((double) partialtick);

		for (int n = 0; n < list.size(); n++) {

			BraceBase base = list.get(n);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			GL11.glTranslated(base.pos.xCoord, base.pos.yCoord, base.pos.zCoord);

			BB_Render render = BB_ResisteredList.getBraceBaseRender(base);
			if (base.shouldUpdate) {
				render.updateRender(base, partialtick);
			}

			int i = base.getBrightnessForRender(partialtick);
			int j = i % 65536;
			int k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);

			render.doRender(base, partialtick);

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();
		}

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
	}

}
