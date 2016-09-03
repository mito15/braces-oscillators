package com.mito.mitomod.BraceBase;

import com.mito.mitomod.BraceBase.Brace.FakeBlock;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraftforge.common.util.ForgeDirection;

public class BB_BlockAccess extends World implements IBlockAccess {

	FakeBlock fake;
	public TileEntity te = null;
	public World world;

	public BB_BlockAccess(FakeBlock b) {
		super(new SaveHandlerMP(), "Brace", new BB_WorldProvider(), new WorldSettings(0L, GameType.CREATIVE, false, false, WorldType.FLAT), new Profiler());
		fake = b;
		this.chunkProvider = this.createChunkProvider();
		this.isRemote = b.worldObj.isRemote;
		this.world = b.worldObj;
	}

	@Override
	public Block getBlock(int x, int y, int z) {
		if (fake != null && fake.contain != null && x == 0 && y == 0 && z == 0) {
			return fake.contain;
		}
		return Blocks.air;
	}

	@Override
	public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_) {
		if (p_147438_2_ >= 0 && p_147438_2_ < 256) {
			TileEntity tileentity = null;
			int l;
			TileEntity tileentity1;
			/*for (l = 0; l < this.addedTileEntityList.size(); ++l) {
				tileentity1 = (TileEntity) this.addedTileEntityList.get(l);
				if (!tileentity1.isInvalid() && tileentity1.xCoord == p_147438_1_ && tileentity1.yCoord == p_147438_2_ && tileentity1.zCoord == p_147438_3_) {
					tileentity = tileentity1;
					break;
				}
			}*/
			tileentity1 = this.te;
			if (!tileentity1.isInvalid() && tileentity1.xCoord == p_147438_1_ && tileentity1.yCoord == p_147438_2_ && tileentity1.zCoord == p_147438_3_) {
				tileentity = tileentity1;
			}
			if (tileentity == null) {
				Chunk chunk = this.getChunkFromChunkCoords(p_147438_1_ >> 4, p_147438_3_ >> 4);
				if (chunk != null) {
					tileentity = chunk.func_150806_e(p_147438_1_ & 15, p_147438_2_, p_147438_3_ & 15);
				}
			}
			return tileentity;
		} else {
			return null;
		}
	}

	@Override
	public int getLightBrightnessForSkyBlocks(int x, int y, int z, int p_72802_4_) {
		int i1 = world.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, x, y, z);
		int j1 = world.getSkyBlockTypeBrightness(EnumSkyBlock.Block, x, y, z);

		if (j1 < p_72802_4_) {
			j1 = p_72802_4_;
		}

		return fake.getBrightnessForRender(1.0F);
	}

	@Override
	public int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_) {
		return fake.meta;
	}

	@Override
	public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_) {
		return 0;
	}

	@Override
	public boolean isAirBlock(int x, int y, int z) {
		if (0 == x && 0 == y && 0 == z) {
			return false;
		}
		return true;
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_) {
		return world.getBiomeGenForCoords(p_72807_1_, p_72807_2_);
	}

	@Override
	public int getHeight() {
		return world.getHeight();
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		return false;
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean d) {
		return false;
	}

	@Override
	protected IChunkProvider createChunkProvider() {
		return new BB_ChunkProvider(this, fake);
	}

	@Override
	protected int func_152379_p() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public Entity getEntityByID(int p_73045_1_) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean setBlock(int p_147465_1_, int p_147465_2_, int p_147465_3_, Block p_147465_4_, int p_147465_5_, int p_147465_6_) {
		if (p_147465_1_ >= -30000000 && p_147465_3_ >= -30000000 && p_147465_1_ < 30000000 && p_147465_3_ < 30000000) {
			if (p_147465_2_ < 0) {
				return false;
			} else if (p_147465_2_ >= 256) {
				return false;
			} else {
				Chunk chunk = this.getChunkFromChunkCoords(p_147465_1_ >> 4, p_147465_3_ >> 4);
				Block block1 = null;
				net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;

				if ((p_147465_6_ & 1) != 0) {
					block1 = chunk.getBlock(p_147465_1_ & 15, p_147465_2_, p_147465_3_ & 15);
				}

				if (this.captureBlockSnapshots && !this.isRemote) {
					blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(this, p_147465_1_, p_147465_2_, p_147465_3_, p_147465_6_);
					this.capturedBlockSnapshots.add(blockSnapshot);
				}

				boolean flag = chunk.func_150807_a(p_147465_1_ & 15, p_147465_2_, p_147465_3_ & 15, p_147465_4_, p_147465_5_);

				if (!flag && blockSnapshot != null) {
					this.capturedBlockSnapshots.remove(blockSnapshot);
					blockSnapshot = null;
				}

				this.theProfiler.startSection("checkLight");
				this.func_147451_t(p_147465_1_, p_147465_2_, p_147465_3_);
				this.theProfiler.endSection();

				if (flag && blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
				{
					// Modularize client and physic updates
					this.markAndNotifyBlock(p_147465_1_, p_147465_2_, p_147465_3_, chunk, block1, p_147465_4_, p_147465_6_);
				}

				return flag;
			}
		} else {
			return false;
		}
	}

	public void setTileEntity(int p_147455_1_, int p_147455_2_, int p_147455_3_, TileEntity tile) {
		tile.setWorldObj(this);
		this.te = tile;
	}

}
