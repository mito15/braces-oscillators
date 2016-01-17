package com.mito.mitomod.common.entity;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityBraceBase extends Entity {

	private final Random random = new Random();

	public EntityBraceBase(World world) {
		super(world);
	}

	
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		return true;
	}

	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return p_70114_1_.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
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
	}

	@Override
	public void onUpdate() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
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

}
