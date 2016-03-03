<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查询账单费用明细</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	function search() {
		$('#dg').datagrid('load', {
			client : $('#q_client').combogrid('getValue'),
			key : $('#q_key').val(),
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

		var tag = $('#bill_tag').val();
		if (tag == 'check') {
			$('#btn_burn').linkbutton('disable');
			//$('#btn_export').linkbutton('disable');
		}
	});

	function formatClient(value, row) {
		if (row.client) {
			return row.client.cname;
		} else {
			return value;
		}
	}

	function formatBill(value, row) {
		if (row.delivery) {
			return row.delivery.code;
		} else if (row.stock_in) {
			return row.stock_in.code;
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

	function burn() {
		if (!$('#fm').form('validate'))
			return;
		var rows = $('#dg').datagrid('getChecked'); // Return all rows where the checkbox has been checked
		if (rows) {
			var ids = new Array();
			if (rows.length > 0) {
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				bPost(ids);
			} else
				$.messager.alert('提示', '请选择要导出的账单！', 'error');
		}
	}

	function exportBill() {
		window.open(path + '/fees/bills/report');
	}

	function bPost(ids) {
		$.ajax({
			url : path + '/fees/bill',
			data : {
				ids : ids.join(','),
				client : $('#q_client').combogrid('getValue'),
				sdate : $('#q_sdate').datebox('getValue'),
				edate : $('#q_edate').datebox('getValue')
			},
			async : false,
			type : 'GET',
			contentType : 'application/json',
			dataType : 'json',
			error : function(data) {
				alert("error:" + data.responseText);
			},
			success : function(result) {
				if (result.success) {
					alert('生成完毕！');
					reload();
				} else {
					alert(result.msg);
				}

			}
		});
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<table id="dg" class="easyui-datagrid" fit="true"
			url="${path}/fees/bills/delivery?fflag=${f_flag}&bflag=${delivery_flag}"
			method="GET" toolbar="#toolbar" pagination="false" fitColumns="true"
			singleSelect="true" checkOnSelect="false" selectOnCheck="false"
			rownumbers="true" striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'id',hidden:true"></th>
					<th data-options="field:'client',width:100,formatter:formatClient">客户</th>
					<th data-options="field:'delivery',width:100,formatter:formatBill">单号</th>
					<!-- 
					<th
						data-options="field:'delivery',width:50,formatter:formatCaseModel">柜型</th>
					<th
						data-options="field:'delivery',width:50,formatter:formatAddress">提柜点</th>
					<th
						data-options="field:'delivery',width:50,formatter:formatEndPort">目的地</th>
					<th data-options="field:'delivery',width:50,formatter:formatRport">还柜点</th>
					<th data-options="field:'delivery',width:50,formatter:formatCaseNo">柜号</th>
					 -->
					<th field="fname" width="50">费目</th>
					<th field="amount" width="50">金额</th>
				</tr>
			</thead>
		</table>

		<!-- 按钮 -->
		<div id="toolbar">
			<form id="fm">
				<input type="hidden" id="bill_tag" value="${tag}" /> <input
					type="hidden" id="delivery_flag" value="${delivery_flag}" /><input
					type="hidden" id="stock_in_flag" value="${stock_in_flag}" /> <input
					class="easyui-textbox" type="text" id="q_client"
					data-options="required:true" />
				<!-- 
				<input
				class="easyui-textbox" type="text" id="q_key"
				data-options="prompt:'输入客户名称或订单号查询'" />
				 -->
				从：<input id="q_sdate" type="text" class="easyui-datebox" /> 到：<input
					id="q_edate" type="text" class="easyui-datebox" /> <a
					href="javascript:search()" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'">查询</a> <a
					href="javascript:burn()" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" id="btn_burn">出账单</a> <a
					href="javascript:exportBill()" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" id="btn_export">导出账单</a>
			</form>
		</div>

	</div>
</body>
</html>
