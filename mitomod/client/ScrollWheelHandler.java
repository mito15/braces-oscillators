package com.mito.mitomod.client;

import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitomain;
import com.mito.mitomod.common.item.ItemBar;
import com.mito.mitomod.common.item.ItemBarPacketProcessor;
import com.mito.mitomod.common.item.ItemBender;
import com.mito.mitomod.common.item.ItemBrace;
import com.mito.mitomod.common.item.ItemRuler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
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
		if (event.dwheel != 0 && player != null && player.isSneaking()) {
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null) {
				Item item = stack.getItem();
				if (item instanceof ItemBar) {
					changeSizeBar(stack, player, event.dwheel);
					event.setCanceled(true);
				} else if (item instanceof ItemBrace) {
					changeSizeBrace(stack, player, event.dwheel);
					event.setCanceled(true);
				} else if (item instanceof ItemRuler) {
					changeDivRuler(stack, player, event.dwheel);
					event.setCanceled(true);
				}
			}
		}
		if (event.dwheel != 0 && player != null && proxy.isAltKeyDown()) {
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null) {
				Item item = stack.getItem();
				if (item instanceof ItemBar || item instanceof ItemBender) {
					changeSelect(stack, player, event.dwheel);
					event.setCanceled(true);
				}
			}
		}

	}

	private void changeDivRuler(ItemStack stack, EntityPlayer player, int dWheel) {
		int w = dWheel / 120;
		int div = stack.getItemDamage() + w;
		if (div < 0) {
			div = 128;
		} else if (div > 128) {
			div = 0;
		}

		stack.setItemDamage(div);

		PacketHandler.INSTANCE.sendToServer(new ItemBarPacketProcessor(player.inventory.currentItem, div, (byte)2));
	}

	private void changeSizeBrace(ItemStack stack, EntityPlayer player, int dWheel) {

		ItemBrace brace = (ItemBrace)mitomain.ItemBrace;
		int w = dWheel / 120;
		int size = brace.getSize(stack) + w;
		if (size > brace.sizeMax) {
			size = brace.sizeMax;
		} else if (size < 1) {
			size = 1;
		}

		brace.setSize(stack, size);
		PacketHandler.INSTANCE.sendToServer(new ItemBarPacketProcessor(player.inventory.currentItem, size, (byte)2));
	}

	private void changeSizeBar(ItemStack stack, EntityPlayer player, int dWheel) {

		int w = dWheel / 120;
		int size = stack.getItemDamage() + w;
		if (size >= sizeNum) {
			size = size % sizeNum;
		} else if (size < 0) {
			size = (size + sizeNum * 500) % sizeNum;
		}

		stack.setItemDamage(size);
		PacketHandler.INSTANCE.sendToServer(new ItemBarPacketProcessor(player.inventory.currentItem, size, (byte)2));
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

		PacketHandler.INSTANCE.sendToServer(new ItemBarPacketProcessor(player.inventory.currentItem, size, (byte)4));
	}

}
