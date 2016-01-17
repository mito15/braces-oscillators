package com.mito.mitomod.InstObject.Brace;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.mito.mitomod.InstObject.CreateVertexBufferObject;
import com.mito.mitomod.InstObject.VBOHandler;

public class Figure {

	public List<Plane> planes = new ArrayList<Plane>();
	private VBOHandler buffer;

	Figure(List<Plane> list) {
		planes = list;
	}

	Figure(Plane... list) {
		for (int n = 0; n < list.length; n++) {
			planes.add(list[n]);
		}
	}

	public VBOHandler getvbo() {
		if (this.buffer == null) {
			CreateVertexBufferObject c = CreateVertexBufferObject.INSTANCE;

			c.beginRegist(GL15.GL_STATIC_DRAW, GL11.GL_TRIANGLES);
			c.setColor(1f, 1f, 1f, 1f);
			c.setNormal(0f, 0f, 1f);

			for (int n = 0; n < this.planes.size(); n++) {
				Plane plane = this.planes.get(n);
				Vertex v1 = plane.line.get(0);
				for (int n1 = 0; n1 < plane.line.size()-2; n1++) {
					Vertex v2 = plane.line.get(n1+1);
					Vertex v3 = plane.line.get(n1+2);
					c.registVertexWithUV(v1);
					c.registVertexWithUV(v2);
					c.registVertexWithUV(v3);
				}
			}

			this.buffer = c.end();
		}

		return this.buffer;
	}

}
