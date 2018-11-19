package com.nsoft.nphysics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;

public class Credit extends ApplicationAdapter{

	@Override
	public void render() {
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.graphics.setUndecorated(true);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
	}
}
