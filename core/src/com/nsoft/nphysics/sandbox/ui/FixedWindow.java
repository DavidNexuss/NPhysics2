package com.nsoft.nphysics.sandbox.ui;

import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class FixedWindow extends BaseOptionWindow implements Showable{

	public FixedWindow(String title,Form form) {
		super(title,form);
		init();
	}
}
