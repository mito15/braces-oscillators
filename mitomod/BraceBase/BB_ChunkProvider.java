package com.mito.mitomod.BraceBase;

import java.util.List;

import com.mito.mitomod.BraceBase.Brace.FakeBlock;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class BB_ChunkProvider implements IChunkProvider {

	private World world;
	private Chunk chunk;
	FakeBlock fake;

	public BB_ChunkProvider(BB_BlockAccess access, FakeBlock fake) {
		this.world = access;
		this.fake = fake;
		chunk = loadChunk(0, 0);
	}

	@Override
	public boolean chunkExists(int x, int y) {
		if(x == 0 && y == 0){
			return true;
		}
		return false;
	}

	@Override
	public Chunk provideChunk(int x, int y) {
		if(x == 0 && y == 0){
			return chunk;
		}
		return null;
	}

	@Override
	public Chunk loadChunk(int x, int y) {
		if(x == 0 && y == 0){
			return new BB_Chunk(world, fake.getBlocks(), fake.getBytes(), 0, 0);
		}
		return null;
	}

	@Override
	public void populate(IChunkProvider c, int x, int y) {
	}

	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
		return false;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return false;
	}

	@Override
	public String makeString() {
		return null;
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
		return null;
	}

	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int p_82695_1_, int p_82695_2_) {
	}

	@Override
	public void saveExtraData() {
	}

}
