package com.mito.mitomod.client;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mito.mitomod.BraceBase.BB_RenderHandler;
import com.mito.mitomod.client.render.RenderEntityBrace;
import com.mito.mitomod.client.render.RenderItemBrace;
import com.mito.mitomod.client.render.RenderWall;
import com.mito.mitomod.client.render.BlockRenderer.RenderBlockNeuron;
import com.mito.mitomod.client.render.BlockRenderer.RenderBlockOscillator;
import com.mito.mitomod.client.render.BlockRenderer.RenderBlockPipe;
import com.mito.mitomod.client.render.TileRenderer.RenderTileNeuron;
import com.mito.mitomod.client.render.TileRenderer.RenderTileOsChest;
import com.mito.mitomod.client.render.TileRenderer.RenderTileOscillator;
import com.mito.mitomod.client.render.TileRenderer.RenderTilePipe;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.mitoCommonProxy;
import com.mito.mitomod.common.entity.EntityBrace;
import com.mito.mitomod.common.entity.EntityFake;
import com.mito.mitomod.common.entity.EntityWall;
import com.mito.mitomod.common.tile.TileNeuron;
import com.mito.mitomod.common.tile.TileOscillator;
import com.mito.mitomod.common.tile.TileOscillatorChest;
import com.mito.mitomod.common.tile.TileOscillatorPipe;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class mitoClientProxy extends mitoCommonProxy {

	BraceHighLightHandler bh;
	ScrollWheelHandler wh;
	BB_RenderHandler rh;
	private BB_KeyBinding key_ctrl;
	private BB_KeyBinding key_alt;
	private BB_KeyBinding key_shift;

	public mitoClientProxy() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	public BB_Key getKey() {
		return new BB_Key(this.isControlKeyDown(), this.isShiftKeyDown(), this.isAltKeyDown());
	}

	public boolean isControlKeyDown() {
		return Keyboard.isKeyDown(key_ctrl.overrideKeyCode);
	}

	public boolean haswheel() {
		return Mouse.hasWheel();
	}

	public int getwheel() {
		return Mouse.getDWheel();
	}

	public boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(key_shift.overrideKeyCode);
	}

	public boolean isAltKeyDown() {
		return Keyboard.isKeyDown(key_alt.overrideKeyCode);
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public void preInit() {
		super.preInit();
		//mitoLogger.info("on pre initializing");

		this.sg = new BB_SelectedGroup(this);

		this.bh = new BraceHighLightHandler(this);
		MinecraftForge.EVENT_BUS.register(bh);
		FMLCommonHandler.instance().bus().register(bh);

		this.wh = new ScrollWheelHandler(this);
		MinecraftForge.EVENT_BUS.register(wh);
		FMLCommonHandler.instance().bus().register(wh);

		this.rh = new BB_RenderHandler();
		MinecraftForge.EVENT_BUS.register(rh);
		FMLCommonHandler.instance().bus().register(rh);

		BAO_main.OscillatorRenderType = RenderingRegistry.getNextAvailableRenderId();
		BAO_main.PipeRenderType = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void init() {
		super.init();

		//TileEntity

		ClientRegistry.bindTileEntitySpecialRenderer(TileNeuron.class, new RenderTileNeuron());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillator.class, new RenderTileOscillator());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillatorPipe.class, new RenderTilePipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOscillatorChest.class, new RenderTileOsChest());

		//blockRenderer

		RenderingRegistry.registerBlockHandler(new RenderBlockNeuron());
		RenderingRegistry.registerBlockHandler(new RenderBlockOscillator());
		RenderingRegistry.registerBlockHandler(new RenderBlockPipe());

		//entity render resist

		RenderingRegistry.registerEntityRenderingHandler(EntityBrace.class, new RenderEntityBrace());
		RenderingRegistry.registerEntityRenderingHandler(EntityFake.class, new RenderEntityBrace());
		RenderingRegistry.registerEntityRenderingHandler(EntityWall.class, new RenderWall());

		//item renderer

		MinecraftForgeClient.registerItemRenderer(BAO_main.ItemBrace, new RenderItemBrace());

		//key

		this.key_shift = new BB_KeyBinding("Snap Parallel Key", Keyboard.KEY_LSHIFT, "Braces&Oscillators");
		this.key_alt = new BB_KeyBinding("Air Key", Keyboard.KEY_LMENU, "Braces&Oscillators");
		this.key_ctrl = new BB_KeyBinding("Off Snap Key", Keyboard.KEY_LCONTROL, "Braces&Oscillators");
	}

	public void upkey(int count) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP player = mc.thePlayer;
		if (player != null && !mc.isGamePaused() && mc.inGameHasFocus && mc.currentScreen == null) {
			//処理とか
		}
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void playSound(ResourceLocation rl, float vol, float pitch, float x, float y, float z) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(rl, vol, pitch, x, y, z));
	}

	@Override
	public void addDiggingEffect(World world, Vec3 center, double d0, double d1, double d2, Block block, int color) {
		Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(world, d0, d1, d2, d0 - center.xCoord, d1 - center.yCoord, d2 - center.zCoord, block, color))
				.applyColourMultiplier((int) center.xCoord, (int) center.yCoord, (int) center.zCoord));
	}
}
