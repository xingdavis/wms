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
		var url = path + '/stock_ins/bills';
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
		bill.flag = flag

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
				location.href = '${path}/stock_ins/billpage'
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
					$('#e_client').combogrid("setValue", result.obj.id);
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
			//getStockIn($('#e_id').val());
			//$('#btn_save_and_verify').linkbutton('disable');
		} else {
			//$('#e_inDate').datebox('setValue', FormatterDate(new Date()));
		}
		
		$('#e_ddate').datetimebox('setValue', new Date().Format('yyyy-MM-dd hh:mm:ss'));
		
		getOptionList('柜型', [ '#e_caseModel' ]);
		//ports.push($('#e_caseModel'));
		getOptionList('码头', [ '#e_dport', '#e_rport' ]);
		getOptionList('港口', [ '#e_start_port', '#e_end_port' ]);
		
		$('#e_orderCode').combogrid({
			panelWidth : 300,
			queryParams : {
				code : $('#e_orderCode').val()
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
					$('#e_orderCode').combogrid("grid").datagrid("reload", {
						'code' : q
					});
					$('#e_orderCode').combogrid("setValue", q);
				}
			},
			/* onChange:function(newValue,oldValue){
				alert(newValue);
			} */
			onSelect : function(index, row) {
				//$('#e_orderCode').val(row.code);
				//getOrder(row.id);
			}
		});
	});

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
</script>
</head>
<body>
	<div class="easyui-panel" title="New Topic" style="width: auto">
		<div style="padding: 10px 60px 20px 60px">
			<form id="fm" method="post">
				<input type="hidden" name="id" id="e_id" value="${delivery_id}" />
				<table cellpadding="5">
					<tr>
						<td>订舱号:</td>
						<td><input class="easyui-textbox" type="text"
							data-options="required:true" id="e_code" name="code" /></td>
					</tr>
					<tr>
						<td>订单号:</td>
						<td><input class="easyui-textbox" type="text" name="orderCode"
							id="e_orderCode" /></td>
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
							data-options="valueField: 'id',textField: 'oname',method : 'GET'"></td>
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
							data-options="valueField: 'id',textField: 'oname',method : 'GET'" /></td>
					</tr>
					<tr>
						<td>还柜点:</td>
						<td><input class="easyui-combobox" id="e_rport" name="rport"
							data-options="valueField: 'id',textField: 'oname',method : 'GET'" /></td>
					</tr>
					<tr>
						<td>起运港:</td>
						<td><input class="easyui-combobox" id="e_start_port"
							name="startPort"
							data-options="valueField: 'id',textField: 'oname',method : 'GET'" /></td>
					</tr>
					<tr>
						<td>目的港:</td>
						<td><input class="easyui-combobox" id="e_end_port"
							name="endPort"
							data-options="valueField: 'id',textField: 'oname',method : 'GET'" /></td>
					</tr>
					<tr>
						<td>是否过磅:</td>
						<td><input class="easyui-textbox" type="text" id="e_weigh"
							name="weigh" /></td>
					</tr>
					<tr>
						<td>收货人:</td>
						<td><input class="easyui-textbox" type="text"
							id="e_consignee" name="consignee" /></td>
					</tr>
					<tr>
						<td>装货日期(到厂时间):</td>
						<td><input name="ddate" id="e_ddate" type="text"
							class="easyui-datetimebox" data-options="required:true,showSeconds:false" /></td>
					</tr>
					<tr>
						<td>装货地址:</td>
						<td><input class="easyui-textbox" type="text" id="e_address"
							name="address" data-options="width:500" /></td>
					</tr>
					<tr>
						<td>联系方式:</td>
						<td><input class="easyui-textbox" type="text" id="e_contact"
							name="e_contact" data-options="width:500" /></td>
					</tr>
					<!-- 
					<tr>
						<td>到厂时间:</td>
						<td><input name="arrivalTime" id="e_arrivalTime" type="text"
							class="easyui-datebox" /></td>
					</tr>
					 -->
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
					onclick="submitForm(0)">保存</a> <a href="javascript:void(0)"
					class="easyui-linkbutton" onclick="submitForm(1)"
					id="btn_save_and_verify">保存并审批</a>
			</div>
		</div>
	</div>


</body>
</html>