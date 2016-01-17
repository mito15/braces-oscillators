package com.mito.mitomod.client;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mito.mitomod.InstObject.RenderBraceHandler;
import com.mito.mitomod.client.render.RenderEntityBrace;
import com.mito.mitomod.client.render.RenderItemBrace;
import com.mito.mitomod.client.render.RenderWall;
import com.mito.mitomod.client.render.BlockRenderer.RenderBlockOscillator;
import com.mito.mitomod.client.render.BlockRenderer.RenderBlockPipe;
import com.mito.mitomod.client.render.TileRenderer.RenderTileOsChest;
import com.mito.mitomod.client.render.TileRenderer.RenderTileOscillator;
import com.mito.mitomod.client.render.TileRenderer.RenderTilePipe;
import com.mito.mitomod.common.mitoCommonProxy;
import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.mitomain;
import com.mito.mitomod.common.entity.EntityBrace;
import com.mito.mitomod.common.entity.EntityFake;
import com.mito.mitomod.common.entity.EntityWall;
import com.mito.mitomod.common.tile.TileOscillator;
import com.mito.mitomod.common.tile.TileOscillatorChest;
import com.mito.mitomod.common.tile.TileOscillatorPipe;
import com.mito.mitomod.common.tile.TileOscillatorPipeLeaf;
import com.mito.mitomod.common.tile.TileOscillatorPipeNode;
import com.mito.mitomod.common.tile.TileOscillatorPipeRoot;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class mitoClientProxy extends mitoCommonProxy {

	BraceHighLightHandler bh;
	ScrollWheelHandler wh;
	RenderBraceHandler rh;

	public mitoClientProxy() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	public BB_Key getKey() {
		return new BB_Key(this.isControlKeyDown(), this.isShiftKeyDown(), this.isAltKeyDown());
	}

	public boolean isControlKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
	}

	public boolean haswheel() {
		return Mouse.hasWheel();
	}

	public int getwheel() {
		return Mouse.getDWheel();
	}

	public boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}

	public boolean isAltKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public void preInit() {
		super.preInit();

		mitoLogger.info("on pre initializing");

		this.bh = new BraceHighLightHandler(this);
		MinecraftForge.EVENT_BUS.register(bh);
		FMLCommonHandler.instance().bus().register(bh);

		this.wh = new ScrollWheelHandler(this);
		MinecraftForge.EVENT_BUS.register(wh);
		FMLCommonHandler.instance().bus().register(wh);

		this.rh = new RenderBraceHandler();
		MinecraftForge.EVENT_BUS.register(rh);
		FMLCommonHandler.instance().bus().register(rh);

		mitomain.OscillatorRenderType = RenderingRegistry.getNextAvailableRenderId();
		mitomain.PipeRenderType = RenderingRegistry.getNextAvailableRenderId();

	}

	@Override
	public void init() {
		super.init();

		//TileEntity

		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillator.class, new RenderTileOscillator());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillatorPipe.class, new RenderTilePipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillatorPipeNode.class, new RenderTilePipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillatorPipeLeaf.class, new RenderTilePipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillatorPipeRoot.class, new RenderTilePipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillatorChest.class, new RenderTileOsChest());

		//blockRenderer
		RenderingRegistry.registerBlockHandler(new RenderBlockOscillator());
		RenderingRegistry.registerBlockHandler(new RenderBlockPipe());

		//entity render resist

		RenderingRegistry.registerEntityRenderingHandler(EntityBrace.class, new RenderEntityBrace());
		RenderingRegistry.registerEntityRenderingHandler(EntityFake.class, new RenderEntityBrace());
		RenderingRegistry.registerEntityRenderingHandler(EntityWall.class, new RenderWall());

		//item renderer

		MinecraftForgeClient.registerItemRenderer(mitomain.ItemBrace, new RenderItemBrace());
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
