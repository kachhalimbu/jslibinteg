package org.zkoss.sample.jslibinteg;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SpaceTreeComposer extends SelectorComposer<Window> {
	
	public String treeData;
	
	public void doBeforeComposeChildren(Window win) throws Exception {
		super.doBeforeComposeChildren(win);
		JSONArray firstNodeChildren = new JSONArray();
		firstNodeChildren.add(new SpaceTreeNode("node11", "Node 11", new JSONObject(), null));
		firstNodeChildren.add(new SpaceTreeNode("node12", "Node 12", new JSONObject(), null));
		firstNodeChildren.add(new SpaceTreeNode("node13", "Node 13", new JSONObject(), null));

		SpaceTreeNode firstNode = new SpaceTreeNode("node1", "Node 1", new JSONObject(), firstNodeChildren);
		
		JSONArray secondNodeChildren = new JSONArray();
		secondNodeChildren.add(new SpaceTreeNode("node21", "Node 21", new JSONObject(), null));
		secondNodeChildren.add(new SpaceTreeNode("node22", "Node 22", new JSONObject(), null));
		secondNodeChildren.add(new SpaceTreeNode("node23", "Node 23", new JSONObject(), null));
		secondNodeChildren.add(new SpaceTreeNode("node24", "Node 24", new JSONObject(), null));

		SpaceTreeNode secondNode = new SpaceTreeNode("node2", "Node 2", new JSONObject(), secondNodeChildren);

		
		JSONArray thirdNodeChildren = new JSONArray();
		thirdNodeChildren.add(new SpaceTreeNode("node31", "Node 31", new JSONObject(), null));
		thirdNodeChildren.add(new SpaceTreeNode("node32", "Node 32", new JSONObject(), null));
		thirdNodeChildren.add(new SpaceTreeNode("node33", "Node 33", new JSONObject(), null));
		thirdNodeChildren.add(new SpaceTreeNode("node34", "Node 34", new JSONObject(), null));
		thirdNodeChildren.add(new SpaceTreeNode("node35", "Node 35", new JSONObject(), null));

		SpaceTreeNode thirdNode = new SpaceTreeNode("node3", "Node 3", new JSONObject(), thirdNodeChildren);

		JSONArray rootChildren = new JSONArray();
		rootChildren.add(firstNode);
		rootChildren.add(secondNode);
		rootChildren.add(thirdNode);

		SpaceTreeNode root = new SpaceTreeNode("ROOT", "ROOT", new JSONObject(), rootChildren);
		treeData = root.toJSONString();
	}

	@Wire
	Div infovis;
	
	@Wire
	Textbox nodeTxt;

	public String getTreeData() {
		return treeData;
	}

	public void setTreeData(String treeData) {
		this.treeData = treeData;
	}

	
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		infovis.setWidgetListener("onBind", "init('" + treeData + "');");
	}
	
	@Listen("onClick= #add")
	public void addNode() {
		System.out.println("Adding node on server side!!!");
		JSONArray c = new JSONArray();
		c.add(new SpaceTreeNode(nodeTxt.getValue(), nodeTxt.getValue(), new JSONObject(), new JSONArray()));
		Clients.evalJavaScript("addNode(\'" + new SpaceTreeNode("ROOT", "ROOT", new JSONObject(), c).toJSONString() + "\')");
	}

	@Listen("onClick= #refresh")
	public void refreshTree() {
		System.out.println("Refreshing whole SpaceTree on server side!!!");
//		infovis.invalidate();
		Clients.evalJavaScript("refresh('" + treeData + "');");
	}
	
	@Listen("onRemove= #infovis")
	public void nodeRemoved(Event evt) {
		System.out.println("data received" + evt.getData());
	}
}
