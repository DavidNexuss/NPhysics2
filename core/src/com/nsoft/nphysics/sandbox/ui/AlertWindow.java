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
	public static void throwNewAlert(String title,String message,boolean alert) {
		
		int size = 450;
		int prefsize = (int) (new GlyphLayout(FontManager.title, title).width + 20);
		int finalsize = prefsize > size ? prefsize : size;
		
		String finalMessage = Util.capable(finalsize, message);
		
		AlertWindow w = new AlertWindow(title,finalMessage);
		
		w.setWidth(finalsize + 20);
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
		
		if(alert)NPhysics.functions.playSound("win.sound.exclamation");
		
	}
}
