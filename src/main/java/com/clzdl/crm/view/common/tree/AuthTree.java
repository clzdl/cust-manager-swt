package com.clzdl.crm.view.common.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class AuthTree extends Composite {
	private Tree tree;
	private Object parentCode;
	private Map<Object, TreeNodeData> dataMap = new HashMap<Object, TreeNodeData>();

	public AuthTree(Composite parent, int style, Object parentCode) {
		super(parent, style);
		this.parentCode = parentCode;
		createContent();
	}

	public AuthTree(Composite parent, int style, List<TreeNodeData> dataList, Object parentCode) {
		super(parent, style);
		this.parentCode = parentCode;

		createContent();
		fillTree(dataList);
	}

	public void fillTree(List<TreeNodeData> list) {
		if (tree.getItemCount() > 0) {
			tree.removeAll();
		}
		if (CollectionUtils.isNotEmpty(list)) {
			for (TreeNodeData data : list) {
				dataMap.put(data.getCode(), data);
			}
		}
		buildTree();
	}

	public List<TreeNodeData> getSelections() {
		List<TreeNodeData> result = new ArrayList<TreeNodeData>();
		for (Map.Entry<Object, TreeNodeData> entry : dataMap.entrySet()) {
			if (entry.getValue().getSelected()) {
				result.add(entry.getValue());
			}
		}

		return result;
	}

	private void createContent() {
		setLayout(new FillLayout());
		tree = new Tree(this, SWT.BORDER | SWT.CHECK);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				TreeItem item = (TreeItem) e.item;
				if (e.detail == SWT.CHECK) {
					boolean checked = item.getChecked();
					checkItems(item, checked);
					checkPath(item.getParentItem(), checked, false);
				}
			}
		});
	}

	private void checkItems(TreeItem item, boolean checked) {
		item.setGrayed(false);
		item.setChecked(checked);
		dataMap.get(((TreeNodeData) item.getData()).getCode()).setSelected(checked);
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; i++) {
			checkItems(items[i], checked);
		}
	}

	private void checkPath(TreeItem item, boolean checked, boolean grayed) {
		if (item == null)
			return;
		if (grayed) {
			checked = true;
		} else {
			int index = 0;
			TreeItem[] items = item.getItems();
			while (index < items.length) {
				TreeItem child = items[index];
				if (child.getGrayed() || checked != child.getChecked()) {
					checked = grayed = true;
					break;
				}
				index++;
			}
		}
		item.setChecked(checked);
		item.setGrayed(grayed);
		dataMap.get(((TreeNodeData) item.getData()).getCode()).setSelected(checked);
		checkPath(item.getParentItem(), checked, grayed);
	}

	private void buildTree() {
		if (tree.getItemCount() > 0) {
			tree.removeAll();
		}
		TreeItem item = null;
		TreeNodeData data = null;
		for (Map.Entry<Object, TreeNodeData> entry : dataMap.entrySet()) {
			data = entry.getValue();
			if (!data.compareParentCode(parentCode)) {
				continue;
			}

			item = new TreeItem(tree, SWT.NULL);
			item.setData(data);
			item.setText(data.getTitle());
			if (data.getSelected()) {
				checkPath(item, true, false);
			}
			buildTreeItem(item, data.getCode());
		}
	}

	private void buildTreeItem(TreeItem treeItem, Object parentCode) {
		TreeItem item = null;
		TreeNodeData data = null;
		for (Map.Entry<Object, TreeNodeData> entry : dataMap.entrySet()) {
			data = entry.getValue();
			if (!data.compareParentCode(parentCode)) {
				continue;
			}
			item = new TreeItem(treeItem, SWT.NULL);
			item.setData(data);
			item.setText(data.getTitle());
			if (data.getSelected()) {
				checkPath(item, true, false);
			}
			buildTreeItem(item, data.getCode());
		}
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setSize(300, 200);
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		shell.setBounds(x, y, rect.width, rect.height);
		shell.setLayout(new FillLayout());
		List<TreeNodeData> list = new ArrayList<TreeNodeData>();
		list.add(new TreeNodeData(1, 0, "title1", false));
		list.add(new TreeNodeData(2, 0, "title2", false));
		list.add(new TreeNodeData(3, 0, "title3", false));
		list.add(new TreeNodeData(11, 1, "title11", false));
		list.add(new TreeNodeData(12, 1, "title12", true));
		list.add(new TreeNodeData(13, 1, "title13", false));
		list.add(new TreeNodeData(21, 2, "title21", false));
		list.add(new TreeNodeData(22, 2, "title22", false));
		list.add(new TreeNodeData(23, 2, "title23", false));
		list.add(new TreeNodeData(31, 3, "title31", false));
		list.add(new TreeNodeData(32, 3, "title32", false));
		list.add(new TreeNodeData(33, 3, "title33", false));
		AuthTree authTree = new AuthTree(shell, SWT.NONE, 0);
		authTree.fillTree(list);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
