package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class BaseOptionWindow extends VisWindow implements Showable{

	VisTable content = new VisTable();
	
	HashMap<String, VisLabel> texts = new HashMap<>();
	HashMap<String, Option> options = new HashMap<>();
	Form form;
	
	public BaseOptionWindow(String title,Form f) {
		super(title);
		
		getTitleLabel().setStyle(new LabelStyle(FontManager.title, Color.WHITE));
		getTitleTable().pad(5);
		
		form = f;
		
	}

	public void init() {
		
		add(content).expand().fill();
		updateSize();
	}
	public void addOption(Option p) {
		
		p.setForm(form);
		options.put(p.getName(), p);
		Cell<Option> cell = content.add(p).expand().fillX();
		content.row();
		updateSize();
	}
	
	public HashMap<String, Option> getOptions() {
		return options;
	}
	
	public Option getOption(String name) {
		
		return getOptions().get(name);
	}
	
	public void addText(String name,String text) {
		
		VisLabel l = new VisLabel(text);
		l.setStyle(new LabelStyle(FontManager.subtitle, Color.WHITE));
		texts.put(name, l);
		Cell<VisLabel> cell = content.add(l).expand().fillX();
		cell.row();
		updateSize();
	}
	
	public void addText(String text) {addText(text, text);}
	
	public VisLabel getText(String name) {
		
		return texts.get(name);
	}
	
	public void addRawTable(VisTable t){
		
		Cell<VisTable> cell = content.add(t).expand().fillX();
		cell.row();
		updateSize();
	}
	
	public void addSeparator() {
		
		content.addSeparator();
		updateSize();
	}
	
	public Form getForm() {
		return form;
	}

	public void updateSize() {
		/*
		float width = content.getPrefWidth() + 20;
		setSize(width < getTitleTable().getPrefWidth() + 20 ? getTitleTable().getPrefWidth() + 20 : width, content.getPrefHeight() + 40);
	*/
		
		setSize(getPrefWidth(), getPrefHeight());
	
	}

}
