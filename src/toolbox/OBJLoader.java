package toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;

public class OBJLoader {

//	public static RawModel loadObjModel(String fileName, Loader loader) {
//		FileReader fr = null;
//		try {
//			fr = new FileReader(new File(fileName));
//		} catch (FileNotFoundException e) {
//			System.err.println("Could not load file " + fileName);
//			e.printStackTrace();
//		}
//		
//		BufferedReader reader = new BufferedReader(fr);
//		String line;
//		
//		List<Vector3f> vertices = new ArrayList<Vector3f>();
//		List<Vector2f> textures = new ArrayList<Vector2f>();
//		List<Vector3f> normals = new ArrayList<Vector3f>();
//		List<Integer> indices = new ArrayList<Integer>();
//		
//		float[] verticesArray = null;
//		float[] normalsArray = null;
//		float[] texturesArray = null;
//		int[] indicesArray = null;
//		
//		try {
//			while (true) {
//				line = reader.readLine();
//				String[] currentLine = line.split(" ");
//				if (line.startsWith("v ")) {
//					Vector3f vertex = new Vector3f(
//							Float.parseFloat(currentLine[1]),
//							Float.parseFloat(currentLine[2]),
//							Float.parseFloat(currentLine[3])
//					);
//					vertices.add(vertex);
//				} else if (line.startsWith("vt ")) {
//					Vector2f texture = new Vector2f(
//							Float.parseFloat(currentLine[1]),
//							Float.parseFloat(currentLine[2])
//					);
//					textures.add(texture);
//				} else if (line.startsWith("vn ")) {
//					Vector3f normal = new Vector3f(
//							Float.parseFloat(currentLine[1]),
//							Float.parseFloat(currentLine[2]),
//							Float.parseFloat(currentLine[3])
//					);
//					normals.add(normal);
//				} else if (line.startsWith("f ")) {
//					normalsArray = new float[vertices.size()*3];
//					texturesArray = new float[vertices.size()*2];
//					break;
//				}
//			}
//			
//			while (line != null) {
//				if (!line.startsWith("f ")) {
//					line = reader.readLine();
//					continue;
//				}
//				
//				String[] currentLine = line.split(" ");
//				String[] vertex1 = currentLine[1].split("/");
//				String[] vertex2 = currentLine[2].split("/");
//				String[] vertex3 = currentLine[3].split("/");
//				
//				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
//				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
//				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
//				line = reader.readLine();
//			}
//			
//			reader.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		verticesArray = new float[vertices.size() * 3];
//		indicesArray = new int[indices.size()];
//		
//		int vertPtr = 0;
//		for (Vector3f vertex:vertices) {
//			verticesArray[vertPtr++] = vertex.x;
//			verticesArray[vertPtr++] = vertex.y;
//			verticesArray[vertPtr++] = vertex.z;
//		}
//		
//		for (int i = 0; i < indices.size(); i++) {
//			indicesArray[i] = indices.get(i);
//		}
//		
//		return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
//	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, 
			List<Vector3f> normals, float[] texturesArray, float[] normalsArray) {
		int curPtr = Integer.parseInt(vertexData[0]) - 1;
		indices.add(curPtr);
		
		Vector2f curTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		texturesArray[curPtr * 2] = curTex.x;
		texturesArray[curPtr * 2 + 1] = 1 - curTex.y;
		// subtract one since opengl starts at top left, obj starts bottom left
		
		Vector3f curNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[curPtr * 3] = curNorm.x;
		normalsArray[curPtr * 3 + 1] = curNorm.y;
		normalsArray[curPtr * 3 + 2] = curNorm.z;
	}
}
