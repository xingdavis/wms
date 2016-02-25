<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>费用登记</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	function submitForm(flag) {
		if (!$('#fm').form('validate'))
			return;
		var mesTitle = '';
		var method = 'POST';
		var url = path + '/fees';
		if ($('#e_id').val() != '') {
			method = 'PUT';
			url += '/' + $('#e_id').val();
		}

		var bill = $('#fm').serializeObject();

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
				location.href = '${path}/fees/list';
			}
		});
	}

	//页面所有东西加载完执行（包括js、图片）
	$(window).load(function() {

	});

	function getModel(id) {
		$.ajax({
			url : path + '/fees/' + id,
			method : 'GET',
			contentType : 'application/json',
			dataType : 'json',
			error : function(data) {
				alert("error:" + data.responseText);
			},
			success : function(result) {
				//var result = eval('(' + result + ')');
				if (result.success) {
					$('#fm').form('load', result.obj);
				} else {
					alert(result.msg);
				}
			}
		});
	}

	//DOM加载完毕执行
	$(document).ready(function() {
		if ($('#e_id').val() != '') {
			getModel($('#e_id').val());
		} else {
			var d = FormatterDate(new Date());
			$('#e_sdate').datebox('setValue', d);
			$('#e_edate').datebox('setValue', d);
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
				<input type="hidden" name="id" id="e_id" value="${fee_id}" />
				<input type="hidden" name="billCode" id="e_billCode" />
				<table cellpadding="5">
					<tr>
						<td>客户:</td>
						<td><input class="easyui-textbox" type="text" name="clientId"
							id="e_client" data-options="required:true" /></td>
					</tr>
					<tr>
						<td>单号:</td>
						<td><input class="easyui-textbox"
							data-options="required:true" type="text" name="billId"
							id="e_billId" /></td>
					</tr>
					<tr>
						<td>类型:</td>
						<td><select id="e_ftype" name="ftype"><option
									value="1">仓租</option>
								<option value="2">运输费用</option></select></td>
					</tr>
					<tr>
						<td>费目:</td>
						<td><input class="easyui-combobox" id="e_fname" name="fname"
							data-options="valueField: 'oname',textField: 'oname',method : 'GET'"></td>
					</tr>
					<tr>
						<td>单价:</td>
						<td><input id="e_price" name="price" type="text"
							class="easyui-numberbox" value="100"
							data-options="required:true,min:0,precision:1"></td>
					</tr>
					<tr>
						<td>总额:</td>
						<td><input id="e_amount" name="amount" type="text"
							class="easyui-numberbox" value="100"
							data-options="required:true,min:0,precision:1"></td>
					</tr>
					<tr>
						<td>开始日期:</td>
						<td><input name="sdate" id="e_sdate" type="text"
							class="easyui-datebox" required="required" /></td>
					</tr>
					<tr>
						<td>结束日期:</td>
						<td><input name="edate" id="e_edate" type="text"
							class="easyui-datebox" required="required" /></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" id="e_memo"
							name="memo" /></td>
					</tr>
				</table>
			</form>

			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="submitForm(0)">保存</a>
			</div>
		</div>
	</div>

</body>
</html>