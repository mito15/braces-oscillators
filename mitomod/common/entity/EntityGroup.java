package com.mito.mitomod.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mito.mitomod.common.mitoLogger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityGroup extends Entity {

	private final Random random = new Random();

	public boolean shouldInit = true;
	//1:透明化フラグ 2:消去色フラグ
	public byte flags;
	public List<Entity> groupList = new ArrayList<Entity>();
	public List<UUID> uuidList = new ArrayList<UUID>();

	public EntityGroup(World world) {
		super(world);
		isImmuneToFire = true;
		this.noClip = true;
		this.motionX = 0.0;
		this.motionY = 0.0;
		this.motionZ = 0.0;
		//this.setSize(Math.abs(x - x1) > Math.abs(z - z1) ? (float)Math.abs(x - x1) * 2 : (float)Math.abs(z - z1) * 2, (float)Math.abs(y - y1) * 2);
	}

	public EntityGroup(World world, Vec3 v1, Vec3 v2) {
		this(world);
		double x = (v1.xCoord + v2.xCoord) / 2;
		double y = (v1.yCoord + v2.yCoord) / 2;
		double z = (v1.zCoord + v2.zCoord) / 2;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.setSizeAndPos(v1, v2);
		this.boundingBox.setBounds(x - 0.1, y - 0.1, z - 0.1, x + 0.1, y + 0.1, z + 0.1);

	}

	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return p_70114_1_.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		double s = 0.05;
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(this.posX - s / 2, this.posY - s / 2, this.posZ - s / 2, this.posX + s / 2, this.posY + s / 2, this.posZ + s / 2);

		return aabb;
	}

	@Override
	public void onUpdate() {
		//super.onUpdate();
		//this.setDead();

	}

	public void setSizeAndPos(Vec3 v1, Vec3 v2) {

		double offset = 0.05;
		float xs = (float) (Math.abs(v1.xCoord - v2.xCoord) + offset);
		float ys = (float) (Math.abs(v1.yCoord - v2.yCoord) + offset);
		float zs = (float) (Math.abs(v1.zCoord - v2.zCoord) + offset);

		this.yOffset = ys / 2.0F;
		this.setSize(xs > zs ? xs : zs, ys);
		this.setPosition((v1.xCoord + v2.xCoord) / 2, (v1.yCoord + v2.yCoord) / 2, (v1.zCoord + v2.zCoord) / 2);

		this.renderDistanceWeight = 1.0D + 0.02 / (offset * offset);

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		//this.tex = nbt.getInteger("texture");
		//this.setUUID = UUID.fromString(nbt.getString("setEntity"));
		////this.endUUID = UUID.fromString(nbt.getString("endEntity"));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		mitoLogger.info("read from NBT");
		super.readFromNBT(nbt);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {

		mitoLogger.info("write Entity To NBT" + this.getUniqueID().toString());
		//nbt.setString("setEntity", this.setEntity.getUniqueID().toString());
		//nbt.setString("endEntity", this.endEntity.getUniqueID().toString());
		//nbt.setInteger("texture", this.tex);
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

	@Override
	protected void entityInit() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
