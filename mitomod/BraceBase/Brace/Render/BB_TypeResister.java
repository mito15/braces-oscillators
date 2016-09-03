package com.mito.mitomod.BraceBase.Brace.Render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mito.mitomod.client.render.model.BB_LoadModel;
import com.mito.mitomod.client.render.model.BraceShapes;
import com.mito.mitomod.client.render.model.D_Ellipse;
import com.mito.mitomod.client.render.model.D_Face;
import com.mito.mitomod.client.render.model.IDrawBrace;
import com.mito.mitomod.client.render.model.Pattern;
import com.mito.mitomod.client.render.model.Polygon3D;
import com.mito.mitomod.client.render.model.T_OrdinaryModel;
import com.mito.mitomod.client.render.model.Vertex;

import cpw.mods.fml.common.Loader;
import net.minecraft.util.Vec3;

public class BB_TypeResister {

	public static Map<String, IDrawBrace> stringToFigureMapping = new HashMap<String, IDrawBrace>();
	public static Map<IDrawBrace, String> figureToStringMapping = new HashMap<IDrawBrace, String>();
	public static List<String> shapeList = new ArrayList<String>();
	public static List<String> patternList = new ArrayList<String>();
	public static List<IDrawBrace> typeList = new ArrayList<IDrawBrace>();

	public static void addMapping(IDrawBrace figure, String name) {
		addMapping(figure, name, false);
	}

	public static void addMapping(IDrawBrace figure, String name, boolean init) {
		if (stringToFigureMapping.containsKey(name)) {
			throw new IllegalArgumentException("ID is already registered: " + name);
		} else {
			stringToFigureMapping.put(name, figure);
			figureToStringMapping.put(figure, name);
			typeList.add(figure);
			if (init) {
				if (figure instanceof Pattern) {
					patternList.add(name);
				} else {
					shapeList.add(name);
				}
			}
		}
	}

	public static IDrawBrace getFigure(String name) {
		if (!stringToFigureMapping.containsKey(name)) {
			return stringToFigureMapping.get("square");
		}
		return stringToFigureMapping.get(name);
	}

	public static void loadModels() {
		addMapping(createSquare(1, 1, 0, 0), "square", true);
		addMapping(createElipse(1, 1, 0, 0), "round", true);
		addMapping(createSquare(0.2, 1, 0, 0), "vertical", true);
		addMapping(createSquare(1, 0.2, 0, 0), "horizontal", true);
		addMapping(create2d(0.5, 0.55, -0.5, 0.55, -0.5, 0.45, -0.05, 0.45, -0.05, -0.45, -0.5, -0.45, -0.5, -0.55, +0.5, -0.55, 0.5, -0.45, 0.05, -0.45, 0.05, 0.45, 0.5, 0.45), "H-section", true);
		addMapping(new BraceShapes(createSquare(0.1, 1.0, -0.45, 0), createSquare(1, 0.1, 0, -0.45)), "Equal-Angle", true);
		addMapping(new BraceShapes(createSquare(1, 0.1, 0, 0.45), createSquare(1, 0.1, 0, -0.45), createSquare(0.1, 0.8, -0.45, 0)), "U-section", true);
		addMapping(new Pattern(0.6, createRectangle(0, 0, 0.15, 0.9, 0.9, 0.3), createRectangle(0, 0, 0.3, 0.3, 0.3, 0.6)), "pattern", true);
		addMapping(create2d(-0.1123, 0.1545, -0.4756, 0.1545, -0.1817, -0.0590, -0.2939, -0.4045, 0.0000, -0.1910, 0.2939, -0.4045, 0.1817, -0.0590, 0.4756, 0.1545, 0.1123, 0.1545, 0.0000, 0.5000), "star", true);
		if (Loader.isModLoaded("NGTLib")) {
			//addMapping(new Pattern(0.6, new NGTOWrapper("chino.ngto")), "ngto");
		}
		BB_LoadModel.load();
		//ここにロードする
	}

	public static String getName(IDrawBrace shape) {
		return figureToStringMapping.get(shape);
	}

	static public D_Face createSquare(double width, double height, double x, double y) {
		D_Face ret = new D_Face();
		ret.line.add(new Vertex(x + width / 2, y + height / 2, 0, x + width / 2, y - height / 2));
		ret.line.add(new Vertex(x - width / 2, y + height / 2, 0, x - width / 2, y - height / 2));
		ret.line.add(new Vertex(x - width / 2, y - height / 2, 0, x - width / 2, y + height / 2));
		ret.line.add(new Vertex(x + width / 2, y - height / 2, 0, x + width / 2, y + height / 2));
		return ret;
	}

	static public D_Face create2d(double... array) {
		if (array.length % 2 == 1) {
			return null;
		}
		D_Face ret = new D_Face();
		for (int n = 0; n < array.length / 2; n++) {
			Vertex v = new Vertex(array[2 * n], array[2 * n + 1]);
			ret.line.add(v);
		}
		return ret;
	}

	public static Polygon3D createRectangle(double x, double y, double z, double sizeX, double sizeY, double sizeZ) {
		double maxX = x + sizeX / 2;
		double maxY = y + sizeY / 2;
		double maxZ = z + sizeZ / 2;
		double minX = x - sizeX / 2;
		double minY = y - sizeY / 2;
		double minZ = z - sizeZ / 2;
		return new T_OrdinaryModel(
				new D_Face(new Vertex(maxX, maxY, maxZ, maxX, maxY), new Vertex(minX, maxY, maxZ, minX, maxY), new Vertex(minX, minY, maxZ, minX, minY), new Vertex(maxX, minY, maxZ, maxX, minY)),
				new D_Face(new Vertex(maxX, maxY, minZ, minX, maxY), new Vertex(maxX, minY, minZ, minX, minY), new Vertex(minX, minY, minZ, maxX, minY), new Vertex(minX, maxY, minZ, maxX, maxY)),
				new D_Face(new Vertex(maxX, maxY, maxZ, maxZ, maxX), new Vertex(maxX, maxY, minZ, minZ, maxX), new Vertex(minX, maxY, minZ, minZ, minX), new Vertex(minX, maxY, maxZ, maxZ, minX)),
				new D_Face(new Vertex(maxX, minY, maxZ, minZ, maxX), new Vertex(minX, minY, maxZ, minZ, minX), new Vertex(minX, minY, minZ, maxZ, minX), new Vertex(maxX, minY, minZ, maxZ, maxX)),
				new D_Face(new Vertex(maxX, maxY, maxZ, maxY, maxZ), new Vertex(maxX, minY, maxZ, minY, maxZ), new Vertex(maxX, minY, minZ, minY, minZ), new Vertex(maxX, maxY, minZ, maxY, minZ)),
				new D_Face(new Vertex(minX, maxY, maxZ, minY, maxZ), new Vertex(minX, maxY, minZ, minY, minZ), new Vertex(minX, minY, minZ, maxY, minZ), new Vertex(minX, minY, maxZ, maxY, maxZ)));
	}

	public static D_Ellipse createElipse(double i, double j, int k, int l) {
		return new D_Ellipse(Vec3.createVectorHelper(k, l, 0), Vec3.createVectorHelper(0, 0, 1), i, j);
	}

}
