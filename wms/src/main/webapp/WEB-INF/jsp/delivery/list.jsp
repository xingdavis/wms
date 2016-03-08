<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>提货单列表</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	//DOM加载完毕执行
	$(document).ready(function() {

	});
	function search() {
		$('#dg').datagrid('load', {
			code : $('#q_code').val()
		});
	}

	//刷新
	function reload() {
		$('#dg').datagrid('reload');
	}

	function del() {
		if (window.confirm("确认删除吗？")) {
			var r = $('#dg').datagrid('getSelected');
			if (r) {
				$.ajax({
					url : path + '/deliverys/' + r.id,
					async : false,
					method : 'DELETE',
					contentType : 'application/json',
					dataType : 'json',
					error : function(data) {
						alert("error:" + data.responseText);
					},
					success : function(result) {
						if (result.success) {
							location.href = '${path}/deliverys/list';
						} else {
							alert('删除失败!');
						}
					}
				});
			}
		}
	}

	function edit() {
		var r = $('#dg').datagrid('getSelected');
		if (r)
			location.href = '${path}/deliverys/page/' + r.id;
	}

	function addFee(type) {
		var r = $('#dg').datagrid('getSelected');
		var client_id = 0;
		var bill_id = 0;
		if (r) {
			bill_id = r.id;
			if (r.order)
				client_id=r.order.clientId;
		}
		_AddTab('费用管理-新增[' + type + '-' + client_id + '-' + bill_id + ']',
				'${path}/fees/page/' + type + '/' + client_id + '/' + bill_id
						+ '/');
	}
	
	function formatStatus(value, row) {
		if (value == '0')
			return '登记';
		else if (value == '1')
			return '出账';
		else
			return value;
	}
	
	function exportBill() {
		var r = $('#dg').datagrid('getSelected');
		if (r)
			window.open(path + '/deliverys/report/' + r.id);
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar">
			<input class="easyui-textbox" type="text" name="code" id="q_code"
				data-options="prompt:'输入订舱号查询'" /> <a href="javascript:search()"
				class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			<a href="${path}/deliverys/page" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增</a><a href="javascript:del()"
				class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
			<a href="javascript:edit()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">编辑</a> <a
				href="javascript:addFee(2)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增费用</a><a
				href="javascript:exportBill()" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">打印派车单</a>
		</div>
		<table id="dg" class="easyui-datagrid" fit="true"
			url="${path}/deliverys/datagrid" method="GET" toolbar="#toolbar"
			pagination="true" fitColumns="true" singleSelect="true"
			rownumbers="true" striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th field="code" width="100">订舱号</th>
					<th field="ddate" width="100">装货时间</th>
					<th field="orderCode" width="120">入仓订单号</th>
					<th field="carNo" width="100">车牌号</th>
					<th field="driver" width="100">司机</th>
					<th field="dport" width="100">提柜点</th>
					<th field="rport" width="100">还柜点</th>
					<th field="consignee" width="100">收货人</th>
					<th field="goodsName" width="100">货名</th>
					<th field="caseModel" width="100">柜型</th>
					<th field="caseNo" width="100">柜号</th>
					<th field="sealNo" width="100">封号</th>
					<!-- 
					<th field="startPort" width="100">起运港</th>
					<th field="endPort" width="100">目的港</th>
					 -->
					<th data-options="field:'flag',width:100,formatter:formatStatus">当前状态</th>
				</tr>
			</thead>
		</table>

	</div>
</body>
</html>
