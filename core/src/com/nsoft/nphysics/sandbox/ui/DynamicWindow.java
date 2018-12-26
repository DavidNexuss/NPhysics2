package com.nsoft.nphysics.sandbox.ui;

import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Form;

public class DynamicWindow extends BaseOptionWindow {

	private static DynamicWindow copyBuffer;
	
	private VisTable main;
	
	VisImageButton copy;
	VisImageButton paste;
	
	public DynamicWindow(String title,Form form) {
		super(title,form);
	}
	
	
	public void paste() {
		
		if(copyBuffer == null) return;
		if(copyBuffer == this) return;
		if(copyBuffer.form.getClass() == form.getClass()) {
			
			for (Entry<String, Option> option: copyBuffer.options.entrySet()) {
				
				if(option.getValue().canCopy)options.get(option.getKey()).setValue(option.getValue().getValue());
			}
			
			form.updateValuesFromForm();
		}else throw new IllegalStateException();
	}
	public void copy() {
	
		if(copyBuffer != null) paste.setDisabled(false);
		copyBuffer = this;
		paste.setDisabled(true);
	}
	
	public void overrideBuffer() {
		
		copyBuffer = this;
	}
	public void clearBuffer() {
		
		copyBuffer = null;
	}
	
	public static void dumpConfiguration(Form from,Form to) {
		
		dumpConfiguration(from.getForm(), to.getForm());
	}
	
	public static void dumpConfiguration(BaseOptionWindow from,BaseOptionWindow to) {
		
		if(from.form.getClass() == to.form.getClass()) {
			
			for (Entry<String, Option> option: from.options.entrySet()) {
				
				if(option.getValue().canCopy)to.options.get(option.getKey()).setValue(option.getValue().getValue());
			}
			
			to.form.updateValuesFromForm();
		}
	}
	public boolean isAForm() {return form != null;}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		Actor hit = super.hit(x, y, touchable);
		if(hit != null) NPhysics.currentStage.setFocus(false);
		else NPhysics.currentStage.setFocus(true);
		return hit;
	}

	public static DynamicWindow createDefaultWindowStructure(String name,Form form) {
		
		final DynamicWindow d = new DynamicWindow(name,form);
		
		VisTable t = new VisTable();
		t.pad(5f);
		t.add(d.content).expand().fill();
		t.row();
		
		Table table_text = new Table();
		VisTextButton text = new VisTextButton(NDictionary.get("closewindow"));

		text.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				hideWindow(d);
				
			}
		});
		
		VisTable copypaste = new VisTable();
		
		d.copy = new VisImageButton(Util.getDrawable(new Texture(Gdx.files.internal("menu/copy.png"))));
		
		d.copy.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				d.copy();
			}
		});
		
		
		d.paste = new VisImageButton(Util.getDrawable(new Texture(Gdx.files.internal("menu/paste.png")))) {
			
			@Override
			public void setDisabled(boolean disabled) {
				
				if(disabled) {
					
					setColor(1, 0.2f, .2f, 1);
					setTouchable(Touchable.disabled);
				}else {
					
					setColor(Color.WHITE);
					setTouchable(Touchable.enabled);
				}
				super.setDisabled(disabled);
			}
		};
		
		d.paste.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				d.paste();
			}
		});

		copypaste.add(d.copy).pad(5);
		copypaste.add(d.paste).pad(5);
		
		table_text.add().expand().fill();
		table_text.add(text).expand().fillX();
		table_text.add(copypaste).expand().fill();
		t.add(table_text).fill();
		d.add(t).expand().fill();
		d.main = t;
		d.addListener(new InputListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				super.touchDown(event, x, y, pointer, button);
				return true;
			}
		});
		
		return d;
	}
	
	public static void showWindow(DynamicWindow w) {
	
		if(copyBuffer != null) {

			if(w.form.getClass() == copyBuffer.form.getClass() && w != copyBuffer) 
				w.paste.setDisabled(false);
			else w.paste.setDisabled(true);
		}
		w.setVisible(true);
		w.addAction(Actions.fadeIn(0.5f,Interpolation.exp5)); 	
	}
	
	public static void hideWindow(DynamicWindow w) {
		
		for (Entry<String, Option> option: w.options.entrySet()) {
			
			if(!option.getValue().isReady()) return;
		}
		w.addAction(Actions.fadeOut(0.5f,Interpolation.exp5)); 
		if(w.isAForm())w.form.updateValuesFromForm();
		ThreadManager.createTask(()->{ 
			w.setVisible(false);
			NPhysics.currentStage.setFocus(true);
		}, 0.5f);
	}

	@Override
	public Form getForm() {
		return form;
	}


	

}
