package com.mito.mitomod.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.mito.mitomod.common.mitoLogger;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class RenderClassAdapter extends ClassVisitor implements Opcodes {

	public RenderClassAdapter(int api, ClassVisitor cv) {
		super(api, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

		String srgMethod = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(BB_Transformer.name1, name, desc);
		String srgDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
		if (("renderEntities".equals(srgMethod) || "func_147589_a".equals(srgMethod)) && "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/culling/ICamera;F)V".equals(srgDesc)) {
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
			mitoLogger.info("adapt" + opcode);
			if (opcode == RETURN) {
				super.visitVarInsn(ALOAD, 1);
				super.visitVarInsn(ALOAD, 2);
				super.visitVarInsn(FLOAD, 3);
				super.visitMethodInsn(INVOKESTATIC, "com/mito/mitomod/BraceBase/BB_RenderHandler", "onRenderEntities", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/culling/ICamera;F)V", false);
			}
			if (mv != null) {
				mv.visitInsn(opcode);
			}
		}

		@Override
		public void visitVarInsn(int opcode, int var) {
			super.visitVarInsn(opcode, var);
		}
	}

}
