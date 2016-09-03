package com.mito.mitomod.common.item;

import com.mito.mitomod.client.BB_Key;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

public class ItemBarPacketProcessor implements IMessage, IMessageHandler<ItemBarPacketProcessor, IMessage> {

	private int slot;
	private int dwheel;
	private BB_Key key;

	public ItemBarPacketProcessor() {
	}

	public ItemBarPacketProcessor(int slot, BB_Key key, int dwheel) {
		this.slot = slot;
		this.dwheel = dwheel;
		this.key = key;
	}

	@Override
	public IMessage onMessage(ItemBarPacketProcessor message, MessageContext ctx) {
		try {
			ItemStack stack = null;

			if (message.slot > -1 && message.slot < 9) {
				stack = ctx.getServerHandler().playerEntity.inventory.getStackInSlot(message.slot);
			}
			if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemBraceBase) {
				((ItemBraceBase)stack.getItem()).wheelEvent(ctx.getServerHandler().playerEntity, stack, message.key, message.dwheel);
			}
		} finally {
		}

		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readInt();
		dwheel = buf.readInt();
		key = new BB_Key(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
		buf.writeInt(dwheel);
		buf.writeInt(key.ikey);
	}

}
