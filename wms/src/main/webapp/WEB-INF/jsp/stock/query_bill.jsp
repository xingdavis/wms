<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查单</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	//DOM加载完毕执行
	$(document).ready(function() {
		var d = new Date();
		$('#q_sdate').datebox('setValue', FormatterDate(d));
		$('#q_edate').datebox('setValue', FormatterDate(d));
	});

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

	function formatClient(value, row) {
		if (row.client) {
			return row.client.cname;
		} else {
			return value;
		}
	}

	function del() {
		if (window.confirm("确认删除吗？")) {
			var r = $('#dg').datagrid('getSelected');
			if (r) {
				$.ajax({
					url : path + '/stock_ins/' + r.id,
					async : false,
					method : 'DELETE',
					contentType : 'application/json',
					dataType : 'json',
					error : function(data) {
						alert("error:" + data.responseText);
					},
					success : function(result) {
						//var result = eval('(' + result + ')');
						if (result.success) {
							reload();
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
			location.href = '${path}/stock_ins/page/' + r.id;
	}

	function verify(flag) {
		var r = $('#dg').datagrid('getSelected');
		if (r) {
			$.ajax({
				url : path + '/stock_ins/bills/' + flag + '/' + r.id,
				async : false,
				type : 'PUT',
				contentType : 'application/json',
				dataType : 'json',
				error : function(data) {
					alert("error:" + data.responseText);
				},
				success : function(result) {
					//var result = eval('(' + result + ')');
					if (result.success) {
						reload();
					} else {
						alert(result.msg);
					}
				}
			});
		}
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar">
			<input class="easyui-textbox" type="text" id="q_key"
				data-options="prompt:'输入客户名称或订单号查询'" /> 从：<input id="q_sdate"
				type="text" class="easyui-datebox" /> 到：<input id="q_edate"
				type="text" class="easyui-datebox" /> <a href="javascript:search()"
				class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			<a href="${path}/stock_ins/page" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">入仓</a> <a href="javascript:del()"
				class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
			<a href="javascript:edit()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">编辑</a> <a
				href="javascript:verify(1)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">审批</a> <a
				href="javascript:verify(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">反审批</a> <a
				href="javascript:verify(2)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">出仓</a>
		</div>
		<table id="dg" class="easyui-datagrid" fit="true"
			url="${path}/stock_ins/bills" method="GET" toolbar="#toolbar"
			pagination="true" fitColumns="true" singleSelect="true"
			rownumbers="true" striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th field="code" width="100">单号</th>
					<th field="orderCode" width="100">订单号</th>
					<th data-options="field:'client',width:100,formatter:formatClient">客户</th>
					<th field="carNo" width="100">车牌号</th>
					<th field="inDate" width="100">进仓日期</th>
					<th field="outDate" width="100">出仓日期</th>
					<th field="flag" width="100">当前状态</th>
				</tr>
			</thead>
		</table>

	</div>
</body>
</html>
