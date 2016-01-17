package com.mito.mitomod.common.item;

import com.mito.mitomod.common.mitomain;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

public class ItemBarPacketProcessor implements IMessage, IMessageHandler<ItemBarPacketProcessor, IMessage> {

	private int slot;
	private int size;
	private byte key;

	public ItemBarPacketProcessor() {
	}

	public ItemBarPacketProcessor(int slot, int size, byte key) {

		this.slot = slot;
		this.size = size;
		this.key = key;
	}

	@Override
	public IMessage onMessage(ItemBarPacketProcessor message, MessageContext ctx) {
		try {
			ItemStack stack = null;

			if (message.slot > -1 && message.slot < 9) {
				stack = ctx.getServerHandler().playerEntity.inventory.getStackInSlot(message.slot);
			}
			if (stack != null) {
				if (stack.getItem() != null) {
					if (message.key == (byte) 2) {
						if (stack.getItem() instanceof ItemBar) {
							stack.setItemDamage(message.size);
						} else if (stack.getItem() instanceof ItemBrace) {
							ItemBrace brace = (ItemBrace) mitomain.ItemBrace;
							brace.setSize(stack, message.size);
						} else if (stack.getItem() instanceof ItemRuler) {
							stack.setItemDamage(message.size);
						}
					} else if (message.key == (byte) 4) {
						stack.getTagCompound().setInteger("selectNum", message.size);
					}
				}
			}
		} finally {
		}

		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readInt();
		size = buf.readInt();
		key = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
		buf.writeInt(size);
		buf.writeByte(key);
	}

}
