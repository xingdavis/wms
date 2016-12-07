<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的订单</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>
<script type="text/javascript"
	src="${path}/js/easyui/datagrid-detailview.js"></script>
<script type="text/javascript">
	//DOM加载完毕执行
	$(document).ready(function() {
		$('#q_client').combogrid({
			panelWidth : 300,
			queryParams : {
				cname : 'please input client name'
			},
			mode : 'remote',
			idField : 'id',
			textField : 'cname',
			method : 'get',
			url : path + '/clients/order_client',
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
						'cname' : $.trim(q)
					});
					$('#q_client').combogrid("setValue", $.trim(q));
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
		if (!$('#fm').form('validate'))
			return;
		$('#dg').datagrid('load', {
			clientId : $('#q_client').textbox('getValue'),
			code : $.trim($('#q_code').textbox('getValue'))
		});
	}

	//刷新
	function reload() {
		$('#dg').datagrid('reload');
	}

	//刷新
	function exportOrder() {
		var row = $('#dg').datagrid('getSelected');
		if (row)
			window.open(path + '/orders/report/' + row.id);
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

	function detailFormatter(rowIndex, rowData) {
		var html = '<div class="ddv" style="padding:5px 0"><table border="0" cellspacing="0" cellpadding="20" style="width:100%;">';
		if (rowData.order_details) {
			for (var i = 0; i < rowData.order_details.length; i++) {
				html += '<tr style="height: 34px;"><td>货名：'
						+ rowData.order_details[i].cname + '</td><td>件数：'
						+ rowData.order_details[i].num + '</td><td>体积：'
						+ rowData.order_details[i].vol + '</td><td>重量：'
						+ rowData.order_details[i].weight + '</td></tr>';
			}
		}
		html += '</table></div>';
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
	
	function dgDblClick(index, row){
		if (row)
			window.open('${path}/stock_ins/billpage/' + row.id + '?flag=client');
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar">
			<form id="fm" method="post">
				<input class="easyui-textbox" type="text" name="clientId"
					id="q_client" data-options="prompt:'输入客户名称查询'" /> <input
					class="easyui-textbox" type="text" name="code" id="q_code"
					data-options="prompt:'输入单号查询',required:true" /> <a
					href="javascript:search()" class="easyui-linkbutton"
					data-options="iconCls:'icon-search',required:true">查询</a> <a
					href="${path}/orders/new" class="easyui-linkbutton"
					data-options="iconCls:'icon-add'">录入入仓订单</a> <a
					href="javascript:exportOrder()" class="easyui-linkbutton"
					data-options="iconCls:'icon-add'">打印路线图</a>
			</form>
		</div>
		<table id="dg" class="easyui-datagrid" fit="true" toolbar="#toolbar"
			border="false" nowrap="false"
			data-options="view:detailview,rownumbers:true,singleSelect:true,
                url:'${path}/orders/my',onLoadSuccess:onLoadDataSuccess,onExpandRow:onExpandRow,
                autoRowHeight:false,method:'GET',pagination:true,fitColumns:true,detailFormatter:detailFormatter,onDblClickRow:dgDblClick">
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
