/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Vertexbuffer {

	public static int MAX_PART_SIZE_QUADS = Quad.INDEXED_QUADS_COUNT / 2; //My PC can't work with more quads for some reason
	public static int MAX_PART_SIZE_FLOATS = MAX_PART_SIZE_QUADS * 16; //each quad is 16 floats
	public static int MAX_PART_SIZE_BYTES = MAX_PART_SIZE_FLOATS * 4; //each float is 4 bytes

	private int[] ids;
	private int buffersCount = 0;
	private FloatBuffer vertices;
	private int updateStart, updateEnd;

	private static final ArrayList<Vertexbuffer> buffers = new ArrayList<>();

	public Vertexbuffer( FloatBuffer vertices ) {
		ids = new int[0];
		buffersCount = 0;
		synchronized (buffers) {

			this.vertices = vertices;
			allocateBuffers();
			buffers.add(this);

			updateStart = 0;
			updateEnd = vertices.limit();
		}
	}

	private void allocateBuffers() {
		int newBuffersCount = vertices.limit() / MAX_PART_SIZE_FLOATS;
		if (vertices.limit() % MAX_PART_SIZE_FLOATS > 0) {
			newBuffersCount++;
		}
		if (newBuffersCount == 0) {
			newBuffersCount = 1;
		}
		if (newBuffersCount <= buffersCount) return;
		int[] newIds = new int[newBuffersCount];

		System.arraycopy(ids, 0, newIds, 0, buffersCount);
		for (int i = buffersCount; i < newBuffersCount; i++) {
			newIds[i] = Gdx.gl.glGenBuffer();
		}

		ids = newIds;
		buffersCount = newBuffersCount;
	}

	//For flagging the buffer for a full update without changing anything
	public void updateVertices(){
		updateVertices(vertices);
	}

	//For flagging an update with a full set of new data
	public void updateVertices( FloatBuffer vertices ){
		updateVertices(vertices, 0, vertices.limit());
	}

	//For flagging an update with a subset of data changed
	public void updateVertices( FloatBuffer vertices, int start, int end){
		this.vertices = vertices;
		allocateBuffers();
		if (updateStart == -1)
			updateStart = start;
		else
			updateStart = Math.min(start, updateStart);

		if (updateEnd == -1)
			updateEnd = end;
		else
			updateEnd = Math.max(end, updateEnd);
	}

	public void updateGlDataComplex() {
		int partSizeInElements = this.MAX_PART_SIZE_FLOATS;
		int startOffset = updateStart;
		int realEnd = updateEnd;
		((Buffer) vertices).position(updateStart);
		for (int part = startOffset / partSizeInElements; part <= (realEnd - 1) / partSizeInElements; part++) {
			int bufferOffset = Math.max(0, part * partSizeInElements);
			int length = Math.min(partSizeInElements, realEnd - bufferOffset); //todo fix this
			this.bind(part);
			vertices.position(bufferOffset);
			Gdx.gl.glBufferData(Gdx.gl.GL_ARRAY_BUFFER, length * 4, vertices.slice(), Gdx.gl.GL_DYNAMIC_DRAW);
			this.release();
		}
		updateStart = updateEnd = -1;
	}

	public void updateGLData(){
		if (updateStart == -1) return;

		if (buffersCount > 1) {
			//use special data update for big panel
			updateGlDataComplex();
			return;
		}

		((Buffer)vertices).position(updateStart);
		bind();

		if (updateStart == 0 && updateEnd == vertices.limit()){
			Gdx.gl.glBufferData(Gdx.gl.GL_ARRAY_BUFFER, vertices.limit()*4, vertices, Gdx.gl.GL_DYNAMIC_DRAW);
		} else {
			Gdx.gl.glBufferSubData(Gdx.gl.GL_ARRAY_BUFFER, updateStart*4, (updateEnd - updateStart)*4, vertices);
		}

		release();
		updateStart = updateEnd = -1;
	}

	public void bind(){
		Gdx.gl.glBindBuffer(Gdx.gl.GL_ARRAY_BUFFER, ids[0]);
	}

	public void bind(int part) {
		Gdx.gl.glBindBuffer(Gdx.gl.GL_ARRAY_BUFFER, ids[part]);
	}
	public void release(){
		Gdx.gl.glBindBuffer(Gdx.gl.GL_ARRAY_BUFFER, 0);
	}

	public void delete(){
		synchronized (buffers) {
			for (int bufferId : ids) {
				Gdx.gl.glDeleteBuffer(bufferId);
			}
			buffers.remove(this);
		}
	}

	public static void clear(){
		synchronized (buffers) {
			for (Vertexbuffer buf : buffers.toArray(new Vertexbuffer[0])) {
				buf.delete();
			}
		}
	}

	public static void reload(){
		synchronized (buffers) {
			for (Vertexbuffer buf : buffers) {
				buf.updateVertices();
				buf.updateGLData();
			}
		}
	}

}
