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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.Segment;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class AngleArcActor extends Actor implements Showable{

	Position center;
	Segment s;
	float radius;
	float angle;
	
	
	public AngleArcActor(Position center,Segment s) {
		
		this.center = center;
		this.s = s;
		update();
		setAlpha(0);
		setDebug(true);
	}
	

	@Override
	public void drawDebug(ShapeRenderer shapes) {} //EMPTY SO IT DOESN'T RENDER ANYTHING
	
	public void setAngle(float angle) { this.angle = angle; }
	public void setRadius(float newRadius) { radius = newRadius; }
	
	
	public void update() {
		
		setHeight(MathUtils.sinDeg(angle)*radius/3f);
		setWidth(radius/3f);	
	}
	
	boolean hit = false;
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		if (touchable && this.getTouchable() != Touchable.enabled) return null;
		if(getAlpha() == 0) return null;
		if(x > 0 && y > 0 && x < getWidth() && y < getHeight() && MathUtils.atan2(y, x)*MathUtils.radDeg < angle && x*x + y*y < radius/3f*radius/3f) {
			
			if(!hit)addAction(Actions.scaleTo(1.2f, 1.2f, 0.5f, Interpolation.exp5));
			s.hit =false;
			hit = true;
			return this;
		}else {
			
			if(hit)addAction(Actions.scaleTo(1f, 1f, 0.5f, Interpolation.exp5));
			hit = false;
			return null;
		}
		
	}
	
	final static Color arcColor = new Color(0.5f, 0.5f, 0.9f, 0.4f);
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		NPhysics.sandbox.shapefill.setColor(arcColor);
		
		NPhysics.currentStage.shapefill.arc(center.getX(), center.getY(), radius*getAlpha()*getScaleX()/3f, 0, angle);
		Sandbox.bitmapfont.setColor(Color.BLACK);
		Sandbox.bitmapfont.draw(batch, "Ángulo actual" + angle, 100, 100);
		
		super.draw(batch, parentAlpha);
	}
}
