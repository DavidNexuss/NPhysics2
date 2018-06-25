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

import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 * @author Kotcrab
 * @since 1.1.2
 */
public interface HighlightRule {
	/**
	 * Process this rule. This method should detect matches in text area text, create {@link Highlight} instances and add them to provided
	 * highlights array.
	 * @param textArea text area
	 * @param highlights current highlights, new highlights can be added to this list however it should not be modified in any other ways
	 */
	void process (HighlightTextArea textArea, Array<Highlight> highlights);
}
