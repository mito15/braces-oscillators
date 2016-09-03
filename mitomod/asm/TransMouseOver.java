package com.mito.mitomod.asm;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TransMouseOver extends TransInfo {

	public TransMouseOver() {
		super();
		this.targetMethodName = "getMouseOver";
		this.targetDeobfMethodName = "func_72945_a";
		this.targetMethoddesc = "(F)V";
	}

	public void transform(MethodNode mnode) {
		InsnList overrideList = new InsnList();

		//p_78473_1_
		overrideList.add(new VarInsnNode(ALOAD, 1));
		//BraceCoreHooks.rayTrace(p_78473_1_);
		overrideList.add(new MethodInsnNode(INVOKESTATIC, "com/mito/mitomod/asm/BraceCoreHooks", "rayTrace", "(F)V", false));
		mnode.instructions.insert(mnode.instructions.get(mnode.instructions.size()-1), overrideList);
		int i = 0;
	}
}
