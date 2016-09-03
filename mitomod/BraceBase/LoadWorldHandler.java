package com.mito.mitomod.BraceBase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mito.mitomod.common.mitoLogger;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;

public class LoadWorldHandler {

	public static LoadWorldHandler INSTANCE = new LoadWorldHandler();

	public LoadWorldHandler() {
	}

	public Map getMap() {
		return BB_DataLists.worldDataMap;
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
		if (!BB_DataLists.existChunkData(chunk)) {
			return;
		}
		NBTTagCompound nbt = e.getData();
		BB_DataWorld worldData = (BB_DataWorld) this.getMap().get(world);
		if (worldData == null) {
			worldData = new BB_DataWorld(world);
			this.getMap().put(e.world, worldData);
		}
		BB_DataChunk chunkData = BB_DataLists.getChunkData(chunk);

		if (chunkData.groupList.size() == 0 || (chunkData.groupList.size() == 1 && chunkData.groupList.get(0).isEmpty())) {
			return;
		}

		NBTTagCompound nbt1;
		NBTTagList taglistBraces = new NBTTagList();
		Iterator iterator1;
		Iterator iterator2;

		NBTTagList taglistGroups = new NBTTagList();

		iterator1 = chunkData.groupList.iterator();

		while (iterator1.hasNext()) {
			BB_DataGroup group = (BB_DataGroup) iterator1.next();
			//mitoLogger.info("LoadWorldHandler.java save1");

			nbt1 = new NBTTagCompound();

			try {
				if (group.writeToNBTOptional(nbt1)) {
					NBTTagList taglistGroup = new NBTTagList();
					Map<BraceBase, Integer> BraceBaseToIntMapping = new HashMap<BraceBase, Integer>();

					for (int n = 0; n < group.list.size(); n++) {
						BraceBase fobj1 = group.list.get(n);
						BraceBaseToIntMapping.put(fobj1, new Integer(n));
					}

					for (int n = 0; n < group.list.size(); n++) {
						BraceBase fobj1 = group.list.get(n);
						NBTTagCompound nbt2 = new NBTTagCompound();
						//mitoLogger.info("LoadWorldHandler.java save2   (bbid : " + fobj1.BBID);
						if (fobj1.writeToNBTOptional(nbt2)) {
							taglistGroup.appendTag(nbt2);
							fobj1.writeNBTAssociate(nbt2, BraceBaseToIntMapping);
							//mitoLogger.info("LoadWorldHandler.java save complete   (bbid : " + fobj1.BBID);
						}
						//mitoLogger.info("save group brace");
					}
					nbt1.setTag("BB_Group", taglistGroup);

					taglistGroups.appendTag(nbt1);
				}
			} catch (Exception ex) {
				mitoLogger.warn("chunk save error on Braces&Oscillators\n");
				//ex.printStackTrace();
			}
		}

		nbt.setTag("BB_Groups", taglistGroups);

		if (/*chunkData.isDead*/!e.getChunk().isChunkLoaded) {
			Iterator iterator = chunkData.braceList.iterator();
			while (iterator.hasNext()) {
				BraceBase base = (BraceBase) iterator.next();
				base.datachunk = null;
				base.removeFromWorld();
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
		NBTTagList taglistGroups = nbt == null ? null : nbt.getTagList("BB_Groups", 10);

		if (taglistGroups != null) {
			for (int l = 0; l < taglistGroups.tagCount(); ++l) {
				NBTTagCompound nbttagcompound3 = taglistGroups.getCompoundTagAt(l);
				BB_DataGroup group = new BB_DataGroup(world, e.getChunk(), nbttagcompound3.getBoolean("single"));
				boolean flag = false;
				if (group.single) {
					group.addToWorld();
					flag = true;
				}

				NBTTagList taglistGroup = nbttagcompound3 == null ? null : nbttagcompound3.getTagList("BB_Group", 10);
				if (taglistGroup != null) {
					Map<Integer, BraceBase> IntToBraceBaseMapping = new HashMap<Integer, BraceBase>();
					Map<BraceBase, Integer> BraceBaseToIntMapping = new HashMap<BraceBase, Integer>();
					for (int n = 0; n < taglistGroup.tagCount(); ++n) {
						NBTTagCompound nbttagcompound4 = taglistGroup.getCompoundTagAt(n);
						BraceBase base = BB_ResisteredList.createBraceBaseFromNBT(nbttagcompound4, world);
						if (base != null) {
							IntToBraceBaseMapping.put(new Integer(n), base);
							BraceBaseToIntMapping.put(base, new Integer(n));
							base.addToWorld();
							group.add(base);
							//mitoLogger.info("load group brace");
						}
					}
					//bugが
					for (int n = 0; n < group.list.size(); ++n) {
						BraceBase base = group.list.get(n);
						NBTTagCompound nbttagcompound4 = taglistGroup.getCompoundTagAt(BraceBaseToIntMapping.get(base));
						if (base != null) {
							base.readNBTAssociate(nbttagcompound4, IntToBraceBaseMapping);
						}
					}
				}
				if (group.list.size() != 0 && !flag) {
					group.addToWorld();
				}
			}
		}
	}

	public void onWorldTickEvent(TickEvent.WorldTickEvent e) {
		BB_DataWorld data = BB_DataLists.getWorldData(e.world);
		data.onUpDate();
	}

	// 重複については未処理  unload -> save

	/*public void onChunkLoad(ChunkEvent.Load e) {
		//BB_DataWorld data = DataLists.getWorldData(e.world);
		//data.chunkToDataMapping.put(e.getChunk(), new FOChunkData(e.world, e.getChunk()));
	}

	public void onChunkUnload(ChunkEvent.Unload e) {
		//BB_DataChunk datachunk = BB_DataLists.getChunkData(e.getChunk());
		//datachunk.setDead();
	}*/

}
