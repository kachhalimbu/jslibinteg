package org.zkoss.sample.jslibinteg;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

public class SpaceTreeNode implements JSONAware {

	String id;
	String name;
	JSONObject data;
	JSONArray children;

	public SpaceTreeNode(String id, String name, JSONObject data,
			JSONArray children) {
		super();
		this.id = id;
		this.name = name;
		this.data = data;
		this.children = children;
	}

	public String toJSONString() {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("data", data);
        map.put("children", children);
		return JSONObject.toJSONString(map);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public JSONArray getChildren() {
		return children;
	}

	public void setChildren(JSONArray children) {
		this.children = children;
	}
}
