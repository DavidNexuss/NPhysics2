package com.nsoft.nphysics.sandbox.ui.option;

import com.kotcrab.vis.ui.widget.VisTextField;

public class UIOptionField extends UIOptionComponent<String,VisTextField>{
    

    private String initial;
    public UIOptionField(){
        this("");
    }
    public UIOptionField(String initial){
        this.initial = initial;
        canCopy = false;
    }

    @Override
    public String getValue() {
        return getComponent().getText();
    }

    @Override
    public boolean setValue(String newVal) {
        getComponent().setText(newVal);
        return true;
    }

    @Override
    public void createComponent() {
        setComponent(new VisTextField());
        getComponent().setText(initial);
    }
}