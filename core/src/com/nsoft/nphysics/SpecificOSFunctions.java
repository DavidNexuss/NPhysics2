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

/**
 * Interfas per poder executar funcions úniques de cada sistema operatius.
 * Per exemple per poder utilitzar una classe com JFrame en la verió de la aplicació per a escritori,
 * no podem fer referència a aquesta classe en el projecte Core només ho podem fer en el Desktop.
 * 
 * Per poder executar aquestes funcions depenents de la plataforma hem de crear una interfas i executar
 * aquestes funcions de forma indirecta.
 * @author David
 */
public interface SpecificOSFunctions {

	public void playSound(String name);
	public Thread getCurrentThread();
	public StackTraceElement[] getStackTrace();
}
