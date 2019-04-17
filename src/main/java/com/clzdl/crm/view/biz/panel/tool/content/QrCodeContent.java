package com.clzdl.crm.view.biz.panel.tool.content;

import java.awt.image.BufferedImage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.springboot.auth.EnumSysPermissionProfile;
import com.clzdl.crm.utils.ImageConvertor;
import com.clzdl.crm.view.common.AbstractComposite;
import com.clzdl.crm.view.common.MsgBox;
import com.framework.common.util.image.QRCodeUtil;
import com.framework.common.util.net.ip.IpUtil;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCodeContent extends AbstractComposite {
	private final static Logger _logger = LoggerFactory.getLogger(QrCodeContent.class);
	private final static String title = "二维码生成";
	private List ipList;
	private final static Integer _web_port = 8088;
	private Label qrCodeShow;

	public QrCodeContent(Composite parent, int style) {
		super(parent, style, title, EnumSysPermissionProfile.NONE);
		setLayout(new FormLayout());
		ipList = new List(this, SWT.NONE);
		try {
			java.util.List<String> list = IpUtil.getLocalIpAddr();
			for (String ip : list) {
				ipList.add(ip);
			}
		} catch (Exception e) {
			_logger.error(e.getMessage(), e);
		}

		Button btnWebQrCodeCreate = new Button(this, SWT.PUSH);
		btnWebQrCodeCreate.setText("生成站点二维码");
		btnWebQrCodeCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				Integer selIndex = ipList.getSelectionIndex();
				if (selIndex == -1) {
					new MsgBox(getShell(), "请先选择ip").open();
					return;
				}
				StringBuilder sb = new StringBuilder();
				sb.append("http://");
				sb.append(ipList.getItem(selIndex)).append(":").append(_web_port);
				sb.append("/web");

				try {
					BufferedImage qrImage = QRCodeUtil.encode(sb.toString(), null, false, ErrorCorrectionLevel.H);
					qrCodeShow.setImage(new Image(getDisplay(), ImageConvertor.convertToSWT(qrImage)));
				} catch (Exception e1) {
					_logger.error(e1.getMessage(), e1);
				}
			}
		});

		qrCodeShow = new Label(this, SWT.NONE);

		FormData ipListFormData = new FormData();
		ipListFormData.top = new FormAttachment(0, 10);
		ipListFormData.left = new FormAttachment(0, 10);
		ipListFormData.right = new FormAttachment(30);
		ipListFormData.bottom = new FormAttachment(30);
		ipList.setLayoutData(ipListFormData);

		FormData btnWebQrCodeCreateFormData = new FormData();
		btnWebQrCodeCreateFormData.left = new FormAttachment(0, 20);
		btnWebQrCodeCreateFormData.right = new FormAttachment(30);
		btnWebQrCodeCreateFormData.top = new FormAttachment(ipList, 10);
		btnWebQrCodeCreate.setLayoutData(btnWebQrCodeCreateFormData);

		FormData qrCodeShowFormData = new FormData();
		qrCodeShowFormData.top = new FormAttachment(0, 10);
		qrCodeShowFormData.left = new FormAttachment(ipList, 10);
		qrCodeShowFormData.right = new FormAttachment(100, -10);
		qrCodeShowFormData.bottom = new FormAttachment(100);
		qrCodeShow.setLayoutData(qrCodeShowFormData);
	}

}
