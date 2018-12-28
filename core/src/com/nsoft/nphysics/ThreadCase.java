package com.nsoft.nphysics;

/**
 * Interfas amb la utilitat d'executar fils, el propòsit es merament utilitzari.
 * @author David
 */
public interface ThreadCase {

	void startThread(Runnable r,long delay);
}
