package com.clzdl.crm.view.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class SelectTree extends Composite {
	private Text edit;
	private Shell popup;
	private Tree tree;

	public SelectTree(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		edit = new Text(this, SWT.BORDER | SWT.SINGLE);
		edit.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				Rectangle rect = getDisplay().map(getShell(), null, edit.getBounds());
				if (popup.isDisposed()) {
					createPopup();
				}
				popup.setBounds(rect.x, rect.y + rect.height, rect.width, 100);
				popup.open();
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				popup.dispose();
			}
		});

		createPopup();
	}

	private void createPopup() {
		popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
		popup.setLayout(new FillLayout());
		tree = new Tree(popup, SWT.CHECK | SWT.BORDER);

		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (e.detail == SWT.CHECK) {
					TreeItem item = (TreeItem) e.item;
					boolean checked = item.getChecked();
					checkItems(item, checked);
					checkPath(item.getParentItem(), checked, false);
				} else {
					System.out.println(e.detail);
				}
			}
		});

		for (int i = 0; i < 4; i++) {
			TreeItem itemI = new TreeItem(tree, SWT.NULL);
			itemI.setText("Item " + i);
			for (int j = 0; j < 4; j++) {
				TreeItem itemJ = new TreeItem(itemI, SWT.NULL);
				itemJ.setText("Item " + i + " " + j);
				for (int k = 0; k < 4; k++) {
					TreeItem itemK = new TreeItem(itemJ, SWT.NULL);
					itemK.setText("Item " + i + " " + j + " " + k);
				}
			}
		}
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

		new SelectTree(shell, SWT.NONE);
		new Text(shell, SWT.BORDER);
		Tree tree = new Tree(shell, SWT.CHECK);
		for (int i = 0; i < 4; i++) {
			TreeItem itemI = new TreeItem(tree, SWT.NULL);
			itemI.setText("Item " + i);
			for (int j = 0; j < 4; j++) {
				TreeItem itemJ = new TreeItem(itemI, SWT.NULL);
				itemJ.setText("Item " + i + " " + j);
				for (int k = 0; k < 4; k++) {
					TreeItem itemK = new TreeItem(itemJ, SWT.NULL);
					itemK.setText("Item " + i + " " + j + " " + k);
				}
			}
		}
		tree.setSize(200, 200);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
