/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics.sandbox.interfaces;

import com.nsoft.nphysics.NPhysics;

public interface VertexBuffer {

	void createVertexArray();
	void updateVertexArray();
	
	float[][] getVertexBuffer();
	
	default float[][] createEmptyVertexArray(int size){
		
		return new float[size][2];
	}
	default float[] getVertex(int index) {
		
		return getVertexBuffer()[index];
	}
	
	default float getVX(int index) {
		
		return getVertex(index)[0];
	}
	
	default float getVY(int index) {
		
		return getVertex(index)[1];
	}
	
	default void drawTriangle(int v1,int v2,int v3) {
		
		NPhysics.currentStage.shapefill.triangle(getVX(v1), getVY(v1), getVX(v2), getVY(v2), getVX(v3), getVY(v3));
	}
}
