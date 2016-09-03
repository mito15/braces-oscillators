package com.mito.mitomod.BraceBase.Brace;

import java.util.Map;

import com.mito.mitomod.BraceBase.BB_PacketProcessor;
import com.mito.mitomod.BraceBase.BB_PacketProcessor.Mode;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.item.ItemBar;
import com.mito.mitomod.common.item.ItemBraceBase;
import com.mito.mitomod.utilities.Line;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LinearMotor extends BraceBase {

	public Brace railBrace;
	public boolean active = false;
	public double speed = 0.1;
	//trueでstart>end方向
	public boolean direction = true;
	private int debug;

	public LinearMotor(World world) {
		super(world);
	}

	public LinearMotor(World world, Vec3 pos) {
		super(world, pos);
	}

	public boolean connectBrace(BraceBase base) {

		if (base == null || !(base instanceof Brace)) {
			return false;
		}
		if (!this.worldObj.isRemote) {
			this.bindDataGroup(base);
		}
		this.addBrace(base, 1);
		return base.addBrace(this, 0);
	}

	public boolean addBrace(BraceBase base, int id) {

		boolean flag = false;
		if (base == null) {
			return false;
		} else if (id == 0) {
			flag = this.bindBraces.add(base);
		} else if (id == 1) {
			this.railBrace = (Brace) base;
			flag = true;
		}
		if (!this.worldObj.isRemote && flag) {
			PacketHandler.INSTANCE.sendToAll(new BB_PacketProcessor(Mode.BIND, this.BBID, base.BBID, id));
		}

		return flag;
	}

	/*public void moveLinearMotor() {
		double d1 = MitoMath.abs2(MitoMath.vectorSub(this.railBrace.pos, this.pos));
		double d2 = MitoMath.abs2(MitoMath.vectorSub(this.railBrace.end, this.pos));
		double l = MitoMath.abs2(MitoMath.vectorSub(this.railBrace.pos, this.railBrace.end));
		double k = (d1 - d2 + l) / (2 * l);
		Vec3 motion;
		if (k >= 1) {
			this.direction = false;
			motion = MitoMath.vectorSub(MitoMath.vectorPul(this.railBrace.end, MitoMath.vectorMul(this.railBrace.getUnit(), -this.speed)), this.pos);
		} else if (k <= 0) {
			this.direction = true;
			motion = MitoMath.vectorSub(MitoMath.vectorPul(this.railBrace.pos, MitoMath.vectorMul(this.railBrace.getUnit(), this.speed)), this.pos);
		} else {
			Vec3 ret = MitoMath.vectorPul(MitoMath.vectorMul(MitoMath.vectorSub(this.railBrace.end, this.railBrace.pos), k), this.railBrace.pos);
			if (this.direction) {
				motion = MitoMath.vectorSub(MitoMath.vectorPul(ret, MitoMath.vectorMul(this.railBrace.getUnit(), this.speed)), this.pos);
			} else {
				motion = MitoMath.vectorSub(MitoMath.vectorPul(ret, MitoMath.vectorMul(this.railBrace.getUnit(), -this.speed)), this.pos);
			}
		}

		this.moveRequest(motion, command++);
		this.move(motion, command++);
	}*/

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.active && this.railBrace != null && this.pos != null) {
			this.isStatic = false;
			this.prevPos = MitoMath.copyVec3(this.pos);
			//this.moveLinearMotor();
		} else {
			this.isStatic = true;
		}/*

		debug++;
		if (debug == 20) {
			int i = MathHelper.floor_double(this.pos.xCoord / 16.0D);
			int j = MathHelper.floor_double(this.pos.zCoord / 16.0D);
			mitoLogger.info("Im here(ID:" + this.BBID + ")  " + "rail data: " + (this.railBrace != null) );//+ "  " + this.group.list.size());
			debug = 0;
		}*/
	}

	/*@Override
	public double getYaw() {
		if (this.railBrace != null) {
			this.rotationYaw = MitoMath.getYaw(railBrace.pos, railBrace.end);
		}
		return this.rotationYaw;
	}

	@Override
	public double getPitch() {
		if (this.railBrace != null) {
			this.rotationPitch = MitoMath.getPitch(railBrace.pos, railBrace.end);
		}
		return this.rotationPitch;
	}*/

	@Override
	public int getBrightnessForRender(float partialtick) {
		int i = MathHelper.floor_double(this.pos.xCoord - 0.2);
		int j = MathHelper.floor_double(this.pos.zCoord - 0.2);
		int k = MathHelper.floor_double(this.pos.yCoord - 0.2);

		int i1 = MathHelper.floor_double(this.pos.xCoord + 0.2);
		int j1 = MathHelper.floor_double(this.pos.zCoord + 0.2);
		int k1 = MathHelper.floor_double(this.pos.yCoord + 0.2);

		if (this.worldObj.blockExists(i, 0, j)) {
			return Math.max(this.worldObj.getLightBrightnessForSkyBlocks(i, k, j, 0), this.worldObj.getLightBrightnessForSkyBlocks(i1, k1, j1, 0));
		} else {
			return 0;
		}
	}

	@Override
	public Line interactWithRay(Vec3 set, Vec3 end) {
		if (this.railBrace == null || set == null || end == null) {
			return null;
		}
		Line ret = null;
		for (int n = 0; n < 4; n++) {
			Line line = MitoMath.getLineNearPoint(set, end, this.getJunction(n));
			if (line.getAbs() < 0.4) {
				if (ret == null || ret.getAbs() > line.getAbs()) {
					ret = line;
				}
			}
		}
		return ret;
	}

	public Vec3 getJunction(int n) {
		Vec3 ret = null;
		if (this.railBrace != null) {
			Vec3 v1;
			switch (n) {
			case 0:
				v1 = Vec3.createVectorHelper(0, 0.25, 0);
				break;
			case 1:
				v1 = Vec3.createVectorHelper(0, -0.25, 0);
				break;
			case 2:
				v1 = Vec3.createVectorHelper(0, 0, 0.25);
				break;
			case 3:
				v1 = Vec3.createVectorHelper(0, 0, -0.25);
				break;
			default:
				v1 = Vec3.createVectorHelper(0, 0, 0);
				break;
			}
			Vec3 v2 = MitoMath.rot(v1, this.getRoll(), this.getPitch(), this.getYaw());
			ret = MitoMath.vectorPul(v2, this.pos);
		}
		return ret;
	}

	@Override
	public boolean rightClick(EntityPlayer player, Vec3 pos, ItemStack itemstack) {
		if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.setDead();
			return true;
		} else if (itemstack != null && itemstack.getItem() instanceof ItemBraceBase) {
		} else {
			this.active();
			return true;
		}
		return false;
	}

	private void active() {
		if(this.active){
			this.stop(command++);
		}
		this.active = this.active ? false : true;

	}

	@Override
	protected void readBraceBaseFromNBT(NBTTagCompound nbt) {
		this.direction = nbt.getBoolean("direction");
	}

	@Override
	protected void writeBraceBaseToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("direction", this.direction);
	}

	@Override
	public void writeNBTAssociate(NBTTagCompound nbt, Map<BraceBase, Integer> map) {
		try {
			int[] aint = new int[this.bindBraces.size()];
			for (int n = 0; n < this.bindBraces.size(); n++) {
				aint[n] = map.get(this.bindBraces.get(n));
			}
			nbt.setIntArray("Association", aint);
			nbt.setInteger("rail", map.get(this.railBrace));

		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving fixed object NBT");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Fixed object being saved");
			//this.addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
	}

	@Override
	public void readNBTAssociate(NBTTagCompound nbt, Map<Integer, BraceBase> map) {
		int[] aint = nbt.getIntArray("Association");
		for (int n = 0; n < aint.length; n++) {
			this.addBrace(map.get(aint[n]), 0);
		}
		this.addBrace(map.get(nbt.getInteger("rail")), 1);
	}

}
