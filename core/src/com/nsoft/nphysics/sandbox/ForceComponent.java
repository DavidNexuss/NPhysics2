package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.Draggable.DragAdapter;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.Option;

public class ForceComponent extends ObjectChildren implements Form{

	ArrowActor arrow;
	Vector2 force;
	
	boolean relative = false;
	private boolean hook = false;
	public ForceComponent(PolygonActor parent,Vector2 start) {
		
		super(parent);

		initBasicForm();
		setPosition(start.x, start.y);
		
		System.out.println(start);
		
		arrow = new ArrowActor(start) {
			
			@Override
			public void select(int pointer) {
				
				showWindow();
			}
		};
		arrow.setHandler(parent.getSelectHandleInstance());
		arrow.setColor(Color.BLACK);
		addActor(arrow);
		
		getForm().setSize(400, 450);
		getForm().addSeparator();
		getForm().addText("force", "Set force vector");
		getForm().addOption(Option.createOptionNumber("forcex", "Force in x"));
		getForm().addOption(Option.createOptionNumber("forcey", "Force in y"));
		getForm().addSeparator();
		getForm().addOption(Option.createOptionNumber("forcemod", "Force modulus"));
		getForm().addOption(Option.createOptionNumber("forceangle", "Force angle"));
		getForm().addSeparator();
		getForm().addText("type", "Set force vector type");
		getForm().addOption(Option.createOptionTypeSlider("type", "Vector type", "World","Translation relative","Relative"));
		
	}
	
	
	public boolean isHooking() {return hook;}
	public void hook() {hook = true;}
	public void unhook() {hook = false;}
	public void shook() {hook = !hook;}
	
	public Vector2 getPosition() {
		
		return new Vector2(getX(), getY());
	}
	public void update() {
		
		force = arrow.getEnd().sub(getPosition());
	}
	
	public Vector2 getForce() {return new Vector2(force);}
	@Override
	public void act(float delta) {
		
		if(hook) {
			arrow.setEnd(NPhysics.currentStage.getUnprojectX(), NPhysics.currentStage.getUnprojectY());
			update();
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
	}
	@Override
	public boolean isInside(float x, float y) {
		return arrow.isInside(x, y);
	}

	
	@Override
	public void select(int pointer) {
		shook();
	}

	@Override
	public void unselect() {
		shook();
	}

	private void showWindow() {
		
		getForm().setPosition(Gdx.graphics.getWidth() - getForm().getWidth() - 80, Gdx.graphics.getHeight() - getForm().getHeight() - 80);
		updateValuesToForm();
		DynamicWindow.showWindow(getForm());
		
	}
	//------------------------getForm()---------------------------

	@Override
	public void updateValuesFromForm() {
		super.updateValuesFromForm();
		
		arrow.setStart(getPosition().x, getPosition().y);
		update();
		
	}
	
	private void updateValuesToForm() {
		
		getForm().getOption("forcex").setValue(force.x / Util.UNIT);
		getForm().getOption("forcey").setValue(force.y / Util.UNIT);
		
		getForm().getOption("forcemod").setValue(force.len() / Util.UNIT);
		getForm().getOption("forceangle").setValue(force.angle());
	}
	
}
