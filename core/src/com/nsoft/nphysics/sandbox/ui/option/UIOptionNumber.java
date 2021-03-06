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
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;
import com.nsoft.nphysics.Say;

public class UIOptionNumber extends UIOptionComponent<Float,VisTextField> implements Say{

	@Override
	public void createComponent() {
		
		setComponent(new VisTextField("0"));
		getComponent().setFocusTraversal(false);
		getComponent().setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(VisTextField textField, char c) {
				
				if(c == '\r' && !isEmpty()) {
					
					updateValue();
				}
				
				if(isTextNull(getRawText())) {
					
					setNull(true);
					enableComponent(true);
					return;
				}else setNull(false);;
				
				if(!isMessageNumber()) {
					
					setReady(false);
				}else {
					
					setReady(true);
				}
			
			}
		});
	}
	
	public boolean isEmpty() {
		
		return getComponent().getText().isEmpty();
	}
	@Override
	public Float getValue() {
		
		return isTextNull(getRawText()) ? Float.MAX_VALUE : Float.parseFloat(getRawText() == "" ? "0" : getRawText());
		
	}

	public String getRawText() {
		
		return getComponent().getText();
	}
	@Override
	public boolean setValue(Float newVal) {
		
		if(newVal.intValue() == newVal.floatValue()) {
			
			getComponent().setText("" + newVal.intValue());
		}else {
			getComponent().setText("" + newVal.floatValue());
		}
		return true;
	}
	
	private boolean isTextNull(String n) {
		
		return n.equals("NULL");
	}
	private boolean isNumber(char c) {
		
		return c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '0' || c == '.' || c == '-'; 
	}
	
	private boolean isMessageNumber() {
		
		if(getRawText().length() == 0) return false;
		for (int i = 0; i < getRawText().length(); i++) {
			
			char c = getRawText().charAt(i);
			if(!isNumber(c)) return false;
		}
		
		return true;
	}
	
	
}
