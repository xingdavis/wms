<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>提货单</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	function submitForm(flag) {
		if (!$('#fm').form('validate'))
			return;
		var mesTitle = '';
		var method = 'POST';
		var url = path + '/deliverys';
		if ($('#e_id').val() != '') {
			method = 'PUT';
			url += '/' + $('#e_id').val();
		}

		//$('#e_order_details').val(items);
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
					//location.href = '${path}/deliverys/list';
				} else {
					alert(result.msg);
				}
			}
		});
	}

	//页面所有东西加载完执行（包括js、图片）
	$(window).load(function() {

	});

	//DOM加载完毕执行
	$(document).ready(
			function() {
				initClientData();
				initOrderData();
				if ($('#e_id').val() != '') {
					getModel($('#e_id').val());
				} else {
					$('#e_ddate').datetimebox('setValue',
							new Date().Format('yyyy-MM-dd hh:mm:ss'));
				}

				getOptionList('柜型', [ '#e_caseModel' ]);
				//ports.push($('#e_caseModel'));
				getOptionList('码头', [ '#e_dport', '#e_rport' ]);
				//getOptionList('港口', [ '#e_start_port', '#e_end_port' ]);
			});

	function initOrderData() {
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
			}
		});
	}

	function initClientData() {
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
					'clienId' : row.id
				});
			}
		});
	}

	function getOptionList(flag, els) {
		$.ajax({
			type : 'GET',
			url : path + '/options/list?otype=' + encodeURIComponent(flag),
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
					for (i = 0; i < els.length; i++)
						$(els[i]).combobox('loadData', result.obj);
				} else {
					alert('获取' + flag + '数据失败！');
				}

			}
		});
	}

	function getModel(id) {
		$.ajax({
			url : path + '/deliverys/' + id,
			method : 'GET',
			contentType : 'application/json',
			dataType : 'json',
			error : function(data) {
				alert("error:" + data.responseText);
			},
			success : function(result) {
				//var result = eval('(' + result + ')');
				if (result.success) {
					//$('#e_orderId').combogrid("setValue", result.obj.orderId);
					//$('#e_inDate').datebox('setValue',FormatterDate(new Date(result.obj.inDate)));

					$('#fm').form('load', result.obj);
				} else {
					alert(result.msg);
				}
			}
		});
	}

	function goBack() {
		location.href = '${path}/deliverys/list';
	}
	function calWeight() {
		var netWeight = $('#e_totalWeight').numberbox('getValue')
				- $('#e_caseWeight').numberbox('getValue')
				- $('#e_carWeight').numberbox('getValue');
		$('#e_netWeight').numberbox('setValue', netWeight);
	}
</script>
</head>
<body>
	<div class="easyui-panel" title="New Topic" style="width: auto">
		<div style="padding: 10px 60px 20px 60px">
			<form id="fm" method="post">
				<input type="hidden" name="id" id="e_id" value="${delivery_id}" />
				<input type="hidden" name="orderCode" id="e_orderCode" />
				<table cellpadding="5">
					<tr>
						<td>订舱号:</td>
						<td><input class="easyui-textbox" type="text"
							data-options="required:true" id="e_code" name="code" /></td>
					</tr>
					<tr>
						<td>客户:</td>
						<td><input class="easyui-textbox" type="text" name="clientId"
							id="e_client" /></td>
					</tr>
					<tr>
						<td>入仓订单号:</td>
						<td><input class="easyui-textbox" type="text" name="orderId"
							id="e_orderId" /></td>
					</tr>
					<tr>
						<td>生产编号(东方):</td>
						<td><input class="easyui-textbox" type="text"
							id="e_clientCode" name="clientCode" /></td>
					</tr>
					<tr>
						<td>车牌号:</td>
						<td><input class="easyui-textbox" type="text" id="e_carNo"
							name="carNo" /></td>
					</tr>
					<tr>
						<td>司机:</td>
						<td><input class="easyui-textbox" type="text" id="e_driver"
							name="driver" /></td>
					</tr>
					<tr>
						<td>司机电话:</td>
						<td><input class="easyui-textbox" type="text"
							id="e_driverPhone" name="driverPhone" /></td>
					</tr>
					<tr>
						<td>货名:</td>
						<td><input class="easyui-textbox" type="text"
							id="e_goodsName" name="goodsName" /></td>
					</tr>
					<tr>
						<td>柜型:</td>
						<td><input class="easyui-combobox" id="e_caseModel"
							name="caseModel"
							data-options="valueField: 'oname',textField: 'oname',method : 'GET'"></td>
					</tr>
					<tr>
						<td>箱号:</td>
						<td><input class="easyui-textbox" type="text" id="e_caseNo"
							name="caseNo" /></td>
					</tr>
					<tr>
						<td>封号:</td>
						<td><input class="easyui-textbox" type="text" id="e_sealNo"
							name="sealNo" /></td>
					</tr>
					<tr>
						<td>提柜点:</td>
						<td><input class="easyui-combobox" id="e_dport" name="dport"
							data-options="valueField: 'oname',textField: 'oname',method : 'GET'" /></td>
					</tr>
					<tr>
						<td>目的地:</td>
						<td><input class="easyui-textbox" type="text"
							id="e_destination" name="destination" /></td>
					</tr>
					<tr>
						<td>还柜点:</td>
						<td><input class="easyui-combobox" id="e_rport" name="rport"
							data-options="valueField: 'oname',textField: 'oname',method : 'GET'" /></td>
					</tr>
					<!-- 
					<tr>
						<td>起运港:</td>
						<td><input class="easyui-combobox" id="e_start_port"
							name="startPort"
							data-options="valueField: 'oname',textField: 'oname',method : 'GET'" /></td>
					</tr>
					<tr>
						<td>目的港:</td>
						<td><input class="easyui-combobox" id="e_end_port"
							name="endPort"
							data-options="valueField: 'oname',textField: 'oname',method : 'GET'" /></td>
					</tr>
					 -->
					<tr>
						<td>是否过磅:</td>
						<td><input class="easyui-textbox" type="text" id="e_weigh"
							name="weigh" /></td>
					</tr>
					<tr>
						<td>联系人:</td>
						<td><input class="easyui-textbox" type="text"
							id="e_consignee" name="consignee" /></td>
					</tr>
					<tr>
						<td>装货日期(到厂时间):</td>
						<td><input name="ddate" id="e_ddate" type="text"
							class="easyui-datetimebox"
							data-options="required:true,showSeconds:false" /></td>
					</tr>
					<tr>
						<td>装货地址:</td>
						<td><input class="easyui-textbox" type="text" id="e_address"
							name="address" data-options="width:500" /></td>
					</tr>
					<tr>
						<td>联系方式:</td>
						<td><input class="easyui-textbox" type="text" id="e_contact"
							name="contact" data-options="width:500" /></td>
					</tr>
					<tr>
						<td>吉柜重量:</td>
						<td><input id="e_caseWeight" name="caseWeight" type="text"
							class="easyui-numberbox" value="0"
							data-options="min:0,precision:1,onChange:function(newValue,oldValue){calWeight();}"></td>
					</tr>
					<tr>
						<td>车自重:</td>
						<td><input id="e_carWeight" name="carWeight" type="text"
							class="easyui-numberbox" value="0"
							data-options="min:0,precision:1,onChange:function(newValue,oldValue){calWeight();}"></td>
					</tr>
					<tr>
						<td>货重:</td>
						<td><input id="e_netWeight" name="netWeight" type="text"
							class="easyui-numberbox" value="0"
							data-options="min:0,precision:1,onChange:function(newValue,oldValue){calWeight();}"></td>
					</tr>
					<tr>
						<td>过磅总重量:</td>
						<td><input id="e_totalWeight" name="totalWeight" type="text"
							class="easyui-numberbox" value="0"
							data-options="min:0,precision:1,onChange:function(newValue,oldValue){calWeight();}"></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" id="e_memo"
							name="memo" data-options="width:500" /></td>
					</tr>
					<tr>
						<td>注意事项:</td>
						<td><input class="easyui-textbox" type="text"
							id="e_attention" name="attention" data-options="width:500" /></td>
					</tr>
				</table>
			</form>

			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="submitForm(0)">保存</a> <a href="javascript:goBack()"
					class="easyui-linkbutton">返回</a>
			</div>
		</div>
	</div>


</body>
</html>