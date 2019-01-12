package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.sandbox.Util;

public class AlertWindow extends VisWindow{

	VisLabel msg;
	public AlertWindow(String title,String message) {
		super(title);
		getTitleLabel().setStyle(new LabelStyle(FontManager.title, Color.WHITE));
		getTitleTable().pad(5);
		
		msg = new VisLabel(message);
	}

	private void destroy() {
		
		addAction(Actions.fadeOut(0.5f, Interpolation.exp5));
		ThreadManager.createTask(()->{ setVisible(false);}, 0.5f);
	}
	public static void throwNewAlert(String title,String message) {
		
		int size = 450;
		int prefsize = (int) (new GlyphLayout(FontManager.title, title).width + 20);
		int finalsize = prefsize > size ? prefsize : size;
		
		String finalMessage = Util.capable(finalsize, message);
		
		AlertWindow w = new AlertWindow(title,finalMessage);
		
		w.setWidth(finalsize);
		w.setHeight(new GlyphLayout(Util.getNormalFont(), finalMessage).height + 100);
		
		w.setPosition((Gdx.graphics.getWidth() - w.getWidth())/2f, (Gdx.graphics.getHeight() - w.getHeight())/2f);
		w.setColor(new Color(1, 1, 1, 0));
		
		VisTable main = new VisTable();
		
		main.pad(5f);
		main.add(w.msg).expand();
		main.row();
		
		VisTable closeT = new VisTable();
		VisTextButton closeB = new VisTextButton(NDictionary.get("closewindow"));	
		
		closeB.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				w.destroy();
			}
		});
		
		closeT.add().expand();
		closeT.add(closeB).expand().fill();
		closeT.add().expand();
		
		main.add(closeT).expand().fillX();
		
		w.add(main).expand().fill();
		
		NPhysics.ui.addActor(w);
		w.addAction(Actions.fadeIn(0.3f,Interpolation.exp10));
		
		NPhysics.functions.playSound("win.sound.exclamation");
		
	}
}
