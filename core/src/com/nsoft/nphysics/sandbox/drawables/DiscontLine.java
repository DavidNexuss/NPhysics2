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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;

public class DiscontLine extends Actor{

	private Vector2 A,B;
	private Vector2 Offset = new Vector2();
	private Color color = Color.BLACK;
	private boolean hook;
	
	public DiscontLine(Vector2 A,Vector2 B) {
		this.A = A;
		this.B = B;
	}
	
	public void setPositionA(Vector2 A) {this.A = A;}
	public void setPositionB(Vector2 B) {this.B = B;}
	public void setOffset(Vector2 offset) {Offset = offset;}
	public void hook(boolean hook) {this.hook = hook;}
	
	public Vector2 getDiff() {
		
		return new Vector2(B).sub(A);
	}
	@Override
	public void act(float delta) {
		
		if(hook) {
			
			B.set(NPhysics.currentStage.getUnproject(false).sub(Offset));
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Gdx.gl.glLineWidth(3);
		NPhysics.currentStage.shapeline.begin(ShapeType.Line);
		NPhysics.currentStage.shapeline.setColor(color);
		
		Vector2 v = new Vector2(B).sub(A);
		
		Vector2 v1 = new Vector2().setAngle(v.angle());
		Vector2 v2 = new Vector2(15,0).setAngle(v.angle());
		float lenght = v.len();
		
		for (int i = 0; i < lenght; i+= 30) {
			
			v1.set(i, 0);
			v1.setAngle(v.angle());
			NPhysics.currentStage.shapeline.line(v1.x + A.x, v1.y + A.y, v1.x + v2.x + A.x, v1.y + v2.y + A.y);
		}
		
		NPhysics.currentStage.shapeline.end();
	}
}
