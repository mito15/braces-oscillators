package com.mito.mitomod.common;

import net.minecraft.item.ItemFood;
import net.minecraft.potion.Potion;

public class mitomodFood extends ItemFood{

	public mitomodFood(int par1, int par2, boolean par3)
	{
		super(par1, par2, par3);
		this.setUnlocalizedName("SampleFood");	//システム名の登録
		this.setTextureName("sample_food");	//テクスチャの指定
		this.setMaxStackSize(64);	//スタックできる量
 
		//以下不要なら消してください
		this.setAlwaysEdible();	//お腹すいてなくても食べれる。
		this.setPotionEffect(Potion.poison.id, 5, 0, 0.6F);	//ポーション効果 ID, 時間(秒), レベル(書いた値+1になる), 確率(0.0～1.0F　1.0Fの時100%)
	}
	
}
