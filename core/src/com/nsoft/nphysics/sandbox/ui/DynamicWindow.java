package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.nsoft.nphysics.ThreadManager;

public class DynamicWindow extends VisWindow{

	Table content;
	public DynamicWindow(String title) {
		super(title);
		
		content = new Table();
	}
	
	public static DynamicWindow createDefaultWindowStructure(String name) {
		
		final DynamicWindow d = new DynamicWindow(name);
		d.setSize(600, 400);
		
		Table t = new Table();
		t.pad(15f);
		t.add(d.content).expand().fill();
		t.row();
		
		Table table_text = new Table();
		VisTextButton text = new VisTextButton("Close");

		text.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				hideWindow(d);
			}
		});
		
		table_text.add().expand();
		table_text.add(text).expand().fill();
		table_text.add().expand();
		t.add(table_text).fill();
		d.add(t).expand().fill();
		return d;
	}
	public static void showWindow(DynamicWindow w) {
	
		w.setVisible(true);
		w.addAction(Actions.fadeIn(0.5f,Interpolation.exp5)); 	
	}
	
	public static void hideWindow(DynamicWindow w) {
		
		w.addAction(Actions.fadeOut(0.5f,Interpolation.exp5)); 	
		ThreadManager.createTask(()->{ w.setVisible(false);}, 0.5f);
	}
}
