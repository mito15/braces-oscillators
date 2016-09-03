package com.mito.mitomod.common;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.BB_SelectedGroup;
import com.mito.mitomod.common.tile.TileNeuron;
import com.mito.mitomod.common.tile.TileOscillator;
import com.mito.mitomod.common.tile.TileOscillatorChest;
import com.mito.mitomod.common.tile.TileOscillatorPipe;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class mitoCommonProxy {

	public BB_SelectedGroup sg;

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

		GameRegistry.registerTileEntity(TileNeuron.class, "TileNeuron");
		GameRegistry.registerTileEntity(TileOscillator.class, "TileOscillator");
		GameRegistry.registerTileEntity(TileOscillatorPipe.class, "TileOscillatorPipe");
		GameRegistry.registerTileEntity(TileOscillatorChest.class, "TileOscillatorChest");
	}

	public void preInit() {
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

	public void playSound(ResourceLocation rl, float vol, float pitch, float x, float y, float z){
	}

	public void addDiggingEffect(World world, Vec3 center, double d0, double d1, double d2, Block block, int color) {
	}

}
