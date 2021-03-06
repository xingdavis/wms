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

	function add() {
		_AddTab('[新增提货单]', '${path}/deliverys/page/');
	}

	function edit() {
		var r = $('#dg').datagrid('getSelected');
		showEditPage(r);
	}

	function showEditPage(row) {
		if (row)
			_AddTab('[提货单详情-' + row.id + ']', '${path}/deliverys/page/'
					+ row.id);
	}

	function dgDblClick(index, row) {
		showEditPage(row);
	}

	function addFee(type) {
		var r = $('#dg').datagrid('getSelected');
		var client_id = 0;
		var bill_id = 0;
		if (r) {
			bill_id = r.id;
			if (r.clientId > 0)
				client_id = r.clientId;
		}
		_AddTab('费用管理-新增[' + type + '-' + client_id + '-' + bill_id + ']',
				'${path}/fees/page/' + type + '/' + client_id + '/' + bill_id
						+ '/');
	}

	function editFee() {
		var r = $('#dg').datagrid('getSelected');
		if (r) {
			var clientId = r.clientId;
			var strTitle = '费用管理-[' + r.code + ' | ' + r.id;
			var strUrl = '${path}/fees/list?key=' + r.code + '&ftype=2&billId='
					+ r.id;

			if (r.clientId) {
				strTitle = strTitle + ' | ' + clientId;
				strUrl = strUrl + '&clientId=' + clientId;
			}
			strTitle = strTitle + ']';
			_AddTab(strTitle, strUrl);
		}
	}

	function formatStatus(value, row) {
		if (value == '0')
			return '登记';
		else if (value == '1')
			return '出账';
		else
			return value;
	}

	function exportBill(type) {
		var r = $('#dg').datagrid('getSelected');
		if (r)
			window.open(path + '/deliverys/report/' + type + "/" + r.id);
	}
	function formatClient(value, row) {
		if (row.client) {
			return row.client.cname;
		} else {
			return value;
		}
	}

	function clone() {
		var r = $('#dg').datagrid('getSelected');
		if (r) {
			$.ajax({
				url : '${path}/deliverys/copy/' + r.id,
				async : false,
				type : 'GET',
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
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar">
			<input class="easyui-textbox" type="text" name="code" id="q_code"
				data-options="prompt:'输入订舱号/生产编号/客户名称/入仓单号查询'" /> <a
				href="javascript:search()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查询</a> <a
				href="javascript:add()" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增</a><a href="javascript:del()"
				class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
			<a href="javascript:edit()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">编辑</a> <a
				href="javascript:editFee()" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">单据费用</a><a
				href="javascript:exportBill(1)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">信树派车单</a> <a
				href="javascript:exportBill(2)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">东方派车单</a> <a
				href="javascript:clone()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">复制</a>
		</div>
		<table id="dg" class="easyui-datagrid" fit="true"
			data-options="url:'${path}/deliverys/datagrid',toolbar:'#toolbar',method:'get',fitColumns:true,singleSelect:true,pagination:true,rownumbers:true,onDblClickRow:dgDblClick">
			<thead>
				<tr>
					<th field="id" width="50">流水号</th>
					<th field="ddate" width="100">装货时间</th>
					<th data-options="field:'client',width:100,formatter:formatClient">客户</th>
					<th field="orderCode" width="120">入仓订单号</th>
					<th field="clientCode" width="120">生产编号</th>
					<th field="code" width="100">订舱号</th>
					<th field="dport" width="100">提柜点</th>
					<th field="destination" width="100">目的地</th>
					<th field="rport" width="100">还柜点</th>
					<th field="caseNo" width="100">柜号</th>
					<th field="sealNo" width="100">封号</th>
					<th field="carNo" width="100">车牌号</th>
					<!--
					<th field="driver" width="100">司机</th>
					<th field="consignee" width="100">收货人</th>
					<th field="goodsName" width="100">货名</th>
					<th field="caseModel" width="100">柜型</th>
					 
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
