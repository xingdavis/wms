<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>费用查询</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	function search() {
		$('#dg').datagrid('load', {
			key : $('#q_key').val(),
			sdate : $('#q_sdate').val(),
			edate : $('#q_edate').val()
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
					url : path + '/fees/' + r.id,
					async : false,
					method : 'DELETE',
					contentType : 'application/json',
					dataType : 'json',
					error : function(data) {
						alert("error:" + data.responseText);
					},
					success : function(result) {
						if (result.success) {
							reload();
						} else {
							alert(result.msg);
						}
					}
				});
			}
		}
	}

	function edit() {
		var r = $('#dg').datagrid('getSelected');
		if (r)
			location.href = '${path}/fees/page/' + r.ftype + '/' + r.id;
		_AddTab('费用管理-编辑[' + r.id + ']', '${path}/fees/page/' + r.ftype + '/'
				+ r.id);
	}

	function add(type) {
		var r = $('#dg').datagrid('getSelected');
		var client_id = 0;
		var bill_id = 0;
		if (r) {
			client_id = r.clientId;
			bill_id = r.billId;
		}
		_AddTab('费用管理-新增[' + type + '-' + client_id + '-' + bill_id + ']',
				'${path}/fees/page/' + type + '/' + client_id + '/' + bill_id
						+ '/');
	}

	//DOM加载完毕执行
	$(document).ready(function() {
		var d = new Date();
		$('#q_sdate').datebox('setValue', FormatterDate(d));
		$('#q_edate').datebox('setValue', FormatterDate(d));
	});

	function formatClient(value, row) {
		if (row.client) {
			return row.client.cname;
		} else {
			return value;
		}
	}

	function formatDate(value, row) {
		if (value) {
			return new Date(value).Format("yyyy-MM-dd");
		} else {
			return value;
		}
	}

	function formatType(value, row) {
		if (value) {
			return value == "1" ? "仓租" : "运输费用";
		} else {
			return value;
		}
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<!-- 用户信息列表 title="用户管理" -->
		<table id="dg" class="easyui-datagrid" fit="true"
			url="${path}/fees/datagrid" method="GET" toolbar="#toolbar"
			pagination="true" fitColumns="true" singleSelect="true"
			rownumbers="true" striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th field="billCode" width="100">单号</th>
					<th data-options="field:'client',width:100,formatter:formatClient">客户</th>
					<th data-options="field:'ftype',width:50,formatter:formatType">类型</th>
					<th field="fname" width="50">费用名称</th>
					<th field="price" width="50">单价</th>
					<th field="amount" width="50">金额</th>
					<th data-options="field:'sdate',width:50,formatter:formatDate">开始</th>
					<th data-options="field:'edate',width:50,formatter:formatDate">结束</th>
					<th field="flag" width="50">状态</th>
				</tr>
			</thead>
		</table>

		<!-- 按钮 -->
		<div id="toolbar">
			<input class="easyui-textbox" type="text" id="q_key"
				data-options="prompt:'输入客户名称或单号查询'" /> 从：<input id="q_sdate"
				type="text" class="easyui-datebox" /> 到：<input id="q_edate"
				type="text" class="easyui-datebox" /> <a href="javascript:search()"
				class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			<a href="javascript:add(1)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增仓租</a> <a
				href="javascript:add(2)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增运输费用</a> <a
				href="javascript:del()" class="easyui-linkbutton"
				data-options="iconCls:'icon-remove'">删除</a> <a
				href="javascript:edit()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">编辑</a>
		</div>

	</div>
</body>
</html>
