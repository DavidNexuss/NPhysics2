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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Util;

public class Scale extends VisTable{

	VisLabel text;
	
	public Scale() {
		
		text = new VisLabel();
		text.setStyle(new LabelStyle(FontManager.title, Color.WHITE));
		add(text).fill().center();
	}
	
	@Override
	public void act(float delta) {
		
		text.setText(Util.getFancyMeterFactor());
		text.setColor(NPhysics.currentStage.getInvertedBackColor());
		super.act(delta);
	}
	
}
