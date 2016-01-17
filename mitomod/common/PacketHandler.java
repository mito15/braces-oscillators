package com.mito.mitomod.common;

import com.mito.mitomod.InstObject.BB_ClickPacketProcessor;
import com.mito.mitomod.InstObject.BB_PacketProcessor;
import com.mito.mitomod.common.entity.BracePacketProcessor;
import com.mito.mitomod.common.entity.BraceSyncCall;
import com.mito.mitomod.common.item.BendPacketProcessor;
import com.mito.mitomod.common.item.ItemBarPacketProcessor;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	
	public static int nex = -1;
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("mitomod");
 
 
    public static void init() {
        
    	INSTANCE.registerMessage(ItemUsePacketProcessor.class, ItemUsePacketProcessor.class, nex++, Side.SERVER);
    	INSTANCE.registerMessage(ItemBarPacketProcessor.class, ItemBarPacketProcessor.class, nex++, Side.SERVER);
    	INSTANCE.registerMessage(BendPacketProcessor.class, BendPacketProcessor.class, nex++, Side.CLIENT);
    	INSTANCE.registerMessage(BracePacketProcessor.class, BracePacketProcessor.class, nex++, Side.CLIENT);
    	INSTANCE.registerMessage(BraceSyncCall.class, BraceSyncCall.class, nex++, Side.SERVER);
    	INSTANCE.registerMessage(BB_PacketProcessor.class, BB_PacketProcessor.class, nex++, Side.SERVER);
    	INSTANCE.registerMessage(BB_PacketProcessor.class, BB_PacketProcessor.class, nex++, Side.CLIENT);
    	INSTANCE.registerMessage(BB_ClickPacketProcessor.class, BB_ClickPacketProcessor.class, nex++, Side.SERVER);
    }

}
