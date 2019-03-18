package com.clzdl.crm.view.common;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MsgBox extends MessageBox {
	public MsgBox(Shell parent, String msg) {
		super(parent);
		setMessage(msg);
	}

	public MsgBox(Shell parent, int style, String msg) {
		super(parent, style);
		setMessage(msg);
	}

	public int open() {
		return super.open();
	}

	// 继承shell 需要此函数
	protected void checkSubclass() {
	}

}
