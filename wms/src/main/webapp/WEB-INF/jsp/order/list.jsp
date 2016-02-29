<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的订单</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	//DOM加载完毕执行
	$(document).ready(function() {
		$('#q_client').combogrid({
			panelWidth : 300,
			queryParams : {
				cname : $('#clientId').val()
			},
			mode : 'remote',
			idField : 'id',
			textField : 'cname',
			method : 'get',
			url : path + '/clients',
			fitColumns : true,
			columns : [ [ {
				field : 'id',
				title : 'id',
				hidden : true
			}, {
				field : 'cname',
				title : '名称',
				width : 200
			} ] ],
			keyHandler : {
				up : function() {
				},
				down : function() {
				},
				enter : function() {
				},
				query : function(q) {
					//动态搜索
					$('#q_client').combogrid("grid").datagrid("reload", {
						'cname' : q
					});
					$('#q_client').combogrid("setValue", q);
				}
			},
			/* onChange:function(newValue,oldValue){
				alert(newValue);
			} */
			onSelect : function(index, row) {
				//$('#e_contact_man').textbox('setValue',row.contactMan);
				//$('#e_contact_tel').textbox('setValue',row.contactTel);
			}
		});
	});
	function search() {
		$('#dg').datagrid('load', {
			clientId : $('#q_client').textbox('getValue'),
			code : $('#q_code').textbox('getValue')
		});
	}

	//刷新
	function reload() {
		$('#dg').datagrid('reload');
	}

	function formatDate(value, row) {
		if (value) {
			return new Date(value).Format("yyyy-MM-dd");
		} else {
			return value;
		}
	}

	function formatStatus(value, row) {
		if (value == '0')
			return '登记';
		else if (value == '1')
			return '审批';
		else if (value == '2')
			return '出仓';
		else if (value == '3')
			return '出账单';
		else
			return value;
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar">
		<input class="easyui-textbox" type="text" id="q_client" />
			<input class="easyui-textbox" type="text" name="code" id="q_code"
				data-options="prompt:'输入客户名称或单号查询'" /> <a href="javascript:search()"
				class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			<a href="${path}/orders/new" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增</a>
		</div>
		<table id="dg" class="easyui-datagrid" fit="true"
			url="${path}/orders" method="GET" toolbar="#toolbar"
			pagination="true" fitColumns="true" singleSelect="true"
			rownumbers="true" striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th field="code" width="100">单号</th>
					<th field="contactMan" width="100">联系人</th>
					<th field="contactTel" width="100">电话</th>
					<th data-options="field:'orderDate',width:80,formatter:formatDate">日期</th>
					<th data-options="field:'flag',width:80,formatter:formatStatus">当前状态</th>
				</tr>
			</thead>
		</table>

	</div>
</body>
</html>
