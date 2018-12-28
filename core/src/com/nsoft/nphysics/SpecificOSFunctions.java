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
