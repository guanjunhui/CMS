package cn.cebest.util;

import java.util.HashMap;
import java.util.Map;

public class CodeMap {
	private Map<String, String> map;
	
	public CodeMap(){
		Map<String, String> pMap = new HashMap<>();
		pMap.put("Sportz Titanium-Onyx Black", "A");
		pMap.put("Sportz Titanium-Ocean Blue", "B");
		pMap.put("Sportz Titanium-Lava Red", "C");
		pMap.put("Sportz Titanium with Mic-Onyx Black", "D");
		pMap.put("Sportz Titanium with Mic-Ocean Blue", "E");
		pMap.put("Sportz Titanium with Mic-Lava Red", "F");
		pMap.put("Trekz Air Slate Grey", "G");
		pMap.put("Trekz Air Midnight Blue", "H");
		pMap.put("Trekz Air Canyon Red", "I");
		pMap.put("Trekz Air Forest Green", "G");
		pMap.put("Trekz Titanium(AS600OB)-Ocean Blue", "K");
		pMap.put("Trekz Titanium(AS600IV)-Ivy Green", "L");
		pMap.put("Trekz Titanium(AS600SG)-Slate Grey", "M");
		pMap.put("Trekz Titanium(AS600PK)-Pink", "N");
		pMap.put("Trekz Titanium (AS600RD)-Red", "O");
		pMap.put("Trekz Titanium (AS600SG Mini)-Slate Grey", "P");
		pMap.put("Trekz Titanium (AS600PK Mini)-Pink", "Q");
		pMap.put("Other (Please specify below )", "R");
		this.map = pMap;
	}

	public Map<String, String> getMap() {
		return map;
	}
	
	
}
