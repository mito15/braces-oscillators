package com.mito.mitomod.utilities;

import net.minecraft.util.Vec3;

public class Line {

	public Vec3 start;
	public Vec3 end;

	public Line (Vec3 s, Vec3 e){
		this.start = s;
		this.end = e;
	}

	public double getAbs(){
		return MitoMath.subAbs(this.start, this.end);
	}

}
