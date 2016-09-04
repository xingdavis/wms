<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看明细</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	//DOM加载完毕执行
	$(document).ready(function() {
	});
	function formatDate(value, row) {
		if (value) {
			return new Date(value).Format("yyyy-MM-dd");
		} else {
			return value;
		}
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar"></div>
		<table id="dg" class="easyui-datagrid" fit="true"
			data-options="url:'${path}/stock_ins/details/${billId}',toolbar:'#toolbar',method:'get',fitColumns:true,singleSelect:true,pagination:false,rownumbers:true">
			<thead>
				<tr>
					<th field="orderCode" width="100">订单号</th>
					<th field="carNo" width="100">车牌号</th>
					<th field="cname" width="100">货名</th>
					<th field="num" width="100">件数</th>
					<th field="weight" width="100">重量</th>
					<th field="yard" width="100">堆场</th>
					<th field="rental" width="100">仓租</th>
					<th field="memo" width="100">备注</th>
					<th data-options="field:'inDate',width:100,formatter:formatDate">入仓日期</th>
					<th data-options="field:'outDate',width:100,formatter:formatDate">出仓日期</th>
				</tr>
			</thead>
		</table>

	</div>
</body>
</html>
