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
