package com.mito.mitomod.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BB_RenderAdapter implements Opcodes {

	public static class ClassAdapter extends ClassVisitor {
		public ClassAdapter(ClassVisitor cv) {
			super(ASM4, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			/* if("func_174904_a".equals(mapMethodName("", name, desc)) && toDesc(int.class, "net.minecraft.item.ItemStack").equals(desc)){
			    return new MethodAdapter(super.visitMethod(access, name, desc, signature, exceptions));
			}*/
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}

	public static class MethodAdapter extends MethodVisitor {
		public MethodAdapter(MethodVisitor mv) {
			super(ASM4, mv);
		}

		@Override
		public void visitIntInsn(int opcode, int operand) {
			super.visitIntInsn(opcode, 400);
		}
	}

}
