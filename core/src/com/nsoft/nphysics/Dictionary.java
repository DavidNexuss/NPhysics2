package com.nsoft.nphysics;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.Serializable;

public class Dictionary implements Serializable{

	public static enum Languages{ESP,ENG}
	private static Languages currentLanguage = Languages.ENG;
	private static String languagepath = "dic.json";
	private static Json json;
	
	private static Dictionary dictionary;
	private HashMap<String, LanguageEntry> keys = new HashMap<>();
	
	public static void init() {
		json = new Json();
		dictionary = json.fromJson(Dictionary.class, Gdx.files.internal(languagepath).readString());
	}
	
	private void readEntries(Json json,JsonValue jsonData) {
		
		if(jsonData == null) return;
		keys.put(jsonData.name, new LanguageEntry().init(json, jsonData));
		readEntries(json, jsonData.next);
	}
	@Override
	public void read(Json json, JsonValue jsonData) {
		
		jsonData = jsonData.child;
		if(jsonData == null) return;
		readEntries(json, jsonData);
		
	}
	@Override
	public void write(Json json) {
		
		for (Entry<String, LanguageEntry> entries : keys.entrySet()) {
			
			json.writeValue(entries.getKey(), entries.getValue(), LanguageEntry.class, getClass());
		}
	}
	
	@Override
	public String toString() {
		
		return json.prettyPrint(this);
	}
	
	public static String get(String key) {
		
		return dictionary.keys.get(key).get();
	}
	public static String get(String key,Languages l) {
		
		return dictionary.keys.get(key).get(l);
	}
	
	public static class LanguageEntry implements Serializable{
		HashMap<Languages, String> keys = new HashMap<>();
		
		public LanguageEntry() {}
		
		public LanguageEntry(Val ... vals) {
			
			for (Val val : vals) {
				
				keys.put(val.l, val.s);
			}
		}
		public String get() {
			
			return get(currentLanguage);
		}
		public String get(Languages l) {
			
			return keys.get(l);
		}
		
		public LanguageEntry init(Json json,JsonValue jsonData) {
			
			read(json, jsonData.child);
			return this;
		}
		
		@Override
		public void read(Json json, JsonValue jsonData) {
			
			if(jsonData == null) return;
			keys.put(Languages.valueOf(jsonData.name), jsonData.asString());
			read(json, jsonData.next);
		}
		
		@Override
		public void write(Json json) {
			
			for (Entry<Languages, String> entries : keys.entrySet()) {
				
				json.writeValue(entries.getKey().name(),entries.getValue());
			}
		}
	}
	

	public static class Val{
		
		Languages l;
		String s;
		
		public Val(Languages l,String s) {
			
			this.l = l;
			this.s = s;
		}
	}
}
