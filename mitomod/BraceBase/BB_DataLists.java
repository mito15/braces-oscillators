package com.mito.mitomod.BraceBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class BB_DataLists {

	public static List<Integer> Incomplist = new ArrayList<Integer>();
	public static Map<WorldServer, BB_DataWorld> worldDataMap = new HashMap<WorldServer, BB_DataWorld>();

	public static BB_DataWorld getWorldData(World world) {
		if (world.isRemote) {
			return LoadClientWorldHandler.INSTANCE.data;
		} else {
			return worldDataMap.get(world);
		}
	}

	public static boolean existChunkData(Chunk chunk) {
		return getWorldData(chunk.worldObj).coordToDataMapping.containsItem(ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition));
	}

	public static BB_DataChunk getChunkData(Chunk chunk) {
		return BB_DataLists.getChunkData(chunk.worldObj, chunk.xPosition, chunk.zPosition);
	}

	public static BB_DataChunk getChunkData(World world, int i, int j) {
		BB_DataChunk ret;
		ret = (BB_DataChunk) getWorldData(world).coordToDataMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(i, j));
		if (ret == null) {
			//world.getChunkProvider().chunkExists(i, j);
			ret = BB_DataLists.newDataChunk(world, i, j);
		}
		return ret;
	}

	public static BraceBase getFixedObj(World world, int id) {
		return getWorldData(world).getBraceBaseByID(id);
	}

	public static BB_DataChunk newDataChunk(Chunk chunk) {
		return newDataChunk(chunk.worldObj, chunk.xPosition, chunk.zPosition);
	}

	public static BB_DataChunk newDataChunk(World world, int i, int j) {
		BB_DataChunk ret = new BB_DataChunk(world, i, j);
		getWorldData(world).coordToDataMapping.add(ChunkCoordIntPair.chunkXZ2Int(i, j), ret);
		return ret;
	}

	public static boolean isChunkExist(World world, int i, int j) {
		return BB_DataLists.getWorldData(world).coordToDataMapping.containsItem(ChunkCoordIntPair.chunkXZ2Int(i, j));
	}
}
