package com.nsoft.nphysics;

/**
 * Interfas per poder executar funcions �niques de cada sistema operatius.
 * Per exemple per poder utilitzar una classe com JFrame en la veri� de la aplicaci� per a escritori,
 * no podem fer refer�ncia a aquesta classe en el projecte Core nom�s ho podem fer en el Desktop.
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
