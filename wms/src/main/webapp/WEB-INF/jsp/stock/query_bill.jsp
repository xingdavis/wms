<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查入仓单</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>
<script type="text/javascript"
	src="${path}/js/easyui/datagrid-detailview.js"></script>
<style type="text/css">
	.dv-table td{
		border:5px;
	}
	.dv-label{
		font-weight:bold;
		color:#15428B;
		width:60px;
	}
</style>
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
							alert(result.msg);
						}
					}
				});
			}
		}
	}

	function add() {
		_AddTab('[新增入仓单]', '${path}/stock_ins/page/');
	}

	function edit() {
		var r = $('#dg').datagrid('getSelected');
		showEditPage(r);
	}

	function showEditPage(row) {
		if (row)
			_AddTab('[编辑入仓单-' + row.id + ']', '${path}/stock_ins/page/'
					+ row.id);
	}
	
	function showViewPage(row) {
		if (row)
			_AddTab('[入仓单详情-' + row.id + ']', '${path}/stock_ins/detailpage/'
					+ row.id);
	}

	function dgDblClick(index, row) {
		showViewPage(row);
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

	function formatStatus(value, row) {
		if (value == '0')
			return '登记';
		else if (value == '1')
			return '审批';
		else if (value == '2')
			return '出仓';
		else if (value == '3')
			return '出账';
		else
			return value;
	}

	function exportBill() {
		var row = $('#dg').datagrid('getSelected');
		if (row)
			window.open(path + '/stock_ins/report/' + row.id);
	}

	function formatDate(value, row) {
		if (value) {
			return new Date(value).Format("yyyy-MM-dd");
		} else {
			return value;
		}
	}

	function editFee() {
		var r = $('#dg').datagrid('getSelected');
		if (r) {
			var strTitle = '费用管理-[' + r.code + ' | ' + r.id + ' | '
					+ r.clientId + ']';
			var strUrl = '${path}/fees/list?key=' + r.code + '&ftype=1&billId='
					+ r.id + '&clientId=' + r.clientId;
			_AddTab(strTitle, strUrl);
		}
	}
	
	function detailFormatter(rowIndex, rowData) {
		var html = '<table class="dv-table" border="0" style="width:100%;">';
		if (rowData.items) {
			for (var i = 0; i < rowData.items.length; i++) {
				html += '<tr><td class="dv-label">货名:</td><td>'
						+ rowData.items[i].cname + '</td><td class="dv-label">数量:</td><td>'
						+ rowData.items[i].num + '</td><td class="dv-label">体积: </td><td>'
						+ rowData.items[i].vol + '</td><td class="dv-label">重量:</td><td>'
						+ rowData.items[i].weight + '</td><td class="dv-label">堆位: </td><td>'
						+ rowData.items[i].yard + '</td><td class="dv-label">备注:</td><td>'
						+ rowData.items[i].memo + '</td></tr>';
			}
		}
		html += '</table>';
		return html;
	}

	function onExpandRow(index, row) {
		var ddv = $(this).datagrid('getRowDetail', index).find('div.ddv');
		ddv.panel({
			height : 80,
			border : false,
			cache : false,
			//href:'datagrid21_getdetail.php?itemid='+row.itemid,
			onLoad : function() {
				$('#dg').datagrid('fixDetailRowHeight', index);
			}
		});
		$('#dg').datagrid('fixDetailRowHeight', index);
	}

	function onLoadDataSuccess(data) {
		var me = this;
		setTimeout(function() {
			$(me).parent().find('span.datagrid-row-expander').trigger('click');
		}, 10);
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
			<a href="javascript:add()" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">入仓</a> <a href="javascript:del()"
				class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
			<a href="javascript:edit()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">编辑</a> <a
				href="javascript:verify(1)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">审批</a> <a
				href="javascript:verify(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">反审批</a> <a
				href="javascript:verify(2)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">出仓</a> <a
				href="javascript:editFee()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">单据费用</a> <a
				href="javascript:exportBill()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">打印单据</a>
		</div>
		<table id="dg" class="easyui-datagrid" fit="true"
			data-options="view:detailview,onLoadSuccess:onLoadDataSuccess,onExpandRow:onExpandRow,detailFormatter:detailFormatter,url:'${path}/stock_ins/bills/${order_id}',toolbar:'#toolbar',method:'get',fitColumns:true,singleSelect:true,pagination:false,rownumbers:true,onDblClickRow:dgDblClick">
			<thead>
				<tr>
					<th field="id" width="50">流水号</th>
					<th field="orderCode" width="100">订单号</th>
					<th data-options="field:'client',width:100,formatter:formatClient">客户</th>
					<th field="carNo" width="100">车牌号</th>
					<th data-options="field:'inDate',width:100,formatter:formatDate">进仓日期</th>
					<th data-options="field:'outDate',width:100,formatter:formatDate">出仓日期</th>
					<th field="rental" width="100">仓租</th>
					<th data-options="field:'flag',width:100,formatter:formatStatus">当前状态</th>
					<th field="sourceId" width="50">复制单</th>
				</tr>
			</thead>
		</table>

	</div>
</body>
</html>
