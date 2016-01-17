package com.mito.mitomod.common;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class NewDamageSource extends DamageSource {

	private final String message;

	public NewDamageSource(String msg) {
		super("custom");
		message = msg;
	}

	@Override
	public final IChatComponent func_151519_b(EntityLivingBase e)
	{
		IChatComponent ch = new ChatComponentTranslation(e.getCommandSenderName()+" "+message);
		return ch;
	}

}
