package com.mito.mitomod.client;

import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.item.ItemBarPacketProcessor;
import com.mito.mitomod.common.item.ItemBraceBase;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;

public class ScrollWheelHandler {

	mitoClientProxy proxy;
	int sizeNum = 4;

	public ScrollWheelHandler(mitoClientProxy p) {
		proxy = p;
	}

	//1:ctrl 2:shift 4:alt

	@SubscribeEvent
	public void mouseEvent(MouseEvent event) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (event.dwheel != 0 && player != null) {
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null) {
				Item item = stack.getItem();
				if (item instanceof ItemBraceBase) {
					boolean flag = ((ItemBraceBase) item).wheelEvent(player, stack, proxy.getKey(), event.dwheel);
					event.setCanceled(flag);
					if(flag)PacketHandler.INSTANCE.sendToServer(new ItemBarPacketProcessor(player.inventory.currentItem, proxy.getKey(), event.dwheel));
				}
			}
		}

	}

	/*private void changeSizeBar(ItemStack stack, EntityPlayer player, int dWheel) {

		int w = dWheel / 120;
		int size = stack.getItemDamage() + w;
		if (size >= sizeNum) {
			size = size % sizeNum;
		} else if (size < 0) {
			size = (size + sizeNum * 500) % sizeNum;
		}

		stack.setItemDamage(size);
		//PacketHandler.INSTANCE.sendToServer(new ItemBarPacketProcessor(player.inventory.currentItem, size, (byte) 2));
	}

	private void changeSelect(ItemStack stack, EntityPlayer player, int dWheel) {

		int w = dWheel / 120;
		int size = stack.getTagCompound().getInteger("selectNum") + w;
		if (size < 0) {
			size = 50000;
		} else if (size > 50000) {
			size = 0;
		}

		stack.getTagCompound().setInteger("selectNum", size);

		//PacketHandler.INSTANCE.sendToServer(new ItemBarPacketProcessor(player.inventory.currentItem, size, (byte) 4));
	}*/

}
