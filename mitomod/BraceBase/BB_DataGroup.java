package com.mito.mitomod.BraceBase;

import java.util.ArrayList;
import java.util.List;

import com.mito.mitomod.BraceBase.Brace.Scale;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BB_DataGroup {

	public List<BraceBase> list = new ArrayList<BraceBase>();
	public World world;
	public BB_DataChunk datachunk;
	public boolean single = false;

	public BB_DataGroup(World w, BB_DataChunk d) {
		this.world = w;
		this.datachunk = d;
	}

	public BB_DataGroup(World w, Chunk c) {
		this.world = w;
		this.datachunk = BB_DataLists.getChunkData(c);
	}

	public BB_DataGroup(World w, BB_DataChunk d, boolean b) {
		this(w, d);
		single = b;
	}

	public BB_DataGroup(World w, Chunk c, boolean b) {
		this(w, c);
		single = b;
	}

	public boolean writeToNBTOptional(NBTTagCompound nbt) {
		nbt.setBoolean("single", this.single);
		nbt.setInteger("chunkX", this.datachunk.xPosition);
		nbt.setInteger("chunkZ", this.datachunk.zPosition);
		return true;
	}

	public void add(BraceBase base) {
		if(base instanceof Scale){
			return;
		}
		if (base.group != null) {
			base.group.remove(base);
		}
		base.group = this;
		this.list.add(base);
	}

	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	public void addToWorld() {
		if (this.datachunk != null) {
			this.datachunk.addGroup(this);
		}
	}

	public void remove(BraceBase base) {
		this.list.remove(base);
	}

	public void add(BraceBase base1, BraceBase base2) {
		this.add(base1);
		this.add(base2);
	}

	public void integrate(BB_DataGroup group) {
		if (group.equals(this)) {
			return;
		}
		for (int n = 0; n < group.list.size(); n++) {
			this.add(group.list.get(n));
		}
		group.list.clear();
		group.datachunk.groupList.remove(group);
	}

}
