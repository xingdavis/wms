<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>进仓登记</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	var editIndex = undefined;
	function endEditing() {
		if (editIndex == undefined) {
			return true
		}
		if ($('#dg').datagrid('validateRow', editIndex)) {
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}

	function onClickCell(index, field, value) {
		if (editIndex != index) {
			if (endEditing()) {
				$('#dg').datagrid('selectRow', index).datagrid('beginEdit',
						index);
				var ed = $('#dg').datagrid('getEditor', {
					index : index,
					field : field
				});
				if (ed) {
					($(ed.target).data('textbox') ? $(ed.target).textbox(
							'textbox') : $(ed.target)).focus();
				}
				editIndex = index;
			} else {
				$('#dg').datagrid('selectRow', editIndex);
			}
		}
	}
	function append() {
		if (endEditing()) {
			$('#dg').datagrid('appendRow', {
				id : 0,
				billId:0
			});
			editIndex = $('#dg').datagrid('getRows').length - 1;
			$('#dg').datagrid('selectRow', editIndex).datagrid('beginEdit',
					editIndex);
		}
	}
	function removeit() {
		if (editIndex == undefined) {
			return
			

		}
		$('#dg').datagrid('cancelEdit', editIndex).datagrid('deleteRow',
				editIndex);
		editIndex = undefined;
	}
	function accept() {
		if (endEditing()) {
			$('#dg').datagrid('acceptChanges');
		}
	}
	function reject() {
		$('#dg').datagrid('rejectChanges');
		editIndex = undefined;
	}
	function getChanges() {
		var rows = $('#dg').datagrid('getChanges');
		alert(rows.length + ' rows are changed!');
	}

	function autocode() {
		$('#e_code').textbox('clear');
	}

	function submitForm(flag) {
		if (!$('#fm').form('validate'))
			return;
		var mesTitle = '';
		var method = 'POST';
		var url =path+'/stock_ins/bills';
		if ($('#e_id').val() != '') {
			method = 'PUT';
			url += '/' + $('#e_id').val();			
		}

		var items = $('#dg').datagrid('getData').rows;
		if (items.length == 0) {
			alert('请录入货物明细！')
			return;
		}

		//$('#e_order_details').val(items);
		var bill = $('#fm').serializeObject();
		bill.items = items;
		bill.flag=flag

		var d = JSON.stringify(bill);
		//return;
		$.ajax({
			url : url,
			data : d,
			async : false,
			type : method,
			contentType : 'application/json',
			dataType : 'json',
			error : function(data) {
				alert("error:" + data.responseText);
			},
			success : function(result) {
				//var result = eval('(' + result + ')');
				if (result.success) {
					alert('已保存!');					
				} else {
					alert(result.msg);
				}
				//location.href = '${path}/stock_ins/billpage';
			}
		});
	}

	//页面所有东西加载完执行（包括js、图片）
	$(window).load(function() {

	});

	function getStockIn(id) {
		$.ajax({
			url : path + '/stock_ins/' + id,
			method : 'GET',
			contentType : 'application/json',
			dataType : 'json',
			error : function(data) {
				alert("error:" + data.responseText);
			},
			success : function(result) {
				//var result = eval('(' + result + ')');
				if (result.success) {
					$('#e_client').combogrid("setValue", result.obj.clientId);
					$('#e_orderId').combogrid("setValue", result.obj.orderId);
					$('#e_carNo').textbox("setValue", result.obj.carNo);
					$('#e_inDate').datebox('setValue',
							FormatterDate(new Date(result.obj.inDate)));
					$('#dg').datagrid('loadData', result.obj.items);
				} else {
					alert(result.msg);
				}
			}
		});
	}

	//DOM加载完毕执行
	$(document).ready(function() {
		if ($('#e_id').val() != '') {
			getStockIn($('#e_id').val());
			$('#btn_save_and_verify').linkbutton('disable');
		} else {
			$('#e_inDate').datebox('setValue', FormatterDate(new Date()));
		}

		$('#e_client').combogrid({
			panelWidth : 300,
			queryParams : {
				cname : $('#e_client').val()
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
					$('#e_client').combogrid("grid").datagrid("reload", {
						'cname' : q
					});
					$('#e_client').combogrid("setValue", q);
				}
			},
			/* onChange:function(newValue,oldValue){
				alert(newValue);
			} */
			onSelect : function(index, row) {
				$('#e_orderId').combogrid("grid").datagrid("reload", {
					'clientId' : row.id
				});
			}
		});

		$('#e_orderId').combogrid({
			panelWidth : 300,
			queryParams : {
				code : $('#e_orderId').val()
			},
			mode : 'remote',
			idField : 'id',
			textField : 'code',
			method : 'get',
			url : path + '/orders',
			fitColumns : true,
			columns : [ [ {
				field : 'id',
				title : 'id',
				hidden : true
			}, {
				field : 'code',
				title : '单号',
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
					$('#e_orderId').combogrid("grid").datagrid("reload", {
						'code' : q
					});
					$('#e_orderId').combogrid("setValue", q);
				}
			},
			/* onChange:function(newValue,oldValue){
				alert(newValue);
			} */
			onSelect : function(index, row) {
				$('#e_orderCode').val(row.code);
				getOrder(row.id);
			}
		});
	});

	function getOrder(id) {
		$.ajax({
			type : 'GET',
			url : path + '/orders/' + id,
			async : false,
			contentType : 'application/json',
			dataType : 'json',
			error : function(data) {
				alert("error:" + data.responseText);
			},
			success : function(result) {
				//var result = eval('(' + result + ')');
				if (result.success) {
					//$('#dg').datagrid('loadData', result.obj.order_details);
					for (var i = 0; i < result.obj.order_details.length; i++) {
						var obj = result.obj.order_details[i];
						$('#dg').datagrid('appendRow', {
							cname : obj.cname,
							num : obj.num,
							vol : obj.vol,
							weight : obj.weight,
							yard : ''
						});

					}
				} else {
					alert('获取订单失败！');
				}

			}
		});
	}
</script>
</head>
<body>
	<div class="easyui-panel" title="New Topic" style="width: auto">
		<div style="padding: 10px 60px 20px 60px">
			<form id="fm" method="post">
				<input type="hidden" name="id" id="e_id" value="${stock_in_id}" />
				<input type="hidden" name="crDate" /> <input type="hidden"
					name="flag" /> <input type="hidden" name="orderCode"
					id="e_orderCode" />
				<table cellpadding="5">
					<tr>
						<td>公司:</td>
						<td><input class="easyui-textbox" type="text" name="clientId"
							id="e_client" data-options="required:true" /></td>
					</tr>
					<tr>
						<td>订单号:</td>
						<td><input class="easyui-textbox"
							data-options="required:true" type="text" name="orderId"
							id="e_orderId" /></td>
					</tr>
					<tr>
						<td>车牌号:</td>
						<td><input class="easyui-textbox" type="text" id="e_carNo"
							name="carNo" /></td>
					</tr>
					<tr>
						<td>进仓日期:</td>
						<td><input name="inDate" id="e_inDate" type="text"
							class="easyui-datebox" required="required" /></td>
					</tr>
				</table>

				<table id="dg" class="easyui-datagrid" title="货物清单"
					style="width: 700px; height: auto"
					data-options="
                iconCls: 'icon-edit',
                singleSelect: true,
                toolbar: '#tb',
                method: 'get',onClickCell: onClickCell">
					<thead>
						<tr>
							<!-- <th data-options="field:'id',width:0,hidden:true">Item ID</th>							
								 -->
							<th
								data-options="field:'cname',width:100,editor:{type:'validatebox',options:{required:true}}">货名</th>
							<th
								data-options="field:'num',width:80,align:'right',editor:{type:'numberbox',options:{precision:0}}">数量</th>
							<th
								data-options="field:'vol',width:80,align:'right',editor:{type:'numberbox',options:{precision:2}}">体积</th>
							<th
								data-options="field:'weight',width:80,editor:{type:'numberbox',options:{precision:2}}">重量</th>
							<th data-options="field:'yard',width:100,editor:'text'">堆位</th>
							<th data-options="field:'memo',width:100,editor:'text'">备注</th>
						</tr>
					</thead>
				</table>

				<div id="tb" style="height: auto">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-add',plain:true" onclick="append()">增加</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true"
						onclick="removeit()">删除</a> <a href="javascript:void(0)"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-save',plain:true" onclick="accept()">完成</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-undo',plain:true" onclick="reject()">撤销</a>
				</div>

			</form>

			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="submitForm(0)">保存</a> <a href="javascript:void(0)"
					class="easyui-linkbutton" onclick="submitForm(1)"
					id="btn_save_and_verify">保存并审批</a>
			</div>
		</div>
	</div>


</body>
</html>