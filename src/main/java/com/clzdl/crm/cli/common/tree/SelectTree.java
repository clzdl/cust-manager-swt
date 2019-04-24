package com.clzdl.crm.cli.common.tree;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
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
	private Listener filter, listener;
	private Boolean hasFocus = false;

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
		int[] popupEvents = { SWT.Close, SWT.Paint };
		for (int i = 0; i < popupEvents.length; i++) {
			popup.addListener(popupEvents[i], listener);
		}

		int treeStyle = SWT.BORDER;
		if ((getStyle() & SWT.CHECK) != 0) {
			treeStyle |= SWT.CHECK;
			isMulti = true;
		}
		tree = new Tree(popup, treeStyle);
		int[] treeEvents = { SWT.Selection, SWT.FocusOut };
		for (int i = 0; i < treeEvents.length; ++i) {
			tree.addListener(treeEvents[i], listener);
		}

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
		Display display = getDisplay();
		if (!dropped) {
			display.removeFilter(SWT.Selection, filter);
			popup.setVisible(false);
			if (!isDisposed() && isFocusControl()) {
				text.setFocus();
			}
			return;
		}
		if (!isVisible())
			return;
		if (getShell() != popup.getParent()) {
			tree.removeListener(SWT.Dispose, listener);
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
		if (isFocusControl()) {
			tree.setFocus();
		}
		popup.setVisible(true);

		/*
		 * Add a filter to listen to scrolling of the parent composite, when the
		 * drop-down is visible. Remove the filter when drop-down is not visible.
		 */

		display.removeFilter(SWT.Selection, filter);
		display.addFilter(SWT.Selection, filter);

	}

	private void createContent() {
		setLayout(new FillLayout());
		text = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		listener = event -> {
			if (isDisposed())
				return;
			if (popup == event.widget) {
				popupEvent(event);
				return;
			}
			if (text == event.widget) {
				textEvent(event);
				return;
			}
			if (tree == event.widget) {
				treeEvent(event);
				return;
			}

			if (SelectTree.this == event.widget) {
				selectTreeEvent(event);
				return;
			}
			if (getShell() == event.widget) {
				getDisplay().asyncExec(() -> {
					if (isDisposed())
						return;
					handleFocus(SWT.FocusOut);
				});
			}
		};

		filter = event -> {
			if (isDisposed())
				return;
			if (event.type == SWT.Selection) {
				if (event.widget instanceof ScrollBar) {
					handleScroll(event);
				}
				return;
			}
			Shell shell = ((Control) event.widget).getShell();
			if (shell == SelectTree.this.getShell()) {
				handleFocus(SWT.FocusOut);
			}
		};
		int[] textEvents = { SWT.MouseDown, SWT.FocusOut };
		for (int i = 0; i < textEvents.length; ++i) {
			text.addListener(textEvents[i], listener);
		}

		int[] selectTreeEvents = { SWT.Dispose, SWT.FocusIn, SWT.Move, SWT.Resize, SWT.FocusOut };
		for (int i = 0; i < selectTreeEvents.length; i++)
			this.addListener(selectTreeEvents[i], listener);

	}

	private void popupEvent(Event event) {
		switch (event.type) {
		case SWT.Paint:
			// draw black rectangle around list
			Rectangle listRect = tree.getBounds();
			Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
			event.gc.setForeground(black);
			event.gc.drawRectangle(0, 0, listRect.width + 1, listRect.height + 1);
			break;
		case SWT.Close:
			event.doit = false;
			dropDown(false);
			break;
		}
	}

	private void handleScroll(Event event) {
		ScrollBar scrollBar = (ScrollBar) event.widget;
		Control scrollableParent = scrollBar.getParent();
		if (scrollableParent.equals(tree))
			return;
		if (isParentScrolling(scrollableParent))
			dropDown(false);
	}

	private boolean isParentScrolling(Control scrollableParent) {
		Control parent = this.getParent();
		while (parent != null) {
			if (parent.equals(scrollableParent))
				return true;
			parent = parent.getParent();
		}
		return false;
	}

	private void textEvent(Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			handleFocus(SWT.FocusIn);
			break;
		}
		case SWT.DefaultSelection: {
			dropDown(false);
			Event e = new Event();
			e.time = event.time;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.DefaultSelection, e);
			break;
		}
		case SWT.DragDetect:
		case SWT.MouseDoubleClick:
		case SWT.MouseMove:
		case SWT.MouseEnter:
		case SWT.MouseExit:
		case SWT.MouseHover: {
			Point pt = getDisplay().map(text, this, event.x, event.y);
			event.x = pt.x;
			event.y = pt.y;
			notifyListeners(event.type, event);
			event.type = SWT.None;
			break;
		}
		case SWT.KeyDown: {
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
			if (!dropped) {
				setFocus();
			}
			dropDown(!dropped);
			// Further work : Need to add support for incremental search in
			// pop up list as characters typed in text widget
			break;
		}
		case SWT.KeyUp: {
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.keyLocation = event.keyLocation;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyUp, e);
			event.doit = e.doit;
			break;
		}
		case SWT.MenuDetect: {
			Event e = new Event();
			e.time = event.time;
			e.detail = event.detail;
			e.x = event.x;
			e.y = event.y;
			if (event.detail == SWT.MENU_KEYBOARD) {
				Point pt = getDisplay().map(text, null, text.getCaretLocation());
				e.x = pt.x;
				e.y = pt.y;
			}
			notifyListeners(SWT.MenuDetect, e);
			event.doit = e.doit;
			event.x = e.x;
			event.y = e.y;
			break;
		}
		case SWT.Modify: {
			tree.deselectAll();
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.Modify, e);
			break;
		}
		case SWT.MouseDown: {
			Point pt = getDisplay().map(text, this, event.x, event.y);
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
			if (!dropped)
				setFocus();
			dropDown(!dropped);
			break;
		}
		case SWT.MouseUp: {
			Point pt = getDisplay().map(text, this, event.x, event.y);
			Event mouseEvent = new Event();
			mouseEvent.button = event.button;
			mouseEvent.count = event.count;
			mouseEvent.stateMask = event.stateMask;
			mouseEvent.time = event.time;
			mouseEvent.x = pt.x;
			mouseEvent.y = pt.y;
			notifyListeners(SWT.MouseUp, mouseEvent);
			if (isDisposed())
				break;
			event.doit = mouseEvent.doit;
			if (!event.doit)
				break;
			if (event.button != 1)
				return;
			if (text.getEditable())
				return;
			if (text.getEditable() && text.isFocusControl())
				text.selectAll();
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				// The enter causes default selection and
				// the arrow keys are used to manipulate the list contents so
				// do not use them for traversal.
				event.doit = false;
				break;
			case SWT.TRAVERSE_TAB_PREVIOUS:
				event.doit = traverse(SWT.TRAVERSE_TAB_PREVIOUS);
				event.detail = SWT.TRAVERSE_NONE;
				return;
			}
			Event e = new Event();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.keyLocation = event.keyLocation;
			notifyListeners(SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
		case SWT.Verify: {
			Event e = new Event();
			e.text = event.text;
			e.start = event.start;
			e.end = event.end;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.keyLocation = event.keyLocation;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.Verify, e);
			event.text = e.text;
			event.doit = e.doit;
			break;
		}
		}
	}

	private void treeEvent(Event event) {
		switch (event.type) {
//		case SWT.Dispose:
//			if (getShell() != popup.getParent()) {
//				String[] items = tree.getItems();
//				int selectionIndex = list.getSelectionIndex();
//				popup = null;
//				list = null;
//				createPopup(items, selectionIndex);
//			}
//			break;
		case SWT.FocusIn: {
			handleFocus(SWT.FocusIn);
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1)
				return;
			dropDown(false);
			break;
		}
		case SWT.Selection: {
			Event e = new Event();
			e.time = event.time;
			e.stateMask = event.stateMask;
			e.doit = event.doit;
			notifyListeners(SWT.Selection, e);
			event.doit = e.doit;

			if (!isDropped()) {
				return;
			}

			TreeItem[] itemSelected = tree.getSelection();
			if (0 >= itemSelected.length) {
				return;
			}

			for (TreeItem item : itemSelected) {
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

			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_ESCAPE:
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				event.doit = false;
				break;
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
				event.doit = text.traverse(event.detail);
				event.detail = SWT.TRAVERSE_NONE;
				if (event.doit)
					dropDown(false);
				return;
			}
			Event e = new Event();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.keyLocation = event.keyLocation;
			notifyListeners(SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
		case SWT.KeyUp: {
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.keyLocation = event.keyLocation;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyUp, e);
			event.doit = e.doit;
			break;
		}
		case SWT.KeyDown: {
			if (event.character == SWT.ESC) {
				// Escape key cancels popup list
				dropDown(false);
			}
			if ((event.stateMask & SWT.ALT) != 0
					&& (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN)) {
				dropDown(false);
			}
			if (event.character == SWT.CR) {
				// Enter causes default selection
				dropDown(false);
				Event e = new Event();
				e.time = event.time;
				e.stateMask = event.stateMask;
				notifyListeners(SWT.DefaultSelection, e);
			}
			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed())
				break;
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.keyLocation = event.keyLocation;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyDown, e);
			event.doit = e.doit;
			break;

		}
		}
	}

	void selectTreeEvent(Event event) {
		switch (event.type) {
		case SWT.Dispose:
			removeListener(SWT.Dispose, listener);
			notifyListeners(SWT.Dispose, event);
			event.type = SWT.None;

			if (popup != null && !popup.isDisposed()) {
				tree.removeListener(SWT.Dispose, listener);
				popup.dispose();
			}
			Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			popup = null;
			text = null;
			tree = null;
			break;
		case SWT.FocusIn:
			Control focusControl = getDisplay().getFocusControl();
			if (focusControl == tree)
				return;
			if (isDropped()) {
				tree.setFocus();
			} else {
				text.setFocus();
			}
			break;
		case SWT.FocusOut:
			text.clearSelection();
			break;
		case SWT.Move:
			dropDown(false);
			break;
		case SWT.Resize:
			internalLayout(false);
			break;
		}
	}

	private void handleFocus(int type) {
		switch (type) {
		case SWT.FocusIn: {
			if (hasFocus)
				return;
			hasFocus = true;
			Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			shell.addListener(SWT.Deactivate, listener);
			Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			display.addFilter(SWT.FocusIn, filter);
			Event e = new Event();
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			if (!hasFocus)
				return;
			Control focusControl = getDisplay().getFocusControl();
			if (focusControl == tree || focusControl == text)
				return;
			hasFocus = false;
			Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			Event e = new Event();
			notifyListeners(SWT.FocusOut, e);
			break;
		}
		}
	}

	private void internalLayout(boolean changed) {
		if (isDropped())
			dropDown(false);
		Rectangle rect = getClientArea();
		int width = rect.width;
		int height = rect.height;
		text.setBounds(0, 0, width, height);
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
