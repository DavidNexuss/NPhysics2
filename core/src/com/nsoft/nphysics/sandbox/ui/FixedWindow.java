package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class FixedWindow extends VisWindow implements Showable{

	VisTable content = new VisTable();
	HashMap<String, Option> options = new HashMap<>();
	Form form;
	
	public FixedWindow(String title,Form form) {
		super(title);
		
		add(content).expand().fill();
		dragging = false;
		this.form = form;

	}
	
	public Cell<Option> addOption(Option p) {
		
		p.setForm(form);
		options.put(p.getName(), p);
		Cell<Option> cell = content.add(p).expand().fillX();
		content.row();
		return cell;
	}
	
	public static FixedWindow createFixedWindow(String name,float x,float y,float w,float h,Form form) {
		
		FixedWindow f = new FixedWindow(name,form);
		f.setBounds(x, y, w, h);
		return f;
	}
	
}
