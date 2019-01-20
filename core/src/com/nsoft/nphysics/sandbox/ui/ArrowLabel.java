package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Util;

public class ArrowLabel {

	public VisLabel label;
	
	public ArrowLabel(Group g) {
		label = new VisLabel();
		label.setStyle(new LabelStyle(FontManager.title, Color.WHITE));
		g.addActor(label);
	}
	
	public void setStyle(BitmapFont f,Color c) {
		label.setStyle(new LabelStyle(f,c));
	}
	public void setText(String str) {
		
		label.setText(str);
	}
	
	public void setColor(Color r) {
		label.setColor(r);
	}
	
	public Color getColor() {return label.getColor();}
	
	public void setFloat(float t) {
		
		label.setText(Util.notation(t) + "");
	}
	
	public void conc(String f) {
		
		label.setText(label.getText() + f);
	}
	public void setPosition(Vector2 p) {
		
		Vector3 np = NPhysics.currentStage.getCamera().project(new Vector3(p, 0));
		label.setPosition(np.x, np.y);
	}
	
	public void setVisible(boolean visible) {
		label.setVisible(visible);
	}
	
	public boolean isVisible() {
		return label.isVisible();
	}
}
