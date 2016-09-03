package com.mito.mitomod.client.render.model;

import com.mito.mitomod.BraceBase.CreateVertexBufferObject;
import com.mito.mitomod.BraceBase.VBOList;
import com.mito.mitomod.BraceBase.Brace.Brace;

public interface IDrawBrace {

	public void renderBraceAt(Brace brace, float partialTickTime);

	public void drawBrace(VBOList buffer, Brace brace);

	public void drawBraceSquare(CreateVertexBufferObject c, Brace brace);

	public void drawBraceTriangle(CreateVertexBufferObject buffer, Brace brace);

}
