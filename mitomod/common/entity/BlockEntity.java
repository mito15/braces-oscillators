package com.mito.mitomod.common.entity;

import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.tile.mitoMachineTile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BlockEntity extends Entity {

	protected int containerMeta = 0;
	protected boolean allowChops = false;

	private boolean v = true;

	public int setX;
	public int setY;
	public int setZ;
	public int setD;
	public int endD;
	public int endZ;
	public int endY;
	public int endX;
	public EntityPlayer player;
	public double[][] coRecord;
	public double[][] spRecord;
	public int color = (int) Math.floor(Math.random() * 6);
	public boolean switchV = false;
	public int brightness;
	public Block block;

	public BlockEntity(World world) {
		super(world);
		preventEntitySpawning = false;
		noClip = true;
		isImmuneToFire = true;
		this.setSize(20, 20);
		double[] a = new double[3];double d = a[4];
		mitoLogger.info("blockEntity initialize");
	}

	public BlockEntity(World world, double x, double y, double z, double x1, double y1, double z1) {
		this(world);
		this.setPosition(x, y, z);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.yOffset = 0.0F;
		//this.setSize(Math.abs(x - x1) > Math.abs(z - z1) ? (float)Math.abs(x - x1) * 2 : (float)Math.abs(z - z1) * 2, (float)Math.abs(y - y1) * 2);
		//this.setSize(Math.abs(setX - endX) > Math.abs(setZ - endZ) ? (float)Math.abs(setX - endX) * 2 : (float)Math.abs(setZ - endZ) * 2, (float)Math.abs(setY - endY) * 2);
	}

	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(17, new Integer(0));
		this.dataWatcher.addObject(18, new Integer(1));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		mitoLogger.info("BlockEntity is on update");

		if (v) {

			mitoLogger.info("try to setup. x:" + posX + " y:" + posY + " z:" + posZ);

			mitoMachineTile tile1 = (mitoMachineTile) this.worldObj.getTileEntity((int) this.posX, (int) this.posY,
					(int) this.posZ);

			if (tile1 != null) {

				mitoLogger.info("tile on me is not null");

				mitoMachineTile tile2 = (mitoMachineTile) this.worldObj.getTileEntity(tile1.getX(), tile1.getY(),
						tile1.getZ());
				if (tile2 != null) {

					mitoLogger.info("tile pair is not null");
					tile1.cable = this;
					tile2.cable = this;

					this.setX = tile1.xCoord;
					this.setY = tile1.yCoord;
					this.setZ = tile1.zCoord;
					this.setD = tile1.getDirect();

					this.endX = tile2.xCoord;
					this.endY = tile2.yCoord;
					this.endZ = tile2.zCoord;
					this.endD = tile2.getDirect();
					
					this.block = this.worldObj.getBlock(setX, setY, setZ);
					this.brightness = block.getMixedBrightnessForBlock(worldObj, (int)(setX+endX)/2, (int)(setY+endY)/2 - 2, (int)(setZ+endZ)/2);

					mitoLogger.info("setup on initialize!!");

				}
				if (!worldObj.isRemote && !tile1.getSwitch()) {

					mitoLogger.info("initialize and remove!!cuz tile is not activated");

					worldObj.removeEntity(this);

				}

			} else if (!worldObj.isRemote) {

				mitoLogger.info("initialize and remove!!cuz tile is null");

				worldObj.removeEntity(this);

			}

			v = false;
		}
		int rand = (int) Math.floor(Math.random() * 100);
		
		if (rand == 0) {this.block = this.worldObj.getBlock(setX, setY, setZ);}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {

		this.setContainerMeta(nbt.getShort("meta"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {

		nbt.setShort("meta", (short) this.getItemMetadata());
	}

	public int getItemMetadata() {
		return this.dataWatcher.getWatchableObjectInt(17);
	}

	public void setContainerMeta(int m) {
		this.containerMeta = m;
		this.dataWatcher.updateObject(17, Integer.valueOf(m));
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double par1, double par3, double par5) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.3F;
	}

	@SideOnly(Side.CLIENT)
	public void func_70270_d(boolean par1) {
	}

	@SideOnly(Side.CLIENT)
	protected void generateRandomParticles(byte b) {

	}

}
