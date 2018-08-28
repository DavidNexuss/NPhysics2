package com.nsoft.nphysics.sandbox.ui;

import java.util.ArrayList;

import javax.lang.model.type.ExecutableType;

import com.nsoft.nphysics.sandbox.interfaces.Execute;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class StaticMenu{


	static Execute<FixedWindow> show = (obj)->{obj.show();};
	static Execute<FixedWindow> hide = (obj)->{obj.hide();};
	
	ArrayList<FixedWindow> windows = new ArrayList<>();
	
	public StaticMenu() {
		
	}
	public void addWindow(FixedWindow w){
		
		windows.add(w);
		w.setVisible(false);
		w.setColor(1, 1, 1, 0);
	}
	
	public void show() {
		
		show.execute(windows);
	}
	
	public void hide() {
		
		hide.execute(windows);
	}
	
}
