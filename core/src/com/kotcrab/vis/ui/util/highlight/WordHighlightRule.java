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
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 * Highlighter rule using {@link String#indexOf(String)} to detect text matches.
 * @author Kotcrab
 * @since 1.1.2
 */
public class WordHighlightRule implements HighlightRule {
	private Color color;
	private String word;

	public WordHighlightRule (Color color, String word) {
		this.color = color;
		this.word = word;
	}

	@Override
	public void process (HighlightTextArea textArea, Array<Highlight> highlights) {
		String text = textArea.getText();
		int index = text.indexOf(word);
		while (index >= 0) {
			highlights.add(new Highlight(color, index, index += word.length()));
			index = text.indexOf(word, index);
		}
	}
}
