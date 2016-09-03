package com.mito.mitomod.asm;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TransRender extends TransInfo {

	public TransRender() {
		super();
		this.targetMethodName = "renderEntities";
		this.targetDeobfMethodName = "func_147589_a";
		this.targetMethoddesc = "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/culling/ICamera;F)V";
	}
	
	public void transform(MethodNode mnode){
		InsnList overrideList = new InsnList();
		//RenderGlobal a;

		//com.mito.mitomod.BraceBase.BB_RenderHandler のstaticなメソッド onRenderEntities(p_147589_1_, p_147589_2_, p_147589_3_)

		//p_147589_1_
		overrideList.add(new VarInsnNode(ALOAD, 1));
		//p_147589_2_
		overrideList.add(new VarInsnNode(ALOAD, 2));
		//p_147589_3_
		overrideList.add(new VarInsnNode(FLOAD, 3));
		//onRenderEntities(p_147589_1_, p_147589_2_, p_147589_3_)
		overrideList
				.add(new MethodInsnNode(INVOKESTATIC, "com/mito/mitomod/BraceBase/BB_RenderHandler", "onRenderEntities", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/culling/ICamera;F)V", false));

		//mnode.instructions.insert(mnode.instructions.get(1), overrideList);
	}

}
