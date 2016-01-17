package com.mito.mitomod.InstObject;

import java.util.Iterator;
import java.util.Map;

import com.mito.mitomod.common.mitoLogger;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class LoadWorldHandler {

	public static LoadWorldHandler INSTANCE = new LoadWorldHandler();

	public LoadWorldHandler() {
	}

	public Map getMap() {
		return DataLists.worldDataMap;
	}

	public void onLoadWorld(WorldEvent.Load e) {
		this.getMap().put(e.world, new BB_DataWorld(e.world));
	}

	public void onUnloadWorld(WorldEvent.Unload e) {
		this.getMap().remove(e.world);
	}

	public void onUpdate(TickEvent.ServerTickEvent e) {
	}

	public void onChunkDataSave(ChunkDataEvent.Save e) {
		World world = e.world;
		Chunk chunk = e.getChunk();
		NBTTagCompound nbt = e.getData();
		BB_DataWorld worldData = (BB_DataWorld) this.getMap().get(world);
		if (worldData == null) {
			worldData = new BB_DataWorld(world);
			this.getMap().put(e.world, worldData);
		}
		BB_DataChunk chunkData = DataLists.getChunkData(chunk);

		NBTTagCompound nbt1;
		NBTTagList nbttaglist = new NBTTagList();
		Iterator iterator1;

		iterator1 = chunkData.list.iterator();
		boolean flag = false;

		while (iterator1.hasNext()) {
			BraceBase fobj = (BraceBase) iterator1.next();
			nbt1 = new NBTTagCompound();
			flag = true;

			try {
				if (fobj.writeToNBTOptional(nbt1)) {
					nbttaglist.appendTag(nbt1);
				}
			} catch (Exception ex) {
				mitoLogger.warn("chunk save error on Braces&Oscillators");
			}
		}

		nbt.setTag("Braces", nbttaglist);
		if (flag)
			mitoLogger.info("tagcount is " + nbt.getTagList("Braces", 10).tagCount());

		if (chunkData.isDead) {
			Iterator iterator = chunkData.list.iterator();
			while (iterator.hasNext()) {
				BraceBase fobj = (BraceBase) iterator.next();
				fobj.datachunk = null;
				fobj.removeFromWorld();
			}
			worldData.removeDataChunk(chunkData);
		}

	}

	public void onChunkDataLoad(ChunkDataEvent.Load e) {
		World world = e.world;
		BB_DataWorld worldData = (BB_DataWorld) this.getMap().get(world);
		if (worldData == null) {
			worldData = new BB_DataWorld(world);
			this.getMap().put(e.world, worldData);
		}
		NBTTagCompound nbt = e.getData();
		NBTTagList nbttaglist = nbt == null ? null : nbt.getTagList("Braces", 10);

		if (nbttaglist != null) {
			for (int l = 0; l < nbttaglist.tagCount(); ++l) {
				NBTTagCompound nbttagcompound3 = nbttaglist.getCompoundTagAt(l);
				BraceBase fobj = BB_ResisteredList.createBraceBaseFromNBT(nbttagcompound3, world);
				if (fobj != null) {
					fobj.addToWorld();
				}
			}
		} else {
			mitoLogger.info();
		}

	}

	public void onWorldTickEvent(TickEvent.WorldTickEvent e) {
		BB_DataWorld data = DataLists.getWorldData(e.world);

		//mitoLogger.info(""+e.world.toString());
		data.onUpDate();

	}

	// 重複については未処理  unload -> save

	public void onChunkLoad(ChunkEvent.Load e) {
		//BB_DataWorld data = DataLists.getWorldData(e.world);
		//data.chunkToDataMapping.put(e.getChunk(), new FOChunkData(e.world, e.getChunk()));
	}

	public void onChunkUnload(ChunkEvent.Unload e) {
		BB_DataChunk datachunk = DataLists.getChunkData(e.getChunk());
		datachunk.setDead();
	}

}
