package com.clzdl.crm.view.common.tree;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class SelectTree extends Composite {
	private Text text;
	private Shell popup;
	private Tree tree;
	private List<TreeNodeData> dataList;
	private Object parentCode;
	private Boolean isMulti = false;
	private TreeNodeData result;
	private Object defaultCode;

	public SelectTree(Composite parent, int style, Object parentCode) {
		super(parent, style);
		this.parentCode = parentCode;
		createContent();
	}

	public SelectTree(Composite parent, int style, List<TreeNodeData> dataList, Object parentCode) {
		super(parent, style);
		this.dataList = dataList;
		this.parentCode = parentCode;
		createContent();
		createPopup();
	}

	public Object getCodeValue() {
		if (null == result) {
			return null;
		}
		return result.getCode();
	}

	public String getTitleValue() {
		if (null == result) {
			return null;
		}
		return result.getTitle();
	}

	public void setDataList(List<TreeNodeData> dataList) {
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
					result = (TreeNodeData) item.getData();
					if (!isMulti) {
						dropDown(false);
					}
				}
			}
		});

		tree.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				if (isFocusControl()) {
					return;
				}
				dropDown(false);
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
		if (isFocusControl()) {
			tree.setFocus();
		}
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
					if (text.getEditable() && text.isFocusControl()) {
						text.selectAll();
					}
//					if (!dropped) {
//						setFocus();
//					}
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
		for (TreeNodeData data : dataList) {
			if (!data.compareParentCode(parentCode)) {
				continue;
			}

			item = new TreeItem(tree, SWT.NULL);
			item.setData(data);
			item.setText(data.getTitle());
			if (data.getSelected() || data.compareCode(defaultCode)) {
				tree.setSelection(item);
				text.setText(data.getTitle());
			}
			buildTreeItem(item, data.getCode());
		}
	}

	private void buildTreeItem(TreeItem treeItem, Object parentCode) {
		TreeItem item = null;
		for (TreeNodeData data : dataList) {
			if (!data.compareParentCode(parentCode)) {
				continue;
			}
			item = new TreeItem(treeItem, SWT.NULL);
			item.setData(data);
			item.setText(data.getTitle());
			if (data.getSelected() || data.compareCode(defaultCode)) {
				tree.setSelection(item);
				text.setText(data.getTitle());
			}
			buildTreeItem(item, data.getCode());
		}
	}

	@Override
	public boolean isFocusControl() {
		checkWidget();
		if (isFocus(text) || isFocus(tree) || isFocus(popup)) {
			return true;
		}
		return super.isFocusControl();
	}

	private boolean isFocus(Control control) {
		return control != null && !control.isDisposed() && control.isFocusControl();
	}

}
