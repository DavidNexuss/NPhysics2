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
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/package com.nsoft.nphysics.desktop;

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