/*系统管理-菜单*/
CREATE TABLE `sys_menu` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '系统菜单表',
	`name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '菜单名称',
	`href` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '菜单链接地址',
	`parent_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '父节点id',
	`icon` VARCHAR(15) NOT NULL DEFAULT '&#xe66b;' COMMENT '菜单icon',
	`path` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '节点路径',
	`sort_no` INT(11) NOT NULL DEFAULT '0' COMMENT '排序号',
	`menu_type` tinyint NOT NULL DEFAULT '0' COMMENT '类型；0-菜单权限;1-功能权限',
	 
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

/*系统管理-系统用户*/
CREATE TABLE `sys_user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '系统用户表',
	`name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '姓名',
	`phone` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '手机',
	`email` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '邮箱',
	`sex` CHAR(1) NOT NULL DEFAULT '1' COMMENT '性别，1男 0女',
	`login_name` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '登陆名',
	`login_pwd` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '密码',
	`user_type` TINYINT NOT NULL DEFAULT '0' COMMENT '用户类型;0-系统，1-商户',
	
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

/*系统管理-角色*/
CREATE TABLE `sys_role` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '系统角色表',
	`role_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '名称',
	`description` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '说明',
	
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

/*系统管理-角色菜单中间表*/
CREATE TABLE `sys_role_menu` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '系统角色菜单关系表',
	`sys_role_id` BIGINT(20) NOT NULL  DEFAULT '0' COMMENT '角色id',
	`sys_menu_id` BIGINT(20) NOT NULL  DEFAULT '0' COMMENT '菜单id',
	
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

/*系统管理-角色用户中间表*/
CREATE TABLE `sys_role_user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '系统角色用户关系表',
	`sys_role_id` BIGINT(20) NOT NULL  DEFAULT '0' COMMENT '角色id',
	`sys_user_id` BIGINT(20) NOT NULL  DEFAULT '0' COMMENT '用户id',
	
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

/*系统管理-系统操作日志*/
CREATE TABLE `sys_operation_log` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '系统操作日志表',
	`sys_user_id` BIGINT(11) NOT NULL  DEFAULT '0' COMMENT '用户id',
	`contents` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '日志',
	
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);


/*初始化角色*/
INSERT INTO `sys_role` (`id`, `role_name`, `description`) VALUES (1, 'admin', '系统管理员');
/*初始化角色用户*/
INSERT INTO `sys_role_user` (`id`, `sys_role_id`, `sys_user_id`) VALUES (1, 1, 1);
/*password  123456*/
INSERT INTO `sys_user` (`id`, `name`, `email`, `phone`, `sex`, `login_name`, `login_pwd`) 
	VALUES (1, '管理员', 'admin@clzdl.com', '18645005420', '1', 'admin@clzdl.com', 'e10adc3949ba59abbe56e057f20f883e');

