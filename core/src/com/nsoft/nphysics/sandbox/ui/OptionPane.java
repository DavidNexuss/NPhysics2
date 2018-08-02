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
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class OptionPane extends VisTable implements Showable {

	static final Color initial = new Color(0.4f, 0.4f, 0.4f, 1f);
	public OptionPane() {
		
		setColor(initial);
	}
	@Override
	public void pack() {
		
		setBackground(generateBackground(this));
		super.pack();
	}
	public static TextureRegionDrawable generateBackground(Table t) {
	
		Pixmap a = new Pixmap((int)t.getWidth(), (int)t.getHeight(), Pixmap.Format.RGB888);
		a.setColor(t.getColor());
		a.fillRectangle(0, 0, a.getWidth(), a.getHeight());
		return new TextureRegionDrawable(new TextureRegion(new Texture(a)));
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
	
	@Override
	public void show() {
		
		if(!isVisible()) {
			Showable.super.show();
			setVisible(true);
		}
	}
	
	@Override
	public void hide() {
		
		if(isVisible()) {
			Showable.super.hide();
			ThreadManager.createTask(()->{setVisible(false);}, getFadeDuration());
		}
	}
	
	@Override
	public void act(float delta) {
		
		super.act(delta);
		
	}
	@Override
	public float getFadeDuration() {return 0.2f;}
}
