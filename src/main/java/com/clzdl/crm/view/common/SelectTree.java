package com.clzdl.crm.view.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class SelectTree extends Composite {
	private Text text;
	private Shell popup;
	private Tree tree;
	private List<SelectTreeData> dataList;
	private Object parentCode;
	private Boolean isMulti = false;
	private SelectTreeData result;
	private Object defaultCode;

	public static class SelectTreeData {
		private Object code;
		private Object parentCode;
		private String title;
		private Boolean selected = false;

		public SelectTreeData(Object code, Object parentCode, String title, Boolean selected) {
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

	public SelectTree(Composite parent, int style, Object parentCode) {
		super(parent, style);
		this.parentCode = parentCode;
		createContent();
	}

	public SelectTree(Composite parent, int style, List<SelectTreeData> dataList, Object parentCode) {
		super(parent, style);
		this.dataList = dataList;
		this.parentCode = parentCode;
		createContent();
		createPopup();
	}

	public Object getCodeValue() {
		return result.getCode();
	}

	public String getTitleValue() {
		return result.getTitle();
	}

	public void setDataList(List<SelectTreeData> dataList) {
		this.dataList = dataList;
		createPopup();
	}

	public void setDefault(Object defaultCode) {
		this.defaultCode = defaultCode;
	}

	private boolean isDropped() {
		return !isDisposed() && popup.getVisible();
	}

	private void createPopup() {
		popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
		popup.setLayout(new FillLayout());
		int treeStyle = SWT.BORDER;
		if ((getStyle() & SWT.CHECK) != 0) {
			treeStyle |= SWT.CHECK;
			isMulti = true;
		}
		tree = new Tree(popup, treeStyle);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				TreeItem item = (TreeItem) e.item;
				if (e.detail == SWT.CHECK) {
					boolean checked = item.getChecked();
					checkItems(item, checked);
					checkPath(item.getParentItem(), checked, false);
				} else {
					text.setText(item.getText());
					result = (SelectTreeData) item.getData();
					if (!isMulti) {
						dropDown(false);
					}
				}
			}
		});
		buildTree();
		tree.setSize(200, 200);
	}

	private void checkItems(TreeItem item, boolean checked) {
		item.setGrayed(false);
		item.setChecked(checked);
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
		checkPath(item.getParentItem(), checked, grayed);
	}

	private void dropDown(Boolean dropped) {
		if (dropped == isDropped()) {
			return;
		}
		if (!isVisible()) {
			return;
		}
		if (!dropped) {
			popup.setVisible(false);
			if (!isDisposed() && isFocusControl()) {
				text.setFocus();
			}
			return;
		}
		if (getShell() != popup.getParent()) {
			popup.dispose();
			popup = null;
			tree = null;
			createPopup();
		}
		Rectangle rect = getDisplay().map(this, null, text.getBounds());
		if (popup.isDisposed()) {
			createPopup();
		}
		popup.setBounds(rect.x, rect.y + rect.height, rect.width, 100);
		popup.setVisible(true);
	}

	private void createContent() {
		setLayout(new FillLayout());
		text = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		Listener textListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.MouseDown: {
					Point pt = getDisplay().map(text, SelectTree.this, event.x, event.y);
					Event mouseEvent = new Event();
					mouseEvent.button = event.button;
					mouseEvent.count = event.count;
					mouseEvent.stateMask = event.stateMask;
					mouseEvent.time = event.time;
					mouseEvent.x = pt.x;
					mouseEvent.y = pt.y;
					notifyListeners(SWT.MouseDown, mouseEvent);
					if (isDisposed())
						break;
					event.doit = mouseEvent.doit;
					if (!event.doit)
						break;
					if (event.button != 1)
						return;
					if (text.getEditable())
						return;
					boolean dropped = isDropped();
					if (text.getEditable() && text.isFocusControl())
						text.selectAll();
					if (!dropped) {
						setFocus();
					}
					dropDown(!dropped);
					break;
				}
				}
			}
		};
		int[] textEvents = { SWT.MouseDown };
		for (int i = 0; i < textEvents.length; ++i) {
			text.addListener(textEvents[i], textListener);
		}
	}

	private void buildTree() {
		if (tree.getItemCount() > 0) {
			tree.removeAll();
		}
		TreeItem item = null;
		for (SelectTreeData data : dataList) {
			if (!data.compareParentCode(parentCode)) {
				continue;
			}

			item = new TreeItem(tree, SWT.NULL);
			item.setData(data);
			item.setText(data.getTitle());
			if (data.getSelected() || data.compareCode(defaultCode)) {
				tree.setSelection(item);
			}
			buildTreeItem(item, data.getCode());
		}
	}

	private void buildTreeItem(TreeItem treeItem, Object parentCode) {
		TreeItem item = null;
		for (SelectTreeData data : dataList) {
			if (!data.compareParentCode(parentCode)) {
				continue;
			}
			item = new TreeItem(treeItem, SWT.NULL);
			item.setData(data);
			item.setText(data.getTitle());
			if (data.getSelected() || data.compareCode(defaultCode)) {
				tree.setSelection(item);
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
		List<SelectTreeData> list = new ArrayList<SelectTree.SelectTreeData>();
		list.add(new SelectTreeData(1, 0, "title1", false));
//		list.add(new SelectTreeData(2, 0, "title2", false));
//		list.add(new SelectTreeData(3, 0, "title3", false));
//		list.add(new SelectTreeData(11, 1, "title11", false));
//		list.add(new SelectTreeData(12, 1, "title12", false));
//		list.add(new SelectTreeData(13, 1, "title13", false));
//		list.add(new SelectTreeData(21, 2, "title21", false));
//		list.add(new SelectTreeData(22, 2, "title22", false));
//		list.add(new SelectTreeData(23, 2, "title23", false));
//		list.add(new SelectTreeData(31, 3, "title31", false));
//		list.add(new SelectTreeData(32, 3, "title32", false));
//		list.add(new SelectTreeData(33, 3, "title33", false));
		SelectTree stTree = new SelectTree(shell, SWT.NONE, 0);
		stTree.setDefault(3);
		stTree.setDataList(list);
		new Text(shell, SWT.BORDER);
		stTree.setDefault(2);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
