package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.VisUI;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.drawables.ArrowActor;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.ui.FontManager;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dsl.Force.Variable;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;

public class ForceComponent extends ObjectChildren implements Form{

	public static enum Type{WORLD,TRANS,REL}
	public static ArrayList<ForceComponent> list = new ArrayList<>();
	ArrowActor arrow;
	Vector2 force;
	Type type = Type.WORLD;
	boolean relative = false;
	private boolean hook = false;
	private  boolean var;
	private Label label;
	private static LabelStyle style;
	
	public Variable variable;
	
	static ForceComponent temp;
	
	public ForceComponent(PhysicalActor<ObjectDefinition> parent,Vector2 start) {
		
		super(parent);
		list.add(this);
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
		getForm().addText("force", NDictionary.get("force"));
		getForm().addOption(Options.createOptionNumber("forcex"));
		getForm().addOption(Options.createOptionNumber("forcey"));
		getForm().addSeparator();
		getForm().addOption(Options.createOptionNumber("forcemod"));
		getForm().addOption(Options.createOptionNumber("forceangle"));
		getForm().addSeparator();
		getForm().addOption(Options.createCheckBoxOption("fvar"));
		getForm().addText("ftypeset", NDictionary.get("ftypeset"));
		getForm().addOption(Options.createOptionTypeSlider("ftype", NDictionary.get("fworld"),NDictionary.get("ftrelative"),NDictionary.get("frelative")));
	
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
	public void setForce(Vector2 f) { force.set(f);}
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
		//getPolygon().updateForceVariableCount();
		
		arrow.setStart(getPosition().x, getPosition().y);
		
		float forcex;
		float forcey;
		
		variable = variable.NONE;
		arrow.setColor(Color.BLACK);
		if(getForm().getOption("forcex").isNull()) {
			
			variable = Variable.X;
			arrow.setColor(Color.RED);
			forcex = 50;
		}else forcex = getForm().getOption("forcex").getValue() * Util.UNIT /SimulationStage.ForceMultiplier;
		
		if(getForm().getOption("forcey").isNull()) {
			
			variable = Variable.Y;
			arrow.setColor(Color.RED);
			forcey = 50;
		}else forcey = getForm().getOption("forcey").getValue() * Util.UNIT /SimulationStage.ForceMultiplier;
		
		int a = (int) getForm().getOption("ftype").getValue();
		
		if(variable != Variable.NONE) getForm().getOption("fvar").setValue(1);
		
		if(a == 0) type = Type.WORLD; else
		if(a == 1) type = Type.TRANS; else
		if(a == 2) type = Type.REL;
		arrow.setEnd(getPosition().x + forcex, getPosition().y + forcey);
		update();
		
		checknullValues();
	}
	
	
	public void updateValuesToForm() {
		
		if(!getForm().getOption("forcex").isNull())getForm().getOption("forcex").setValue(force.x / Util.UNIT * SimulationStage.ForceMultiplier);
		if(!getForm().getOption("forcey").isNull())getForm().getOption("forcey").setValue(force.y / Util.UNIT * SimulationStage.ForceMultiplier);
		
		checknullValues();
		
	}
	
	private void checknullValues() {
		
		if(variable == Variable.X || variable == Variable.Y) {
			

			getForm().getOption("forcemod").setEnable(false);
			getForm().getOption("forceangle").setEnable(false);
		}
		else {
		
			getForm().getOption("forcemod").setEnable(true);
			getForm().getOption("forceangle").setEnable(true);
			getForm().getOption("forcemod").setValue(force.len() / Util.UNIT);
			getForm().getOption("forceangle").setValue(force.angle());
			
		}
	}
	
}
