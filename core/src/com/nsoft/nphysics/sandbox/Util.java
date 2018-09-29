package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;

public class Util {


	public static <T> boolean isInstance(Class<T> clas,Object p) {
		try {
			
			T a = (T)p;
			
			return true;
		} catch (ClassCastException  e) {
			
			return false;
		}
	}
	
	public static int UNIT = 30;
	
	public static float METER_FACTOR = 1;
	public static float NEWTON_FACTOR = 10;
	
	public static float notation(float n) {
		
		return ((int)(n*100))/100f;
	}
	/*
	 * p[0] =  A.x
	 * p[1] =  A.y
	 * p[2] =  B.x
	 * p[3] =  B.y
	 * p[4] =  C.x
	 * p[5] =  C.y
	 */
	public static float triangleArea(float ... p) {
		
		return Math.abs((p[0]*(p[3] - p[5]) + p[2]*(p[5] - p[1]) + p[4]*(p[1] - p[3]))/2);
	}
	
	public static void renderPolygonPos(ShapeRenderer rend,ArrayList<PositionVector> points,ArrayList<Integer> indexes) {
		
		for (int i = 0; i < indexes.size(); i+=3) {
			
			rend.triangle(points.get(indexes.get(i)).getX(), 
									   points.get(indexes.get(i)).getY(), 
									   points.get(indexes.get(i + 1)).getX(), 
									   points.get(indexes.get(i + 1)).getY(), 
									   points.get(indexes.get(i + 2)).getX(), 
									   points.get(indexes.get(i + 2)).getY());
		}
	}

	public static void renderPolygon(ShapeRenderer rend,ArrayList<Point> points,List<Integer> indexes) {
		
		for (int i = 0; i < indexes.size(); i+=3) {
			
			rend.triangle(points.get(indexes.get(i)).getX(), 
									   points.get(indexes.get(i)).getY(), 
									   points.get(indexes.get(i + 1)).getX(), 
									   points.get(indexes.get(i + 1)).getY(), 
									   points.get(indexes.get(i + 2)).getX(), 
									   points.get(indexes.get(i + 2)).getY());
		}
	}
	public static float rotx(float x,float y,float rad) {
		
		return x*MathUtils.cos(rad) - y*MathUtils.sin(rad);
	}
	
	public static float roty(float x,float y,float rad){
		
		return y*MathUtils.cos(rad) + x*MathUtils.sin(rad);
	}
	
	public static float rotVectorX(Vector2 point,float anglerad) {
		
		return rotx(point.x, point.y, anglerad);
	}
	
	public static float rotVectorY(Vector2 point,float anglerad) {
		
		return roty(point.x, point.y, anglerad);
	}
	public static Vector2 rotPivot(Vector2 pivot,Vector2 point,float anglerad) {
		
		Vector2 dif = new Vector2(point).sub(pivot);
		
		float x;
		float y;
		
		x = rotVectorX(dif, anglerad);
		y = rotVectorY(dif, anglerad);
		
		return new Vector2(x, y).add(pivot);
	}
	
	public static Vector2 rot(Vector2 point,float anglerad) {
		
		return rotPivot(new Vector2(), point, anglerad);
	}
	
	public static void proj(float[][] vertices,float[][] buffer,float xoffset,float yoffset,float anglerad) {
		
		for (int i = 0; i < buffer.length; i++) {
			
			buffer[i][0] = rotx(vertices[i][0], vertices[i][1], anglerad) + xoffset;
			buffer[i][1] = roty(vertices[i][0], vertices[i][1], anglerad) + yoffset;
		}
	}
	
	public static Drawable getDrawable(Texture t) {
		
		return new TextureRegionDrawable(new TextureRegion(t));
	}
	
	static GlyphLayout layout;
	static BitmapFont current = new Label("",VisUI.getSkin()).getStyle().font;
	
	public static BitmapFont getNormalFont() {return current;}
	public static String capable(float dimensions,String text) {
		
		layout = new GlyphLayout(current, text);
		
		String[] parts = text.split(" ");
		if(parts.length == 1) return parts[0];
		
		ArrayList<String> lines = new ArrayList<>();
		lines.add(parts[0]);
		int c = 0;
		
		for (int i = 1; i < parts.length; i++) {
			
			if(new GlyphLayout(current, lines.get(c) + " " + parts[i]).width > dimensions) {
				
				lines.add(parts[i]);
				c++;
			}else {
				
				lines.set(c, lines.get(c) + " " + parts[i]);
			}
		}
		
		String fstring = "";
		for (String string : lines) {
			
			fstring += string != lines.get(lines.size() - 1) ? string + "\n" : string;
		}
		
		return fstring;
	}
}
