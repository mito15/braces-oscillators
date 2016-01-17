package com.mito.mitomod.client.render;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

public final class RenderCore {

	public static void renderInvCuboid(RenderBlocks renderer, Block block, double sizeX, double sizeY,
			double sizeZ, int metadata) {

		renderInvCuboid(renderer, block, 0.5 - sizeX / 2, 0.5 - sizeY / 2, 0.5 - sizeX / 2, 0.5 + sizeX / 2, 0.5 + sizeY / 2, 0.5 + sizeX / 2, metadata);

	}

	public static void renderInvCuboid(RenderBlocks renderer, Block block, double minX, double minY,
			double minZ, double maxX, double maxY, double maxZ, int metadata) {

		Tessellator tessellator = Tessellator.instance;
		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

	}

	public static void renderInvCuboid(double x, double y, double z, float minX, float minY, float minZ, float maxX,
			float maxY, float maxZ) {

		Tessellator tessellator = Tessellator.instance;

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);

		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

	}

	public static void renderSlantingY(IBlockAccess world, Block block, int x, int y, int z, float minY, float maxY,
			float nnSize, float npSize) {

		renderSlantingY(world, block, x, y, z, minY, maxY, nnSize, npSize, 0.5F, 0.5F);

	}

	public static void renderSlantingY(IBlockAccess world, Block block, int x, int y, int z, float minY, float maxY,
			float ppSize, float pnSize, float cordX, float cordZ) {

		IIcon icon = block.getIcon(0, 0);

		Tessellator tessellator = Tessellator.instance;

		double cos = Math.cos(Math.toRadians(45));
		double sin = Math.cos(Math.toRadians(45));

		double d1Xp = ppSize / 2 * cos - pnSize / 2 * sin;
		double d1Zp = ppSize / 2 * sin + pnSize / 2 * cos;

		double d2Xp = -ppSize / 2 * cos - pnSize / 2 * sin;
		double d2Zp = -ppSize / 2 * sin + pnSize / 2 * cos;

		double d1X = x + cordX + d1Xp;
		double d1Z = z + cordZ + d1Zp;
		double d2X = x + cordX + d2Xp;
		double d2Z = z + cordZ + d2Zp;
		double d3X = x + cordX - d1Xp;
		double d3Z = z + cordZ - d1Zp;
		double d4X = x + cordX - d2Xp;
		double d4Z = z + cordZ - d2Zp;

		double Hy = y + maxY;
		double Ly = y + minY;

		double d5 = (double) icon.getInterpolatedU(8.0D - ppSize * 8.0D);
		double d6 = (double) icon.getInterpolatedU(8.0D + ppSize * 8.0D);

		double d7 = (double) icon.getInterpolatedU(8.0D - pnSize * 8.0D);
		double d8 = (double) icon.getInterpolatedU(8.0D + pnSize * 8.0D);

		double d9 = (double) icon.getInterpolatedV(16.0D - maxY * 16.0D);
		double d10 = (double) icon.getInterpolatedV(16.0D - minY * 16.0D);

		double d11 = (double) icon.getInterpolatedV(8.0D - pnSize * 8.0D);
		double d12 = (double) icon.getInterpolatedV(8.0D + pnSize * 8.0D);

		if (ppSize > 1.0F) {
			d5 = (double) icon.getInterpolatedU(0);
			d6 = (double) icon.getInterpolatedU(16);
		}

		if (pnSize > 1.0F) {
			d7 = (double) icon.getInterpolatedU(0);
			d8 = (double) icon.getInterpolatedU(16);
			d11 = (double) icon.getInterpolatedV(0);
			d12 = (double) icon.getInterpolatedV(16);
		}

		if (minY < 0 || maxY > 1) {
			d9 = (double) icon.getInterpolatedV(0);
			d10 = (double) icon.getInterpolatedV((maxY - minY) * 16.0D);
		}

		if ((maxY - minY) > 1.0F) {
			d9 = (double) icon.getInterpolatedV(0);
			d10 = (double) icon.getInterpolatedV(16);
		}

		int lightValue = block.getMixedBrightnessForBlock(world, x, y, z);
		tessellator.setBrightness(lightValue);
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.addVertexWithUV(d1X, Ly, d1Z, d5, d11);
		tessellator.addVertexWithUV(d2X, Ly, d2Z, d5, d12);
		tessellator.addVertexWithUV(d3X, Ly, d3Z, d6, d12);
		tessellator.addVertexWithUV(d4X, Ly, d4Z, d6, d11);

		tessellator.addVertexWithUV(d1X, Hy, d1Z, d5, d11);
		tessellator.addVertexWithUV(d4X, Hy, d4Z, d5, d12);
		tessellator.addVertexWithUV(d3X, Hy, d3Z, d6, d12);
		tessellator.addVertexWithUV(d2X, Hy, d2Z, d6, d11);

		tessellator.addVertexWithUV(d1X, Hy, d1Z, d7, d9);
		tessellator.addVertexWithUV(d1X, Ly, d1Z, d7, d10);
		tessellator.addVertexWithUV(d4X, Ly, d4Z, d8, d10);
		tessellator.addVertexWithUV(d4X, Hy, d4Z, d8, d9);

		tessellator.addVertexWithUV(d4X, Hy, d4Z, d5, d9);
		tessellator.addVertexWithUV(d4X, Ly, d4Z, d5, d10);
		tessellator.addVertexWithUV(d3X, Ly, d3Z, d6, d10);
		tessellator.addVertexWithUV(d3X, Hy, d3Z, d6, d9);

		tessellator.addVertexWithUV(d3X, Hy, d3Z, d7, d9);
		tessellator.addVertexWithUV(d3X, Ly, d3Z, d7, d10);
		tessellator.addVertexWithUV(d2X, Ly, d2Z, d8, d10);
		tessellator.addVertexWithUV(d2X, Hy, d2Z, d8, d9);

		tessellator.addVertexWithUV(d2X, Hy, d2Z, d5, d9);
		tessellator.addVertexWithUV(d2X, Ly, d2Z, d5, d10);
		tessellator.addVertexWithUV(d1X, Ly, d1Z, d6, d10);
		tessellator.addVertexWithUV(d1X, Hy, d1Z, d6, d9);
	}

	public static void renderSlantingX(IBlockAccess world, Block block, int x, int y, int z, float minX, float maxX,
			float nnSize, float npSize) {

		renderSlantingX(world, block, x, y, z, minX, maxX, nnSize, npSize, 0.5F, 0.5F);

	}

	public static void renderSlantingX(IBlockAccess world, Block block, int x, int y, int z, float minX, float maxX,
			float ppSize, float pnSize, float cordY, float cordZ) {

		IIcon icon = block.getIcon(0, 0);

		Tessellator tessellator = Tessellator.instance;

		double cos = Math.cos(Math.toRadians(45));
		double sin = Math.cos(Math.toRadians(45));

		double d1Yp = ppSize / 2 * cos - pnSize / 2 * sin;
		double d1Zp = ppSize / 2 * sin + pnSize / 2 * cos;

		double d2Yp = -ppSize / 2 * cos - pnSize / 2 * sin;
		double d2Zp = -ppSize / 2 * sin + pnSize / 2 * cos;

		double d1Y = y + cordY + d1Yp;
		double d1Z = z + cordZ + d1Zp;
		double d2Y = y + cordY + d2Yp;
		double d2Z = z + cordZ + d2Zp;
		double d3Y = y + cordY - d1Yp;
		double d3Z = z + cordZ - d1Zp;
		double d4Y = y + cordY - d2Yp;
		double d4Z = z + cordZ - d2Zp;

		double Hx = x + maxX;
		double Lx = x + minX;

		double d5 = (double) icon.getInterpolatedU(8.0D - ppSize * 8.0D);
		double d6 = (double) icon.getInterpolatedU(8.0D + ppSize * 8.0D);

		double d7 = (double) icon.getInterpolatedU(8.0D - pnSize * 8.0D);
		double d8 = (double) icon.getInterpolatedU(8.0D + pnSize * 8.0D);

		double d9 = (double) icon.getInterpolatedV(16.0D - maxX * 16.0D);
		double d10 = (double) icon.getInterpolatedV(16.0D - minX * 16.0D);

		double d11 = (double) icon.getInterpolatedV(8.0D - pnSize * 8.0D);
		double d12 = (double) icon.getInterpolatedV(8.0D + pnSize * 8.0D);

		if (ppSize > 1.0F) {
			d5 = (double) icon.getInterpolatedU(0);
			d6 = (double) icon.getInterpolatedU(16);
		}

		if (pnSize > 1.0F) {
			d7 = (double) icon.getInterpolatedU(0);
			d8 = (double) icon.getInterpolatedU(16);
			d11 = (double) icon.getInterpolatedV(0);
			d12 = (double) icon.getInterpolatedV(16);
		}

		if (minX < 0 || maxX > 1) {
			d9 = (double) icon.getInterpolatedV(0);
			d10 = (double) icon.getInterpolatedV((maxX - minX) * 16.0D);
		}

		if ((maxX - minX) > 1.0F) {
			d9 = (double) icon.getInterpolatedV(0);
			d10 = (double) icon.getInterpolatedV(16);
		}

		int lightValue = block.getMixedBrightnessForBlock(world, x, y, z);
		tessellator.setBrightness(lightValue);
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.addVertexWithUV(Hx, d1Y, d1Z, d5, d11);
		tessellator.addVertexWithUV(Hx, d2Y, d2Z, d5, d12);
		tessellator.addVertexWithUV(Hx, d3Y, d3Z, d6, d12);
		tessellator.addVertexWithUV(Hx, d4Y, d4Z, d6, d11);

		tessellator.addVertexWithUV(Lx, d1Y, d1Z, d5, d11);
		tessellator.addVertexWithUV(Lx, d4Y, d4Z, d5, d12);
		tessellator.addVertexWithUV(Lx, d3Y, d3Z, d6, d12);
		tessellator.addVertexWithUV(Lx, d2Y, d2Z, d6, d11);

		tessellator.addVertexWithUV(Lx, d1Y, d1Z, d7, d9);
		tessellator.addVertexWithUV(Hx, d1Y, d1Z, d7, d10);
		tessellator.addVertexWithUV(Hx, d4Y, d4Z, d8, d10);
		tessellator.addVertexWithUV(Lx, d4Y, d4Z, d8, d9);

		tessellator.addVertexWithUV(Lx, d2Y, d2Z, d7, d9);
		tessellator.addVertexWithUV(Hx, d2Y, d2Z, d7, d10);
		tessellator.addVertexWithUV(Hx, d1Y, d1Z, d8, d10);
		tessellator.addVertexWithUV(Lx, d1Y, d1Z, d8, d9);

		tessellator.addVertexWithUV(Lx, d3Y, d3Z, d7, d9);
		tessellator.addVertexWithUV(Hx, d3Y, d3Z, d7, d10);
		tessellator.addVertexWithUV(Hx, d2Y, d2Z, d8, d10);
		tessellator.addVertexWithUV(Lx, d2Y, d2Z, d8, d9);

		tessellator.addVertexWithUV(Lx, d4Y, d4Z, d7, d9);
		tessellator.addVertexWithUV(Hx, d4Y, d4Z, d7, d10);
		tessellator.addVertexWithUV(Hx, d3Y, d3Z, d8, d10);
		tessellator.addVertexWithUV(Lx, d3Y, d3Z, d8, d9);
	}

	public static void renderSlantingZ(IBlockAccess world, Block block, int x, int y, int z, float minZ, float maxZ,
			float nnSize, float npSize) {

		renderSlantingZ(world, block, x, y, z, minZ, maxZ, nnSize, npSize, 0.5F, 0.5F);

	}

	public static void renderSlantingZ(IBlockAccess world, Block block, int x, int y, int z, float minZ, float maxZ,
			float ppSize, float pnSize, float cordX, float cordY) {

		IIcon icon = block.getIcon(0, 0);

		Tessellator tessellator = Tessellator.instance;

		double cos = Math.cos(Math.toRadians(45));
		double sin = Math.cos(Math.toRadians(45));

		double d1Xp = ppSize / 2 * cos - pnSize / 2 * sin;
		double d1Yp = ppSize / 2 * sin + pnSize / 2 * cos;

		double d2Xp = -ppSize / 2 * cos - pnSize / 2 * sin;
		double d2Yp = -ppSize / 2 * sin + pnSize / 2 * cos;

		double d1X = x + cordX + d1Xp;
		double d1Y = y + cordY + d1Yp;
		double d2X = x + cordX + d2Xp;
		double d2Y = y + cordY + d2Yp;
		double d3X = x + cordX - d1Xp;
		double d3Y = y + cordY - d1Yp;
		double d4X = x + cordX - d2Xp;
		double d4Y = y + cordY - d2Yp;

		double Hz = z + maxZ;
		double Lz = z + minZ;

		double d5 = (double) icon.getInterpolatedU(8.0D - ppSize * 8.0D);
		double d6 = (double) icon.getInterpolatedU(8.0D + ppSize * 8.0D);

		double d7 = (double) icon.getInterpolatedU(8.0D - pnSize * 8.0D);
		double d8 = (double) icon.getInterpolatedU(8.0D + pnSize * 8.0D);

		double d9 = (double) icon.getInterpolatedV(16.0D - maxZ * 16.0D);
		double d10 = (double) icon.getInterpolatedV(16.0D - minZ * 16.0D);

		double d11 = (double) icon.getInterpolatedV(8.0D - pnSize * 8.0D);
		double d12 = (double) icon.getInterpolatedV(8.0D + pnSize * 8.0D);

		if (ppSize > 1.0F) {
			d5 = (double) icon.getInterpolatedU(0);
			d6 = (double) icon.getInterpolatedU(16);
		}

		if (pnSize > 1.0F) {
			d7 = (double) icon.getInterpolatedU(0);
			d8 = (double) icon.getInterpolatedU(16);
			d11 = (double) icon.getInterpolatedV(0);
			d12 = (double) icon.getInterpolatedV(16);
		}

		if (minZ < 0 || maxZ > 1) {
			d9 = (double) icon.getInterpolatedV(0);
			d10 = (double) icon.getInterpolatedV((maxZ - minZ) * 16.0D);
		}

		if ((maxZ - minZ) > 1.0F) {
			d9 = (double) icon.getInterpolatedV(0);
			d10 = (double) icon.getInterpolatedV(16);
		}

		int lightValue = block.getMixedBrightnessForBlock(world, x, y, z);
		tessellator.setBrightness(lightValue);
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.addVertexWithUV(d1X, d1Y, Hz, d5, d11);
		tessellator.addVertexWithUV(d2X, d2Y, Hz, d5, d12);
		tessellator.addVertexWithUV(d3X, d3Y, Hz, d6, d12);
		tessellator.addVertexWithUV(d4X, d4Y, Hz, d6, d11);

		tessellator.addVertexWithUV(d1X, d1Y, Lz, d5, d11);
		tessellator.addVertexWithUV(d4X, d4Y, Lz, d5, d12);
		tessellator.addVertexWithUV(d3X, d3Y, Lz, d6, d12);
		tessellator.addVertexWithUV(d2X, d2Y, Lz, d6, d11);

		tessellator.addVertexWithUV(d1X, d1Y, Lz, d7, d9);
		tessellator.addVertexWithUV(d1X, d1Y, Hz, d7, d10);
		tessellator.addVertexWithUV(d4X, d4Y, Hz, d8, d10);
		tessellator.addVertexWithUV(d4X, d4Y, Lz, d8, d9);

		tessellator.addVertexWithUV(d2X, d2Y, Lz, d7, d9);
		tessellator.addVertexWithUV(d2X, d2Y, Hz, d7, d10);
		tessellator.addVertexWithUV(d1X, d1Y, Hz, d8, d10);
		tessellator.addVertexWithUV(d1X, d1Y, Lz, d8, d9);

		tessellator.addVertexWithUV(d3X, d3Y, Lz, d7, d9);
		tessellator.addVertexWithUV(d3X, d3Y, Hz, d7, d10);
		tessellator.addVertexWithUV(d2X, d2Y, Hz, d8, d10);
		tessellator.addVertexWithUV(d2X, d2Y, Lz, d8, d9);

		tessellator.addVertexWithUV(d4X, d4Y, Lz, d7, d9);
		tessellator.addVertexWithUV(d4X, d4Y, Hz, d7, d10);
		tessellator.addVertexWithUV(d3X, d3Y, Hz, d8, d10);
		tessellator.addVertexWithUV(d3X, d3Y, Lz, d8, d9);
	}

	public static void renderCubeOff(double size, int ac, double r) {

		if (r > 1.0 || r < 0) {
			return;
		}

		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();

		double min = 0.5 - size / 2;
		double max = 0.5 + size / 2;

		double mino = 0.5 - size * (1 - r) / 2;
		double maxo = 0.5 + size * (1 - r) / 2;

		tessellator.setColorRGBA_F(1.0F, 0.6F, 0.6F, 0.8F);

		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator.addVertexWithUV(mino, mino, min, 1, 1);
		tessellator.addVertexWithUV(mino, maxo, min, 1, 0);
		tessellator.addVertexWithUV(maxo, maxo, min, 0, 0);
		tessellator.addVertexWithUV(maxo, mino, min, 0, 1);

		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertexWithUV(mino, maxo, max, 1, 0);
		tessellator.addVertexWithUV(mino, mino, max, 1, 1);
		tessellator.addVertexWithUV(maxo, mino, max, 0, 1);
		tessellator.addVertexWithUV(maxo, maxo, max, 0, 0);

		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		tessellator.addVertexWithUV(mino, min, mino, 1, 1);
		tessellator.addVertexWithUV(maxo, min, mino, 1, 0);
		tessellator.addVertexWithUV(maxo, min, maxo, 0, 0);
		tessellator.addVertexWithUV(mino, min, maxo, 0, 1);

		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(mino, max, mino, 1, 0);
		tessellator.addVertexWithUV(mino, max, maxo, 1, 1);
		tessellator.addVertexWithUV(maxo, max, maxo, 0, 1);
		tessellator.addVertexWithUV(maxo, max, mino, 0, 0);

		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(min, mino, mino, 1, 1);
		tessellator.addVertexWithUV(min, mino, maxo, 1, 0);
		tessellator.addVertexWithUV(min, maxo, maxo, 0, 0);
		tessellator.addVertexWithUV(min, maxo, mino, 0, 1);

		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(max, mino, maxo, 1, 0);
		tessellator.addVertexWithUV(max, mino, mino, 1, 1);
		tessellator.addVertexWithUV(max, maxo, mino, 0, 1);
		tessellator.addVertexWithUV(max, maxo, maxo, 0, 0);

		tessellator.draw();

		double prad = Math.PI / 2 / ac;
		double[] sin = new double[ac + 1];
		double[] cos = new double[ac + 1];
		double[] s = new double[ac + 1];
		double[] c = new double[ac + 1];

		for (int n = 0; n < ac + 1; n++) {

			s[n] = Math.sin(n * prad);
			c[n] = Math.cos(n * prad);
			sin[n] = s[n] * size / 2 * r;
			cos[n] = c[n] * size / 2 * r;

		}

		for (int m = 0; m < 3; m++) {

			GL11.glPushMatrix();

			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			if (m == 1) {
				GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
			} else if (m == 2) {
				GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			}

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

			tessellator.startDrawingQuads();

			tessellator.setColorRGBA_F(1.0F, 0.6F, 0.6F, 0.8F);

			for (int n = 0; n < ac; n++) {

				tessellator.setNormal((float) (c[n]), (float) (s[n]), 0.0F);
				tessellator.addVertexWithUV(maxo + cos[n], maxo + sin[n], maxo, 1, 1);
				tessellator.addVertexWithUV(maxo + cos[n], maxo + sin[n], mino, 1, 0);
				tessellator.addVertexWithUV(maxo + cos[n + 1], maxo + sin[n + 1], mino, 0, 0);
				tessellator.addVertexWithUV(maxo + cos[n + 1], maxo + sin[n + 1], maxo, 0, 1);

				tessellator.setNormal((float) (-s[n]), (float) (c[n]), 0.0F);
				tessellator.addVertexWithUV(mino - sin[n], maxo + cos[n], maxo, 1, 1);
				tessellator.addVertexWithUV(mino - sin[n], maxo + cos[n], mino, 1, 0);
				tessellator.addVertexWithUV(mino - sin[n + 1], maxo + cos[n + 1], mino, 0, 0);
				tessellator.addVertexWithUV(mino - sin[n + 1], maxo + cos[n + 1], maxo, 0, 1);

				tessellator.setNormal((float) (-c[n]), (float) (-s[n]), 0.0F);
				tessellator.addVertexWithUV(mino - cos[n], mino - sin[n], maxo, 1, 1);
				tessellator.addVertexWithUV(mino - cos[n], mino - sin[n], mino, 1, 0);
				tessellator.addVertexWithUV(mino - cos[n + 1], mino - sin[n + 1], mino, 0, 0);
				tessellator.addVertexWithUV(mino - cos[n + 1], mino - sin[n + 1], maxo, 0, 1);

				tessellator.setNormal((float) (s[n]), (float) (-c[n]), 0.0F);
				tessellator.addVertexWithUV(maxo + sin[n], mino - cos[n], maxo, 1, 0.1);
				tessellator.addVertexWithUV(maxo + sin[n], mino - cos[n], mino, 1, 0);
				tessellator.addVertexWithUV(maxo + sin[n + 1], mino - cos[n + 1], mino, 0, 0);
				tessellator.addVertexWithUV(maxo + sin[n + 1], mino - cos[n + 1], maxo, 0, 1);

			}

			tessellator.draw();

			GL11.glPopMatrix();
		}

		GL11.glPushMatrix();

		for (int l = 0; l < 8; l++) {

			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			if (l == 4 || l == 5) {
				GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
			} else if (l > 5) {
				GL11.glRotatef(90.0f, 0.0f, 0.0f, -1.0f);
			} else {
				GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
			}

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

			tessellator.startDrawingQuads();

			tessellator.setColorRGBA_F(1.0F, 0.6F, 0.6F, 0.8F);

			for (int m = 0; m < ac; m++) {

				for (int n = 0; n < ac; n++) {

					tessellator.setNormal(1.0F, 1.0F, 1.0F);
					tessellator.addVertexWithUV(maxo + cos[n] * s[m], maxo + sin[n], maxo + cos[n] * c[m], 1, 1);
					tessellator.addVertexWithUV(maxo + cos[n] * s[m + 1], maxo + sin[n], maxo + cos[n] * c[m + 1], 1, 0);
					tessellator.addVertexWithUV(maxo + cos[n + 1] * s[m + 1], maxo + sin[n + 1], maxo + cos[n + 1] * c[m + 1], 0, 0);
					tessellator.addVertexWithUV(maxo + cos[n + 1] * s[m], maxo + sin[n + 1], maxo + cos[n + 1] * c[m], 0, 1);

				}

			}

			tessellator.draw();
		}

		GL11.glPopMatrix();

	}

	public static void renderQuadBezier(int accuracy, Vec3 set, Vec3 d1, Vec3 d2, Vec3 end, double size1, double size2, float alpha) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		tessellator.setColorRGBA_F(1, 1, 1, alpha);
		double s1 = size1 / 2;
		double s2 = size2 / 2;
		double r = Math.sqrt(Math.pow(s1, 2) + Math.pow(s2, 2));

		double ratio;
		Vec3 v1 = set;
		Vec3 v2 = set;
		Vec3 a0[];
		Vec3 a1[] = new Vec3[accuracy];
		double dn[];
		for (int n = 0; n < accuracy; n++) {

			ratio = ((double) n + 1.0D) / (double) accuracy;
			v1 = v2;
			v2 = MitoMath.vectorBezier(set, d1, d2, end, ratio);
			a0 = a1;
			a1 = makeQuadArray(v1, v2, s1, s2);

			renderBox(v1, v2, a1, alpha);
			if (n != 0) {
				rendercylUp(4, v1, a0, a1, r, alpha);
			}
		}

	}

	public static void renderSimpleBezier(int accuracy, Vec3 set, Vec3 d1, Vec3 d2, Vec3 end, int size, float alpha) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double r = (double) size * 0.05 / 2.0;

		double ratio;
		Vec3 v1 = set;
		Vec3 v2 = set;
		Vec3 a0[];
		Vec3 a1[] = new Vec3[accuracy];

		double dn[];
		for (int n = 0; n < accuracy; n++) {

			ratio = ((double) n + 1.0D) / (double) accuracy;
			v1 = v2;
			v2 = MitoMath.vectorBezier(set, d1, d2, end, ratio);
			a0 = a1;
			a1 = makeCircleArray(v1, v2, 12, r);

			renderCyllinder(12, v1, v2, a1, r, alpha);
			if (n != 0) {
				rendercylUp(12, v1, a0, a1, r, alpha);
			}
		}

	}

	public static Vec3[] makeQuadArray(Vec3 v1, Vec3 v2, double size1, double size2) {

		Vec3[] a = new Vec3[4];

		double x1 = v1.xCoord - v2.xCoord;
		double y1 = v1.yCoord - v2.yCoord;
		double z1 = v1.zCoord - v2.zCoord;
		double length = MitoMath.abs(x1, y1, z1);
		Vec3 b;
		Vec3 c;
		Vec3 a0;

		if (length < 0) {
			return null;
		}

		double xabs = Math.abs(x1);
		double yabs = Math.abs(y1);
		double zabs = Math.abs(z1);

		if (xabs < 0.001 && zabs < 0.001) {

			a[0] = Vec3.createVectorHelper(size1, 0, size2);
			a[3] = Vec3.createVectorHelper(-size1, 0, size2);
			a[2] = Vec3.createVectorHelper(-size1, 0, -size2);
			a[1] = Vec3.createVectorHelper(size1, 0, -size2);

		} else {

			b = MitoMath.unitVector(Vec3.createVectorHelper(z1, 0, -x1));
			b= MitoMath.vectorMul(b, size2);
			c = MitoMath.unitVector(Vec3.createVectorHelper(-x1 * y1, x1 * x1 + z1 * z1, -z1 * y1));
			c= MitoMath.vectorMul(c, size1);
			
			a[0] = MitoMath.vectorPul(b, c);
			a[3] = MitoMath.vectorPul(MitoMath.vectorMul(b, -1), c);
			a[2] = MitoMath.vectorPul(MitoMath.vectorMul(b, -1), MitoMath.vectorMul(c, -1));
			a[1] = MitoMath.vectorPul(b, MitoMath.vectorMul(c, -1));

		}

		return a;
	}

	public static Vec3[] makeCircleArray(Vec3 v1, Vec3 v2, int accuracy, double r) {

		Vec3[] a = new Vec3[accuracy];

		double x1 = v1.xCoord - v2.xCoord;
		double y1 = v1.yCoord - v2.yCoord;
		double z1 = v1.zCoord - v2.zCoord;
		double length = MitoMath.abs(x1, y1, z1);
		double ang;
		Vec3 b;
		Vec3 c;
		Vec3 a0;

		if (length < 0) {
			return null;
		}

		double xabs = Math.abs(x1);
		double yabs = Math.abs(y1);
		double zabs = Math.abs(z1);

		if (xabs < 0.001 && zabs < 0.001) {

			for (int n = 0; n < accuracy; n++) {

				ang = Math.toRadians((n + 1) * 360 / accuracy);

				a[n] = Vec3.createVectorHelper(Math.cos(ang) * r, 0, Math.sin(ang) * r);

			}

		} else {

			b = MitoMath.unitVector(Vec3.createVectorHelper(z1, 0, -x1));
			c = MitoMath.unitVector(Vec3.createVectorHelper(-x1 * y1, x1 * x1 + z1 * z1, -z1 * y1));

			for (int n = 0; n < accuracy; n++) {
				ang = Math.toRadians(n * 360 / accuracy);

				a0 = MitoMath.vectorPul(MitoMath.vectorMul(c, Math.cos(ang) * r),
						MitoMath.vectorMul(b, Math.sin(ang) * r));

				a[n] = a0;

			}
		}

		return a;
	}
	
	public static void renderBox(Vec3 v1, Vec3 v2, Vec3[] a1, float alpha) {
		Tessellator tessellator = Tessellator.instance;

		double x1 = v1.xCoord - v2.xCoord;
		double y1 = v1.yCoord - v2.yCoord;
		double z1 = v1.zCoord - v2.zCoord;
		double length = MitoMath.abs(x1, y1, z1);

		float maxU = (float) 16;
		float maxV = (float) (length <= 16 ? length : 16);
		
		double xs = v2.xCoord;
		double xe = v1.xCoord;
		double ys = v2.yCoord;
		double ye = v1.yCoord;
		double zs = v2.zCoord;
		double ze = v1.zCoord;

		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(1, 1, 1, alpha);

		for (int n = 0; n < 4; n++) {

			double x3 = a1[n].xCoord;
			double y3 = a1[n].yCoord;
			double z3 = a1[n].zCoord;
			double x4 = n == 4 - 1 ? a1[0].xCoord : a1[n + 1].xCoord;
			double y4 = n == 4 - 1 ? a1[0].yCoord : a1[n + 1].yCoord;
			double z4 = n == 4 - 1 ? a1[0].zCoord : a1[n + 1].zCoord;

			double u1 = maxU * n / 4;
			double u2 = maxU * (n + 1) / 4 + 1;

			tessellator.setNormal((float) (x3 + x4) * 5, (float) (y3 + y4) * 5, (float) (z3 + z4) * 5);
			tessellator.addVertexWithUV(xs + x3, ys + y3, zs + z3, u1, maxV);
			tessellator.addVertexWithUV(xe + x3, ye + y3, ze + z3, u1, 0);
			tessellator.addVertexWithUV(xe + x4, ye + y4, ze + z4, u2, 0);
			tessellator.addVertexWithUV(xs + x4, ys + y4, zs + z4, u2, maxV);

		}

		tessellator.draw();

	}

	public static void renderCyllinder(int accuracy, Vec3 v1, Vec3 v2, Vec3[] a1, double r, float alpha) {
		Tessellator tessellator = Tessellator.instance;

		double x1 = v1.xCoord - v2.xCoord;
		double y1 = v1.yCoord - v2.yCoord;
		double z1 = v1.zCoord - v2.zCoord;
		double length = MitoMath.abs(x1, y1, z1);

		float maxU = (float) (r * 3 <= 16 ? r * 3 : 16);
		float maxV = (float) (length <= 16 ? length : 16);

		double xs;
		double xe;
		double ys;
		double ye;
		double zs;
		double ze;

		xs = v2.xCoord;
		xe = v1.xCoord;
		ys = v2.yCoord;
		ye = v1.yCoord;
		zs = v2.zCoord;
		ze = v1.zCoord;

		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(1, 1, 1, alpha);

		for (int n = 0; n < accuracy; n++) {

			double x3 = a1[n].xCoord;
			double y3 = a1[n].yCoord;
			double z3 = a1[n].zCoord;
			double x4 = n == accuracy - 1 ? a1[0].xCoord : a1[n + 1].xCoord;
			double y4 = n == accuracy - 1 ? a1[0].yCoord : a1[n + 1].yCoord;
			double z4 = n == accuracy - 1 ? a1[0].zCoord : a1[n + 1].zCoord;

			double u1 = maxU * n / accuracy;
			double u2 = maxU * (n + 1) / accuracy + 1;

			tessellator.setNormal((float) (x3 / r), (float) (y3 / r), (float) (z3 / r));
			tessellator.addVertexWithUV(xs + x3, ys + y3, zs + z3, u1, maxV);
			tessellator.setNormal((float) (x3 / r), (float) (y3 / r), (float) (z3 / r));
			tessellator.addVertexWithUV(xe + x3, ye + y3, ze + z3, u1, 0);
			tessellator.setNormal((float) (x4 / r), (float) (y4 / r), (float) (z4 / r));
			tessellator.addVertexWithUV(xe + x4, ye + y4, ze + z4, u2, 0);
			tessellator.setNormal((float) (x4 / r), (float) (y4 / r), (float) (z4 / r));
			tessellator.addVertexWithUV(xs + x4, ys + y4, zs + z4, u2, maxV);

		}

		tessellator.draw();

	}

	public static void rendercylUp(int accuracy, Vec3 v1, Vec3[] a0, Vec3[] a1, double r, float alpha) {

		Tessellator tessellator = Tessellator.instance;

		double xe = v1.xCoord;
		double ye = v1.yCoord;
		double ze = v1.zCoord;

		double x5;
		double y5;
		double z5;
		double x6;
		double y6;
		double z6;

		tessellator.startDrawing(8);
		tessellator.setColorRGBA_F(1, 1, 1, alpha);

		for (int n = 0; n < accuracy + 1; n++) {

			x5 = a0[n == accuracy ? 0 : n].xCoord;
			y5 = a0[n == accuracy ? 0 : n].yCoord;
			z5 = a0[n == accuracy ? 0 : n].zCoord;
			x6 = a1[n == accuracy ? 0 : n].xCoord;
			y6 = a1[n == accuracy ? 0 : n].yCoord;
			z6 = a1[n == accuracy ? 0 : n].zCoord;

			tessellator.setNormal((float) (x5 / r), (float) (y5 / r), (float) (z5 / r));
			tessellator.addVertexWithUV(xe + x6, ye + y6, ze + z6, 4, 4);
			tessellator.addVertexWithUV(xe + x5, ye + y5, ze + z5, 0, 4);

		}

		tessellator.draw();

	}

}
