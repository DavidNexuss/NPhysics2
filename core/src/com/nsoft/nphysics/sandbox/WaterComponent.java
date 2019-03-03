package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;

public class WaterComponent extends Actor implements Parent<Point>,Removeable{

	private Point a,b,c,d;
	public Vector2 va,vb,vc,vd;
	final static Color color = new Color(0.2f, 0.2f, 0.8f, 0.6f);
	public WaterComponent(Point a,Point b,Point c,Point d) {
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		
		a.setObjectParent(this);
		b.setObjectParent(this);
		c.setObjectParent(this);
		d.setObjectParent(this);
		
		va = new Vector2();
		vb = new Vector2();
		vc = new Vector2();
		vd = new Vector2();
		
		updateVertexs();
	}

	private void updateVertexs() {
		
		va.set(a.getVector());
		vb.set(b.getVector());
		vc.set(c.getVector());
		vd.set(d.getVector());
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {

		NPhysics.currentStage.shapefill.setColor(color);
		NPhysics.currentStage.shapefill.triangle(va.x, va.y, vb.x, vb.y, vc.x, vc.y);
		NPhysics.currentStage.shapefill.triangle(vb.x, vb.y, vc.x, vc.y, vd.x, vd.y);
	}
	@Override
	public void updatePosition(float x, float y, Point p) {
		updateVertexs();
	}

	@Override
	public ArrayList<Point> getChildList() {
		
		return null;
	}

	@Override
	public boolean remove() {
		return super.remove();
	}
}
