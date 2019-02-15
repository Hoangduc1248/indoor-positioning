package com.example.vlc_project_open_campus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;

class TreeElement {
	public String name;
	public String parent;
	public ArrayList<String> path;
	
	public TreeElement(String name) {
		this.name = name;
		this.parent = null;
		this.path = new ArrayList<String>();
	}
}

public class PathFinding {
	private Map<String, TreeElement> tree;
	
	public PathFinding(Vector<String> lines) {
		tree = new HashMap<String, TreeElement>();
		String[] name = lines.get(0).split(",");
		for(int i=1; i<lines.size(); ++i) {
			TreeElement el = new TreeElement(name[i]);
			String[] str = lines.get(i).split(",");
			for(int j=1; j<str.length; ++j){
				if(str[j].equals("1") && i!=j) el.path.add(name[j]);
			}
			tree.put(name[i], el);
		}
	}
	public void clear() {
		for(String key : tree.keySet()){
			tree.get(key).parent = null;
		}
	}
	public void setTree(String root){
		tree.get(root).parent = root;
		Queue<String> qu = new LinkedList<String>();
		qu.offer(root);
		while(!qu.isEmpty()){
			int size = qu.size();
			for(int k=0; k<size; k++){
				String name = qu.poll();
				for(String item : tree.get(name).path) {
					if(tree.get(item).parent == null) {
						tree.get(item).parent = name;
						qu.offer(item);
					}
				}
			}
		}
	}
	public String getParent(String name) {
		return tree.get(name).parent;
	}
}
