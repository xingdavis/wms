<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>仓储单汇总</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	//DOM加载完毕执行
	$(document).ready(function() {
	});

	function search() {
		$('#dg').datagrid('load', {
			key : $('#q_key').val()
		});
	}

	//刷新
	function reload() {
		$('#dg').datagrid('reload');
	}

	function dgDblClick(index, row) {
		if (row) {
			_AddTab('[订单-'+ row.orderId + '的入仓记录]',
					'${path}/stock_ins/billpage/' + row.orderId + '/');
		}
	}

	function edit() {
		var r = $('#dg').datagrid('getSelected');
		if (r)
			location.href = '${path}/stock_ins/page/' + r.id;
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar">
			<input class="easyui-textbox" type="text" id="q_key"
				data-options="prompt:'输入客户名称或订单号查询'" /> <a
				href="javascript:search()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查询</a>
		</div>
		<table id="dg" class="easyui-datagrid" fit="true"
			data-options="url:'${path}/stock_ins/sumstock',toolbar:'#toolbar',method:'get',fitColumns:true,singleSelect:true,pagination:true,rownumbers:true,onDblClickRow:dgDblClick">
			<thead>
				<tr>
					<!--
					<th field="orderId" width="100">订单Id</th>
					-->
					<th field="clientCode" width="50">客户编号</th>
					<th field="clientName" width="150">客户名称</th>
					<th field="orderCode" width="100">订单号</th>
					<th field="cargoName" width="100">总货名</th>
					<th field="totalNum" width="100">总件数</th>
					<th field="totalVol" width="100">总方数</th>
					<th field="income" width="100">应收</th>
					<th field="pay" width="100">应付</th>
				</tr>
			</thead>
		</table>

	</div>
</body>
</html>
