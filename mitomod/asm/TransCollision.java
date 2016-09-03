package com.mito.mitomod.asm;

import java.io.File;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraftforge.common.config.Configuration;

public class TransCollision extends TransInfo {

	public TransCollision() {
		super();
		this.targetMethodName = "getCollidingBoundingBoxes";
		this.targetDeobfMethodName = "func_72945_a";
		this.targetMethoddesc = "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
	}

	public void transform(MethodNode mnode){
		InsnList overrideList = new InsnList();

		//this.collidingBoundingBoxes.clear(); の後に com/mito/mitomod/asm/BraceCoreHooks のstaticなメソッド getCollisionHook(this, p_72945_2_, this.collidingBoundingBoxes, p_72945_1_)

		boolean debug;
		File mcDir = (File) FMLInjectionData.data()[6];
		File configDir = new File(mcDir, "config");
		File configFile = new File(configDir, "Braces-Oscillators.cfg");
		Configuration cfg = new Configuration(configFile);
		try {
			cfg.load();//コンフィグをロード
			debug = cfg.getBoolean("debug mode", "mode", false, "debug");
		} finally {
			cfg.save();//セーブ
		}
		//this
		overrideList.add(new VarInsnNode(ALOAD, 0));
		//p_72945_2_
		overrideList.add(new VarInsnNode(ALOAD, 2));
		//this.collidingBoundingBoxes
		overrideList.add(new VarInsnNode(ALOAD, 0));
		overrideList.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/World", debug ? "collidingBoundingBoxes" : "field_72998_d", "Ljava/util/ArrayList;"));
		//p_72945_2_
		overrideList.add(new VarInsnNode(ALOAD, 1));
		//getCollisionHook(this, p_72945_2_, this.collidingBoundingBoxes, p_72945_1_)
		overrideList
				.add(new MethodInsnNode(INVOKESTATIC, "com/mito/mitomod/asm/BraceCoreHooks", "getCollisionHook", "(Lnet/minecraft/world/World;Lnet/minecraft/util/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V", false));

		int n = 4;
		/*for (int i = 0; i <= mnode.instructions.indexOf(mnode.instructions.getLast()); i++) {
			mitoLogger.info("transform" + mnode.instructions.get(i).toString());
			if (mnode.instructions.get(i).toString().equals("org.objectweb.asm.tree.MethodInsnNode@4b20ca2b")) {
				n = i;
				break;
			}
		}*/
		mnode.instructions.insert(mnode.instructions.get(n), overrideList);
	}
}
