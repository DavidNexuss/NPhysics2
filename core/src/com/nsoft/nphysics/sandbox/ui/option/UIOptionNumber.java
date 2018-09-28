package com.nsoft.nphysics.sandbox.ui.option;

import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

public class UIOptionNumber extends UIOptionComponent<Float,VisTextField>{

	@Override
	public void createComponent() {
		
		setComponent(new VisTextField("0"));
		getComponent().setFocusTraversal(false);
		getComponent().setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(VisTextField textField, char c) {
				
				if(c == '\r') {
					
					getForm().updateValuesFromForm();
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
