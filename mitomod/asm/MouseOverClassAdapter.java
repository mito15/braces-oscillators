package com.mito.mitomod.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class MouseOverClassAdapter extends ClassVisitor implements Opcodes {

	public MouseOverClassAdapter(int api, ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

		String srgMethod = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(BB_Transformer.name1, name, desc);
		String srgDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
		if (("getMouseOver".equals(srgMethod) || "func_78473_a".equals(srgMethod)) && "(F)V".equals(srgDesc)) {
			return new MethodAdapter(super.visitMethod(access, name, desc, signature, exceptions));
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	public static class MethodAdapter extends MethodVisitor {
		public MethodAdapter(MethodVisitor mv) {
			super(ASM4, mv);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				super.visitVarInsn(FLOAD, 1);
				super.visitMethodInsn(INVOKESTATIC, "com/mito/mitomod/asm/BraceCoreHooks", "rayTrace", "(F)V", false);
			}
			if (mv != null) {
				mv.visitInsn(opcode);
			}
		}
	}

}
