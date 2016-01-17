package com.mito.mitomod.InstObject;

import java.util.ArrayList;
import java.util.List;

import com.mito.mitomod.InstObject.BB_PacketProcessor.Mode;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitoLogger;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class BB_DataWorld {

	// ListをMap化してIDをKeyにするべきか？
	private int cooltime = 0;
	public List<BraceBase> braceBaseList = new ArrayList<BraceBase>();
	public LongHashMap coordToDataMapping = new LongHashMap();
	public World world;
	private double MAX_ENTITY_RADIUS = 100;

	public BB_DataWorld() {
	}

	public BB_DataWorld(World world) {
		this.world = world;
	}

	//boolean b はチャンクに追加するかどうか

	public boolean addFixedObj(BraceBase base, boolean b) {

		if (b) {

			int i = MathHelper.floor_double(base.pos.xCoord / 16.0D);
			int j = MathHelper.floor_double(base.pos.zCoord / 16.0D);	

			BB_DataChunk datachunk = DataLists.getChunkData(world, i, j);

			if (!this.braceBaseList.add(base)) {

				mitoLogger.info("can not add worldlist");
				return false;
			}
			
			if(!datachunk.addFixedObj(base)){
				this.braceBaseList.remove(base);
				return false;
			}

			return true;
		} else {
			return this.braceBaseList.add(base);
		}
	}

	public boolean removeBrace(BraceBase base) {
		if (base.datachunk != null) {
			int i = MathHelper.floor_double(base.pos.xCoord / 16.0D);
			int j = MathHelper.floor_double(base.pos.zCoord / 16.0D);

			if (world.getChunkProvider().chunkExists(i, j)) {
				BB_DataChunk chunkdata = base.datachunk;
				chunkdata.removeFO(base);
			}
		}

		boolean ret = braceBaseList.remove(base);
		
		if(ret){
			PacketHandler.INSTANCE.sendToAll(new BB_PacketProcessor(Mode.DELETE, base));
		}
		
		return ret;
	}

	public BraceBase getBraceBaseByID(int id) {
		return (BraceBase) BraceBase.BBIDMap.lookup(id);
	}

	public void onUpDate() {
		for (int n = 0; n < this.braceBaseList.size(); n++) {
			BraceBase base = this.braceBaseList.get(n);
			if (base.isDead) {
				this.removeBrace(base);
				continue;
			}
			base.onUpdate();
		}
	}

	public void removeDataChunk(BB_DataChunk d) {
		this.coordToDataMapping.remove(ChunkCoordIntPair.chunkXZ2Int(d.xPosition, d.zPosition));
	}

	public List getBraceBaseWithAABB(AxisAlignedBB boundingBox) {
		ArrayList arraylist = new ArrayList();
        int i = MathHelper.floor_double((boundingBox.minX - MAX_ENTITY_RADIUS ) / 16.0D);
        int j = MathHelper.floor_double((boundingBox.maxX + MAX_ENTITY_RADIUS) / 16.0D);
        int k = MathHelper.floor_double((boundingBox.minZ - MAX_ENTITY_RADIUS) / 16.0D);
        int l = MathHelper.floor_double((boundingBox.maxZ + MAX_ENTITY_RADIUS) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1)
        {
            for (int j1 = k; j1 <= l; ++j1)
            {
                if (this.world.getChunkProvider().chunkExists(i1, j1))
                {
                    DataLists.getChunkData(world, i1, j1).getEntitiesWithinAABBForEntity(boundingBox, arraylist);
                }
            }
        }

        return arraylist;
	}

}
