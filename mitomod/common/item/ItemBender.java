package com.mito.mitomod.common.item;

import com.mito.mitomod.BraceBase.BB_DataLists;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.entity.EntityWrapperBB;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBender extends ItemSet {

	public byte key = 0;

	public ItemBender() {
		super();
		this.setTextureName("mitomod:bender");
		this.setMaxDamage(0);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}

	@Override
	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {
		super.nbtInit(nbt, itemstack);
		nbt.setInteger("brace", -1);
	}

	public double getRayDistance(BB_Key key){
		return key.isAltPressed() ? 3.0 : 5.0;
	}

	public void snapDegree(MovingObjectPosition mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key, NBTTagCompound nbt){
		if (nbt.getBoolean("activated")) {
			Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			MitoUtil.snapByShiftKey(mop, set);
		}
	}

	public boolean snapCenter(){
		return false;
	}

	public void activate(World world, EntityPlayer player, ItemStack itemstack, MovingObjectPosition mop, NBTTagCompound nbt) {


		mitoLogger.info("cul! x : " + MitoMath.rot(Vec3.createVectorHelper(0, 1, 0), Math.PI / 6, Vec3.createVectorHelper(0, 0, 1)));


		if (mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB && ((EntityWrapperBB)mop.entityHit).base instanceof Brace && ((EntityWrapperBB)mop.entityHit).base.isStatic) {
			mitoLogger.info("bender register complete");
			Vec3 set = mop.hitVec;
			Brace brace = (Brace) ((EntityWrapperBB)mop.entityHit).base;
			nbt.setBoolean("activated", true);
			if (set.xCoord == brace.pos.xCoord && set.yCoord == brace.pos.yCoord && set.zCoord == brace.pos.zCoord) {
				nbt.setBoolean("isPos", true);
			} else {
				nbt.setBoolean("isPos", false);
			}
			nbt.setInteger("brace", brace.BBID);
		}
	}

	public void onActiveClick(World world, EntityPlayer player, ItemStack itemstack, MovingObjectPosition movingOP, Vec3 set, Vec3 end, NBTTagCompound nbt) {
		BraceBase base = BB_DataLists.getWorldData(world).getBraceBaseByID(nbt.getInteger("brace"));
		if (base != null && base.isStatic && base instanceof Brace) {
			Brace brace = (Brace) base;
			/*if (nbt.getBoolean("isPos")) {
				end = MitoMath.vectorSub(end, brace.pos);
				brace.offCurvePoints1 = end;
				PacketHandler.INSTANCE.sendToAll(new BendPacketProcessor(brace, end, true));
				mitoLogger.info("bend set");
			} else {
				end = MitoMath.vectorSub(end, brace.end);
				brace.offCurvePoints2 = end;
				PacketHandler.INSTANCE.sendToAll(new BendPacketProcessor(brace, end, false));
				mitoLogger.info("bender end");
			}
			brace.hasCP = true;*/
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTicks, MovingObjectPosition mop) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		if (mop == null || !MitoUtil.canClick(player.worldObj, BAO_main.proxy.getKey(), mop))
			return false;
		Vec3 set = mop.hitVec;

		RenderHighLight rh = RenderHighLight.INSTANCE;
		if (nbt.getBoolean("activated")) {
			//Vec3 end = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			rh.drawFakeBraceBend(player, set, nbt, partialTicks);
		} else {
			if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB && ((EntityWrapperBB)mop.entityHit).base instanceof Brace) {
				rh.drawCenter(player, set, ((Brace) ((EntityWrapperBB)mop.entityHit).base).size / 2 + 0.1, partialTicks);
				this.drawHighLightBrace(player, partialTicks, mop);
			}
		}
		return true;
	}

}
