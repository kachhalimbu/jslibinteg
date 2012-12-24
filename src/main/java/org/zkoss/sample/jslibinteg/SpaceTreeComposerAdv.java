package org.zkoss.sample.jslibinteg;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SpaceTreeComposerAdv extends SelectorComposer<Window> {
	
	public String treeData;
	private String treeData2;
	private String selectedST = "infovis";
	
	public void doBeforeComposeChildren(Window win) throws Exception {
		super.doBeforeComposeChildren(win);
		JSONArray rNode1Children = new JSONArray();
		rNode1Children.add(new SpaceTreeNode("rnode1_1", "Node 11", new JSONObject(), null));
		rNode1Children.add(new SpaceTreeNode("rnode1_2", "Node 12", new JSONObject(), null));
		rNode1Children.add(new SpaceTreeNode("rnode1_3", "Node 13", new JSONObject(), null));
		JSONArray root1Children = new JSONArray();
		SpaceTreeNode rnode1 = new SpaceTreeNode("rnode1", "Node 1", new JSONObject(), rNode1Children);
		root1Children.add(rnode1);

		SpaceTreeNode root1 = new SpaceTreeNode("ROOT1", "ROOT1", new JSONObject(), root1Children);
		treeData = root1.toJSONString();

		JSONArray rNode2Children = new JSONArray();
		rNode2Children.add(new SpaceTreeNode("rnode2_11", "Node 11", new JSONObject(), null));
		rNode2Children.add(new SpaceTreeNode("rnode2_12", "Node 12", new JSONObject(), null));
		rNode2Children.add(new SpaceTreeNode("rnode2_13", "Node 13", new JSONObject(), null));
		JSONArray root2Children = new JSONArray();
		SpaceTreeNode rnode2 = new SpaceTreeNode("rnode2", "Node 1", new JSONObject(), rNode2Children);

		root2Children.add(rnode2);
		SpaceTreeNode root2 = new SpaceTreeNode("ROOT2", "ROOT2", new JSONObject(), root2Children);
		treeData2 = root2.toJSONString();
	
	}

	@Wire
	Div infovis;
	@Wire
	Div infovis2;
	
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
		Clients.response(new AuInvoke(infovis, "_init", treeData));
		Clients.response(new AuInvoke(infovis2, "_init", treeData2));
	}
	
	@Listen("#infovis; #infovis2")
	public void selectInfovis(Event evt) {
		selectedST = evt.getTarget().getId();
	}
	@Listen("onClick= #add")
	public void addNode() {
		System.out.println("Adding node on server side!!!");
		if("infovis".equals(selectedST)) {
			JSONArray c1 = new JSONArray();
			c1.add(new SpaceTreeNode("rnode1" + nodeTxt.getValue(), nodeTxt.getValue(), new JSONObject(), new JSONArray()));
			Clients.response(new AuInvoke(infovis, "_addNode", new SpaceTreeNode("ROOT1", "ROOT1", new JSONObject(), c1).toJSONString()));
		} else {
			JSONArray c2 = new JSONArray();
			c2.add(new SpaceTreeNode("rnode2" + nodeTxt.getValue(), nodeTxt.getValue(), new JSONObject(), new JSONArray()));
			Clients.response(new AuInvoke(infovis2, "_addNode", new SpaceTreeNode("ROOT2", "ROOT2", new JSONObject(), c2).toJSONString()));
		}
	}

	@Listen("onClick= #refresh")
	public void refreshTree() {
		System.out.println("Refreshing whole SpaceTree on server side!!!");
		Clients.response(new AuInvoke(infovis, "_init", treeData));
		Clients.response(new AuInvoke(infovis2, "_init", treeData2));
	}
	
	@Listen("onRemove= #infovis")
	public void nodeInfovisRemoved(Event evt) {
		System.out.println("data received" + evt.getData());
	}
	@Listen("onRemove= #infovis2")
	public void nodeInfovis2Removed(Event evt) {
		System.out.println("data received" + evt.getData());
	}
}
