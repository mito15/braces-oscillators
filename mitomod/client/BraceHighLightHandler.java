package com.mito.mitomod.client;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.common.item.ItemBraceBase;
import com.mito.mitomod.utilities.MitoUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public class BraceHighLightHandler {

	mitoClientProxy proxy;

	public BraceHighLightHandler(mitoClientProxy p) {

		this.proxy = p;
	}

	@SubscribeEvent
	public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
		if (e.currentItem != null && (e.currentItem.getItem() instanceof ItemBraceBase)) {
			ItemBraceBase itembrace = (ItemBraceBase) e.currentItem.getItem();
			MovingObjectPosition mop = itembrace.getMovingOPWithKey(e.currentItem, e.player.worldObj, e.player, proxy.getKey(), e.target, e.partialTicks);
			boolean flag = itembrace.drawHighLightBox(e.currentItem, e.player, e.partialTicks, mop);
			if (flag) {
				if (e.isCancelable()) {
					e.setCanceled(true);
				}
			}
		} else if (e.player.capabilities.isCreativeMode) {
			if (MitoUtil.isBrace(e.target)) {
				GL11.glPushMatrix();
				drawHighLightBrace(e.player, MitoUtil.getBrace(e.target), e.partialTicks);
				GL11.glPopMatrix();
				if (e.isCancelable()) {
					e.setCanceled(true);
				}
			}
		}
	}

	public void drawHighLightBrace(EntityPlayer player, BraceBase base, double partialticks) {
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialticks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialticks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialticks));
		base.drawHighLight(partialticks);
		GL11.glPopMatrix();
	}

}
