package com.mito.mitomod.client.render;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.common.item.ItemBrace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class RenderItemBrace implements IItemRenderer {

	private RenderItem renderer = new RenderItem();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemstack, Object... data) {

		ItemBrace item = (ItemBrace) itemstack.getItem();
		int isize = item.getSize(itemstack);
		int color = item.getColor(itemstack);

		Tessellator tess = Tessellator.instance;

		Minecraft.getMinecraft().renderEngine.bindTexture(item.getResourceLocation(itemstack));

		double size = 0.25 * (double) isize + 0.25;
		size = size >= 5.0 ? 5.0 : size;

		GL11.glPushMatrix();
		GL11.glTranslated(8, 8, 0);

		tess.startDrawingQuads();

		tess.setNormal(0, 0, 1);
		tess.addVertex(6, size, 0);
		tess.addVertex(6, -size, 0);
		tess.addVertex(-6, -size, 0);
		tess.addVertex(-6, size, 0);

		tess.draw();

		GL11.glPopMatrix();

	}
}
