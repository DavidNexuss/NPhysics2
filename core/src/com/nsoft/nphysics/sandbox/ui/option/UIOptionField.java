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
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

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