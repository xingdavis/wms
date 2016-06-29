<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>备份数据</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	//DOM加载完毕执行
	$(document).ready(function() {
		var d = new Date();
		$('#q_sdate').datebox('setValue', FormatterDate(d));
		$('#q_edate').datebox('setValue', FormatterDate(d));
	});

	function exportBackup() {
		var url='${path}/deliverys/backup/' + $('#q_sdate').datebox('getValue') + '/'
		+ $('#q_edate').datebox('getValue');
		alert(url);
		window.open(url);
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<!-- 按钮 -->
		<div id="toolbar">
			从：<input id="q_sdate" type="text" class="easyui-datebox" /> 到：<input
				id="q_edate" type="text" class="easyui-datebox" /> <a
				href="javascript:exportBackup()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" id="btn_export">导出备份</a>
		</div>
	</div>
</body>
</html>
