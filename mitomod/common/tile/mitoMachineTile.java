package com.mito.mitomod.common.tile;

import com.mito.mitomod.common.entity.BlockEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class mitoMachineTile extends TileEntity {

	private String name = "None";
	private int direct = 0;
	private boolean shouldDrawCode = false;
	private int pairX = 0;
	private int pairY = 0;
	private int pairZ = 0;
	private int pairD = 0;
	public EntityPlayer player;
	public BlockEntity cable;
	public boolean selected = false;

	/*
	 * NBTの読み取り。
	 *
	 * このメソッドでは、NBTを介してString（文字列）を読み込んでいます。 文字列以外に、変数やboolean、ItemStackなども扱えます。
	 *
	 * NBTを使えば一時的には記録されますが、 チャンク再生成や再ログイン時にデータが消えてしまいます。
	 * また、このクラスで行われた処理・サーバ側で行われた処理をクライアント側に反映させるためには、 別途パケット処理も必要です。
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.shouldDrawCode = par1NBTTagCompound.getBoolean("shouldDrawCode");
		this.pairX = par1NBTTagCompound.getInteger("pairX" + this.xCoord);
		this.pairY = par1NBTTagCompound.getInteger("pairY");
		this.pairZ = par1NBTTagCompound.getInteger("pairZ");
		this.pairD = par1NBTTagCompound.getInteger("pairD");
		this.direct = par1NBTTagCompound.getInteger("direct");

	}

	/*
	 * こちらはNBTを書き込むメソッド。
	 */
	@Override
	public void writeToNBT(NBTTagCompound NBT) {
		super.writeToNBT(NBT);
		NBT.setInteger("pairX" + this.xCoord, this.pairX);
		NBT.setInteger("pairY", this.pairY);
		NBT.setInteger("pairZ", this.pairZ);
		NBT.setInteger("pairD", this.pairD);
		NBT.setInteger("direct", this.direct);
		NBT.setBoolean("shouldDrawCode", this.shouldDrawCode);

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

	/*
	 * プレイヤー名文字列のゲッターとセッター。
	 */
	public int getX() {
		return this.pairX;
	}

	public void setX(int par1) {
		this.pairX = par1;
	}

	public int getY() {
		return this.pairY;
	}

	public void setY(int par1) {
		this.pairY = par1;
	}

	public int getZ() {
		return this.pairZ;
	}

	public void setZ(int par1) {
		this.pairZ = par1;
	}

	public int getDirectP() {
		return this.pairD;
	}

	public void setDirectP(int par1) {
		this.pairD = par1;
	}

	public int getDirect() {
		return this.direct;
	}

	public void setDirect(int par1) {
		this.direct = par1;
	}

	public boolean getSwitch() {
		return this.shouldDrawCode;
	}

	public void setSwitch(boolean par1) {
		this.shouldDrawCode = par1;
	}

	public int getMetadata() {
		return this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

}
