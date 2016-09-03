package com.mito.mitomod.BraceBase;

import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldEvent;

public class BB_RenderHandler {

	BB_Render renderer;
	LoadClientWorldHandler lcw;
	static boolean usevbos = true;

	public BB_RenderHandler() {
	}

	@SubscribeEvent
	public void onRenderWorld(RenderWorldEvent.Post e) {
		if (!usevbos) {
			int i = e.renderer.posX;
			int j = e.renderer.posY;
			int k = e.renderer.posZ;
			BB_DataChunk ret = (BB_DataChunk) BB_DataLists.getWorldData(e.renderer.worldObj).coordToDataMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(i / 16, k / 16));
			if (ret != null) {
				List<BraceBase> list = ret.braceList;
				for (int n = 0; n < list.size(); n++) {
					//if(list.get(n).renderOnWorldRender()){
					BraceBase base = list.get(n);
					//if (base.buffer != null) {
					//	base.buffer.updateBrightness(base, 1.0F);

					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glPushMatrix();
					GL11.glTranslated(base.pos.xCoord, base.pos.yCoord, base.pos.zCoord);
					BB_Render render = BB_ResisteredList.getBraceBaseRender(base);
					render.staticRender(base);
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glPopMatrix();

					//}
				}
			}
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		}
	}

	public static void enableClient() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
	}

	public static void disableClient() {
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
	}

	public static void onRenderEntities(EntityLivingBase entity, ICamera camera, float partialticks) {

		if (MinecraftForgeClient.getRenderPass() == 0 && usevbos) {
		
			Minecraft.getMinecraft().entityRenderer.enableLightmap((double) partialticks);
			//GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			enableClient();
			double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialticks;
			double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialticks;
			double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialticks;
			GL11.glTranslated(-d0, -d1, -d2);
			List<BraceBase> list = LoadClientWorldHandler.INSTANCE.data.braceBaseList;
		
			//Minecraft.getMinecraft().entityRenderer.enableLightmap((double) partialticks);
			//camera.isBoundingBoxInFrustum(null);
		
			for (int n = 0; n < list.size(); n++) {
		
				BraceBase base = list.get(n);
				AxisAlignedBB aabb = base.getBoundingBox();
				if (aabb != null && !camera.isBoundingBoxInFrustum(aabb)) {
					continue;
				}
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPushMatrix();
				if (base.isStatic) {
					GL11.glTranslated(base.pos.xCoord, base.pos.yCoord, base.pos.zCoord);
				} else {
					double x = base.prevPos.xCoord + (base.pos.xCoord - base.prevPos.xCoord) * (double) partialticks;
					double y = base.prevPos.yCoord + (base.pos.yCoord - base.prevPos.yCoord) * (double) partialticks;
					double z = base.prevPos.zCoord + (base.pos.zCoord - base.prevPos.zCoord) * (double) partialticks;
					GL11.glTranslated(x, y, z);
					int i = base.getBrightnessForRender(partialticks);
					int j = i % 65536;
					int k = i / 65536;
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
				}
		
				BB_Render render = BB_ResisteredList.getBraceBaseRender(base);
				if (base.shouldUpdateRender) {
					render.updateRender(base, partialticks);
				}
		
				render.doRender(base, partialticks);
		
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPopMatrix();
			}
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			disableClient();
			Minecraft.getMinecraft().entityRenderer.disableLightmap((double) partialticks);
		}
	}

}
