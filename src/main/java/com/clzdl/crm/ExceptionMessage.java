package com.clzdl.crm;

import com.base.exception.IExceptionMessage;

public enum ExceptionMessage implements IExceptionMessage {

	SYS_ERROR(10000, "系统异常，请稍后再试"), PARAMS_ERROR(10001, "参数错误"), NO_DATA_ERROR(10002, "数据不存在"),
	NO_RIGHT_ERROR(10003, "无权操作"), WEIXIN_ERROR(10004, "调用微信接口错误"), RAND_ERROR(10005, "验证码错误或已过期"),
	FILE_SIZE_ERROR(10006, "上传文件过大，最大支持1M"), FILE_NAME_EXT_ERROR(10007, "上传文件格式不对"), DATA_EXISTS_ERROR(10008, "数据已存在"),
	ALI_NOTIFY_SIGN_ERROR(10009, "验签失败"), EMAIL_FMT_ERR(10010, "邮箱格式错误"), MOBILE_FMT_ERR(10011, "手机号码格式错误"),
	DATA_OUTDATE(10012, "数据已过期"), DATA_CONFILCT(10013, "刷新数据出现冲突"), NOT_SUFFICIENT_FUNDS(10015, "资金不足"),
	NOT_ENOUGH_INVENTORY(10016, "库存不足"), NOT_SUPPORT(10017, "系统暂不支持"), TENCENT_COS_WRONG(10018, "腾讯云处理失败"),
	TASK_STATUS_DOING(10019, "任务进行中"),

	USER_LOGIN_ERROR(20001, "登录名或密码错误"), USER_OLD_PWD_ERROR(20003, "旧密码输入错误，请重新输入"), USER_VALIDCODE_ERR(20006, "验证码错误"),
	USER_ORDER_NOPAY(20007, "用户订单未支付"), WEBORDER_MONEY_WRONG(20008, "用户订单金额不正确"), SURVEY_ALREAD_ANSWER(20009, "问卷已提交"),
	USER_BUY_LIMIT(20010, "用户购买次数受限"), USER_NO_CONSUME(20011, "用户无消费记录"), USER_VOTE_LIMIT(20012, "用户投票次数受限"),
	USER_MAC_WRONG(20013, "用户MAC非法"), TENCENT_TRASFER_WRONG(20014, "调用腾讯转码加密接口失败"),
	ONLY_BUY_SECOND_LEVEL_CLASSIFY(20015, "只能购买二级分类"), SAMEWORKD_NOT_ADMIRE_(20016, "不能重复点赞"),
	EXCEED_MAX_TIMES(20017, "超过允许的最大次数"), PWD_WRONG(20018, "密码错误"), USER_DISREWARD_NOT_ENOUGH(20019, "用户佣金不足"),

	SYS_ORG_OPERATION_ERROR(40001, "组织机构下有系统用户，不能删除"), SYS_ROLE_OPERATION_ERROR(40002, "角色下有系统用户，不能删除"),
	SYS_ORG_DELETE_ERROR(40003, "此组织机构为父节点，不能删除"), SYS_MENU_DELETE_ERROR(40004, "菜单关联了角色，不能删除"),
	SYS_WX_FRISTMENU_ADD_ERROR(40005, "微信一级菜单最多添加3个"), SYS_WX_SECONDMENU_ADD_ERROR(40006, "微信二级菜单最多添加5个");

	public static ExceptionMessage getEnum(String message) {
		if (message == null) {
			return null;
		}
		for (ExceptionMessage exceptionMessage : ExceptionMessage.values()) {
			if (exceptionMessage.getMessage().equals(message)) {
				return exceptionMessage;
			}
		}
		return null;
	}

	private Integer code;
	private String message;

	private ExceptionMessage(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Integer code() {
		return getCode();
	}

	@Override
	public String message() {
		return getMessage();
	}

}
