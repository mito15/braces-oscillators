package com.mito.mitomod.client.render.BlockRenderer;

import com.mito.mitomod.client.render.RenderCore;
import com.mito.mitomod.common.mitomain;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderBlockOscillator implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		// TileEntityRendererDispatcher.instance.getSpecialRenderer(testTileEntity).renderTileEntityAt(testTileEntity,
		// -0.5D, -0.5D, -0.5D, 0.0F);

		RenderCore.renderInvCuboid(renderer, block, 1.0, 1.0, 1.0, 1);
		RenderCore.renderInvCuboid(renderer, block, 0.7, 0.7, 0.7, 4);

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		
		if (modelId == this.getRenderId()) {
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.03D, 0.03D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 0.03D, 1.0D, 0.03D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 0.03D, 0.03D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.97D, 0.0D, 0.0D, 1.0D, 0.03D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.97D, 0.0D, 0.0D, 1.0D, 1.0D, 0.03D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.97D, 0.0D, 0.03D, 1.0D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.97D, 0.0D, 1.0D, 1.0D, 0.03D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.0D, 0.97D, 1.0D, 0.03D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.0D, 0.97D, 0.03D, 1.0D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.0D, 0.97D, 0.97D, 1.0D, 1.0D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.97D, 0.0D, 0.97D, 1.0D, 1.0D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			renderer.setRenderBounds(0.97D, 0.97D, 0.0D, 1.0D, 1.0D, 1.0D);
			renderer.renderStandardBlock(block, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return mitomain.OscillatorRenderType;
	}

}
