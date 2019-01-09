package com.nsoft.nphysics;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.nsoft.nphysics.NDictionary.Languages;

public class Options implements Serializable, Say{

	public Languages currentLanguage = Languages.CAT;
	
	public static Json json = new Json();
	public static String path = "options.json";
	public static FileHandle file;
	
	public static Options options = new Options();
	
	public Options() {}
	
	static void init() {
		file = Gdx.files.local(path);
		load();
	}

	public static void load() {
		
		options = json.fromJson(Options.class, file);
	}
	
	public static void save() {
		
		file.writeString(json.prettyPrint(options), false);
	}

	Json j;
	public void writeField(String name) {
		
		try {
			
			Field f = getClass().getField(name);
			json.writeValue(name, f.get(this), f.getType(), getClass());
			
		} catch (NoSuchFieldException | SecurityException  | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void write(Json json) {
		
		j = json;
		writeField("currentLanguage");
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		
		jsonData = jsonData.child;
		currentLanguage = Languages.valueOf(jsonData.asString());
	}
}
