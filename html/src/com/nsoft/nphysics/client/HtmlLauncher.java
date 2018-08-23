package com.nsoft.nphysics.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.nsoft.nphysics.NPhysics;

public class HtmlLauncher extends GwtApplication {

        // UNCOMMENT THIS CODE FOR A RESIZABLE APPLICATION
        // PADDING is to avoid scrolling in iframes, set to 20 if you have problems
         private static final int PADDING = 0;
         private GwtApplicationConfiguration cfg;
        
         @Override
         public GwtApplicationConfiguration getConfig() {
             int w = 1280;
             int h = 720;
             cfg = new GwtApplicationConfiguration(w, h);
             Window.enableScrolling(true);
             Window.setMargin("0");
             cfg.preferFlash = false;
             return cfg;
         }
   

        @Override
        public ApplicationListener createApplicationListener () {
                return new NPhysics();
        }
}