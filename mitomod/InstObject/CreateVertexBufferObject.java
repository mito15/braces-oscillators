package com.mito.mitomod.InstObject;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import com.mito.mitomod.InstObject.Brace.Vertex;

import net.minecraft.util.Vec3;


public class CreateVertexBufferObject {

	public static CreateVertexBufferObject INSTANCE = new CreateVertexBufferObject();
	public List<Float> vertexDataList = new ArrayList<Float>();
	public List<Float> brightnessDataList = new ArrayList<Float>();
	private int size = 0;
	private double scale = 1;
	private float[] colorArray = new float[4];
	private float[] textureArray = new float[2];
	private float[] normalArray = new float[3];
	private float[] brightnessArray = new float[3];
	// 0:all 1:no color 2:no texture
	private int mode;
	private int usage;
	private VBOHandler p;
	private boolean hasBrightness = false;

	public void beginRegist(int usage, int mode) {
		this.p = new VBOHandler();
		this.size = 0;
		this.vertexDataList.clear();
		this.brightnessDataList.clear();
		this.hasBrightness = false;
		this.usage = usage;
		this.p.usage = usage;
		this.mode = mode;
		this.p.mode = mode;
		this.scale = 1;
	}

	public VBOHandler end() {
		float[] vertexData = ArrayUtils.toPrimitive(vertexDataList.toArray(new Float[0]));

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexData.length);
		vertexBuffer.put(vertexData);
		vertexBuffer.flip();

		this.p.size = this.size;
		this.p.buffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.p.buffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, this.usage);

		if (this.hasBrightness) {
			float[] brightnessData = ArrayUtils.toPrimitive(brightnessDataList.toArray(new Float[0]));

			FloatBuffer brightnessBuffer = BufferUtils.createFloatBuffer(brightnessData.length);
			brightnessBuffer.put(brightnessData);
			brightnessBuffer.flip();

			this.p.brightness = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.p.brightness);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, brightnessBuffer, GL15.GL_DYNAMIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		} else {
			this.p.brightness = 0;
		}

		return this.p;
	}

	public void registVertex(float f, float g, float h) {
		List<Float> list;
		this.size++;
		list = Arrays.asList(f, g, h, this.textureArray[0], this.textureArray[1], this.normalArray[0], this.normalArray[1], this.normalArray[2], this.colorArray[0], this.colorArray[1], this.colorArray[2], this.colorArray[3]);
		vertexDataList.addAll(list);
		brightnessDataList.add(this.brightnessArray[0]);
		brightnessDataList.add(this.brightnessArray[1]);
	}

	public void registVertex(double f, double g, double h) {
		registVertex((float) f, (float) g, (float) h);
	}

	public void registVertexWithUV(float f, float g, float h, float i, float j) {
		List<Float> list;
		this.size++;
		list = Arrays.asList(f, g, h, i, j, this.normalArray[0], this.normalArray[1], this.normalArray[2], this.colorArray[0], this.colorArray[1], this.colorArray[2], this.colorArray[3]);
		vertexDataList.addAll(list);
		brightnessDataList.add(this.brightnessArray[0]);
		brightnessDataList.add(this.brightnessArray[1]);
	}

	public void registVertexWithUV(double f, double g, double h, double i, double j) {
		registVertexWithUV((float) f, (float) g, (float) h, (float) i, (float) j);
	}
	
	public void registVertexWithUV(Vec3 vec, double u, double v) {
		registVertexWithUV(vec.xCoord, vec.yCoord, vec.zCoord, u, v);
	}
	
	public void registVertexWithUV(Vertex v) {
		registVertexWithUV(v.pos, v.textureU, v.textureV);
	}

	public void setBrightness(float f, float g) {
		this.hasBrightness = true;
		this.brightnessArray[0] = (float) f;
		this.brightnessArray[1] = (float) g;
	}

	public void setTextureUV(float f, float g) {
		this.textureArray[0] = f;
		this.textureArray[1] = g;
	}

	public void setNormal(float f, float g, float h) {
		this.normalArray[0] = f;
		this.normalArray[1] = g;
		this.normalArray[2] = h;
	}

	public void setNormal(double f, double g, double h) {
		this.normalArray[0] = (float) f;
		this.normalArray[1] = (float) g;
		this.normalArray[2] = (float) h;
	}

	public void setColor(float f, float g, float h, float i) {
		this.colorArray[0] = f;
		this.colorArray[1] = g;
		this.colorArray[2] = h;
		this.colorArray[3] = i;
	}

	public void setColor(float f, float g, float h) {
		this.colorArray[0] = f;
		this.colorArray[1] = g;
		this.colorArray[2] = h;
		this.colorArray[3] = 1.0f;
	}

	public void setScale(double size) {
		this.scale = size;

	}

}
