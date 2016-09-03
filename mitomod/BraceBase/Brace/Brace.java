package com.mito.mitomod.BraceBase.Brace;

import java.util.List;

import com.mito.mitomod.BraceBase.BB_EnumTexture;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Render.BB_TypeResister;
import com.mito.mitomod.client.render.model.IDrawBrace;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.item.ItemBar;
import com.mito.mitomod.common.item.ItemBrace;
import com.mito.mitomod.utilities.Line;
import com.mito.mitomod.utilities.MitoMath;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Brace extends BraceBase {

	//pos > cpoint > end
	public Vec3 rand = Vec3.createVectorHelper(Math.random() * 0.002 - 0.001, Math.random() * 0.002 - 0.001, Math.random() * 0.002 - 0.001);
	public ILineBrace line = null;
	public int color = 0;
	public BB_EnumTexture texture = BB_EnumTexture.IRON;
	public IDrawBrace shape;
	public double size;
	private int debug = 0;
	public Vec3 unitVector = null;

	public Brace(World world) {
		super(world);
	}

	public Brace(World world, Vec3 pos) {
		super(world, pos);
	}

	public Brace(World world, Vec3 pos, Vec3 end, IDrawBrace shape, BB_EnumTexture material, int tex, double size) {
		this(world, pos);
		this.line = new Line(pos, end);
		this.shape = shape;
		this.size = size;
		this.texture = material;
		this.color = tex;
	}

	@Override
	public void onUpdate() {
		/*if(!this.equals(this.dataworld.getBraceBaseByID(this.BBID))){
			mitoLogger.info("delete phase mmm");
		}*/
		/*debug++;
		if (debug == 20) {
			int i = MathHelper.floor_double(this.pos.xCoord / 16.0D);
			int j = MathHelper.floor_double(this.pos.zCoord / 16.0D);
			//if (!this.worldObj.isRemote) {

			BB_DataChunk ret = (BB_DataChunk) BB_DataLists.getWorldData(worldObj).coordToDataMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(i, j));
			//mitoLogger.info("Im here(ID:" + this.BBID + ")  " + "chunk data: " + this.group.list.size() + "  : " + this.group.datachunk.xPosition + ", " + this.group.datachunk.zPosition + "  "
			//		+ BB_DataLists.getWorldData(worldObj).braceBaseList.size() + "  " + this.isDead);
			//}
			debug = 0;
		}*/
	}

	@Override
	public void move(Vec3 motion, int command) {
		if (this.currentCommand == command) {
			return;
		}
		this.currentCommand = command;

		this.prevPos = MitoMath.copyVec3(this.pos);
		this.pos = MitoMath.vectorPul(this.pos, motion);
		this.line.move(motion, command);
		for (int n = 0; n < this.bindBraces.size(); n++) {
			BraceBase base = this.bindBraces.get(n);
			base.move(motion, command);
		}
		this.isStatic = false;
	}

	@Override
	protected void readBraceBaseFromNBT(NBTTagCompound nbt) {
		//this.line.readNBT(nbt);

		Vec3 start = getVec3(nbt, "start");
		Vec3 end = getVec3(nbt, "end");
		line = new Line(start, end);
		this.shape = BB_TypeResister.getFigure(nbt.getString("shape"));
		this.size = nbt.getDouble("size");
		this.texture = BB_EnumTexture.values()[nbt.getInteger("texture")];
		this.color = nbt.getInteger("color");
	}

	private void setVec3(NBTTagCompound nbt, String name, Vec3 vec) {
		nbt.setDouble(name + "X", vec.xCoord);
		nbt.setDouble(name + "Y", vec.yCoord);
		nbt.setDouble(name + "Z", vec.zCoord);
	}

	private Vec3 getVec3(NBTTagCompound nbt, String name) {
		return Vec3.createVectorHelper(nbt.getDouble(name + "X"), nbt.getDouble(name + "Y"), nbt.getDouble(name + "Z"));
	}

	@Override
	protected void writeBraceBaseToNBT(NBTTagCompound nbt) {
		if (line != null && shape != null) {
			line.writeNBT(nbt);
			nbt.setString("shape", BB_TypeResister.getName(this.shape));
			nbt.setDouble("size", this.size);
			nbt.setInteger("texture", this.texture.ordinal());
			nbt.setInteger("color", this.color);
		}
	}

	@Override
	public boolean interactWithAABB(AxisAlignedBB boundingBox) {
		return line == null ? false : line.interactWithAABB(boundingBox, size);
		/*boolean ret = false;
		if (boundingBox.expand(this.size, this.size, this.size).calculateIntercept(this.pos, this.end) != null
				|| (boundingBox.expand(this.size, this.size, this.size).isVecInside(this.pos) && boundingBox.expand(this.size, this.size, this.size).isVecInside(this.end))) {
			ret = true;
		}
		return ret;*/
	}

	@Override
	public Vec3 interactWithLine(Vec3 s, Vec3 e) {
		return line == null ? null : line.interactWithLine(s, e);
	}

	/*public Vec3 getUnit() {
		if (this.unitVector == null) {
			this.unitVector = MitoMath.vectorSub(this.end, this.pos).normalize();
		}
		return this.unitVector;
	}*/

	@Override
	public Line interactWithRay(Vec3 set, Vec3 end) {
		/*if (this.pos.distanceTo(this.end) < 0.01) {
			Vec3 ve = MitoMath.getNearPoint(set, end, this.pos);
			if (ve.distanceTo(this.pos) < this.size / 1.5) {
				return new Line(ve, this.pos);
			}
		}
		Line line = MitoMath.getDistanceLine(set, end, this.pos, this.end);
		if (line.getAbs() < this.size / 1.5 && !(MitoUtil.isVecEqual(line.end, this.pos) || MitoUtil.isVecEqual(line.end, this.end))) {
			return line;
		}*/
		return line == null ? null : line.interactWithRay(set, end, size);
	}

	public void breakBrace(EntityPlayer player) {
		if (!player.worldObj.isRemote) {
			if (!player.capabilities.isCreativeMode) {
				this.dropItem();
			}

			this.setDead();
			/*for (int n = 0; n < this.bindBraces.size(); n++) {
				this.bindBraces.get(n).setDead();
			}*/
		} else {
			BAO_main.proxy.playSound(new ResourceLocation(this.texture.getBreakSound()), this.texture.getVolume(), this.texture.getPitch(), (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord);

			//破壊時パーティクル
			//this.setDead();
			/*int b0 = (int) (this.size * 4) + 1;
			Vec3 center = MitoMath.vectorRatio(this.end, this.pos, 0.5);
			int div = (int) (MitoMath.subAbs(this.pos, this.end) * 4) + 1;
			Vec3 vec = MitoMath.vectorSub(this.end, this.pos);

			for (int i1 = 0; i1 < b0; ++i1) {
				for (int j1 = 0; j1 < b0; ++j1) {
					for (int k1 = 0; k1 < div; ++k1) {
						double d0 = this.pos.xCoord + vec.xCoord * (double) k1 / (double) div + ((double) j1 * size) / (double) b0 - (size / 2);
						double d1 = this.pos.yCoord + vec.yCoord * (double) k1 / (double) div + ((double) i1 * size) / (double) b0 - (size / 2);
						double d2 = this.pos.zCoord + vec.zCoord * (double) k1 / (double) div + ((double) j1 * size) / (double) b0 - (size / 2);
						//Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(this.worldObj, d0, d1, d2, d0 - center.xCoord, d1 - center.yCoord, d2 - center.zCoord, this.texture.getBlock(), this.color))
						//.applyColourMultiplier((int) center.xCoord, (int) center.yCoord, (int) center.zCoord));
						BAO_main.proxy.addDiggingEffect(worldObj, center, d0, d1, d2, this.texture.getBlock(), color);
					}
				}
			}*/
			if (line != null)
				line.particle();
		}
	}

	public boolean leftClick(EntityPlayer player, ItemStack itemstack) {
		if (player.capabilities.isCreativeMode) {
			this.breakBrace(player);
			return true;
		} else if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.breakBrace(player);
			return true;
		}
		return false;
	}

	public boolean rightClick(EntityPlayer player, Vec3 pos, ItemStack itemstack) {
		if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.breakBrace(player);
			return true;
		}
		return false;
	}

	public void dropItem() {

		float f = this.random.nextFloat() * 0.2F + 0.1F;
		float f1 = this.random.nextFloat() * 0.2F + 0.1F;
		float f2 = this.random.nextFloat() * 0.2F + 0.1F;

		ItemBrace brace = (ItemBrace) BAO_main.ItemBrace;
		ItemStack itemstack1 = new ItemStack(BAO_main.ItemBrace, 1, this.color);
		brace.setSize(itemstack1, (int) (this.size * 20));
		brace.setType(itemstack1, BB_TypeResister.getName(this.shape));

		NBTTagCompound nbt = itemstack1.getTagCompound();
		itemstack1.setTagCompound(nbt);
		nbt.setBoolean("activated", false);
		nbt.setDouble("setX", 0.0D);
		nbt.setDouble("setY", 0.0D);
		nbt.setDouble("setZ", 0.0D);
		nbt.setBoolean("useFlag", false);
		EntityItem entityitem = new EntityItem(worldObj, (double) ((float) this.pos.xCoord + f), (double) ((float) this.pos.yCoord + f1), (double) ((float) this.pos.zCoord + f2), itemstack1);

		float f3 = 0.05F;
		entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
		entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
		entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
		worldObj.spawnEntityInWorld(entityitem);
	}

	/*public int getBrightnessForRender(float partialtick) {
		int i = MathHelper.floor_double((this.pos.xCoord + this.end.xCoord) / 2);
		int k = MathHelper.floor_double((this.pos.yCoord + this.end.yCoord) / 2);
		int j = MathHelper.floor_double((this.pos.zCoord + this.end.zCoord) / 2);

		if (this.worldObj.blockExists(i, 0, j)) {
			return this.worldObj.getLightBrightnessForSkyBlocks(i, k, j, 0);
		} else {
			return 0;
		}
	}*/

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float partialticks) {
		Vec3 v = this.getPos();
		int i = MathHelper.floor_double(v.xCoord);
		int j = MathHelper.floor_double(v.yCoord);
		int k = MathHelper.floor_double(v.zCoord);
		for (int n1 = 0; n1 < 6; n1++) {
			if (!this.worldObj.getBlock(i + Facing.offsetsXForSide[n1], j + Facing.offsetsYForSide[n1], k + Facing.offsetsZForSide[n1]).isOpaqueCube()) {
				return this.worldObj.getLightBrightnessForSkyBlocks(i + Facing.offsetsXForSide[n1], j + Facing.offsetsYForSide[n1], k + Facing.offsetsZForSide[n1], 0);
			}
		}
		if (this.worldObj.blockExists(i, 0, k)) {
			return this.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
		} else {
			return 0;
		}
	}

	public AxisAlignedBB getBoundingBox() {
		if(line == null){
			return null;
		}
		return line.getBoundingBox(size);
	}

	public void setRoll(double roll) {
		this.rotationRoll = roll;
		this.prevRotationRoll = roll;
		this.shouldUpdateRender = true;
	}

	/*public BezierCurve getBezierCurve() {
		BezierCurve bc;
		Vec3 va = this.end;
		Vec3 vb = MitoMath.vectorPul(va, this.offCurvePoints2);
		Vec3 vc = MitoMath.vectorPul(this.pos, this.offCurvePoints1);
		Vec3 vd = this.pos;
		if (this.offCurvePoints1.lengthVector() < 0.001) {
			bc = new BezierCurve(vd, vb, va);
		} else if (this.offCurvePoints2.lengthVector() < 0.001) {
			bc = new BezierCurve(vd, vc, va);
		} else {
			bc = new BezierCurve(vd, vc, vb, va);
		}
		return bc;
	}*/

	public void addCoordinate(double x, double y, double z) {
		this.pos = this.pos.addVector(x, y, z);
		if (line != null)
			line.addCoordinate(x, y, z);//this.end = this.end.addVector(x, y, z);
	}

	public double getMinY() {
		return line == null ? null : line.getMinY();//Math.min(pos.yCoord, end.yCoord);
	}

	public double getMaxY() {
		return line == null ? null : line.getMaxY();//Math.max(pos.yCoord, end.yCoord);
	}

	public Vec3 getPos() {
		return this.pos;
		//return line == null ? null : line.getPos();//MitoMath.vectorRatio(pos, end, 0.5);
	}

	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity) {
		line.addCollisionBoxesToList(world, aabb, collidingBoundingBoxes, entity, size);
	}

	public void rotation(Vec3 cent, double yaw) {
		Vec3 p = MitoMath.vectorPul(MitoMath.rotY(MitoMath.vectorSub(this.pos, cent), yaw), cent);
		/*end = MitoMath.vectorPul(MitoMath.rotY(MitoMath.vectorSub(this.end, cent), yaw), cent);
		offCurvePoints1 = MitoMath.rotY(offCurvePoints1, yaw);
		offCurvePoints2 = MitoMath.rotY(offCurvePoints2, yaw);*/
		if (line != null)
			line.rotation(cent, yaw);
		this.prevPos.xCoord = this.pos.xCoord = p.xCoord;
		this.prevPos.yCoord = this.pos.yCoord = p.yCoord;
		this.prevPos.zCoord = this.pos.zCoord = p.zCoord;
	}

	public void resize(Vec3 cent, double i) {
		Vec3 p = MitoMath.vectorPul(MitoMath.vectorMul(MitoMath.vectorSub(this.pos, cent), i), cent);
		//end = MitoMath.vectorPul(MitoMath.vectorMul(MitoMath.vectorSub(this.end, cent), i), cent);
		if (line != null)
			line.resize(cent, i);
		this.prevPos.xCoord = this.pos.xCoord = p.xCoord;
		this.prevPos.yCoord = this.pos.yCoord = p.yCoord;
		this.prevPos.zCoord = this.pos.zCoord = p.zCoord;
	}

	public void snap(MovingObjectPosition mop, boolean b) {
		this.line.snap(mop, b);
	}

}
