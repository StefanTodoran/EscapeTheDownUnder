package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadModelToVAO(float[] positions, float[] textureCoords, float[] normals, float[] shineVals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 1, shineVals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadFlatToVAO(float[] screenCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, screenCoords);
		unbindVAO();
		return new RawModel(vaoID, screenCoords.length / 2);
	}
	
	public RawModel loadFlatToVAO(float[] screenCoords, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, screenCoords);
		storeDataInAttributeList(0, 2, textureCoords);
		unbindVAO();
		return new RawModel(vaoID, screenCoords.length / 2);
	}
	
	public RawModel updateModelVAO(int vaoID, float[] positions, float[] textureCoords, float[] normals, float[] shineVals, int[] indices) {
		GL30.glBindVertexArray(vaoID);
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 1, shineVals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel updateFlatVAO(int vaoID, float[] screenCoords) {
		GL30.glBindVertexArray(vaoID);
		storeDataInAttributeList(0, 2, screenCoords);
		unbindVAO();
		return new RawModel(vaoID, screenCoords.length / 2);
	}
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			String path = "textures/" + fileName + ".png";
			texture = TextureLoader.getTexture("PNG", new FileInputStream(path), GL11.GL_NEAREST);
			
//			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
//			
//			if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
//				float amount = Math.min(4, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
//				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
//			} else {
//				System.out.println("(!) Anisotropic filtering not supported");
//			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	public void cleanUp() {
		for (int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		// May need to replace GL_STATIC_DRAW to be able to update VBOs?
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
