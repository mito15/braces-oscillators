package com.mito.mitomod.client;

import java.util.Arrays;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.KeyBinding;

/**
 * Created by Furia on 14/05/24.
 */
public class BB_KeyBinding extends KeyBinding {

	private static final int dumyKeyCode = -200;

	public int overrideKeyCode;
	final public int overrideKeyCodeDefault;

	public BB_KeyBinding(String Description, int keyCode, String Category) {
		super(Description, dumyKeyCode, Category);
		this.overrideKeyCode = keyCode;
		this.overrideKeyCodeDefault = keyCode;

		if (!Arrays.asList(Minecraft.getMinecraft().gameSettings.keyBindings).contains(this)) {
			ClientRegistry.registerKeyBinding(this);
			//FMLCommonHandler.instance().bus().register(this);
		}
	}

	@Override
	public int getKeyCodeDefault() {
		return overrideKeyCodeDefault;
	}

	@Override
	public int getKeyCode() {
		return overrideKeyCode;
	}

	@Override
	public void setKeyCode(int keyCode) {
		this.overrideKeyCode = keyCode;
	}
}