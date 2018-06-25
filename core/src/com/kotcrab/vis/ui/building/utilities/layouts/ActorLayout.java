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

package com.kotcrab.vis.ui.building.utilities.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.building.utilities.CellWidget;

/**
 * An interface that allows to convert multiple widgets into one, providing utilities for complex tables
 * building. For sample implementations, see TableLayout class.
 * @author MJ
 */
public interface ActorLayout {
	/** @return passed actors merged into one widget. */
	public Actor convertToActor (Actor... widgets);

	/** @return passed wrapped actors merged into one widget. */
	public Actor convertToActor (CellWidget<?>... widgets);
}
