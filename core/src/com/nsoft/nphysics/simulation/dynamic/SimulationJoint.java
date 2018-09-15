package com.nsoft.nphysics.simulation.dynamic;

import java.text.DecimalFormat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;
import com.nsoft.nphysics.sandbox.ui.FontManager;

public class SimulationJoint {

	ArrowLabel xlabel;
	ArrowLabel ylabel;
	
	SimpleArrow xreaction;
	SimpleArrow yreaction;
	
	private Joint j;
	
	static DecimalFormat format = new DecimalFormat("#.##");
	
	public SimulationJoint(Joint j) {
		
		xreaction = new SimpleArrow(j.getAnchorA(), new Vector2());
		yreaction = new SimpleArrow(j.getAnchorA(), new Vector2());
		
		xreaction.setColor(Color.GREEN);
		yreaction.setColor(Color.GREEN);
		
		xlabel = new ArrowLabel();
		ylabel = new ArrowLabel();
		
		xlabel.setStyle(FontManager.subtitle, Color.GREEN);
		ylabel.setStyle(FontManager.subtitle, Color.GREEN);
		
		this.j = j;
	}
	
	public Vector2 getPosition() {
		
		return j.getAnchorA().scl(Util.UNIT);
	}
	
	final Vector2 reaction = new Vector2();
	final Vector2 dreaction = new Vector2();
	final Vector2 jointPos = new Vector2();
	
	
	public void draw(Batch batch,float parentAlpha) {
		
		if(SimulationStage.active) {
		
			reaction.set(j.getReactionForce(1f / SimulationStage.getPhysicsDelta()).scl(-1f));
			dreaction.set(new Vector2(reaction).scl(Util.UNIT / SimulationStage.ForceMultiplier));
			jointPos.set(getPosition());
			
			
			xreaction.setStart(jointPos);
			xreaction.setEnd(new Vector2(dreaction).scl(1, 0).add(jointPos));
			
			yreaction.setStart(jointPos);
			yreaction.setEnd(new Vector2(dreaction).scl(0, 1).add(jointPos));
			
			xreaction.updateVertexArray();
			yreaction.updateVertexArray();
			
			xlabel.setText(format.format(reaction.x) + "N");
			ylabel.setText(format.format(reaction.y) + "N");
		}
		
		xreaction.draw(batch, parentAlpha);
		yreaction.draw(batch, parentAlpha);
		
		xlabel.setPosition(new Vector2(jointPos).add(50, -50));
		ylabel.setPosition(new Vector2(jointPos).add(-120,50));
	}
}
