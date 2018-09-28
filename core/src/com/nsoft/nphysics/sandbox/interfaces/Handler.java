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
