package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisTable;

public class OptionPane extends VisTable {

	
	@Override
	public void pack() {
		
		generateBackground();
		super.pack();
	}
	public void generateBackground() {
	
		Pixmap a = new Pixmap((int)getWidth(), (int)getHeight(), Pixmap.Format.RGB888);
		a.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));
		a.fillRectangle(0, 0, a.getWidth(), a.getHeight());
		setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(a))));
	}
	
	@Override
	public <T extends Actor> Cell<T> add(T actor) {
		
		if(!(actor instanceof Separator)) {
			if(getChildren().size != 0)addSeparator(false);
		}else {
			
			row();
			return super.add(actor);
		}
		
		Cell<T> a = super.add(actor);
		
		row();
		return a;
	}
	
}
