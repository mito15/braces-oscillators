package com.mito.mitomod.client.render.BlockRenderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderBlockFractalite extends RenderBlockSaiBase {


	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public int getRenderId() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

}
