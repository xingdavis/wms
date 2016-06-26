<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>费用查询</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>
<script type="text/javascript"
	src="${path}/js/easyui/datagrid-groupview.js"></script>
<script type="text/javascript">
	function search() {
		$('#dg').datagrid('load', {
			key : $('#q_key').val(),
			billId : $('#q_billId').val(),
			ftype : $('#q_ftype').val(),
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
		$("#tr_delivery").css("display", "none");
		getOptionList('费目', [ '#e_fname' ]);

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
			onSelect : function(index, row) {
				if ($('#q_ftype').val() == "1")
					$('#e_billId1').combogrid("grid").datagrid("reload", {
						'key' : row.cname
					});
				else
					$('#e_billId2').combogrid("grid").datagrid("reload", {
						'key' : row.cname
					});
			}
		});

		var client_id = $('#q_clientId').val();
		var bill_id = $('#q_billId').val();
		if ($('#e_id').val() == '' & client_id != '' & client_id != '0')
			$('#e_client').combogrid("setValue", client_id);
		if (bill_id == '0') {
			bill_id = '';
		}

		initBill('e_billId1', path + '/stock_ins/bills/', 'code', {
			code : $('#e_billId').val()
		}, [ [ {
			field : 'id',
			title : 'id',
			hidden : true
		}, {
			field : 'orderCode',
			title : '订单号',
			width : 200
		}, {
			field : 'code',
			title : '单号',
			width : 200
		} ] ], bill_id);

		initBill('e_billId2', path + '/deliverys/datagrid/', 'code', {
			code : $('#e_billId').val()
		}, [ [ {
			field : 'id',
			title : 'id',
			hidden : true
		}, {
			field : 'code',
			title : '订舱号',
			width : 200
		} ] ], bill_id);

		calAmount();
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

	function formatType(value, row) {
		if (value) {
			return value == "1" ? "仓租" : "运输费用";
		} else {
			return value;
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
	function groupFormat(value, rows) {
		var lr = 0;
		for (var i = 0; i < rows.length; i++) {
			lr += rows[i].profit;
		}
		if (lr >= 0)
			return "编号：" + value + '， ' + rows.length + '笔费用， 总利润：' + lr;
		else
			return "编号：" + value + '， ' + rows.length
					+ '笔费用， 总利润：<font color="red">' + lr + '</font>';
	}

	function formatChecked(value, row) {
		if (value == true)
			return '是';
		else
			return '否';
	}

	function formatProfit(value, row, index) {
		if (value < 0) {
			return 'background-color:#ffee00;color:red;';
			// the function can return predefined css class and inline style
			// return {class:'c1',style:'color:red'}
		}
	}
	//form 部分//////////////////////////////////////////////////////////////////
	//请求地址
	var url;
	//提示消息
	var mesTitle;

	//添加信息
	function add(ftype) {
		$('#dlg').dialog('open').dialog('setTitle', '新增');
		$('#fm').form('clear');
		$("#e_price").numberbox('setValue', 0);
		$("#e_amount").numberbox('setValue', 0);
		$("#e_pay").numberbox('setValue', 0);
		$("#e_ftype").val(ftype);
		$("#q_ftype").val(ftype);
		if (ftype == 2) {
			$("#tr_delivery").show();
			$("#tr_stock_in").hide();
		} else {
			$("#tr_delivery").hide();
			$("#tr_stock_in").show();
		}
		var d = FormatterDate(new Date());
		$('#e_sdate').datebox('setValue', d);
		$('#e_edate').datebox('setValue', d);
		//$('#fm').attr("method", "POST");
		//$('#roleid').val("1");//默认选中1
		//$('#e_uname').textbox('readonly', false);
		url = path + "/fees";
		mesTitle = '新增成功';
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			if (ftype == 1) {
				$('#e_billId1').combogrid("setValue", row.billId);
			} else {
				$('#e_billId2').combogrid("setValue", row.billId);
			}
			$('#e_client').combogrid("setValue", row.clientId);
		} else {
			if ($('#q_clientId').val() != '')
				$('#e_client').combogrid("setValue", $('#q_clientId').val());
			if ($('#q_billId').val() != '') {
				if (ftype == 1) {
					$('#e_billId1').combogrid("setValue", $('#q_billId').val());
				} else {
					$('#e_billId2').combogrid("setValue", $('#q_billId').val());
				}
			}
		}
	}

	//编辑信息
	function edit() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			var id = row.id;
			if (row.ftype == 2) {
				$("#tr_delivery").show();
				$("#tr_stock_in").hide();
			} else {
				$("#tr_delivery").hide();
				$("#tr_stock_in").show();
			}

			row.sdate = formatDate(row.sdate, row);//绑定json日期有问题需要格式化
			row.edate = formatDate(row.edate, row);
			$('#dlg').dialog('open').dialog('setTitle', '编辑');
			$('#fm').form('load', row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
			//$('#fm').attr("method", "PUT");
			//$('#e_uname').textbox('readonly', true);
			//if (row.isCollect == true)
			$("#eIsCollect").attr("checked", row.isCollect);
			//if (row.isPay == true)
			$("#eIsPay").attr("checked", row.isPay);
			$('#e_id').val(id);
			url = path + "/fees/" + id;
			mesTitle = '编辑成功';
		} else {
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
	}

	//删除用信息
	function del() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			var id = row.id;
			$('#dlg_delete').dialog('open').dialog('setTitle', '删除');
			//$('#fm').form('load', row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
			url = path + "/fees/" + id;
			mesTitle = '删除成功';
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}

	//保存添加、修改内容
	function saveFee() {
		//alert($('#eIsCollect').is(':checked'));
		//return;
		if (!$('#fm').form('validate'))
			return;

		var method = 'POST';
		if ($('#e_id').val() != '') {
			method = 'PUT';
		}

		var frmObj = $('#fm').serializeObject();
		frmObj.isCollect = $('#eIsCollect').is(':checked');
		frmObj.isPay = $('#eIsPay').is(':checked');
		var r;
		if (frmObj.ftype == '2') {
			frmObj.billId = $('#e_billId2').combogrid("getValue");
			var g = $('#e_billId2').combogrid('grid'); // get datagrid object
			r = g.datagrid('getSelected');
		} else {
			frmObj.billId = $('#e_billId1').combogrid("getValue");
			var g = $('#e_billId1').combogrid('grid'); // get datagrid object
			r = g.datagrid('getSelected');
		}

		if (frmObj.billId && r) {
			var d = JSON.stringify(frmObj);
			$.ajax({
				type : method,
				url : url,
				data : d,
				async : false,
				contentType : 'application/json',
				dataType : 'json',
				error : function(data) {
					alert("error:" + data.responseText);
				},
				success : function(result) {
					//var result = eval('(' + result + ')');
					if (result.success) {
						//$('#dlg').dialog('close');
						$('#dg').datagrid('reload');
					} else {
						mesTitle = '操作失败';
					}
					$.messager.show({
						title : mesTitle,
						msg : result.msg
					});
				}
			});
		} else
			alert('请选择单号！');
	}

	//提交删除内容
	function saveFee_del() {
		$('#_method').val('DELETE');

		$.ajax({
			type : "DELETE",
			url : url,
			async : false,
			error : function(request) {
				alert("发生异常！");
			},
			success : function(result) {
				//var result = eval('(' + result + ')');
				if (result.success) {
					$('#dlg_delete').dialog('close');
					$('#dg').datagrid('reload');
				} else {
					mesTitle = '删除失败';
				}
				$.messager.show({
					title : mesTitle,
					msg : result.msg
				});
			}
		});
	}

	function calAmount() {
		//仓租=(出仓日-入仓日-5)*单价
		if ($('#e_ftype').val() == "1") {
			var sdate = new Date($('#e_sdate').datebox("getValue"));
			var edate = new Date($('#e_edate').datebox("getValue"));
			var diffDays = edate.DiffDays(sdate) - 5;
			if (diffDays > 0)
				$('#e_amount').numberbox('setValue',
						diffDays * $('#e_price').numberbox('getValue'));
			else
				$('#e_amount').numberbox('setValue', 0);
		}
	}

	function initBill(objid, url, txtField, qParms, cols, defaultValue) {
		$('#' + objid).combogrid(
				{
					panelWidth : 300,
					queryParams : qParms,
					mode : 'remote',
					value : defaultValue,
					idField : 'id',
					textField : txtField,
					method : 'get',
					url : url,
					fitColumns : true,
					columns : cols,
					keyHandler : {
						up : function() {
						},
						down : function() {
						},
						enter : function() {
						},
						query : function(q) {
							//动态搜索
							$('#e_billId').combogrid("grid").datagrid("reload",
									{
										'code' : q
									});
							$('#e_billId').combogrid("setValue", q);
						}
					},
					onChange : function(newValue, oldValue) {
						//$('#e_billCode').val(r.code);
						//var g = $('#e_billId').combogrid('grid');
						//g.datagrid('selectRecord', bill_id);
					},
					onSelect : function(index, row) {
						$('#e_billCode').val(row.code);
						//getOrder(row.id);
						if ($('#q_ftype').val() == "1") {
							$('#e_sdate').datebox('setValue',
									new Date(row.inDate).Format('yyyy-MM-dd'));
							if (row.outDate)
								$('#e_edate').datebox(
										'setValue',
										new Date(row.outDate)
												.Format('yyyy-MM-dd'));
							else
								$('#e_edate').datebox('setValue',
										new Date().Format('yyyy-MM-dd'));
						}
					}
				});
	}

	function getOptionList(flag, els) {
		$.ajax({
			type : 'GET',
			url : path + '/options/list',
			data : {
				otype : flag
			},
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
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<!-- 用户信息列表 title="用户管理" -->
		<table id="dg" class="easyui-datagrid" border="false" nowrap="false"
			fit="true" toolbar="#toolbar"
			data-options="url:'${path}/fees/datagrid',queryParams:{key:'${key}',billId:'${billId}',ftype:'${ftype}'}, collapsible:true, fitColumns:true, singleSelect:true,
			rownumbers:true, striped:true, view:groupview, groupField:'billId', method:'GET',groupFormatter:groupFormat">
			<thead>
				<tr>
					<th field="billId" width="50">编号</th>
					<th field="billCode" width="100">单号</th>
					<th data-options="field:'client',width:100,formatter:formatClient">客户</th>
					<th data-options="field:'ftype',width:50,formatter:formatType">类型</th>
					<th field="fname" width="50">费用名称</th>
					<!-- 
					<th field="price" width="50">单价</th>
					 -->
					<th field="amount" width="50">应收</th>
					<th field="pay" width="50">应付</th>
					<th data-options="field:'profit',width:50,styler:formatProfit">利润</th>
					<th
						data-options="field:'isCollect',width:50,formatter:formatChecked">已收</th>
					<th data-options="field:'isPay',width:50,formatter:formatChecked">已付</th>
					<th data-options="field:'sdate',width:50,formatter:formatDate">开始</th>
					<th data-options="field:'edate',width:50,formatter:formatDate">结束</th>
					<th data-options="field:'flag',width:50,formatter:formatStatus">状态</th>
					<th field="memo" width="50">备注</th>
				</tr>
			</thead>
		</table>

		<!-- 按钮 -->
		<div id="toolbar">
			<input class="easyui-textbox" type="text" id="q_key"
				data-options="prompt:'输入客户名称或单号查询'" value="${key}" /> 从：<input
				id="q_sdate" type="text" class="easyui-datebox" /> 到：<input
				id="q_edate" type="text" class="easyui-datebox" /> <a
				href="javascript:search()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查询</a> <a
				href="javascript:add(1)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增仓租</a> <a
				href="javascript:add(2)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">新增运输费用</a> <a
				href="javascript:del()" class="easyui-linkbutton"
				data-options="iconCls:'icon-remove'">删除</a> <a
				href="javascript:edit()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'">编辑</a>
		</div>
		<!-- 添加/修改对话框 -->
		<div id="dlg" class="easyui-dialog"
			style="width: 500px; height: 400px; padding: 30px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<input type="hidden" name="id" id="e_id" value="${fee_id}" /> <input
					type="hidden" name="billCode" id="e_billCode" />
				<table cellpadding="5">
					<tr>
						<td>客户:</td>
						<td><input class="easyui-textbox" style="width: 250px"
							type="text" name="clientId" id="e_client"
							data-options="required:true" /></td>
					</tr>
					<tr id="tr_stock_in">
						<td>入仓单号:</td>
						<td><input class="easyui-textbox" style="width: 250px"
							type="text" name="billId" id="e_billId1" /></td>
					</tr>
					<tr id="tr_delivery">
						<td>提货单号:</td>
						<td><input class="easyui-textbox" style="width: 250px"
							type="text" name="billId" id="e_billId2" /></td>
					</tr>
					<tr>
						<td>类型:</td>
						<td><select id="e_ftype" name="ftype" onfocus="this.blur();"><option
									value="1">仓租</option>
								<option value="2">运输费用</option></select></td>
					</tr>
					<tr>
						<td>费目:</td>
						<td><input class="easyui-combobox" id="e_fname" name="fname"
							data-options="required:true,valueField: 'oname',textField: 'oname',method : 'GET'"></td>
					</tr>
					<tr>
						<td>单价:</td>
						<td><input id="e_price" name="price" type="text"
							class="easyui-numberbox" value="100"
							data-options="required:true,min:0,precision:1,onChange:function(newValue,oldValue){calAmount();}"></td>
					</tr>
					<tr>
						<td>应收:</td>
						<td><input id="e_amount" name="amount" type="text"
							class="easyui-numberbox" value="100"
							data-options="required:true,min:0,precision:1"></td>
					</tr>
					<tr>
						<td>应付:</td>
						<td><input id="e_pay" name="pay" type="text"
							class="easyui-numberbox" value="0"
							data-options="required:true,min:0,precision:1"></td>
					</tr>
					<tr>
						<td>开始日期:</td>
						<td><input name="sdate" id="e_sdate" type="text"
							class="easyui-datebox"
							data-options="required:true,onChange:function(newDate, oldDate){calAmount();}" /></td>
					</tr>
					<tr>
						<td>结束日期:</td>
						<td><input name="edate" id="e_edate" type="text"
							class="easyui-datebox"
							data-options="required:true,onChange:function(newDate, oldDate){calAmount();}" /></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" id="e_memo"
							name="memo" /></td>
					</tr>
					<tr>
						<td>已收款:</td>
						<td><input type="checkbox" id="eIsCollect" name="isCollect"
							onchange="this.value=this.checked" /></td>
					</tr>
					<tr>
						<td>已付款:</td>
						<td><input type="checkbox" id="eIsPay" name="isPay"
							onchange="this.value=this.checked" /></td>
					</tr>
				</table>
			</form>
		</div>
		<!-- 添加/修改对话框按钮 -->
		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="saveFee()" style="width: 90px">保存</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')"
				style="width: 90px">取消</a>
		</div>
		<!-- 删除对话框 -->
		<div id="dlg_delete" class="easyui-dialog"
			style="width: 300px; height: 200px; padding: 30px 20px" closed="true"
			buttons="#dlg-del-buttons">
			<div class="ftitle">请谨慎操作</div>
			<form id="delFm" method="post" novalidate>
				<input type="hidden" id="q_ftype" value="${ftype}" /> <input
					type="hidden" id="q_clientId" value="${clientId}" /><input
					type="hidden" id="q_billId" value="${billId}" /> <label>确定删除该记录吗？</label>
			</form>
		</div>
		<!-- 删除对话框按钮 -->
		<div id="dlg-del-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="saveFee_del()" style="width: 90px">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#dlg_delete').dialog('close')"
				style="width: 90px">取消</a>
		</div>
	</div>
</body>
</html>
