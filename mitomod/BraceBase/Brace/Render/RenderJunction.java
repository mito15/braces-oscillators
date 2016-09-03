package com.mito.mitomod.BraceBase.Brace.Render;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.BraceBase.BB_Render;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Junction;
import com.mito.mitomod.client.RenderHighLight;

public class RenderJunction extends BB_Render {

	public void doRender(BraceBase base, float pt) {
		if (base != null && base instanceof Junction) {
			RenderHighLight rh = RenderHighLight.INSTANCE;
			GL11.glPushMatrix();
			
			
			GL11.glPopMatrix();
		}
	}

}
