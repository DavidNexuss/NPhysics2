package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.Draggable.DragAdapter;
import com.nsoft.nphysics.Dictionary;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.drawables.ArrowActor;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.FontManager;
import com.nsoft.nphysics.sandbox.ui.Option;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;

public class ForceComponent extends ObjectChildren implements Form{

	public static enum Type{WORLD,TRANS,REL}
	
	ArrowActor arrow;
	Vector2 force;
	Type type = Type.WORLD;
	boolean relative = false;
	private boolean hook = false;
	private boolean var;
	private Label label;
	private static LabelStyle style;
	
	static ForceComponent temp;
	
	public ForceComponent(PolygonActor parent,Vector2 start) {
		
		super(parent);

		addInput();
		initBasicForm("Wforce");
		setPosition(start.x, start.y);
		setDrag(false);
		ClickIn pointer = this;
		
		arrow = new ArrowActor(start) {
			
			@Override
			public boolean isSelected() {
				return pointer.isSelected();
			}
		};
		arrow.setHandler(null);
		arrow.setColor(Color.BLACK);
		addActor(arrow);
		
		getForm().setSize(450, 500);
		getForm().addSeparator();
		getForm().addText("force", Dictionary.get("force"));
		getForm().addOption(Option.createOptionNumber("forcex"));
		getForm().addOption(Option.createOptionNumber("forcey"));
		getForm().addSeparator();
		getForm().addOption(Option.createOptionNumber("forcemod"));
		getForm().addOption(Option.createOptionNumber("forceangle"));
		getForm().addSeparator();
		getForm().addOption(Option.createCheckBoxOption("fvar"));
		getForm().addText("ftypeset", Dictionary.get("ftypeset"));
		getForm().addOption(Option.createOptionTypeSlider("ftype", Dictionary.get("fworld"),Dictionary.get("ftrelative"),Dictionary.get("frelative")));
	
		label = new Label("F", VisUI.getSkin());
		label.setStyle(new LabelStyle(label.getStyle()));
		label.getStyle().fontColor.set(1, 0, 0, 1);
		label.getStyle().font = FontManager.title;
		
		label.setPosition(40, 40);
		addActor(label);
	}
	
	
	public boolean isHooking() {return hook;}
	public boolean isVariable() {return var;}
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
	
	public Type getType() {
		
		return type;
	}

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
	//------------------------getForm()---------------------------

	@Override
	public void updateValuesFromForm() {
		super.updateValuesFromForm();
		
		var = getForm().getOption("fvar").getValue() == 1;
		getPolygon().updateForceVariableCount();
		
		arrow.setStart(getPosition().x, getPosition().y);
		
		float forcex = getForm().getOption("forcex").getValue() * Util.UNIT /SimulationStage.ForceMultiplier;
		float forcey = getForm().getOption("forcey").getValue() * Util.UNIT /SimulationStage.ForceMultiplier;
		
		int a = (int) getForm().getOption("ftype").getValue();
		
		if(a == 0) type = Type.WORLD; else
		if(a == 1) type = Type.TRANS; else
		if(a == 2) type = Type.REL;
		arrow.setEnd(getPosition().x + forcex, getPosition().y + forcey);
		update();
		
	}
	
	
	public void updateValuesToForm() {
		
		getForm().getOption("forcex").setValue(force.x / Util.UNIT * SimulationStage.ForceMultiplier);
		getForm().getOption("forcey").setValue(force.y / Util.UNIT * SimulationStage.ForceMultiplier);
		
		getForm().getOption("forcemod").setValue(force.len() / Util.UNIT);
		getForm().getOption("forceangle").setValue(force.angle());
	}
	
}
