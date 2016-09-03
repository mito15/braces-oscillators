package com.mito.mitomod.common.entity;

import java.util.Random;

import com.mito.mitomod.BraceBase.BB_DataLists;
import com.mito.mitomod.BraceBase.BB_EnumTexture;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.BraceBase.Brace.Render.BB_TypeResister;
import com.mito.mitomod.common.BraceData;
import com.mito.mitomod.common.mitoLogger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityBrace extends EntityBraceBase {

	private final Random random = new Random();
	public int brightness, pairID0 = -1, pairID1 = -1;
	public boolean shouldInit = true;
	public EntityFake[] pair = new EntityFake[2];
	private BraceData data;
	boolean flag = false;
	//1:透明化フラグ 2:消去色フラグ
	public byte flags;
	public boolean isBent = false;

	public EntityBrace(World world) {
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

	public EntityBrace(World world, double x, double y, double z, double x1, double y1, double z1, int size, int tex, byte type) {
		this(world);
		this.prevPosX = (x + x1) / 2;
		this.prevPosY = (y + y1) / 2;
		this.prevPosZ = (z + z1) / 2;
		this.setSizeAndPos(x, y, z, x1, y1, z1, size);
		this.setData(new BraceData(this, Vec3.createVectorHelper(x, y, z), Vec3.createVectorHelper(x1, y1, z1), tex, type, size));
	}

	public EntityBrace(World world, Vec3 set, Vec3 end, int size, int tex, byte type) {
		this(world, set.xCoord, set.yCoord, set.zCoord, end.xCoord, end.yCoord, end.zCoord, size, tex, type);

	}

	public void setDead() {
		this.isDead = true;
	}

	public BraceData getData() {
		return this.data;
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		return true;
	}

	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {

		return p_70114_1_.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		/*List list;//複数AABBから成るリスト
		AxisAlignedBB[] aabbs = new AxisAlignedBB[list.size()];
		for (int n = 0; n < list.size(); n++) {
			aabbs[n] = list.get(n);
		}
		if (list.size() > 0) {
			return new MultiBoundingBox(aabbs);
		}*/
		return null;
	}

	public boolean canAttackWithItem() {
		return false;
	}

	public boolean hitByEntity(Entity p_85031_1_) {
		return false;
	}

	//{ "", "C", "noJ", "noJC ", "H", "thin1", "thin2", "thin3", "thin4" };
	private int convert(int i) {
		int[] convert = new int[] { 0, 1, 0, 1, 5, 3, 4, 0, 0, 0, 0, 0, 0 };
		return convert[i];
	}

	@Override
	public void onUpdate() {

		if (!this.worldObj.isRemote) {
			if (this.flag) {
				Brace brace = new Brace(worldObj, this.getSet(), this.getEnd(), BB_TypeResister.getFigure(BB_TypeResister.shapeList.get(convert(this.getType()))), BB_EnumTexture.IRON, this.getTexture(), (double) this.getSize() * 0.05);
				boolean flag = brace.addToWorld();
				BraceBase brace1 = BB_DataLists.getWorldData(worldObj).getBraceBaseByID(brace.BBID);
				if (brace1 != null && brace1.datachunk != null) {
					this.flag = false;
					this.setDead();
				} else {
					//BB_DataLists.getWorldData(worldObj).removeBrace(brace);
					mitoLogger.info("fail entity");
				}
			}
		}

		//if(worldObj.getTotalWorldTime() % 100 == 1)
		//mitoLogger.info(""+this.getEndCP().xCoord+this.getEnd().xCoord+this.getSetCP().xCoord);

		//super.onUpdate();
		//this.setDead();

		/*if (!this.worldObj.isRemote) {
			if (this.shouldInit) {

				this.shouldInit = false;

				if (this.pair[0] == null || this.pair[1] == null) {

					this.pair[0] = new EntityFake(this.worldObj, this.getSet().xCoord, this.getSet().yCoord, this.getSet().zCoord, this.getSize(), this);
					this.pair[1] = new EntityFake(this.worldObj, this.getEnd().xCoord, this.getEnd().yCoord, this.getEnd().zCoord, this.getSize(), this);
					this.worldObj.spawnEntityInWorld(this.pair[0]);
					this.worldObj.spawnEntityInWorld(this.pair[1]);
				}
			}
		} else {
			if (this.shouldInit) {
				this.shouldInit = false;
				PacketHandler.INSTANCE.sendToServer(new BraceSyncCall(this));
			}
			if (this.pair[0] == null && pairID0 != -1) {
				if (worldObj.getEntityByID(pairID0) != null && worldObj.getEntityByID(pairID0) instanceof EntityFake) {
					this.pair[0] = (EntityFake) worldObj.getEntityByID(pairID0);
				}
			}
			if (this.pair[1] == null && pairID1 != -1) {
				if (worldObj.getEntityByID(pairID1) != null && worldObj.getEntityByID(pairID1) instanceof EntityFake) {
					this.pair[1] = (EntityFake) worldObj.getEntityByID(pairID1);
				}
			}
		}*/
	}

	public int getSize() {
		if (this.data == null) {
			return 0;
		}
		return this.getData().getSize();
	}

	public Vec3 getSet() {
		if (this.data == null) {
			return Vec3.createVectorHelper(0, 0, 0);
		}
		return this.getData().getSet();
	}

	public Vec3 getEnd() {
		if (this.data == null) {
			return Vec3.createVectorHelper(0, 0, 0);
		}
		return this.getData().getEnd();
	}

	public void setSizeAndPos(double x, double y, double z, double x1, double y1, double z1, int s) {

		double size = (double) s * 0.05;
		float xs = (float) (Math.abs(x - x1) + size);
		float ys = (float) (Math.abs(y - y1) + size);
		float zs = (float) (Math.abs(z - z1) + size);

		this.yOffset = ys / 2.0F + 1.0F;
		this.setSize(0.2F + (xs > zs ? xs : zs), 0.2F + ys);
		this.setPosition((x + x1) / 2, (y + y1) / 2, (z + z1) / 2);

		this.renderDistanceWeight = 10.0D + 0.02 / (size * size);

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {

		this.flag = true;

		if (this.getData() == null) {
			this.setData(new BraceData());
		}
		this.getData().setSet(Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ")));
		this.getData().setEnd(Vec3.createVectorHelper(nbt.getDouble("endX"), nbt.getDouble("endY"), nbt.getDouble("endZ")));
		this.getData().setSetCP(Vec3.createVectorHelper(nbt.getDouble("setCPX"), nbt.getDouble("setCPY"), nbt.getDouble("setCPZ")));
		this.getData().setEndCP(Vec3.createVectorHelper(nbt.getDouble("endCPX"), nbt.getDouble("endCPY"), nbt.getDouble("endCPZ")));
		this.isBent = nbt.getBoolean("isBent");
		this.getData().setTexture(nbt.getInteger("texture"));
		this.getData().setType(nbt.getByte("type"));
		if (nbt.hasKey("size")) {
			this.getData().setSize((int) (nbt.getDouble("size") * 20));
		} else {
			this.getData().setSize(nbt.getInteger("isize"));
		}
		if (nbt.hasKey("isFake")) {
			if (nbt.getBoolean("isFake")) {
				this.setDead();
			}
		}
		setSizeAndPos(this.getSet(), this.getEnd(), this.data.getSize());
	}

	void setSizeAndPos(Vec3 set, Vec3 end, int size) {
		setSizeAndPos(set.xCoord, set.yCoord, set.zCoord, end.xCoord, end.yCoord, end.zCoord, size);

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {

		nbt.setDouble("setCPX", this.getData().getSetCP().xCoord);
		nbt.setDouble("setCPY", this.getData().getSetCP().yCoord);
		nbt.setDouble("setCPZ", this.getData().getSetCP().zCoord);
		nbt.setDouble("endCPX", this.getData().getEndCP().xCoord);
		nbt.setDouble("endCPY", this.getData().getEndCP().yCoord);
		nbt.setDouble("endCPZ", this.getData().getEndCP().zCoord);
		nbt.setDouble("setX", this.getData().getSet().xCoord);
		nbt.setDouble("setY", this.getData().getSet().yCoord);
		nbt.setDouble("setZ", this.getData().getSet().zCoord);
		nbt.setDouble("endX", this.getData().getEnd().xCoord);
		nbt.setDouble("endY", this.getData().getEnd().yCoord);
		nbt.setDouble("endZ", this.getData().getEnd().zCoord);
		nbt.setInteger("isize", this.getData().getSize());
		nbt.setBoolean("isBent", this.isBent);
		nbt.setInteger("texture", this.getData().getTexture());
		nbt.setByte("type", (byte) this.getData().getType());
	}

	/*public void delete(boolean b) {
		if (!this.isDead) {
			this.pair[0].setDead();
			this.pair[1].setDead();
			dropItem();
			this.setDead();
		}
	}

	public void dropItem() {

		float f = this.random.nextFloat() * 0.2F + 0.1F;
		float f1 = this.random.nextFloat() * 0.2F + 0.1F;
		float f2 = this.random.nextFloat() * 0.2F + 0.1F;

		ItemBrace brace = (ItemBrace) BAO_main.ItemBrace;
		ItemStack itemstack1 = new ItemStack(BAO_main.ItemBrace, 1, this.getData().getTexture());
		brace.setSize(itemstack1, this.getData().getSize());
		brace.setType(itemstack1, "square");

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
	}*/

	public void setSetCP(Vec3 v) {
		if (this.data == null) {
			return;
		}
		this.data.setSetCP(v);
	}

	public void setEndCP(Vec3 v) {
		if (this.data == null) {
			return;
		}
		this.data.setEndCP(v);
	}

	public void setData(BraceData data) {
		this.data = data;
	}

	public int getType() {
		if (this.data == null) {
			return 0;
		}
		return this.data.getType();
	}

	public Vec3 getSetCP() {
		if (this.data == null) {
			return Vec3.createVectorHelper(0, 0, 0);
		}
		return this.data.getSetCP();
	}

	public Vec3 getEndCP() {
		if (this.data == null) {
			return Vec3.createVectorHelper(0, 0, 0);
		}
		return this.data.getEndCP();
	}

	public int getTexture() {
		if (this.data == null) {
			return 0;
		}
		return this.data.getTexture();
	}
}
