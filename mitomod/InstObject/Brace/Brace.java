package com.mito.mitomod.InstObject.Brace;

import java.util.List;

import com.mito.mitomod.InstObject.BraceBase;
import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.utilities.Line;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Brace extends BraceBase {

	//pos > cpoint > end
	public Vec3 rand = Vec3.createVectorHelper(Math.random() * 0.002 - 0.001, Math.random() * 0.002 - 0.001, Math.random() * 0.002 - 0.001);
	public Vec3 end = Vec3.createVectorHelper(0.0, 0.0, 0.0);
	public List<Vec3> offCurvePoints;
	public int color = 0;
	public String texture;
	public Figure shape;
	public double size;
	public Brace bindBrace;
	private int debug = 0;

	public Brace(World world) {
		super(world);
	}

	public Brace(World world, Vec3 pos) {
		super(world, pos);
	}

	public Brace(World world, Vec3 pos, Vec3 end, Figure shape, double size) {
		this(world, pos);
		this.end = end;
		this.shape = shape;
		this.size = size;

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		/*debug++;
		if (debug == 20) {
			int i = MathHelper.floor_double(this.pos.xCoord / 16.0D);
			int j = MathHelper.floor_double(this.pos.zCoord / 16.0D);
			mitoLogger.info("Im here(ID:" + this.BBID + ")  " + DataLists.getChunkData(worldObj, i, j).list.size() + "  " + DataLists.getWorldData(worldObj).braceBaseList.size());
			debug = 0;
		}*/
	}

	@Override
	protected void readBraceBaseFromNBT(NBTTagCompound nbt) {
		this.end = Vec3.createVectorHelper(nbt.getDouble("endX"), nbt.getDouble("endY"), nbt.getDouble("endZ"));
		this.shape = Figures.getFigure(nbt.getString("shape"));
		this.size = nbt.getDouble("size");
	}

	@Override
	protected void writeBraceBaseToNBT(NBTTagCompound nbt) {

		if (end != null && shape != null) {
			nbt.setDouble("endX", end.xCoord);
			nbt.setDouble("endY", end.yCoord);
			nbt.setDouble("endZ", end.zCoord);
			nbt.setString("shape", Figures.getName(this.shape));
			nbt.setDouble("size", this.size);
		}
	}

	@Override
	public boolean interactWithAABB(AxisAlignedBB boundingBox) {
		boolean ret = false;
		if (boundingBox.expand(this.size, this.size, this.size).calculateIntercept(this.pos, this.end) != null || (boundingBox.expand(this.size, this.size, this.size).isVecInside(this.pos) && boundingBox.expand(this.size, this.size, this.size).isVecInside(this.end))) {
			ret = true;
		}
		return ret;
	}

	@Override
	public Vec3 interactWithLine(Vec3 s, Vec3 e) {
		Line line = MitoMath.getDistanceLine(s, e, this.pos, this.end);
		return line.end;
	}

	@Override
	public double getYaw() {
		if (this.rotationYaw > 180 || this.rotationYaw < -180) {
			double xabs = end.xCoord - pos.xCoord;
			this.rotationYaw = Math.atan((end.zCoord - pos.zCoord) / xabs) / Math.PI * 180;
			if (pos.xCoord > end.xCoord) {
				if (pos.zCoord < end.zCoord) {
					this.rotationYaw = this.rotationYaw - 180;
				} else {
					this.rotationYaw = this.rotationYaw + 180;
				}
			}
		}
		return this.rotationYaw;
	}

	@Override
	public double getPitch() {
		if (this.rotationPitch > 90 || this.rotationPitch < -90) {
			double xzabs = Math.sqrt(Math.pow(pos.xCoord - end.xCoord, 2) + Math.pow(pos.zCoord - end.zCoord, 2));
			this.rotationPitch = Math.atan((end.yCoord - pos.yCoord) / xzabs) / Math.PI * 180;
		}
		return this.rotationPitch;
	}
	
	@Override
	public Line interactWithRay(Vec3 set, Vec3 end) {
		Line line = MitoMath.getDistanceLine(set, end, this.pos, this.end);
		if (line.getAbs() < this.size / 1.5) {
			return line;
		}
		return null;
	}
	
	public void rightClick(EntityPlayer player, Vec3 pos) {
		
		this.setDead();
		mitoLogger.info("Brace Action");
		return;
	}

}
