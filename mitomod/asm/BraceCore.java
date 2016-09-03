package com.mito.mitomod.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class BraceCore implements IFMLLoadingPlugin {

	public BraceCore(){
		/*mitoLogger.info("\n\n\n\n"
				+ "BraceCore is loaded"
				+ "\n\n\n\n");*/
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"com.mito.mitomod.asm.BB_Transformer"};
	}

	@Override
	public String getModContainerClass() {
		return "com.mito.mitomod.asm.BraceModContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
