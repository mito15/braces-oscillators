package com.mito.mitomod.common;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.common.item.ItemBraceBase;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

public class ItemUsePacketProcessor implements IMessage, IMessageHandler<ItemUsePacketProcessor, IMessage> {

	public int slot;
	public BB_Key key;

	public ItemUsePacketProcessor() {
	}

	public ItemUsePacketProcessor(BB_Key keyPressed, int slot) {
		this.key = keyPressed;
		this.slot = slot;
	}

	@Override
	public IMessage onMessage(ItemUsePacketProcessor message, MessageContext ctx) {
		ItemStack itemstack = null;
		if (message.slot > -1 && message.slot < 9) {
			itemstack = ctx.getServerHandler().playerEntity.inventory.getStackInSlot(message.slot);
		}
		if (itemstack != null) {
			if (itemstack.getItem() != null && itemstack.getItem() instanceof ItemBraceBase) {
				ItemBraceBase item = (ItemBraceBase) itemstack.getItem();
				item.RightClick(itemstack, ctx.getServerHandler().playerEntity.worldObj, ctx.getServerHandler().playerEntity, message.key, true);
			}
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.key = new BB_Key(buf.readByte());
		this.slot = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.key.key);
		buf.writeInt(this.slot);
	}

}
