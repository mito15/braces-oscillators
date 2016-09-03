package com.mito.mitomod.BraceBase;

import java.util.ArrayList;
import java.util.List;

import com.mito.mitomod.BraceBase.BB_PacketProcessor.Mode;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.entity.EntityWrapperBB;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class BB_DataWorld {

	public EntityWrapperBB wrapper;
	public IntHashMap BBIDMap = new IntHashMap();
	private int cooltime = 0;
	public List<BraceBase> braceBaseList = new ArrayList<BraceBase>();
	public LongHashMap coordToDataMapping = new LongHashMap();
	public World world;
	private double MAX_ENTITY_RADIUS = 100;
	public BB_BindHelper bindhelper = new BB_BindHelper(this);
	private int debug;

	/*public BB_DataWorld() {
	}*/

	public BB_DataWorld(World world) {
		this.world = world;
		this.wrapper = new EntityWrapperBB(world, null);
	}

	//boolean b はチャンクに追加するかどうか

	public boolean addBraceBase(BraceBase base, boolean b) {
		this.BBIDMap.addKey(base.BBID, base);
		if (b) {

			int i = MathHelper.floor_double(base.pos.xCoord / 16.0D);
			int j = MathHelper.floor_double(base.pos.zCoord / 16.0D);

			BB_DataChunk datachunk = BB_DataLists.getChunkData(world, i, j);

			if (!this.braceBaseList.add(base)) {

				mitoLogger.info("can not add worldlist");
				return false;
			}

			if (!datachunk.addBraceBase(base)) {
				this.braceBaseList.remove(base);
				return false;
			}

			return true;
		} else {
			return this.braceBaseList.add(base);
		}
	}

	public boolean removeBrace(BraceBase base) {
		this.BBIDMap.removeObject(base.BBID);
		mitoLogger.info("delete phase 4.5 server " + this.braceBaseList.size());
		if (base.datachunk != null) {
			mitoLogger.info("delete phase 5 server " + this.toString());
			int i = MathHelper.floor_double(base.pos.xCoord / 16.0D);
			int j = MathHelper.floor_double(base.pos.zCoord / 16.0D);
			BB_DataChunk chunkdata = base.datachunk;
			if (chunkdata != null) {
				chunkdata.removeBrace(base);
				mitoLogger.info("delete phase 5.5 server " + chunkdata.toString());
			}
		}
		boolean ret = braceBaseList.remove(base);
		if (!this.world.isRemote) {
			mitoLogger.info("delete phase 6 server   (id : " + base.BBID);
			PacketHandler.INSTANCE.sendToAll(new BB_PacketProcessor(Mode.DELETE, base));
		}
		mitoLogger.info("delete phase 6.5 server   (id : " + ret);

		return ret;
	}

	public BraceBase getBraceBaseByID(int id) {
		return (BraceBase) this.BBIDMap.lookup(id);
	}

	public void onUpDate() {
		debug++;
		if (debug == 20 && this.world.provider.dimensionId == 0) {
			//mitoLogger.info("on update " + this.braceBaseList.size());
			//mitoLogger.info("" + this.bindhelper.bindingMap.size());
			debug = 0;
		}
		for (int n = 0; n < this.braceBaseList.size(); n++) {
			BraceBase base = this.braceBaseList.get(n);
			if (base.isDead) {
				mitoLogger.info("delete phase 4 server " + this.toString());
				this.removeBrace(base);
				//mitoLogger.warn("delete!!!!!!!!!!!!!!!!!");
				continue;
			}
			base.onUpdate();
		}
	}

	public void removeDataChunk(BB_DataChunk d) {
		this.coordToDataMapping.remove(ChunkCoordIntPair.chunkXZ2Int(d.xPosition, d.zPosition));
	}

	public List<BraceBase> getBraceBaseWithAABB(AxisAlignedBB boundingBox) {
		ArrayList<BraceBase> arraylist = new ArrayList<BraceBase>();
		int i = MathHelper.floor_double((boundingBox.minX - MAX_ENTITY_RADIUS) / 16.0D);
		int j = MathHelper.floor_double((boundingBox.maxX + MAX_ENTITY_RADIUS) / 16.0D);
		int k = MathHelper.floor_double((boundingBox.minZ - MAX_ENTITY_RADIUS) / 16.0D);
		int l = MathHelper.floor_double((boundingBox.maxZ + MAX_ENTITY_RADIUS) / 16.0D);

		for (int i1 = i; i1 <= j; ++i1) {
			for (int j1 = k; j1 <= l; ++j1) {
				if (BB_DataLists.isChunkExist(world, i1, j1)) {
					BB_DataLists.getChunkData(world, i1, j1).getEntitiesWithinAABBForEntity(boundingBox, arraylist);
				}
			}
		}

		return arraylist;
	}

}
