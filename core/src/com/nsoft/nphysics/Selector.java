package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;

public class Selector extends Actor implements Say{

	public static final Color color = new Color(0.5f, 0.5f, 1f, 0.4f);
	
	private final Vector2 start = new Vector2();
	private final Vector2 end = new Vector2();
	
	private float width,height;
	
	public ArrayList<Point> pool = new ArrayList<>();
	public Selector() {
	
		setVisible(false);
		getColor().a = 0;
	}
	
	public void setStart(Vector2 v) {start.set(v); update();}
	public void setEnd(Vector2 v) {end.set(v); update();}

	private final Vector2 dst = new Vector2();
	
	private final Vector2 low = new Vector2();
	private final Vector2 high = new Vector2();
	private void update() {
		
		low.set(start.x < end.x ? start.x : end.x, start.y < end.y ? start.y : end.y);
		high.set(start.x > end.x ? start.x : end.x, start.y > end.y ? start.y : end.y);
		
		dst.set(new Vector2(end).sub(start));
		width = dst.x;
		height = dst.y;
	}
	private void updatePool() {

		if(!isVisible() || getColor().a == 0) return;
		ArrayList<Point> newList = new ArrayList<>();
		
		for (Point point : pool) {
			Vector2 posPoint = new Vector2(point.getX(), point.getY());
			if(!(posPoint.x > low.x && posPoint.x < high.x && posPoint.y > low.y && posPoint.y < high.y))
				newList.add(point);
				//if(point.isSelected())Sandbox.mainSelect.unSelect(point);
		}
		for (Point point : Point.allpoints) {
			
			Vector2 posPoint = new Vector2(point.getX(), point.getY());
			if(posPoint.x > low.x && posPoint.x < high.x && posPoint.y > low.y && posPoint.y < high.y) {
				
				say(posPoint);
				newList.add(point);
			}
		}
		
		pool = newList;
		
		for (Point point : pool) {
			
			if(!point.isSelected()) NPhysics.sandbox.mainSelect.setSelected(point);
		}
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		NPhysics.currentStage.shapefill.setColor(color.r, color.g, color.b, color.a * getColor().a);
		NPhysics.currentStage.shapefill.rect(start.x, start.y, width, height);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		/*if((!isVisible() || getColor().a == 0) && pool.size() > 0) {
			
			for (Point point : pool) {
				Sandbox.mainSelect.unSelect(point);
			}
			
			pool.clear();
		}*/
		updatePool();
	}
}
