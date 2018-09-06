package com.nsoft.nphysics.sandbox.ui;

import java.text.DecimalFormat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.nsoft.nphysics.NPhysics;

public class ArrowLabel {

	VisLabel label;
	static DecimalFormat f = new DecimalFormat("#.##");
	public ArrowLabel() {
		
		label = new VisLabel();
		label.setStyle(new LabelStyle(FontManager.title, Color.WHITE));
		NPhysics.ui.addActor(label);
	}
	
	public void setText(String str) {
		
		label.setText(str);
	}
	
	public void setColor(Color r) {
		label.setColor(r);
	}
	
	public void setFloat(float t) {
		
		label.setText(f.format(t) + "");
	}
	
	public void conc(String f) {
		
		label.setText(label.getText() + f);
	}
	public void setPosition(Vector2 p) {
		
		Vector3 np = NPhysics.currentStage.getCamera().project(new Vector3(p, 0));
		label.setPosition(np.x, np.y);
	}
}