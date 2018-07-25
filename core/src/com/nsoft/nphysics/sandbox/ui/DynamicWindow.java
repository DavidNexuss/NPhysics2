package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.nsoft.nphysics.Dictionary;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.sandbox.interfaces.Form;

public class DynamicWindow extends VisWindow{

	VisTable content;
	HashMap<String, Option> options = new HashMap<>();
	HashMap<String, VisLabel> texts = new HashMap<>();
	Form form;
	
	public DynamicWindow(String title) {
		super(title);
		getTitleLabel().setStyle(new LabelStyle(FontManager.title, Color.WHITE));
		getTitleTable().pad(5);
		content = new VisTable();
	}
	
	public void setAsForm(Form superior) {
		form = superior;
	}
	
	public boolean isAForm() {return form != null;}
	public Option getOption(String name) {return options.get(name);}
	
	public Cell<Option> addOption(Option p) {
		
		p.setForm(form);
		options.put(p.getName(), p);
		Cell<Option> cell = content.add(p).expand().fillX();
		content.row();
		return cell;
	}
	
	public Cell<VisLabel> addText(String name,String text) {
		
		VisLabel l = new VisLabel(text);
		l.setStyle(new LabelStyle(FontManager.subtitle, Color.WHITE));
		texts.put(name, l);
		Cell<VisLabel> cell = content.add(l).expand().fillX();
		cell.row();
		return cell;
	}
	
	public void addSeparator() {
		
		content.addSeparator();
	}
	public static DynamicWindow createDefaultWindowStructure(String name) {
		
		return createDefaultWindowStructure(name, 0, 0);
	}
	public static DynamicWindow createDefaultWindowStructure(String name,float width,float height) {
		
		final DynamicWindow d = new DynamicWindow(name);
		if(!(width == 0 && height == 0))d.setSize(width, height);
		
		Table t = new Table();
		t.pad(5f);
		t.add(d.content).expand().fill();
		t.row();
		
		Table table_text = new Table();
		VisTextButton text = new VisTextButton(Dictionary.get("closewindow"));

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
	
		w.setVisible(true);
		w.addAction(Actions.fadeIn(0.5f,Interpolation.exp5)); 	
	}
	
	public static void hideWindow(DynamicWindow w) {
		
		for (Entry<String, Option> option: w.options.entrySet()) {
			
			if(!option.getValue().isReady()) return;
		}
		w.addAction(Actions.fadeOut(0.5f,Interpolation.exp5)); 
		if(w.isAForm())w.form.updateValuesFromForm();
		ThreadManager.createTask(()->{ w.setVisible(false);}, 0.5f);
	}
	

}
