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

package com.nsoft.nphysics;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
/**
 * Classe encarregada de l'emmagatzament de les traduccions de totes les paraules clau del programa
 * escrites al document dic.json
 * @author David
 */
public class NDictionary implements Serializable,Say{

	public static enum Languages{ESP,ENG,CAT}
	private static String languagepath = "dic.json";
	private static Json json;
	
	public static NDictionary dictionary;
	private HashMap<String, LanguageEntry> keys = new HashMap<>();
	
	public static void init() {
		json = new Json();
		dictionary = json.fromJson(NDictionary.class, Gdx.files.internal(languagepath).readString());
	}
	
	private void readEntries(Json json,JsonValue jsonData) {
		
		if(jsonData == null) return;
		keys.put(jsonData.name, new LanguageEntry().init(json, jsonData));
		readEntries(json, jsonData.next);
	}
	@Override
	public void read(Json json, JsonValue jsonData) {
		
		if(jsonData == null) return;
		JsonValue cur = jsonData.child;
		readEntries(json, cur.child);
	}
	@Override
	public void write(Json json) {
		

		json.writeObjectStart("languages");
		for (Entry<String, LanguageEntry> entries : keys.entrySet()) {
			
			json.writeValue(entries.getKey(), entries.getValue(), LanguageEntry.class, getClass());
		}
		json.writeObjectEnd();
	}
	
	@Override
	public String toString() {
		
		return json.prettyPrint(this);
	}
	
	public static String get(String key) {
		
		return get(key, Options.options.currentLanguage);
	}
	public static String get(String key,Languages l) {
		
		if(dictionary.keys.containsKey(key)) {
			
			if(dictionary.keys.get(key).isMapped(l)) {
				
				return dictionary.keys.get(key).get(l);
				
			}else {
				
				dictionary.say(key + "_" + l.name() + "  NOT MAPPED");
				return key + "_" + l.name();
			}
		}else {
			
			dictionary.say(key + "  NOT MAPPED");
			return key;
		}
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
			
			return get(Options.options.currentLanguage);
		}
		
		public boolean isMapped(Languages l) {
			
			return keys.containsKey(l);
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
