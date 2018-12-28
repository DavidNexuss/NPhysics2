package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.sandbox.Util;

public class Scale extends VisTable{

	VisLabel text;
	
	public Scale() {
		
		text = new VisLabel();
		text.setStyle(new LabelStyle(FontManager.title, Color.BLACK));
		add(text).fill().center();
	}
	
	@Override
	public void act(float delta) {
		
		text.setText(Util.getFancyMeterFactor());
		super.act(delta);
	}
	
}
