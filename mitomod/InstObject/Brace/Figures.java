package com.mito.mitomod.InstObject.Brace;

import java.util.HashMap;
import java.util.Map;

public class Figures {

	public static Map<String, Figure> stringToFigureMapping = new HashMap<String, Figure>();
	public static Map<Figure, String> figureToStringMapping = new HashMap<Figure, String>();

	public static void addMapping(Figure figure, String name) {
		if (stringToFigureMapping.containsKey(name)) {
			throw new IllegalArgumentException("ID is already registered: " + name);
		} else {
			stringToFigureMapping.put(name, figure);
			figureToStringMapping.put(figure, name);
		}
	}

	public static Figure getFigure(String name) {
		return stringToFigureMapping.get(name);
	}

	static {
		addMapping(new Figure(new Plane(1, 1, 0, 0)), "square");
		addMapping(new Figure(new Plane(0.2, 1, 0, 0)), "vertical");
		addMapping(new Figure(new Plane(1, 0.2, 0, 0)), "horizontal");
		addMapping(new Figure(new Plane(1, 0.2, 0, 0.5), new Plane(1, 0.2, 0, -0.5), new Plane(0.2, 0.8, 0, 0)), "H-section");
	}

	public static String getName(Figure shape) {
		return figureToStringMapping.get(shape);
	}

}
