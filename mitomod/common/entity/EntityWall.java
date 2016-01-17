package com.mito.mitomod.common.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.utilities.MitoMath;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityWall extends Entity {

	private final Random random = new Random();
	public Vec3 rand = Vec3.createVectorHelper(Math.random() * 0.002 - 0.001, Math.random() * 0.002 - 0.001, Math.random() * 0.002 - 0.001);
	public int brightness;
	public boolean shouldInit = true;
	public int tex = 0;
	//1:透明化フラグ 2:消去色フラグ
	public byte flags;
	public EntityBrace setEntity;
	public EntityBrace endEntity;
	UUID setUUID, endUUID;
	//client
	public int clientInit;
	public Vec3 v1, v2, v3, v4;
	public boolean shouldDivine = false;
	public boolean ref = false;

	public EntityWall(World world) {
		super(world);
		isImmuneToFire = true;
		this.noClip = true;
		this.motionX = 0.0;
		this.motionY = 0.0;
		this.motionZ = 0.0;
		//this.setSize(Math.abs(x - x1) > Math.abs(z - z1) ? (float)Math.abs(x - x1) * 2 : (float)Math.abs(z - z1) * 2, (float)Math.abs(y - y1) * 2);
	}

	public EntityWall(World world, EntityBrace e1, EntityBrace e2, int tex) {
		this(world);
		double x = (e1.posX + e2.posX) / 2;
		double y = (e1.posY + e2.posY) / 2;
		double z = (e1.posZ + e2.posZ) / 2;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		isImmuneToFire = true;
		this.setSize((float) 0.1, (float) 0.1);
		this.setPosition(x, y, z);
		this.boundingBox.setBounds(x - 0.1, y - 0.1, z - 0.1, x + 0.1, y + 0.1, z + 0.1);
		this.tex = tex;
		this.setEntity = e1;
		this.endEntity = e2;
		//this.setSize(Math.abs(x - x1) > Math.abs(z - z1) ? (float)Math.abs(x - x1) * 2 : (float)Math.abs(z - z1) * 2, (float)Math.abs(y - y1) * 2);
	}

	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return p_70114_1_.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		double s = 0.05;
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(this.posX - s / 2, this.posY - s / 2, this.posZ - s / 2, this.posX + s / 2, this.posY + s / 2, this.posZ + s / 2);

		return aabb;
	}

	public boolean canAttackWithItem() {
		return false;
	}

	public boolean hitByEntity(Entity p_85031_1_) {
		return false;
	}

	@Override
	protected void entityInit() {
		//isServerInit
		this.dataWatcher.addObject(19, new Byte((byte) 0));
		//texture color
		this.dataWatcher.addObject(28, new Integer(0));
		this.dataWatcher.addObject(29, new Integer(0));
		this.dataWatcher.addObject(30, new Integer(0));
	}

	public void dataReadFlags() {

		this.flags = this.dataWatcher.getWatchableObjectByte(30);
	}

	public void dataWriteFlags() {

		this.dataWatcher.updateObject(30, this.flags);
	}

	public void dataRead() {

		this.tex = this.dataWatcher.getWatchableObjectInt(28);
		this.setEntity = (EntityBrace) worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(29));
		this.endEntity = (EntityBrace) worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(30));
	}

	public void dataWrite() {
		this.dataWatcher.updateObject(28, this.tex);
		this.dataWatcher.updateObject(29, this.setEntity.getEntityId());
		this.dataWatcher.updateObject(30, this.endEntity.getEntityId());
	}

	@Override
	public void onUpdate() {
		//super.onUpdate();
		//this.setDead();

		if (!this.worldObj.isRemote) {

			if (this.shouldInit) {

				//UUIDからEntityを取得
				if (this.setEntity == null || this.endEntity == null) {
					List entityList = worldObj.loadedEntityList;
					EntityBrace end = null, set = null;
					for (int n = 0; n < entityList.size(); n++) {
						if (entityList.get(n) != null && entityList.get(n) instanceof EntityBrace) {
							EntityBrace entity1 = (EntityBrace) entityList.get(n);
							if (entity1.getUniqueID().equals(this.setUUID)) {
								set = entity1;
							} else if (entity1.getUniqueID().equals(this.endUUID)) {
								end = entity1;
							}
						}
					}
					this.setEntity = set;
					this.endEntity = end;
				}

				if (this.setEntity != null && this.endEntity != null) {
					this.shouldInit = false;
					this.dataWrite();
				} else {
					mitoLogger.info("entity is null");
					this.setDead();
				}
				this.dataWatcher.updateObject(19, (byte) 1);
				//this.setSizeAndPos(set.xCoord, set.yCoord, set.zCoord, end.xCoord, end.yCoord, end.zCoord, this.size);
			}

			if (this.setEntity == null || this.endEntity == null || this.setEntity.isDead || this.endEntity.isDead) {
				this.setDead();
			}

		} else {
			if (this.dataWatcher.getWatchableObjectByte(19) != this.clientInit) {
				
				this.dataRead();
				if (this.setEntity != null && this.endEntity != null && this.setEntity.getData() != null && this.endEntity.getData() != null) {
					this.clientInit = this.dataWatcher.getWatchableObjectByte(19);
					this.v1 = this.setEntity.getData().getSet();
					this.v2 = this.setEntity.getData().getEnd();
					if (MitoMath.subAbs(v1, this.endEntity.getData().getSet()) + MitoMath.subAbs(v2, this.endEntity.getData().getEnd()) > MitoMath.subAbs(v1, this.endEntity.getData().getEnd()) + MitoMath.subAbs(v2, this.endEntity.getData().getSet())) {
						this.v3 = this.endEntity.getData().getSet();
						this.v4 = this.endEntity.getData().getEnd();
						this.ref = true;
					} else {
						this.v3 = this.endEntity.getData().getEnd();
						this.v4 = this.endEntity.getData().getSet();
					}
					if (this.setEntity.isBent || this.endEntity.isBent || MitoMath.distancePointPlane(v1, v2, v3, v4) > 0.1D) {
						this.shouldDivine = true;
					}
				}

				//this.setSizeAndPos(set.xCoord, set.yCoord, set.zCoord, end.xCoord, end.yCoord, end.zCoord, this.size);
			}
		}
	}

	public void setSizeAndPos(double x, double y, double z, double x1, double y1, double z1, int s) {

		double size = (double) s * 0.05;
		float xs = (float) (Math.abs(x - x1) + size);
		float ys = (float) (Math.abs(y - y1) + size);
		float zs = (float) (Math.abs(z - z1) + size);

		this.yOffset = ys / 2.0F;
		this.setSize(xs > zs ? xs : zs, ys);
		this.setPosition((x + x1) / 2, (y + y1) / 2, (z + z1) / 2);

		this.renderDistanceWeight = 1.0D + 0.02 / (size * size);

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.tex = nbt.getInteger("texture");
		this.setUUID = UUID.fromString(nbt.getString("setEntity"));
		this.endUUID = UUID.fromString(nbt.getString("endEntity"));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setString("setEntity", this.setEntity.getUniqueID().toString());
		nbt.setString("endEntity", this.endEntity.getUniqueID().toString());
		nbt.setInteger("texture", this.tex);
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
		dropItem();
		this.setDead();
	}

	public void dropItem() {

		/**
		float f = this.random.nextFloat() * 0.2F + 0.1F;
		float f1 = this.random.nextFloat() * 0.2F + 0.1F;
		float f2 = this.random.nextFloat() * 0.2F + 0.1F;
		int isize1 = 0;
		
		if (Math.abs(this.size - 0.05) < 0.01)
			isize1 = 0;
		if (Math.abs(this.size - 0.1) < 0.01)
			isize1 = 1;
		if (Math.abs(this.size - 0.2) < 0.01)
			isize1 = 2;
		if (Math.abs(this.size - 0.4) < 0.01)
			isize1 = 3;
		if (Math.abs(this.size - 1.0) < 0.01)
			isize1 = 4;
		
		ItemBrace brace = (ItemBrace) mitomain.ItemBrace;
		ItemStack itemstack1 = new ItemStack(mitomain.ItemBrace, 1, this.tex);
		brace.setSize(itemstack1, isize1);
		brace.setType(itemstack1, this.type);
		
		NBTTagCompound nbt = itemstack1.getTagCompound();
		itemstack1.setTagCompound(nbt);
		nbt.setBoolean("activated", false);
		nbt.setDouble("setX", 0.0D);
		nbt.setDouble("setY", 0.0D);
		nbt.setDouble("setZ", 0.0D);
		nbt.setBoolean("useFlag", false);
		EntityItem entityitem = new EntityItem(worldObj, (double) ((float) this.posX + f), (double) ((float) this.posY + f1), (double) ((float) this.posZ + f2), itemstack1);
		
		float f3 = 0.05F;
		entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
		entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
		entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
		worldObj.spawnEntityInWorld(entityitem);
		**/
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
