package com.mito.mitomod.common.item;

import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.BraceBase.Brace.LinearMotor;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.common.entity.EntityWrapperBB;
import com.mito.mitomod.utilities.MitoMath;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemLinearMotor extends ItemBraceBase {

	public ItemLinearMotor() {
		super();
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.none;
	}

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition mop, BB_Key key, boolean p_77663_5_) {

		NBTTagCompound nbt = itemstack.getTagCompound();
		if (!world.isRemote) {

			if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB) {

				if (((EntityWrapperBB)mop.entityHit).base instanceof Brace) {
					Brace brace = (Brace) ((EntityWrapperBB)mop.entityHit).base;
					Vec3 pos = MitoMath.copyVec3(mop.hitVec);

					LinearMotor motor = new LinearMotor(world, pos);
					motor.addToWorld();
					motor.connectBrace(brace);

					if (!player.capabilities.isCreativeMode) {
						itemstack.stackSize--;
						if (itemstack.stackSize == 0) {
							player.destroyCurrentEquippedItem();
						}
					}
				}

			}
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTicks, MovingObjectPosition mop) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		double size = 0.5;
		if (mop == null)
			return false;
		Vec3 set = mop.hitVec;
		RenderHighLight rh = RenderHighLight.INSTANCE;
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB)
			rh.drawBox(player, set, size, partialTicks);

		return true;

	}

	public void onUpdate(ItemStack itemstack, World world, Entity entity, int meta, boolean p_77663_5_) {
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player) {
		return true;
	}

}
