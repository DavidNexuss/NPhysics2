package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;

import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class FixedWindow extends BaseOptionWindow implements Showable{

	public FixedWindow(String title,Form form) {
		super(title,form);
		init();
	}
}
