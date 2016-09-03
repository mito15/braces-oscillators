package com.mito.mitomod.BraceBase;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class BB_GUIHandler {

	public static boolean flag = false;
	public static boolean flag2 = false;

	public static void openEvent(PlayerOpenContainerEvent e) {
		if (flag) {
			if (e.entityPlayer.inventoryContainer instanceof ContainerPlayer) {
				if (flag2 == true) {
					flag = false;
					flag2 = false;
				}
			} else {
				flag2 = true;
			}
			e.setResult(Event.Result.ALLOW);
		}
	}

}
