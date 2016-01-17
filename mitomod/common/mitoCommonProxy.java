package com.mito.mitomod.common;

import com.mito.mitomod.client.BB_Key;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class mitoCommonProxy {

	public mitoCommonProxy() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	public boolean isControlKeyDown() {
		return false;
	}

	public boolean isShiftKeyDown() {
		return false;
	}

	public boolean isAltKeyDown() {
		return false;
	}

	public World getClientWorld() {
		return null;
	}

	public BB_Key getKey() {
		return new BB_Key(0);
	}

	public void init() {
	}

	public void preInit() {
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

}
