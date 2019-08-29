/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

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
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Form;

public class DynamicWindow extends BaseOptionWindow implements Say{

	private static DynamicWindow copyBuffer;
	
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

	public boolean isReady(){return true;}
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
				
				if(d.isReady())
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
		boolean ready = true; 
		if(w.isAForm()){
			ready = w.form.updateValuesFromForm();
	//		w.say(ready);
		}
		if(!ready)return;
		w.addAction(Actions.fadeOut(0.5f,Interpolation.exp5));
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
