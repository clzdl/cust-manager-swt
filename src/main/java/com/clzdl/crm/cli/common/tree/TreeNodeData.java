package com.clzdl.crm.cli.common.tree;

public class TreeNodeData {
	private Object code;
	private Object parentCode;
	private String title;
	private Boolean selected = false;

	public TreeNodeData(Object code, Object parentCode, String title, Boolean selected) {
		this.code = code;
		this.parentCode = parentCode;
		this.title = title;
		this.selected = selected;
	}

	public Object getCode() {
		return code;
	}

	public void setCode(Object code) {
		this.code = code;
	}

	public Object getParentCode() {
		return parentCode;
	}

	public void setParentCode(Object parentCode) {
		this.parentCode = parentCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Boolean compareParentCode(Object parentCode) {
		if (parentCode instanceof Integer || parentCode instanceof Long || parentCode instanceof Double) {
			return this.parentCode == parentCode;
		} else {
			return this.parentCode.equals(parentCode);
		}
	}

	public Boolean compareCode(Object parentCode) {
		if (parentCode instanceof Integer || parentCode instanceof Long || parentCode instanceof Double) {
			return this.code == parentCode;
		} else {
			return this.code.equals(parentCode);
		}
	}
}
