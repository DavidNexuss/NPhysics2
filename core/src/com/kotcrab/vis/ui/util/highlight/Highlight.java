/*
 * Copyright 2014-2017 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;

/**
 * Represents single highlight.
 * @author Kotcrab
 * @since 1.1.2
 */
public class Highlight implements Comparable<Highlight> {
	private Color color;
	private int start;
	private int end;

	public Highlight (Color color, int start, int end) {
		if (color == null) throw new IllegalArgumentException("color can't be null");
		if (start >= end) throw new IllegalArgumentException("start can't be >= end: " + start + " >= " + end);
		this.color = color;
		this.start = start;
		this.end = end;
	}

	public Color getColor () {
		return color;
	}

	public int getStart () {
		return start;
	}

	public int getEnd () {
		return end;
	}

	@Override
	public int compareTo (Highlight o) {
		return getStart() - o.getStart();
	}
}
