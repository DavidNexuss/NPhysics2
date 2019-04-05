package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
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
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;

/**
 * Classe encarregada de definir una força a la fasse Sandbox amb l'objectiu de poder
 * recrear-la a la simulació
 * @author David
 */
public class ForceComponent extends ObjectChildren implements Form{

	public static enum Type{WORLD,TRANS,REL}
	public static ArrayList<ForceComponent> list = new ArrayList<>();
	ArrowActor arrow;
	Vector2 force;
	Type type = Type.WORLD;
	
	boolean relative = false;
	private boolean hook = false;
	private boolean var;
	private Label label;
	
	static ForceComponent temp;
	public static ForceComponent crrnt;
	
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
	
	public void setForce(Vector2 v) {
		
		arrow.setEnd(v.x * Util.NEWTONS_UNIT() + getPosition().x, v.y * Util.NEWTONS_UNIT() + getPosition().y);
		update();
	}
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
		
		if(var) 
			arrow.setColor(Color.RED);
		else    arrow.setColor(Color.BLACK);
		super.draw(batch, parentAlpha);
	}
	@Override
	public boolean isInside(float x, float y) {
		return arrow.isInside(x, y);
	}
	
	public void setVar(boolean newVar) {
		
		var = newVar;
		getForm().getOption("fvar").setValue(newVar ? 1f : 0f);
	}
	//------------------------getForm()---------------------------

	
	private float lastMod,lastAngle;
	private float lastX,lastY;
	
	@Override
	public void updateValuesFromForm() {
		super.updateValuesFromForm();
		
		var = getForm().getOption("fvar").getValue() == 1;
		//getPolygon().updateForceVariableCount();
		if(var) {
			crrnt = this;
			getPolygon().unknown = this;
		}
		arrow.setStart(getPosition().x, getPosition().y);

		float newForcex = getForm().getOption("forcex").getValue();
		float newForcey = getForm().getOption("forcey").getValue();
		
		float newForceAngle = getForm().getOption("forceangle").getValue();
		float newForceMod = getForm().getOption("forcemod").getValue();
		
		
		int a = (int) getForm().getOption("ftype").getValue();
		
		if(a == 0) type = Type.WORLD; else
		if(a == 1) type = Type.TRANS; else
		if(a == 2) type = Type.REL;
		
		
		if((newForcex != lastX || newForcey != lastY) && (lastMod == newForceMod && lastAngle == newForceAngle)) {
			
			float forcex;
			float forcey;
			
			forcex = getForm().getOption("forcex").getValue() * Util.NEWTONS_UNIT();		
			forcey = getForm().getOption("forcey").getValue() * Util.NEWTONS_UNIT();
			
			arrow.setEnd(getPosition().x + forcex, getPosition().y + forcey);
		}else if((newForceMod != lastMod || newForceAngle != lastAngle) && (lastX == newForcex && lastY == newForcey)) {
			
			float realmod = newForceMod * Util.NEWTONS_UNIT();
			
			float newx = (float) (realmod * Math.cos(newForceAngle * MathUtils.degRad));
			float newy = (float) (realmod * Math.sin(newForceAngle * MathUtils.degRad));
			
			arrow.setEnd(getPosition().x + newx, getPosition().y + newy);
		}else {
			
			updateValuesToForm();
			return;
		}
	
		update();
		
		updateValuesToForm();
	}
	
	
	public void updateValuesToForm() {
		
		if(!getForm().getOption("forcex").isNull())getForm().getOption("forcex").setValue(force.x / Util.NEWTONS_UNIT());
		if(!getForm().getOption("forcey").isNull())getForm().getOption("forcey").setValue(force.y / Util.NEWTONS_UNIT());
		
		//TODO:
		getForm().getOption("forcemod").setValue(new Vector2(force.x, force.y).scl(1f / Util.NEWTONS_UNIT()).len());
		getForm().getOption("forceangle").setValue(new Vector2(force.x, force.y).angle());
		
		lastMod = getForm().getOption("forcemod").getValue();
		lastAngle = getForm().getOption("forceangle").getValue();
		lastX = getForm().getOption("forcex").getValue();
		lastY = getForm().getOption("forcey").getValue();
	}
	
	public static boolean isReady() {
		
		for (ForceComponent forceComponent : list) {
			
			if(forceComponent.var) return false;
		}
		return true;
	}
}
