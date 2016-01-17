package com.mito.mitomod.InstObject;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.mito.mitomod.InstObject.Brace.Brace;
import com.mito.mitomod.InstObject.Brace.BraceRender;
import com.mito.mitomod.InstObject.Brace.LinearMotor;
import com.mito.mitomod.InstObject.Brace.RenderLinearMotor;
import com.mito.mitomod.common.mitoLogger;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BB_ResisteredList {

	public static int nextID = 0;

	public static Map<String, Class> stringToClassMapping = new HashMap<String, Class>();
	public static Map<Class, String> classToStringMapping = new HashMap<Class, String>();
	public static Map<Class, BB_Render> classToRenderMapping = new HashMap<Class, BB_Render>();

	/**
	 * adds a mapping between FObj classes and both a string representation and an ID
	 */
	public static void addMapping(Class ioclass, String name, int id, BB_Render render) {
		if (stringToClassMapping.containsKey(name)) {
			throw new IllegalArgumentException("ID is already registered: " + name);
		} else {
			stringToClassMapping.put(name, ioclass);
			classToStringMapping.put(ioclass, name);
			classToRenderMapping.put(ioclass, render);
		}
	}

	/**
	 * Create a new instance of an entity in the world by using the entity name.
	 */
	public static BraceBase createFixedObjByName(String p_75620_0_, World p_75620_1_) {
		BraceBase iobj = null;

		try {
			Class oclass = (Class) stringToClassMapping.get(p_75620_0_);

			if (oclass != null) {
				iobj = (BraceBase) oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { p_75620_1_ });
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return iobj;
	}

	/**
	 * create a new instance of an entity from NBT store
	 */

	public static BraceBase createBraceBaseFromNBT(NBTTagCompound nbt, World world) {
		return createBraceBaseFromNBT(nbt, world, -1);
	}

	public static BraceBase createBraceBaseFromNBT(NBTTagCompound nbt, World world, int id) {
		BraceBase iobj = null;

		Class oclass = null;
		try {
			if (nbt.getString("id") == null) {
				mitoLogger.info("id is null");
			}else {
				mitoLogger.info("id is " + nbt.getString("id"));
			}
			oclass = (Class) stringToClassMapping.get(nbt.getString("id"));

			if (oclass == null) {
				mitoLogger.info("class is null");
			}
			if (oclass != null) {
				iobj = (BraceBase) oclass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { world });
				if(id != -1){
					iobj.setId(id);
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		if (iobj != null) {
			try {
				iobj.readFromNBT(nbt);
			} catch (Exception e) {
				FMLLog.log(Level.ERROR, e,
						"An Entity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
						nbt.getString("id"), oclass.getName());
				iobj = null;
			}
		} else {
			mitoLogger.warn("Skipping Entity with id " + nbt.getString("id"));
		}

		return iobj;
	}

	/**
	 * Gets the string representation of a specific entity.
	 */
	public static String getBraceBaseString(BraceBase p_75621_0_) {
		return classToStringMapping.get(p_75621_0_.getClass());
	}

	public static BB_Render getBraceBaseRender(BraceBase p_75621_0_) {
		return classToRenderMapping.get(p_75621_0_.getClass());
	}

	static {
		addMapping(Brace.class, "Brace", nextID++, new BraceRender());
		addMapping(LinearMotor.class, "LinearMotor", nextID++, new RenderLinearMotor());
	}

}
