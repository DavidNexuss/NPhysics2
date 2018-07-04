package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.Util;

public class PolygonObject extends Actor{

	PolygonDefinition def;
	float[][] vert;
	float[][] buff;
	Body b;
	
	public PolygonObject(PolygonDefinition def) {
		
		this.def = def;
		initVertexBuffer();
		createObject();
	}
	
	final Color col = new Color(0.2f, 0.8f, 0.2f, 0.5f);
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		SimulationStage.fill.setColor(col);
		
		for (int i = 0; i < vert.length; i++) {
			
			float x1 = vert[i][0];
			float y1 = vert[i][1];
			float x2 = vert[i][2];
			float y2 = vert[i][3];
			float x3 = vert[i][4];
			float y3 = vert[i][5];
			
			x1 = Util.rotx(x1, y1, b.getAngle());
			y1 = Util.roty(x1, y1, b.getAngle());
			x2 = Util.rotx(x2, y2, b.getAngle());
			y2 = Util.roty(x2, y2, b.getAngle());
			x3 = Util.rotx(x3, y3, b.getAngle());
			y3 = Util.roty(x3, y3, b.getAngle());
			
			Vector2 pos = b.getPosition();
			
			x1 += pos.x;
			y1 += pos.y;
			x2 += pos.x;
			y2 += pos.y;
			x3 += pos.x;
			y3 += pos.y;
			
			
			x1 *= Util.UNIT;
			y1 *= Util.UNIT;
			x2 *= Util.UNIT;
			y2 *= Util.UNIT;
			x3 *= Util.UNIT;
			y3 *= Util.UNIT;
			
			SimulationStage.fill.triangle(x1, y1, x2, y2, x3, y3);
		}
	}
	private void createObject() {
		

		BodyDef bdef = new BodyDef();
		bdef.type = def.type;
		bdef.position.set(def.getCenter(true));
		b = SimulationStage.world.createBody(bdef);
		createFixtures();
	}
	
	private ArrayList<Fixture> createFixtures(){
		
		ArrayList<Fixture> fixtures = new ArrayList<>();
		
		for (int i = 0; i < vert.length; i++) {
			
			FixtureDef fdef = new FixtureDef();
			fdef.density = def.density;
			fdef.friction = def.friction;
			fdef.restitution = def.restitution;
			
			PolygonShape shape = new PolygonShape();
			shape.set(vert[i]);
			fdef.shape = shape;
			b.createFixture(fdef);
		}
		
		return fixtures;
	}

	private void initVertexBuffer() {
		
		vert = def.getTriangles(true, true);
		buff = new float[vert.length][6];
	}
}
