package com.nsoft.nphysics.desktop;

import java.io.File;
import java.io.IOException;

public final class JavaProcess {

    private JavaProcess() {}        

    public static int exec(Class klass) throws IOException,
                                               InterruptedException {

        ProcessBuilder builder = new ProcessBuilder(
        		System.getProperty("java.home") + File.separator + "bin" + File.separator + "java", "-cp", 
        		System.getProperty("java.class.path"), klass.getCanonicalName());

        Process process = builder.start();
        process.waitFor();
        return process.exitValue();
    }

}