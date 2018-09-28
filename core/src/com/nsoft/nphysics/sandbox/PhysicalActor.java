package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.FontManager;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;

public abstract class PhysicalActor<D extends ObjectDefinition> extends Actor implements Form,ClickIn,Draggable{

	private DynamicWindow form;
	private D definition;
	
	public PhysicalActor() {
		
		initDefinition();
		initForm();
	}
	
	abstract void initDefinition();
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

	@Override
	public BaseOptionWindow getForm() {
		return form;
	}


	@Override
	public void updateValuesToForm() {
		
	}
}
