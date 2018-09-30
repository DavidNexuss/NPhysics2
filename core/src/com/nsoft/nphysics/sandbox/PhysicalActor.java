package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Draggable;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.FontManager;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;

public abstract class PhysicalActor<D extends ObjectDefinition> extends Group implements Form,Handler,ClickIn,Draggable,Parent<Point>{

	final static Color shape = 		   new Color(0.2f, 0.8f, 0.2f, 0.6f);
	final static Color shapeSelected = new Color(0.8f, 0.2f, 0.2f, 0.6f);
	final static Color mightSelected = new Color(0.8f,0.5f,0.2f,0.6f);
	final static Color arcColor = 	   new Color(0.5f, 0.5f, 0.9f, 0.4f);
	
	private DynamicWindow form;
	private SelectHandle handler;
	
	private boolean isEnded = false;
	private Color currentColor = shape;
	

	private ArrayList<ObjectChildren> components = new ArrayList<>();
	
	D definition;
	
	protected ArrayList<Point> points = new ArrayList<>();
	
	public PhysicalActor() {
		
		handler = new SelectHandle();
		initDefinition();
		initForm();

		setDebug(true, true);
		addInput();
		addDragListener();
	}
	
	@Override
	public SelectHandle getSelectHandleInstance() {

		return handler;
	}
	@Override
	public SelectHandle getHandler() {
		
		return Sandbox.mainSelect;
	}
	
	abstract void initDefinition();
	
	public boolean isEnded() {return isEnded;}
	public void end() {
		
		isEnded = true;
		for (Point point : points) {
			point.setObjectParent(this);
		}
		
		NPhysics.sandbox.polygonlist.add((PhysicalActor<ObjectDefinition>) this);
	}
	
	public D getDefinition() {return definition;}
	private void initForm() {
		
		form = DynamicWindow.createDefaultWindowStructure("Wpolygon",this);
		form.setSize(450, 450);
		
		form.addOption(Options.createOptionTypeSlider("polygon_phys_state", NDictionary.get("phys_DYNAMIC"),NDictionary.get("phys_KINEMATIC"),NDictionary.get("phys_STATIC")));
		form.addOption(Options.createOptionNumber("polygon_lvel_x"));
		form.addOption(Options.createOptionNumber("polygon_lvel_y"));
		form.addOption(Options.createOptionNumber("polygon_phys_mass"));
		
		form.addOption(Options.createOptionNumber("polygon_phys_density"));
		form.addOption(Options.createOptionNumber("polygon_phys_friction"));
		form.addOption(Options.createOptionNumber("polygon_phys_restitution"));
		
		form.getOption("polygon_phys_density").setValue(definition.density);
		form.getOption("polygon_phys_friction").setValue(definition.friction);
		form.getOption("polygon_phys_restitution").setValue(definition.restitution);
		
		
		VisTable solve_dsl = new VisTable();
		VisLabel dsl_t = new VisLabel(NDictionary.get("dsl_unknowns"));
		dsl_t.setStyle(new LabelStyle(FontManager.subtitle, Color.WHITE));
		VisLabel dsl_n = new VisLabel() {
			
			@Override
			public void act(float delta) {
				
				setText(""); //TODO
				super.act(delta);
			}
		};
		VisTextButton dsl_b= new VisTextButton(NDictionary.get("dsl_solve"));
		
		solve_dsl.add(dsl_t).expand().align(Align.left);
		solve_dsl.add(dsl_n).prefWidth(50).width(50);
		solve_dsl.add(dsl_b).fillX().expand().padLeft(15);
		
		solve_dsl.pad(5);
		form.addRawTable(solve_dsl);
		
		form.setVisible(false);
		NPhysics.ui.addActor(form);
	}

	public boolean keyDown(int keycode){
		
		if(keycode == Keys.Q) {
			
			if(!form.isVisible()) showForm();
			return true;
		}
		
		return false;
	}

	@Override
	public void act(float delta) {
		
		super.act(delta);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);

		NPhysics.currentStage.shapefill.setColor(isLastSelected() ? shapeSelected : currentColor);
	}
	@Override
	public void unselect() {
		
		currentColor = shape;
		UIStage.contextMenu.hide();

		
	}
	@Override
	public void select(int pointer) {
		
		currentColor = mightSelected;
		UIStage.contextMenu.show();

	}
	
	@Override
	public BaseOptionWindow getForm() {
		return form;
	}


	@Override
	public void updateValuesToForm() {
		
	}
	
	public void addComponent(ObjectChildren child) {
		
		components.add(child);
	}
	
	public void removeComponent(ObjectChildren child) {
		
		components.remove(child);
	}
	
	public ArrayList<ObjectChildren> getComponents(){
		
		return components;
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY())) ? this : null;
	}
	
	@Override
	public boolean remove() {
		
		NPhysics.sandbox.polygonlist.remove(this);
		for (Point p : points) {
			
			ArrayList<PolygonActor> list = p.getObjectParents(PolygonActor.class);
			
			boolean canRemove = true;
			
			for (PolygonActor polygonActor : list) {
				if(polygonActor != this) {
					canRemove = false;
					break;
				}
			}
			if(!canRemove) continue;
			p.remove();
		}
		

		return super.remove();
	}
}
