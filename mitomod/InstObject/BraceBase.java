package com.mito.mitomod.InstObject;

import java.util.UUID;

import com.mito.mitomod.InstObject.BB_PacketProcessor.Mode;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.utilities.Line;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class BraceBase {

	public static int nextID;
	public static IntHashMap BBIDMap = new IntHashMap();

	public boolean isDead = false;
	public int frequency;
	public boolean isStatic;
	public Vec3 pos;
	//public Vec3 motion;
	public World worldObj;
	public UUID uuid;
	public int fire;
	public int BBID;
	public NBTTagCompound customFixedObjectData;

	public VBOHandler buffer;

	public double rotationYaw = 360;

	public double rotationPitch = 360;

	public double rotationRoll = 360;

	public Vec3 prevPos;

	public Vec3 lastTickPos;

	//public float prevRotationYaw;

	//public float prevRotationPitch;

	public BB_DataWorld dataworld;
	public BB_DataChunk datachunk;
	public boolean shouldUpdate = true;

	public BraceBase(World world, Vec3 pos) {
		this(world);
		this.pos = pos;
	}

	public BraceBase(World world) {
		this.worldObj = world;
		this.BBID = nextID++;
		this.dataworld = DataLists.getWorldData(worldObj);
		if (dataworld == null) {
			mitoLogger.warn("bracebase data world is null !!!!!!!!!!!!!!\n\n\n\n\n\n\n\n\n\n\n\n\n");
		}
		BBIDMap.addKey(this.BBID, this);
		this.frequency = 100;
		this.isStatic = true;
	}

	public boolean addToWorld() {
		if (worldObj == null) {
			return false;
		}

		if (worldObj.isRemote) {
			return dataworld.addFixedObj(this, true);
		} else {

			boolean ret;

			if (pos != null) {
				ret = dataworld.addFixedObj(this, true);
			} else {
				ret = dataworld.addFixedObj(this, false);
			}

			if (ret) {

				PacketHandler.INSTANCE.sendToAll(new BB_PacketProcessor(Mode.SUGGEST, this));
			}

			return ret;
		}
	}

	public boolean removeFromWorld() {
		BBIDMap.removeObject(this.BBID);

		return dataworld.removeBrace(this);

	}

	public void setId(int id) {
		if (id != this.BBID) {
			BBIDMap.removeObject(this.BBID);
			this.BBID = id;
			BBIDMap.addKey(this.BBID, this);
		} else {
			return;
		}
	}

	public void setDead() {
		this.isDead = true;
	}

	public void onUpdate() {
	}

	public boolean renderOnWorldRender() {
		return false;
	}

	public NBTTagCompound getNBTTagCompound() {
		if (customFixedObjectData == null) {
			customFixedObjectData = new NBTTagCompound();
			this.writeToNBTOptional(customFixedObjectData);
		}
		return customFixedObjectData;
	}

	protected NBTTagList newDoubleNBTList(double... p_70087_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		double[] adouble = p_70087_1_;
		int i = p_70087_1_.length;

		for (int j = 0; j < i; ++j) {
			double d1 = adouble[j];
			nbttaglist.appendTag(new NBTTagDouble(d1));
		}

		return nbttaglist;
	}

	protected NBTTagList newFloatNBTList(float... p_70049_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		float[] afloat = p_70049_1_;
		int i = p_70049_1_.length;

		for (int j = 0; j < i; ++j) {
			float f1 = afloat[j];
			nbttaglist.appendTag(new NBTTagFloat(f1));
		}

		return nbttaglist;
	}

	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		String s = BB_ResisteredList.getBraceBaseString(this);

		if (!this.isDead && s != null) {
			p_70039_1_.setString("id", s);
			this.writeToNBT(p_70039_1_);
			return true;
		} else {
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		try {
			nbt.setTag("Pos", this.newDoubleNBTList(new double[] { this.pos.xCoord, this.pos.yCoord, this.pos.zCoord }));
			//nbt.setTag("Motion", this.newDoubleNBTList(new double[] { this.motion.xCoord, this.motion.yCoord, this.motion.zCoord }));
			//nbt.setTag("Rotation", this.newFloatNBTList(new float[] { this.rotationYaw, this.rotationPitch, this.rotationRoll }));
			nbt.setShort("Fire", (short) this.fire);
			if (this.uuid != null) {
				nbt.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
				nbt.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
			}

			this.writeBraceBaseToNBT(nbt);

		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving fixed object NBT");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Fixed object being saved");
			//this.addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
	}

	private UUID getUniqueID() {
		return this.uuid;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		//		try {
		NBTTagList nbttaglist = nbt.getTagList("Pos", 6);
		//			NBTTagList nbttaglist1 = nbt.getTagList("Motion", 6);
		//			NBTTagList nbttaglist2 = nbt.getTagList("Rotation", 5);
		//			this.motion.xCoord = nbttaglist1.func_150309_d(0);
		//			this.motion.yCoord = nbttaglist1.func_150309_d(1);
		//			this.motion.zCoord = nbttaglist1.func_150309_d(2);
		//
		//			if (Math.abs(this.motion.xCoord) > 10.0D) {
		//				this.motion.xCoord = 0.0D;
		//			}
		//
		//			if (Math.abs(this.motion.yCoord) > 10.0D) {
		//				this.motion.yCoord = 0.0D;
		//			}
		//
		//			if (Math.abs(this.motion.zCoord) > 10.0D) {
		//				this.motion.zCoord = 0.0D;
		//			}

		this.prevPos = Vec3.createVectorHelper(0, 0, 0);
		this.lastTickPos = Vec3.createVectorHelper(0, 0, 0);
		this.pos = Vec3.createVectorHelper(0, 0, 0);
		this.prevPos.xCoord = this.lastTickPos.xCoord = this.pos.xCoord = nbttaglist.func_150309_d(0);
		this.prevPos.yCoord = this.lastTickPos.yCoord = this.pos.yCoord = nbttaglist.func_150309_d(1);
		this.prevPos.zCoord = this.lastTickPos.zCoord = this.pos.zCoord = nbttaglist.func_150309_d(2);
		//			this.prevRotationYaw = this.rotationYaw = nbttaglist2.func_150308_e(0);
		//			this.prevRotationPitch = this.rotationPitch = nbttaglist2.func_150308_e(1);
		this.fire = nbt.getShort("Fire");

		if (nbt.hasKey("UUIDMost", 4) && nbt.hasKey("UUIDLeast", 4)) {
			this.uuid = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
		}

		this.setPosition(this.pos.xCoord, this.pos.yCoord, this.pos.zCoord);
		//this.setRotation(this.rotationYaw, this.rotationPitch, this.rotationRoll);

		if (nbt.hasKey("PersistentIDMSB") && nbt.hasKey("PersistentIDLSB")) {
			this.uuid = new UUID(nbt.getLong("PersistentIDMSB"), nbt.getLong("PersistentIDLSB"));
		}
		this.readBraceBaseFromNBT(nbt);

		if (this.shouldSetPosAfterLoading()) {
			this.setPosition(this.pos.xCoord, this.pos.yCoord, this.pos.zCoord);
		}
		/*} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading fixed object NBT");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Fixed object being loaded");
			//this.addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}*/
	}

	private void setPosition(double xCoord, double yCoord, double zCoord) {
		this.pos = Vec3.createVectorHelper(xCoord, yCoord, zCoord);

	}

	private boolean shouldSetPosAfterLoading() {
		return true;
	}

	//	public void addEntityCrashInfo(CrashReportCategory p_85029_1_) {
	//		p_85029_1_.addCrashSectionCallable("Entity Type", new Callable() {
	//			private static final String __OBFID = "CL_00001534";
	//
	//			public String call() {
	//				return FixedObjList.getFObjString(InstObj.this) + " (" + InstObj.this.getClass().getCanonicalName() + ")";
	//			}
	//		});
	//		p_85029_1_.addCrashSection("Entity ID", Integer.valueOf(this.InstObjID));
	//		p_85029_1_.addCrashSectionCallable("Entity Name", new Callable() {
	//			private static final String __OBFID = "CL_00001535";
	//
	//			public String call() {
	//				return InstObj.this.getCommandSenderName();
	//			}
	//		});
	//		p_85029_1_.addCrashSection("Fixed object\'s Exact location", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.pos.xCoord), Double.valueOf(this.pos.yCoord), Double.valueOf(this.pos.zCoord) }));
	//		p_85029_1_.addCrashSection("Fixed object\'s Block location", CrashReportCategory.getLocationInfo(MathHelper.floor_double(this.pos.xCoord), MathHelper.floor_double(this.pos.yCoord), MathHelper.floor_double(this.pos.zCoord)));
	//		p_85029_1_.addCrashSection("Fixed object\'s Momentum", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.motion.xCoord), Double.valueOf(this.motion.yCoord), Double.valueOf(this.motion.zCoord) }));
	//	}

	public String getCommandSenderName() {
		String s = BB_ResisteredList.getBraceBaseString(this);

		if (s == null) {
			s = "generic";
		}

		return StatCollector.translateToLocal("entity." + s + ".name");
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected abstract void readBraceBaseFromNBT(NBTTagCompound p_70037_1_);

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected abstract void writeBraceBaseToNBT(NBTTagCompound p_70014_1_);

	public void onChunkLoad() {
	}

	public int getBrightnessForRender(float partialtick) {
		int i = MathHelper.floor_double(this.pos.xCoord);
		int j = MathHelper.floor_double(this.pos.zCoord);
		int k = MathHelper.floor_double(this.pos.yCoord);

		if (this.worldObj.blockExists(i, 0, j)) {
			return this.worldObj.getLightBrightnessForSkyBlocks(i, k, j, 0);
		} else {
			return 0;
		}
	}

	public boolean interactWithAABB(AxisAlignedBB boundingBox) {
		boolean ret = false;
		if (boundingBox.isVecInside(pos)) {
			ret = true;
		}
		return ret;
	}

	public double getYaw() {
		return this.rotationYaw;
	}

	public double getPitch() {
		return this.rotationPitch;
	}

	public double getRoll() {
		return this.rotationRoll;
	}

	public Vec3 interactWithLine(Vec3 s, Vec3 e) {
		return null;
	}

	public Line interactWithRay(Vec3 set, Vec3 end) {
		return null;
	}
	
	public void rightClick(EntityPlayer player, Vec3 pos) {
		return;
	}

	public void leftClick(EntityPlayerMP player, Vec3 pos2) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
