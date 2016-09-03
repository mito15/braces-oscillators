package com.mito.mitomod.client.render.model;

import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.BraceBase.VBOList;
import com.mito.mitomod.BraceBase.Brace.Brace;

public class BraceShapes implements IDrawBrace {

	public IDrawBrace[] planes;


	public BraceShapes(Polygon2D... list) {
		planes = list;
	}

	public void drawBrace(VBOList vbolist, Brace brace) {
		for (int n = 0; n < this.planes.length; n++) {
			IDrawBrace plane = this.planes[n];
			if (plane != null)
				plane.drawBrace(vbolist, brace);
		}
	}

	public void drawBraceSquare(CreateVertexBufferObject c, Brace brace) {
		for (int n = 0; n < this.planes.length; n++) {
			IDrawBrace plane = this.planes[n];
			if (plane != null)
				plane.drawBraceSquare(c, brace);
		}
	}

	public void drawBraceTriangle(CreateVertexBufferObject c, Brace brace) {
		for (int n = 0; n < this.planes.length; n++) {
			IDrawBrace plane = this.planes[n];
			if (plane != null)
				plane.drawBraceTriangle(c, brace);
		}
	}

	@Override
	public void renderBraceAt(Brace brace, float partialTickTime) {
		for (int n = 0; n < this.planes.length; n++) {
			IDrawBrace plane = this.planes[n];
			if (plane != null)
				plane.renderBraceAt(brace, partialTickTime);;
		}
	}

}
