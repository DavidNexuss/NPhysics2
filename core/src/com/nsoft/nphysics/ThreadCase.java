package com.nsoft.nphysics;

/**
 * Interfas amb la utilitat d'executar fils, el prop�sit es merament utilitzari.
 * @author David
 */
public interface ThreadCase {

	void startThread(Runnable r,long delay);
}
