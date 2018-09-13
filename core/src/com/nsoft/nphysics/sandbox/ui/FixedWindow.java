package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class FixedWindow extends BaseOptionWindow implements Showable{

	VisTable content = new VisTable();
	HashMap<String, Option> options = new HashMap<>();
	Form form;
	
	public FixedWindow(String title,Form form) {
		super(title,form);
		init();
	}
	
	@Override
	public Form getForm() {
		return form;
	}
	
}
