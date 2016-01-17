package com.mito.mitomod.common.tile;

import java.util.ArrayList;
import java.util.List;

import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.entity.EntityBrace;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileBrace extends TileEntity {

	public EntityPlayer player;
	public List<Entity> listEnt = new ArrayList<Entity>();
	public List<double[]> listVec = new ArrayList<double[]>();
	public boolean selected = false;
	public boolean shouldInit = true;

	public TileBrace() {
	}

	public TileBrace(double[] m) {
		this();
		this.listVec.add(m);
		mitoLogger.info("init" + m[0] + m[1]);
	}

	@Override
	public void updateEntity() {

		if (shouldInit) {

			this.init();
			this.shouldInit = false;
		}
		
	}

	private void init() {
		if (listVec.size() == 1) {
			double[] m = listVec.get(0);
			if (m != null) {
				/** Entity entity = new EntityBrace(this.worldObj, m[0], m[1], m[2], m[3], m[4], m[5], 0.1D, 0, (byte)0);
				if (entity != null) {
					mitoLogger.info("spawn" + m[0] + m[1]);
					this.worldObj.spawnEntityInWorld(entity);
					this.listEnt.add(entity);
					this.worldObj.removeEntity(entity);
				} **/
			}
		}
	}

	protected NBTTagList newDoubleNBTList(double... p_70087_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		double[] adouble = p_70087_1_;
		int i = p_70087_1_.length;

		for (int j = 0; j < i; ++j) {
			double d1 = adouble[j];
			nbttaglist.appendTag(new NBTTagDouble(d1));
		}

		return nbttaglist;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}

	public int getMetadata() {
		return this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

}
