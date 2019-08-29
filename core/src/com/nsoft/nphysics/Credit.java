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
