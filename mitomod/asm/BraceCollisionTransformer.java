package com.mito.mitomod.asm;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.mito.mitomod.common.mitoLogger;

import net.minecraft.launchwrapper.IClassTransformer;

public class BraceCollisionTransformer implements IClassTransformer, Opcodes {

	private static final String TARGET_CLASS_NAME = "net.minecraft.world.World";

	@Override
	public byte[] transform(String name, String transformedName, byte[] data) {

		if (!name.equals(TARGET_CLASS_NAME)) {
			return data;
		}

		try {
			return transformGetCollision(data);

		} catch (Exception e) {
			throw new RuntimeException("failed : BraceCollisionTransformer loading", e);
		}
	}

	private byte[] transformGetCollision(byte[] data) {

		ClassNode cnode = new ClassNode();
		ClassReader reader = new ClassReader(data);
		reader.accept((ClassVisitor) cnode, 0);

		String targetMethodName = "getCollidingBoundingBoxes";

		String targetMethoddesc = "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";

		MethodNode mnode = null;
		for (MethodNode curMnode : (List<MethodNode>) cnode.methods) {
			if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {
				mnode = curMnode;
				break;
			}
		}

		if (mnode != null) {
			InsnList overrideList = new InsnList();

			overrideList.add(new VarInsnNode(ALOAD, 0));
			overrideList.add(new VarInsnNode(ALOAD, 2));
			overrideList.add(new VarInsnNode(ALOAD, 0));
			overrideList.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/World", "collidingBoundingBoxes", "Ljava/util/ArrayList;"));
			overrideList.add(new VarInsnNode(ALOAD, 1));
			overrideList
					.add(new MethodInsnNode(INVOKESTATIC, "com/mito/mitomod/asm/BraceCoreHooks", "getCollisionHook", "(Lnet/minecraft/world/World;Lnet/minecraft/util/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V", false));

			int n = 4;
			for (int i = 0; i <= mnode.instructions.indexOf(mnode.instructions.getLast()); i++) {
				if (mnode.instructions.get(i).toString().equals("org.objectweb.asm.tree.MethodInsnNode@4b20ca2b")) {
					n = i;
					break;
				}
			}
			mnode.instructions.insert(mnode.instructions.get(n), overrideList);

			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			cnode.accept((ClassVisitor) cw);
			data = cw.toByteArray();
			mitoLogger.info("transforming the code is success");
		}

		return data;
	}

}
