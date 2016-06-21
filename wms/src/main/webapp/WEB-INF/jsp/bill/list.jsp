<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>账单查询</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	function search() {
		$('#dg').datagrid('load', {
			client : $('#q_client').val(),
			sdate : $('#q_sdate').val(),
			edate : $('#q_edate').val()
		});
	}

	//刷新
	function reload() {
		$('#dg').datagrid('reload');
	}

	//DOM加载完毕执行
	$(document).ready(function() {
		var d = new Date();
		$('#q_sdate').datebox('setValue', FormatterDate(d));
		$('#q_edate').datebox('setValue', FormatterDate(d));

		$('#q_client').combogrid({
			panelWidth : 300,
			queryParams : {
				cname : $('#q_client').val()
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

			}
		});
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

	function bill_Details(tag) {
		location.href = '${path}/fees/bills/' + tag + '_details';
	}

	function exportBill(bId) {
		var row = $('#dg').datagrid('getSelected');
		if (row)
			window.open('${path}/fees/bills/report/' + row.id);
	}

	function queryUnCheckedFee() {
		var strTitle = '未出账单费用明细';
		var strUrl = '${path}/fees/bills/uncheck_details'
		_AddTab(strTitle, strUrl);
	}

	function queryCheckedFee() {
		var strTitle = '已出账单费用明细';
		var strUrl = '${path}/fees/bills/check_details'
		_AddTab(strTitle, strUrl);
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<table id="dg" class="easyui-datagrid" fit="true"
			url="${path}/fees/bills" method="GET" toolbar="#toolbar"
			pagination="true" fitColumns="true" singleSelect="true"
			rownumbers="true" striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th field="code" width="100">账单号</th>
					<th data-options="field:'client',width:100,formatter:formatClient">客户</th>
					<!-- 
					<th field="total" width="50">金额</th>
					-->
					<th data-options="field:'sdate',width:50,formatter:formatDate">开始时间</th>
					<th data-options="field:'edate',width:50,formatter:formatDate">结束时间</th>
				</tr>
			</thead>
		</table>

		<!-- 按钮 -->
		<div id="toolbar">
			<input class="easyui-textbox" type="text" id="q_client" /> 从：<input
				id="q_sdate" type="text" class="easyui-datebox" /> 到：<input
				id="q_edate" type="text" class="easyui-datebox" /> <a
				href="javascript:search()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查询</a> <a
				href="javascript:exportBill()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" id="btn_export">导出账单</a> <a
				href="javascript:queryUnCheckedFee()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查未出账单费用</a> <a
				href="javascript:queryCheckedFee()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查已出账单费用</a>
		</div>

	</div>
</body>
</html>
