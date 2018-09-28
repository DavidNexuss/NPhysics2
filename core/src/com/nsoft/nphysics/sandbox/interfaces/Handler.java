package com.nsoft.nphysics.sandbox.interfaces;

import com.nsoft.nphysics.sandbox.SelectHandle;
/**
 * Interf�cie que d�na la propietat a una classe de poder funcionar com a controlador
 * d'objectes a ser seleccionats. 
 * 
 * Una classe pot implementar al mateix temps aquesta interf�cie i la de ClickIn, aix� pot
 * actuar al mateix temps com a objecte que controla altres ClickIn com a objecte controlat
 * per un altre Handler.
 * @author David
 */
public interface Handler {

	public SelectHandle getSelectHandleInstance();
}
