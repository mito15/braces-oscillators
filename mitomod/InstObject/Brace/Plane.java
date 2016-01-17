package com.mito.mitomod.InstObject.Brace;

import java.util.ArrayList;
import java.util.List;

public class Plane {

	public List<Vertex> line = new ArrayList<Vertex>();

	Plane(Vertex... list) {
		for (int n = 0; n < list.length; n++) {
			line.add(list[n]);
		}
	}

	Plane(double... list) {
		for (int n = 0; n < list.length / 3; n++) {
			Vertex v = new Vertex(list[3 * n], list[3 * n + 1], list[3 * n + 2], 0, 0);
			this.line.add(v);
		}
	}

	Plane(double width, double height, double x, double y) {
		line.add(new Vertex(x + width / 2, y + height / 2, 0, x + width / 2, y - height / 2));
		line.add(new Vertex(x - width / 2, y + height / 2, 0, x - width / 2, y - height / 2));
		line.add(new Vertex(x - width / 2, y - height / 2, 0, x - width / 2, y + height / 2));
		line.add(new Vertex(x + width / 2, y - height / 2, 0, x + width / 2, y + height / 2));
	}

}
