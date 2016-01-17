package com.mito.mitomod.common.entity;

import java.util.Random;

import com.mito.mitomod.common.BraceData;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFake extends EntityBraceBase {

	private final Random random = new Random();
	public int brightness;
	public boolean shouldInit = true;
	public EntityBrace host;
	public BraceData data;
	public int size;
	public byte flags;
	public boolean isRuler = false;

	public EntityFake(World world) {
		super(world);
		ignoreFrustumCheck = true;
		this.renderDistanceWeight = 10.0D;
		isImmuneToFire = true;
		this.noClip = true;
		this.motionX = 0.0;
		this.motionY = 0.0;
		this.motionZ = 0.0;
		//this.setSize(Math.abs(x - x1) > Math.abs(z - z1) ? (float)Math.abs(x - x1) * 2 : (float)Math.abs(z - z1) * 2, (float)Math.abs(y - y1) * 2);
	}

	public EntityFake(World world, double x, double y, double z, int size, EntityBrace host) {
		this(world);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.size = size;
		this.setSize((float) 0.1, (float) 0.1);
		this.setPosition(x, y, z);
		this.boundingBox.setBounds(x - 0.1, y - 0.1, z - 0.1, x + 0.1, y + 0.1, z + 0.1);
		this.host = host;
		//this.setSize(Math.abs(x - x1) > Math.abs(z - z1) ? (float)Math.abs(x - x1) * 2 : (float)Math.abs(z - z1) * 2, (float)Math.abs(y - y1) * 2);
	}
	
	public EntityFake(World world, double x, double y, double z) {
		this(world);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.size = 0;
		this.setSize((float) 0.1, (float) 0.1);
		this.setPosition(x, y, z);
		this.boundingBox.setBounds(x - 0.1, y - 0.1, z - 0.1, x + 0.1, y + 0.1, z + 0.1);
		this.isRuler = true;
		//this.setSize(Math.abs(x - x1) > Math.abs(z - z1) ? (float)Math.abs(x - x1) * 2 : (float)Math.abs(z - z1) * 2, (float)Math.abs(y - y1) * 2);
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		return true;
	}

	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return p_70114_1_.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		double s = (double) this.size * 0.05;
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(this.posX - s / 2, this.posY - s / 2, this.posZ - s / 2, this.posX + s / 2, this.posY + s / 2, this.posZ + s / 2);

		return null;
	}

	public boolean canAttackWithItem() {
		return false;
	}

	public boolean hitByEntity(Entity p_85031_1_) {
		return false;
	}

	@Override
	protected void entityInit() {
		//position
		this.dataWatcher.addObject(10, new Float(0.0F));
		this.dataWatcher.addObject(11, new Float(0.0F));
		this.dataWatcher.addObject(12, new Float(0.0F));
		//isServerInit
		this.dataWatcher.addObject(19, new Byte((byte) 0));
		//size
		this.dataWatcher.addObject(26, new Integer(0));
		//flags
		this.dataWatcher.addObject(30, new Byte((byte) 0));
		//entity
		this.dataWatcher.addObject(2, new Integer(-1));
	}

	public void dataReadFlags() {

		this.flags = this.dataWatcher.getWatchableObjectByte(30);
	}

	public void dataWriteFlags() {

		this.dataWatcher.updateObject(30, this.flags);
	}

	public void dataReadEntity() {

		this.host = (EntityBrace) worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(2));
	}

	public void dataWriteEntity() {

		if(this.host != null){
		this.dataWatcher.updateObject(2, this.host.getEntityId());} else{this.dataWatcher.updateObject(2, -1);}
	}

	public void dataRead() {

		this.size = this.dataWatcher.getWatchableObjectInt(26);
		this.setPosition(this.dataWatcher.getWatchableObjectFloat(10), this.dataWatcher.getWatchableObjectFloat(11), this.dataWatcher.getWatchableObjectFloat(12));
	}

	public void dataWrite() {

		this.dataWatcher.updateObject(10, (float)this.posX);
		this.dataWatcher.updateObject(11, (float)this.posY);
		this.dataWatcher.updateObject(12, (float)this.posZ);
		this.dataWatcher.updateObject(26, this.size);
	}

	@Override
	public void onUpdate() {

		//if(worldObj.getTotalWorldTime()%20 == 1)
		//mitoLogger.info(""+worldObj.checkChunksExist((int)this.posX, (int)this.posY, (int)this.posZ, (int)this.posX, (int)this.posY, (int)this.posZ));

		//super.onUpdate();
		//this.setDead();
		
		//mitoLogger.info("");

		if (!this.worldObj.isRemote) {
			if (this.shouldInit) {
				boolean flag = true;
				this.shouldInit = false;
				this.dataWrite();

				this.dataWatcher.updateObject(19, (byte) 1);
				this.setSizeAndPos(this.size);

				if (this.host == null && !this.isRuler) {
					this.setDead();
					worldObj.removeEntity(this);
					flag = false;
				}

				if (flag) {
					this.dataWriteEntity();
				}
			}
		} else {
			if (this.dataWatcher.getWatchableObjectByte(19) == 1) {
				this.dataRead();
				this.dataReadEntity();
				this.setSizeAndPos(this.size);
			}
		}
	}

	public void setSizeAndPos(int s) {

		double size = (double) s * 0.05;

		this.yOffset = (float)size / 2.0F;
		this.setSize((float)size, (float)size);
		this.setPosition(this.posX, this.posY, this.posZ);

		this.renderDistanceWeight = 10.0D + 0.02 / (size * size);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("size")) {
			this.size = (int) (nbt.getDouble("size") * 20);
		} else {
			this.size = nbt.getInteger("isize");
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {

		nbt.setInteger("isize", this.size);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
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
		return 0.0F;
	}

	public void delete(boolean b) {
		if (!this.isDead) {
			this.host.delete(true);
			//WorldRenderer r = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.posZ);

		int k = MathHelper.floor_double(this.posY);
		int bright = this.worldObj.getLightBrightnessForSkyBlocks(i, k, j, 0);
		if (bright != 0) {
			return bright;
		} else {

			return 15728640;
		}
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float p_70013_1_) {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.posZ);

		if (this.worldObj.blockExists(i, 0, j)) {
			double d0 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
			int k = MathHelper.floor_double(this.posY - (double) this.yOffset + d0);
			return this.worldObj.getLightBrightness(i, k, j);
		} else {
			return 0.0F;
		}
	}

}
