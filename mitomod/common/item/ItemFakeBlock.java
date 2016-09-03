package com.mito.mitomod.common.item;

import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.BraceBase.Brace.FakeBlock;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.entity.EntityWrapperBB;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemFakeBlock extends ItemBraceBase {

	public ItemFakeBlock() {
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

		MovingObjectPosition mop1 = this.getMovingOPWithKey(itemstack, world, player, key, mop, 1.0);
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (!world.isRemote) {

			if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				Vec3 pos = MitoMath.copyVec3(mop.hitVec);
				FakeBlock block = new FakeBlock(world, pos, world.getBlock(mop.blockX, mop.blockY, mop.blockZ), world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ));
				block.addToWorld();
				if (!player.capabilities.isCreativeMode) {
					itemstack.stackSize--;
					if (itemstack.stackSize == 0) {
						player.destroyCurrentEquippedItem();
					}
				}
			}
		}
	}

	public void snapBlock(MovingObjectPosition mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key) {
		MitoUtil.snapBlock(mop);
		MitoUtil.snapBlockOffset(mop);
	}

	public void snapBraceBase(MovingObjectPosition mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key) {
		//各BraceBaseに振り分け用関数を用意
		BraceBase base = ((EntityWrapperBB)mop.entityHit).base;
		if (base instanceof Brace) {
			Brace brace = (Brace) ((EntityWrapperBB)mop.entityHit).base;
			brace.snap(mop, snapCenter());
		} else if(base instanceof FakeBlock){
			//muzui
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTicks, MovingObjectPosition mop) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		if (mop == null || !MitoUtil.canClick(player.worldObj, BAO_main.proxy.getKey(), mop))
			return false;
		Vec3 set = mop.hitVec;
		RenderHighLight rh = RenderHighLight.INSTANCE;
		rh.drawBox(player, set, 0.95, partialTicks);
		return true;
	}

	public void onUpdate(ItemStack itemstack, World world, Entity entity, int meta, boolean p_77663_5_) {
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player) {
		return true;
	}

}
