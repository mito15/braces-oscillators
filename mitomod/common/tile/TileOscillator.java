package com.mito.mitomod.common.tile;

import com.mito.mitomod.common.Direction26;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileOscillator extends TileEntity implements INeuron {

	double freqRand = Math.random() * 20.0;
	double j = 0.1;
	double ang = 0;
	double angPre = 0;
	public double freqPec = 1;
	public double freqPecRe = 1;
	public double beatSize = 1.0;
	public TileOscillator[] tileOs = new TileOscillator[6];
	public long time;
	public boolean init = true;
	public boolean init0 = true;
	double sigm;
	double randfreq = Math.sqrt(-2 * Math.log(Math.random())) * Math.cos(Math.PI * 2 * Math.random());
	double ra = Math.exp(randfreq / 1000);

	public void init() {

		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;

		for (int n = 0; n < 6; n++) {
			TileEntity tile = this.worldObj.getTileEntity(x + Direction26.offsetsXForSide[n], y + Direction26.offsetsYForSide[n], z + Direction26.offsetsZForSide[n]);
			this.tileOs[n] = (tile instanceof TileOscillator) ? (TileOscillator) tile : null;
		}

	}
	
	public double getVoltage(int time, int direction) {
		if (time == this.time) {
			return volt(this.ang);
		} else {
			return volt(this.angPre);
		}
	}
	
	double volt(double d){
		return (d >= 18) ? 1.0 : 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 409600.0D;
	}

	public double getAng(int d) {
		double ret = this.ang;
		if (this.freqPecRe == 100) {
			if (Direction26.facings[d] == "EAST") {
				ret += 2.5;
			} else if (Direction26.facings[d] == "SOUTH") {
				ret += 7.5;
			} else if (Direction26.facings[d] == "WEST") {
				ret += 12.5;
			} else if (Direction26.facings[d] == "NORTH") {
				ret += 17.5;
			}
			if (ret > 20) {
				ret = ret - 20;
			}
		}
		return ret;
	}

	public double getPreAng(int d) {
		double ret = this.angPre;
		if (this.freqPecRe == 100) {
			if (Direction26.facings[d] == "EAST") {
				ret += 2.5;
			} else if (Direction26.facings[d] == "SOUTH") {
				ret += 7.5;
			} else if (Direction26.facings[d] == "WEST") {
				ret += 12.5;
			} else if (Direction26.facings[d] == "NORTH") {
				ret += 17.5;
			}
			if (ret > 20) {
				ret = ret - 20;
			}
		}
		return ret;
	}

	@Override
	public void updateEntity() {

		if (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) {

			this.freqPecRe = this.freqPec == 1.1 ? 0.9 : this.freqPec + 0.1;

		} else {

			this.freqPecRe = this.freqPec;
		}

		this.time = worldObj.getTotalWorldTime();

		// 周りの振動子数、位相差の正弦の和
		int k = 0;
		sigm = 0;

		// 初期化処理
		if (init || init0) {

			init();
			init = false;
			init0 = false;

		}
		for (int n = 0; n < 6; n++) {
			if (tileOs[n] != null) {
				k++;
				if (tileOs[n].time != this.time) {
					sigm += Math.sin((this.ang - tileOs[n].getAng(n)) * Math.PI * 0.1);
				} else {
					sigm += Math.sin((this.ang - tileOs[n].getPreAng(n)) * Math.PI * 0.1);
				}
			}

		}
		

		double freq = k == 0 ? this.freqPecRe : this.freqPecRe - j / k * sigm;
		this.angPre = this.ang;
		this.ang += freq;

		if (this.ang > 20) {
			this.ang = 0;
		}

		// tから大きさを算出
		double tRate = (double) ang / (double) 20;
		this.beatSize = tRate == 1 ? 0.8 : 0.3 + Math.cos(tRate * Math.PI / 2) * 0.4;
		// mitoLogger.info(""+this.freq);
	}

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
