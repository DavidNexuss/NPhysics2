package com.nsoft.nphysics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Credit extends ApplicationAdapter{

	SpriteBatch b;
	Texture p;
	@Override
	public void create() {
		
		b = new SpriteBatch();
		p = new Texture(Gdx.files.internal("logo.png"));
	}
	@Override
	public void render() {
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.graphics.setUndecorated(true);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		b.begin();
		b.draw(p, 0, 0);
		b.end();
	}
	
	@Override
	public void dispose() {
		
		b.dispose();
		p.dispose();
	}
}
