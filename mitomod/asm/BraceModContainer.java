package com.mito.mitomod.asm;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class BraceModContainer extends DummyModContainer {
	
	public BraceModContainer() {
        super(new ModMetadata());
 
        ModMetadata meta = super.getMetadata();
        meta.modId = "BAOBraceCore";
        meta.name = "BAOBraceCore";
        meta.version = "1.0";
    }
 
    @Override
    public boolean registerBus(EventBus bus, LoadController lc) {
        bus.register(this);
        return true;
    }

}
