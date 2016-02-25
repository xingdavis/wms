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
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<!-- 用户信息列表 title="用户管理" -->
		<table id="dg" class="easyui-datagrid" fit="true" url="${path}/fees/datagrid"
			method="GET" toolbar="#toolbar" pagination="true" fitColumns="true"
			singleSelect="true" rownumbers="true" striped="true" border="false"
			nowrap="false">
			<thead>
				<tr>
					<th field="billCode" width="100">单号</th>
					<th data-options="field:'client',width:100,formatter:formatClient">客户</th>
					<th field="ftype" width="100">类型</th>
					<th field="fname" width="100">费用名称</th>
					<th field="price" width="100">单价</th>
					<th field="amount" width="100">金额</th>
					<th field="sdate" width="100">开始</th>
					<th field="edate" width="100">结束</th>
					<th field="flag" width="100">状态</th>
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
			<a href="${path}/fees/page/1" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增仓租</a> <a
				href="${path}/fees/page/2" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增运输费用</a> <a
				href="javascript:del()" class="easyui-linkbutton"
				data-options="iconCls:'icon-remove'">删除</a> <a
				href="javascript:edit()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">编辑</a>
		</div>

	</div>
</body>
</html>
