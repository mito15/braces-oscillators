package com.mito.mitomod.BraceBase;

public class BB_BindLocation {
	
	public int location;
	public int id;
	public BraceBase base;
	
	public BB_BindLocation (int b, int l){
		this.location = l;
		this.id = b;
	}
	
	public BB_BindLocation (BraceBase b, int l){
		this.location = l;
		this.base = b;
	}

}
