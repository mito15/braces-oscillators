package com.mito.mitomod.client.render.BlockRenderer;

import com.mito.mitomod.client.render.RenderCore;
import com.mito.mitomod.common.LoadBCAPI;
import com.mito.mitomod.common.LoadCoFHLib;
import com.mito.mitomod.common.BAO_main;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;

public class RenderBlockNeuron implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		// TileEntityRendererDispatcher.instance.getSpecialRenderer(testTileEntity).renderTileEntityAt(testTileEntity,
		// -0.5D, -0.5D, -0.5D, 0.0F);

		RenderCore.renderInvCuboid(renderer, block, 0.7, 0.7, 1.0, 1);
		RenderCore.renderInvCuboid(renderer, block, 0.6, 0.6, 0.6, 4);

	}

	public boolean canConnectPipeTo(IBlockAccess world, int x, int y, int z) {

		TileEntity tile = world.getTileEntity(x, y, z);
		boolean flag = false;

		if (Loader.isModLoaded("BuildCraft|Transport")) {
			try {
				flag = LoadBCAPI.isPipe(tile);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}

		if (Loader.isModLoaded("ThermalDynamics")) {
			try {
				flag = LoadCoFHLib.isDuct(tile);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}

		if (tile instanceof IInventory || flag) {

			return true;

		}

		Block block = world.getBlock(x, y, z);
		return block.getRenderType() != BAO_main.PipeRenderType ? false : true;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		if (modelId == this.getRenderId()) {


			double wid = 0.03;
			double size = 0.5;
			double n = 0.5 - size / 2;
			double p = 0.5 + size / 2;

			renderFrame(block, x, y, z, renderer, size, wid);

			for (int n1 = 0; n1 < 6; n1++) {

				int xOff = Facing.offsetsXForSide[n1];
				int yOff = Facing.offsetsYForSide[n1];
				int zOff = Facing.offsetsZForSide[n1];

				if (this.canConnectPipeTo(world, x + xOff, y + yOff, z + zOff)) {

					double x11 = xOff == 0 ? n : (xOff == 1 ? p : 0);
					double y11 = yOff == 0 ? n : (yOff == 1 ? p : 0);
					double z11 = zOff == 0 ? n : (zOff == 1 ? p : 0);
					double x12 = xOff == 0 ? n + wid : (xOff == 1 ? 1 : n);
					double y12 = yOff == 0 ? n + wid : (yOff == 1 ? 1 : n);
					double z12 = zOff == 0 ? n + wid : (zOff == 1 ? 1 : n);
					double x21 = xOff == 0 ? p - wid : (xOff == 1 ? p : 0);
					double y21 = yOff == 0 ? p - wid : (yOff == 1 ? p : 0);
					double z21 = zOff == 0 ? p - wid : (zOff == 1 ? p : 0);
					double x22 = xOff == 0 ? p : (xOff == 1 ? 1 : n);
					double y22 = yOff == 0 ? p : (yOff == 1 ? 1 : n);
					double z22 = zOff == 0 ? p : (zOff == 1 ? 1 : n);

					renderer.setRenderBounds(x11, y11, z11, x12, y12, z12);
					renderer.renderStandardBlock(block, x, y, z);
					renderer.setRenderBounds(x21, y21, z21, x22, y22, z22);
					renderer.renderStandardBlock(block, x, y, z);

					if (xOff != 0) {

						renderer.setRenderBounds(x11, y11, z21, x12, y12, z22);
						renderer.renderStandardBlock(block, x, y, z);
						renderer.setRenderBounds(x21, y21, z11, x22, y12, z12);
						renderer.renderStandardBlock(block, x, y, z);
					} else {

						renderer.setRenderBounds(x11, y21, z21, x12, y22, z22);
						renderer.renderStandardBlock(block, x, y, z);
						renderer.setRenderBounds(x21, y11, z11, x22, y12, z12);
						renderer.renderStandardBlock(block, x, y, z);
					}

				}

			}

			if (this.canConnectPipeTo(world, x - 1, y, z)) {

				renderer.setRenderBounds(0.0, n, n, n, n + wid, n + wid);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0.0, p - wid, n, n, p, n + wid);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0.0, n, p - wid, n, n + wid, p);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(0.0, p - wid, p - wid, n, p, p);
				renderer.renderStandardBlock(block, x, y, z);

			}

			if (this.canConnectPipeTo(world, x + 1, y, z)) {

				renderer.setRenderBounds(p, n, n, 1.0, n + wid, n + wid);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(p, p - wid, n, 1.0, p, n + wid);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(p, n, p - wid, 1.0, n + wid, p);
				renderer.renderStandardBlock(block, x, y, z);
				renderer.setRenderBounds(p, p - wid, p - wid, 1.0, p, p);
				renderer.renderStandardBlock(block, x, y, z);

			}

			return true;
		}
		return false;
	}

	public void renderFrame(Block block, int x, int y, int z, RenderBlocks renderer, double size, double width) {

		double nX = 0.5 - size / 2;
		double nY = 0.5 - size / 2;
		double nZ = 0.5 - size / 2;
		double pX = 0.5 + size / 2;
		double pY = 0.5 + size / 2;
		double pZ = 0.5 + size / 2;
		double wid = width;

		renderer.setRenderBounds(nX, nY, nZ, pX, nY + wid, nZ + wid);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(nX, nY + wid, nZ, nX + wid, pY, nZ + wid);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(nX, nY, nZ + wid, nX + wid, nY + wid, pZ);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(pX - wid, nY, nZ + wid, pX, nY + wid, pZ);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(pX - wid, nY + wid, nZ, pX, pY, nZ + wid);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(nX, pY - wid, nZ + wid, nX + wid, pY, pZ);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(nX + wid, pY - wid, nZ, pZ, pY, nZ + wid);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(nX + wid, nY, pZ - wid, pX, nY + wid, pZ);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(nX, nY + wid, pZ - wid, nX + wid, pY, pZ);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(nX + wid, pY - wid, pZ - wid, pX, pY, pZ);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(pX - wid, nY + wid, pZ - wid, pX, pY, pZ);
		renderer.renderStandardBlock(block, x, y, z);
		renderer.setRenderBounds(pX - wid, pY - wid, nZ + wid, pX, pY, pZ);
		renderer.renderStandardBlock(block, x, y, z);

	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return BAO_main.PipeRenderType;
	}

}
