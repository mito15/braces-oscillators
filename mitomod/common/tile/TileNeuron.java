package com.mito.mitomod.common.tile;

import com.mito.mitomod.common.Direction26;
import com.mito.mitomod.common.mitoLogger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileNeuron extends TileEntity implements INeuron {

	double freqRand = Math.random() * 20.0;
	double a = 0.7;
	double b = 0.8;
	double c = 0.2;
	double g = 3.0;

	double membVol = -0.86;
	double membVolPre = -0.86;

	double linRec = 0;
	double linRecPre = 0;

	public double beatSize = 1.0;
	public INeuron[] tileOs = new INeuron[6];
	public double[] weightOut = new double[6];
	public int time;
	public boolean init = true;
	public boolean init0 = true;

	public void init() {

		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;

		for (int n = 0; n < 6; n++) {
			TileEntity tile = this.worldObj.getTileEntity(x + Direction26.offsetsXForSide[n], y + Direction26.offsetsYForSide[n], z + Direction26.offsetsZForSide[n]);
			if (this.tileOs[n] == null || tile instanceof INeuron || !this.tileOs[n].equals(tile)) {
				this.weightOut[n] = 1.0;
				this.tileOs[n] = (INeuron) tile;
			}
		}

	}

	public double getVoltage(int time, int direction) {
		if (time == this.time) {
			return volt(this.membVol) * this.weightOut[direction];
		} else {
			return volt(this.membVolPre) * this.weightOut[direction];
		}
	}
	
	double volt(double d){
		return (d >= -0.1) ? 1.0 : 0;
	}

	@Override
	public void updateEntity() {
		this.time = (int) (worldObj.getTotalWorldTime() & 3);

		if (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) {
		}

		// 周りの振動子数、位相差の正弦の和
		double stimulus = 0;

		// 初期化処理
		if (init || init0) {

			init();
			init = false;
			init0 = false;

		}
		for (int n = 0; n < 6; n++) {
			if (tileOs[n] != null) {
				double volt = tileOs[n].getVoltage(this.time, Direction26.oppositeSide[n]);
				stimulus = volt > stimulus ? volt : stimulus;
			}
		}

		double freq = membVol - Math.pow(membVol, 3) - this.linRec + stimulus;
		this.membVolPre = this.membVol;
		this.linRecPre = this.linRec;
		this.membVol += freq * c;
		this.linRec += (membVol + a - b * this.linRec) / g;

		// tから大きさを算出
		this.beatSize = membVol + 1.2;
		mitoLogger.info(""+this.membVol + " : " + this.linRec);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.init = nbt.getBoolean("initialize");
		this.membVol = nbt.getDouble("membrane");
		this.linRec = nbt.getDouble("recovery");
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("initialize", this.init);
		nbt.setDouble("membrane", this.membVol);
		nbt.setDouble("recovery", this.linRec);
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

}
