package com.mito.mitomod.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileOscillatorPipeLeaf extends TileOscillatorPipe {

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.freqPec = par1NBTTagCompound.getDouble("frequency");
		this.init = par1NBTTagCompound.getBoolean("initialize");
	}

	/*
	 * こちらはNBTを書き込むメソッド。
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setDouble("frequency", this.freqPec);
		par1NBTTagCompound.setBoolean("initialize", this.init);
	}

	/*
	 * パケットの送信・受信処理。 カスタムパケットは使わず、バニラのパケット送受信処理を使用。
	 */
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

}
