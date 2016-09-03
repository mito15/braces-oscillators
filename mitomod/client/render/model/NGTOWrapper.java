package com.mito.mitomod.client.render.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mito.mitomod.common.BAO_main;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.ngt.ngtlib.block.NGTObject;
import jp.ngt.ngtlib.renderer.NGTRenderer;
import jp.ngt.ngtlib.util.NGTUtil;
import jp.ngt.ngtlib.world.NGTWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class NGTOWrapper extends Polygon3D {

	@SideOnly(Side.CLIENT)
	public World dummyWorld;
	@SideOnly(Side.CLIENT)
	public int displayList;
	@SideOnly(Side.CLIENT)
	public float minimizeRateFloat = 0.0F;
	public NGTObject ngto;
	public int minimizeRate = 10;
	public float rotation = 0;

	public NGTOWrapper(String string) {
		File sd = BAO_main.INSTANCE.shapesDir;
		File impf = new File(sd, string);
		this.ngto = NGTObject.importFromFile(impf);
	}

	@SideOnly(Side.CLIENT)
	public float getMinimizeRate() {
		if (this.minimizeRateFloat == 0.0F) {
			this.minimizeRateFloat = 1.0F / (float) this.minimizeRate;
		}
		return this.minimizeRateFloat;
	}

	/**ミニチュアのディスプレイリスト*/
	private Map<Long, Integer> glListMap = new HashMap<Long, Integer>();

	protected void bindTexture(ResourceLocation p_147499_1_) {
		Minecraft.getMinecraft().renderEngine.bindTexture(p_147499_1_);
	}

	public void renderMiniatureAt(float par8) {
		if (this.ngto == null) {
			return;
		}

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);

		//GL11.glTranslatef((float) par2 + 0.5F, (float) par4, (float) par6 + 0.5F);
		GL11.glRotatef(this.rotation, 0.0F, 1.0F, 0.0F);
		float f0 = this.getMinimizeRate();
		GL11.glScalef(f0, f0, f0);

		if (this.dummyWorld == null) {
			this.dummyWorld = new NGTWorld(NGTUtil.getClientWorld(), this.ngto);
		}

		this.bindTexture(TextureMap.locationBlocksTexture);
		if (this.displayList == 0) {
			if (this.glListMap.containsKey(this.ngto.objId)) {
				this.displayList = this.glListMap.get(this.ngto.objId);
			} else {
				this.displayList = GLAllocation.generateDisplayLists(1);
				this.glListMap.put(this.ngto.objId, this.displayList);

				GL11.glNewList(this.displayList, GL11.GL_COMPILE);
				GL11.glTranslatef(-(float) (this.ngto.xSize >> 1), 0.0F, -(float) (this.ngto.zSize >> 1));
				NGTRenderer.renderNGTObject((NGTWorld) this.dummyWorld, this.ngto);
				GL11.glEndList();
			}
		} else {
			GL11.glCallList(this.displayList);
		}

		this.renderTileEntities(par8);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();

	}

	private void renderTileEntities(float par8) {
		for (TileEntity tile : ((NGTWorld) this.dummyWorld).getTileEntityList()) {
			this.renderTileEntityByRenderer(tile, tile.xCoord, tile.yCoord, tile.zCoord, par8);
		}
	}

	private void renderTileEntityByRenderer(TileEntity tile, double par2, double par4, double par6, float par8) {
		TileEntitySpecialRenderer renderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tile);

		if (renderer != null) {
			try {
				renderer.renderTileEntityAt(tile, par2, par4, par6, par8);
			} catch (Throwable throwable) {
				CrashReport report = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
				CrashReportCategory category = report.makeCategory("Block Entity Details");
				tile.func_145828_a(category);
				throw new ReportedException(report);
			}
		}
	}

	@Override
	public void renderAt(Vec3 offset, double roll, double pitch, double yaw, double size, float partialTickTime) {
		GL11.glPushMatrix();
		GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);
		GL11.glRotated(yaw, 0, 1, 0);
		GL11.glRotated(pitch, 1, 0, 0);
		GL11.glRotated(roll, 0, 0, 1);
		GL11.glScaled(size, size, size);
		GL11.glTranslated(0, -this.ngto.ySize / 2 * getMinimizeRate(), 0);
		renderMiniatureAt(partialTickTime);
		GL11.glPopMatrix();
	}

}
