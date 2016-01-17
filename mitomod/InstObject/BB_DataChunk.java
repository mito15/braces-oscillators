package com.mito.mitomod.InstObject;

import java.util.ArrayList;
import java.util.List;

import com.mito.mitomod.common.mitoLogger;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BB_DataChunk {

	public List<BraceBase> list = new ArrayList<BraceBase>();
	public World world;
	public boolean isDead = false;
	public final int xPosition;
	public final int zPosition;

	public BB_DataChunk(World w, Chunk c) {
		this.world = w;
		this.xPosition = c.xPosition;
		this.zPosition = c.zPosition;
	}

	public BB_DataChunk(World w, int i, int j) {
		this.world = w;
		this.xPosition = i;
		this.zPosition = j;
	}

	public int getLength() {
		return list.size();
	}

	public void removeFO(BraceBase base) {
		list.remove(base);
	}

	public boolean addFixedObj(BraceBase base) {
		int i = MathHelper.floor_double(base.pos.xCoord / 16.0D);
		int j = MathHelper.floor_double(base.pos.zCoord / 16.0D);

		if (i != this.xPosition || j != this.zPosition) {
			mitoLogger.warn("Wrong location! " + base + " (at " + i + ", " + j + " instead of " + this.xPosition + ", " + this.zPosition + ")");
			return false;
		}
		base.datachunk = this;
		return this.list.add(base);
	}

	public void setDead() {
		this.isDead = true;
	}

	public void getEntitiesWithinAABBForEntity(AxisAlignedBB boundingBox, ArrayList arraylist) {

		for (int l = 0; l < list.size(); ++l) {
			BraceBase base = (BraceBase) list.get(l);
			if (base.interactWithAABB(boundingBox)) {
				arraylist.add(base);
			}
		}

	}

}
