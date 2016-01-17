package com.mito.mitomod.common.block;

import java.util.Random;

import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.tile.TileBrace;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrace extends BlockContainer {

	boolean hasEntity;
	public double[] messenger = new double[6];

	public BlockBrace() {
		super(Material.rock);
		setHardness(1.5F);/*硬さ*/
		setResistance(1.0F);/*爆破耐性*/
		setStepSound(Block.soundTypeMetal);
		setLightLevel(0.8F);/*明るさ 1.0F = 15*/
		setLightOpacity(1);
		this.hasEntity = false;
	}

	public BlockBrace(boolean b) {
		this();
		this.hasEntity = b;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
		//ブロックを右クリックした際の動作
		return true;
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		//ブロックを左クリックした際の動作
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {

		super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
		//周囲のブロックが更新された際の動作
	}

	@Override
	public int quantityDropped(Random random) {
		//ドロップさせる量を返す
		return 1;
	}

	public TileEntity createNewTileEntity(World world, int a) {
		if (this.hasEntity) {
			mitoLogger.info();
			return new TileBrace(this.messenger);
		}
		return null;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

}
