package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.drawables.Spring;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

public class SpringComponent extends RawJoint implements ClickIn,Parent<Point>{

	public Spring spring = new Spring();
	
	public static SpringComponent tmp;
	
	public SpringComponent() {
		defaultInit();
		addInput();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		spring.render();
	}
	public void addAnchorA(Point A) {
		if(spring.isEnd()) throw new IllegalStateException();
		spring.addAnchorA(A);
		A.setObjectParent(this);
	}
	
	public void addAnchorB(Point B) {
		if(spring.isEnd())throw new IllegalStateException();
		spring.addAnchorB(B);
		B.setObjectParent(this);
	}
	
	public Position getAnchorA() { return spring.getAnchorA();}
	public Position getAnchorB() { return spring.getAnchorB();}
	
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		spring.updateSpring();
	}

	@Override
	public ArrayList<Point> getChildList() {
		return null;
	}

	@Override
	public boolean isInside(float x, float y) {

		return spring.isInside(x, y);
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY())) ? this : null;
	}
	
	@Override
	public void select(int pointer) {
		spring.selectedFlag = true;
	}

	@Override
	public void unselect() {
		spring.selectedFlag = false;
	}

	@Override
	public SelectHandle getHandler() {
		return Sandbox.mainSelect;
	}

}
