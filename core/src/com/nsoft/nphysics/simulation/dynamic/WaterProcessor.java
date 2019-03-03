package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.SutherlandHodgman;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.WaterComponent;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage.Simulation;

import earcut4j.Earcut;
import earcut4j.EarcutFloat;

public class WaterProcessor implements ForceProcessor,Say{

	WaterComponent component;
	Simulation s;
	
	Vector2 A,B,C,D;
	Polygon polygon_frame;
	float LX,LY,HX,HY;
	public WaterProcessor(WaterComponent component,Simulation s) {
		this.s = s;
		this.component = component;
		
		A = new Vector2(component.va).scl(1f/Util.METERS_UNIT());
		B = new Vector2(component.vb).scl(1f/Util.METERS_UNIT());
		C = new Vector2(component.vc).scl(1f/Util.METERS_UNIT());
		D = new Vector2(component.vd).scl(1f/Util.METERS_UNIT());
		
		float[] buff = new float[8];
		buff[0] = A.x; buff[1] = A.y;
		buff[2] = B.x; buff[3] = B.y;
		buff[4] = D.x; buff[5] = D.y;
		buff[6] = C.x; buff[7] = C.y;
		
		for (int i = 0; i < buff.length; i++) {
			say(buff[i]);
		}
		polygon_frame = new Polygon(buff);
		
	}
	
	ArrayList<SimpleArrow> arrows = new ArrayList<>();
	ArrayList<float[]> buffer = new ArrayList<>();
	ArrayList<ArrowLabel> labels = new ArrayList<>();
	@Override
	public void processForce() {
		
		HashMap<PolygonObject, Polygon> map = new HashMap<>();
		for (PolygonObject polygonObject : s.objects) {
			if(polygonObject.b.isActive() && polygonObject.b.getType() == BodyType.DynamicBody) {
				
				if(polygonObject.def instanceof PolygonDefinition) {
					PolygonDefinition ndef = (PolygonDefinition)polygonObject.def;
					map.put(polygonObject, new Polygon(ndef.updateVertxArray(polygonObject.b.getPosition(), polygonObject.b.getAngle())));
				}
			}
		}
		
		for (PolygonObject obj : map.keySet()) {
			
			boolean clip = false;
			Polygon pol = map.get(obj);
			for (int i = 0; i < map.get(obj).getVertices().length; i+=2) {
				
				float x = pol.getVertices()[i];
				float y = pol.getVertices()[i+1];
				if(x > A.x && x < B.x && y > A.y && y < C.y)continue;
				else {
					
					clip = true;
					break;
				}
			}
	//		System.out.println(s.worldTime + "," + obj.b.getPosition().y);
			float f = 0;
			if(clip) {
				
				List<float[]> l = SutherlandHodgman.clipPolygon(polygon_frame.getVertices(), map.get(obj).getVertices());
				if(l.size() < 2) continue;
				float[] t = new float[l.size()*2];
				
				for (int i = 0; i < l.size()*2; i+=2) {
					
					t[i] = l.get(i/2)[0];
					t[i+ 1] = l.get(i/2)[1];
					
				}
				
				Polygon p = new Polygon(t);
				Vector2 center = GeometryUtils.polygonCentroid(t, 0, t.length, new Vector2());
				f = 9.8f*Math.abs(p.area());
				obj.b.applyForce(0, f, center.x, center.y, true);
				
				Vector2 position = new Vector2(center.x,center.y).scl(Util.METERS_UNIT());
				SimpleArrow fa = new SimpleArrow(position, new Vector2(position).add(0,f*Util.METERS_UNIT()/Util.NEWTON_FACTOR));
				fa.setColor(Color.RED);
				arrows.add(fa);
				
				for (int i = 0; i < l.size()*2; i+=2) {
					
					t[i] *= Util.METERS_UNIT();
					t[i+ 1] *= Util.METERS_UNIT();
					
				}

				buffer.add(t);
			}else {
				
				f = 9.8f*Math.abs(pol.area());
				obj.b.applyForceToCenter(0, f, true);
				Vector2 position = new Vector2(obj.b.getPosition()).scl(Util.METERS_UNIT());
				SimpleArrow fa = new SimpleArrow(position, new Vector2(position).add(0,f*Util.METERS_UNIT()/Util.NEWTON_FACTOR));
				fa.setColor(Color.RED);
				arrows.add(fa);
			}
			
		}
	}

	@Override
	public void render() {
		
		component.draw(null, 0);
		for (SimpleArrow ar : arrows) {
			ar.draw(null, 1);
		}
		arrows.clear();
		
		for (float[] b : buffer) {
			
			List<Integer> indexes = EarcutFloat.earcut(b);
			NPhysics.currentStage.shapefill.setColor(1, 0, 0, 0.4f);
			Util.renderPolygon(NPhysics.currentStage.shapefill, b, indexes);
		}
		buffer.clear();
	}
}
