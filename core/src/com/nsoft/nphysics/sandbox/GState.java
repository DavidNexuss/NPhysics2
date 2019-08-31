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

package com.nsoft.nphysics.sandbox;

import com.nsoft.nphysics.NDictionary;

public enum GState {
	
	START("start"),
	CREATE_POINT("menu-create-point",()->{Point.lastPoint.setVisible(false);},()->{Point.lastPoint.setVisible(true);}),
	CREATE_SEGMENT("menu-create-segment"),
	CREATE_AXIS("menu-create-axis",()->{AxisSupport.temp.setVisible(false);},()->{AxisSupport.temp.setVisible(true);}),
	CREATE_DOUBLE_AXIS("menu-create-double-axis",()->{DoubleAxisComponent.tmp.setVisible(false);},()->{DoubleAxisComponent.tmp.setVisible(true);}),
	CREATE_PRISMATIC("menu-create-prismatic",()->{PrismaticComponent.temp.setVisible(false);},()->{PrismaticComponent.temp.setVisible(true);}),
	CREATE_ROPE("menu-create-rope"),
	CREATE_SPRING("menu-create-spring"),
	CREATE_PULLEY("menu-create-pulley"),
	CREATE_CIRCLE(Flag.POLYGON,"menu-create-circle"),
	CREATE_FAST_POLYGON(Flag.POLYGON,"menu-create-polygon"),
	CREATE_SEGMENTS("menu-create-segments"),
	CREATE_FORCE("menu-create-force"),
	CREATE_WATER("menu-create-water"),
	CREATE_LINE("menu-create-line"),
	CREATE_MEDIATRIX("menu-create-mediatrix"),
	CREATE_ARC("menu-create-arc"),
	CREATE_MATH_CIRCLE("menu-create-math-circle"),
	HOOK_FORCE_ARROW("menu-create-force.1"),
	HOOK_FORCE_ARROW2("menu-create-force.2");	
	
	public String description;
	public Runnable cleanTask;
	public Runnable setUpTask;
	
	public boolean hasCleanTask() {return cleanTask != null;}
	public boolean hasSetUpTask() {return setUpTask != null;}
	
	public Flag fl;
	static enum Flag{
		POLYGON(()->{FastPolygonCreator.temp = null;});
		
		Runnable r;
		Flag(Runnable r){
			
			this.r = r;
		}
	}
	private GState(String description) {
		
		this(description, null);
	}
	
	private GState(Flag fl,String description) {
		
		this(description);
		this.fl = fl;
	}
	GState(String description,Runnable cleanTask) {
		
		this(description, cleanTask,null);
	}
	GState(String description,Runnable cleanTask,Runnable setUpTask){
		
		this.description = NDictionary.get(description);
		this.cleanTask = cleanTask;
		this.setUpTask = setUpTask;
	}
	
}