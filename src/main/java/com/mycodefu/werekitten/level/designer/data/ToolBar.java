package com.mycodefu.werekitten.level.designer.data;

import java.util.Arrays;
import java.util.Map;

public class ToolBar {
private Map<String, MenuItem> objectList;

public MenuItem[] getObjectList() {
	return (MenuItem[]) objectList.values().toArray();
}

public void setObjectList(MenuItem[] objectList) {
	Arrays.stream(objectList)
	.forEach(item->{
		this.objectList.put(item.getDisplayName(), item);
	});
}

}
