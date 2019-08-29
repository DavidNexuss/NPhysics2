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

package com.nsoft.nphysics.sandbox.ui.option;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class UIOptionColorPicker extends UIOptionComponent<Color, VisImageButton>{

	public static final int boxWidth = 15;
	public static final int boxheight = 8;
	
	private ColorPicker picker;
	private Color color = Color.WHITE;
	public UIOptionColorPicker() {
		super();
	}

	@Override
	public Color getValue() {

		return color;
	}

	@Override
	public boolean setValue(Color newVal) {
		
		color.set(newVal);
		getComponent().setBackground(createImage(color));
		return false;
	}

	@Override
	public void createComponent() {
		
		setComponent(new VisImageButton(createImage(color)));
		getComponent().setSize(boxWidth, boxheight);
		getComponent().addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				picker.fadeIn();
				super.clicked(event, x, y);
			}
		});
		
		picker = new ColorPicker(new ColorPickerAdapter() {
			
			@Override
			public void finished(Color newColor) {
				setValue(newColor);
				updateValue();
			}
		});
		
	//	UIStage.stage.addActor(picker);
	}
	
	private Drawable createImage(Color c) {
		
		Pixmap p = new Pixmap(boxWidth, boxheight, Format.RGBA8888);
		p.setColor(c);
		p.fillRectangle(0, 0, boxWidth, boxheight);
		return new TextureRegionDrawable(new TextureRegion(new Texture(p)));
	}

}
