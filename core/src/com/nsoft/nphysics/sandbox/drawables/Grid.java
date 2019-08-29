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

package com.nsoft.nphysics.sandbox.drawables;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.sandbox.interfaces.VertexBuffer;

public class Grid implements VertexBuffer{

	private Vector2 point;
	private float size;
	private float anglerad;
	private float[][] buffervertices;
	
	public Grid(Vector2 point, float size, float angledeg) {
		
		this.point = point;
		this.size = size;
		this.anglerad = MathUtils.degRad * angledeg;
		
		createVertexArray();
	}
	public Vector2 getPoint() { return point; }
	public void setPoint(Vector2 point) { this.point = point; }
	public float getSize() { return size; }
	public void setSize(float size) { this.size = size; }
	public float getAnglerad() { return anglerad; }
	public void setAnglerad(float anglerad) { this.anglerad = anglerad; }
	
	@Override
	public float[][] getVertexBuffer() { return buffervertices; }
	
	@Override
	public void createVertexArray() {
		
		
	}
	@Override
	public void updateVertexArray() {
		
		
	}
	
}
