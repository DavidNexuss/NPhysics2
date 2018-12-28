package com.nsoft.nphysics;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

/**
 * Interfas per fer debugging
 * El propòsit es crear un Log senzill en el que un objecte pugui reportar una informació a la consola
 * y es pugui saber la linea d'execució i el hash del objecte
 * @author David
 */
public interface Say {


	public default String say(Object s) {
		
		System.out.println(getClass().getSimpleName() + "#" + hashCode() + " (" + ___8drrd3148796d_Xaf() + ") : " + s);
		return s.toString();
	}
	
	/** This methods name is ridiculous on purpose to prevent any other method
	 * names in the stack trace from potentially matching this one.
	 * 
	 * @return The line number of the code that called the method that called
	 *         this method(Should only be called by getLineNumber()).
	 * @author Brian_Entei 
	 * @see {@link https://stackoverflow.com/questions/17473148/dynamically-get-the-current-line-number/17473358}
	 * */
	
	static int ___8drrd3148796d_Xaf() {
	    
		if(Gdx.app == null || Gdx.app.getType() == ApplicationType.Desktop) {
	    	
	    	boolean thisOne = false;
		    int thisOneCountDown = 1;
		    StackTraceElement[] elements = NPhysics.functions.getStackTrace();
		    for(StackTraceElement element : elements) {
		        String methodName = element.getMethodName();
		        int lineNum = element.getLineNumber();
		        if(thisOne && (thisOneCountDown == 0)) {
		            return lineNum;
		        } else if(thisOne) {
		            thisOneCountDown--;
		        }
		        if(methodName.equals("___8drrd3148796d_Xaf")) {
		            thisOne = true;
		        }
		    }
	    }

	    return -1;
	}
}
