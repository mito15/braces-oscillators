package com.mito.mitomod.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public abstract class TransInfo implements Opcodes{
	
	public String targetMethodName;
	public String targetDeobfMethodName;
	public String targetMethoddesc;
	
	public void transform(MethodNode mnode){
	}

}
