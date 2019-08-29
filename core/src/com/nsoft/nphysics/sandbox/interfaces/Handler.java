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

package com.nsoft.nphysics.sandbox.interfaces;

import com.nsoft.nphysics.sandbox.SelectHandle;
/**
 * Interfície que dóna la propietat a una classe de poder funcionar com a controlador
 * d'objectes a ser seleccionats. 
 * 
 * Una classe pot implementar al mateix temps aquesta interfície i la de ClickIn, així pot
 * actuar al mateix temps com a objecte que controla altres ClickIn com a objecte controlat
 * per un altre Handler.
 * @author David
 */
public interface Handler {

	public SelectHandle getSelectHandleInstance();
}
