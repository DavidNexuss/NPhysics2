package com.nsoft.nphysics;
/**
 * Interfas per fer debugging
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
	    boolean thisOne = false;
	    int thisOneCountDown = 1;
	    StackTraceElement[] elements = Thread.currentThread().getStackTrace();
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
	    return -1;
	}
}
