package com.mito.mitomod.common.entity;

import com.mito.mitomod.BraceBase.BB_ClickPacketProcessor;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.common.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWrapperBB extends Entity {

	public BraceBase base;

	public EntityWrapperBB(World world) {
		super(world);
	}

	public EntityWrapperBB(World world, BraceBase base) {
		this(world);
		this.base = base;
	}

	@Override
	protected void entityInit() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public EntityWrapperBB wrap(BraceBase base) {
		this.base = base;
		return this;
	}

	public boolean hitByEntity(Entity player) {
		if (player instanceof EntityPlayer) {
			if (base.leftClick((EntityPlayer) player, ((EntityPlayer) player).getCurrentEquippedItem())) {
				PacketHandler.INSTANCE.sendToServer(new BB_ClickPacketProcessor(BB_ClickPacketProcessor.Mode.CLICK_LEFT, base.BBID, Minecraft.getMinecraft().objectMouseOver.hitVec));
				return true;
			}
		}
		return false;
	}

	public boolean interactFirst(EntityPlayer player) {
		if (base.rightClick(player, Minecraft.getMinecraft().objectMouseOver.hitVec, player.getCurrentEquippedItem())) {
			PacketHandler.INSTANCE.sendToServer(new BB_ClickPacketProcessor(BB_ClickPacketProcessor.Mode.CLICK_RIGHT, base.BBID, Minecraft.getMinecraft().objectMouseOver.hitVec));
			return true;
		}
		return false;
	}

}
