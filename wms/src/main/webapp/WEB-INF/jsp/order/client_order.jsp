<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>客户提交订单</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	var editIndex = undefined;
	function endEditing() {
		if (editIndex == undefined) {
			return true
		}
		if ($('#dg').datagrid('validateRow', editIndex)) {
			/* var ed = $('#dg').datagrid('getEditor', {
				index : editIndex,
				field : 'id'
			});
			var cname = $(ed.target).combobox('getText');
			$('#dg').datagrid('getRows')[editIndex]['cname'] = cname;
			$('#dg').datagrid('endEdit', editIndex); */
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	function onClickCell(index,field,value) {
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
				orderId:0
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
	
	function autocode(){
		$('#e_code').textbox('clear');
	}
	
	function submitForm(){
		if (!$('#fm').form('validate'))
        	return;
		var mesTitle='';
		var items = $('#dg').datagrid('getData').rows;
		if (items.length==0){
			$.messager.show({
				title : '提示',
				msg : '请录入货物明细！'
			});
			return;
		}
				
		//$('#e_order_details').val(items);
		var order=$('#fm').serializeObject();
		order.order_details=items;
		var d = JSON.stringify(order);
		//return;
		$.ajax({
			url : path + '/orders/myorder',
			data : d,
			async : false,
			//method:'post',
			contentType : 'application/json',
			dataType : 'json',
			error : function(data) {
				alert("error:" + data.responseText);
			},
			success : function(result) {
				//var result = eval('(' + result + ')');
				if (result.success) {
					if (window.confirm("入仓订单已生成，订单号：" + result.obj.code + "。查看导出入仓路线图吗？"))
						location.href = '${path}/orders/report/' + result.obj.id;
					else
						location.href = '${path}/orders/mypage';
				} else {
					alert(result.msg);
				}
			}
		});
	}
	
	function submitClientForm(){	 
        if (!$('#fm_client').form('validate'))
        	return;
        
		var d = JSON.stringify($('#fm_client').serializeObject());
		
		
		$.ajax({
			url : path + '/clients',
			data : d,
			async : false,
			method:'post',
			contentType : 'application/json',
			dataType : 'json',
			error : function(data) {
				alert("error:" + data.responseText);
			},
			success : function(result) {
				//var result = eval('(' + result + ')');
				if (result.success) {
					$('#dlg').dialog('close');
					//$('#e_client').combogrid('setValue', result.obj);
					$('#e_client').combogrid("grid").datagrid("load", result.obj);
					//$('#e_client').combogrid('setValue', result.obj.id);
					$('#e_client').combogrid("grid").datagrid('selectRecord', result.obj.id);
					$('#e_client').combogrid('setValue', result.obj.id);
					$('#e_contact_man').textbox("setValue",result.obj.contactMan);
					$('#e_contact_tel').textbox("setValue",result.obj.contactTel);
					//$('#e_client').combogrid('setValue', result.obj.id);
				} else {
					$.messager.show({
						title : '新增客户失败！',
						msg : result.msg
					});
				}
				
			}
		});
	}
	
	//页面所有东西加载完执行（包括js、图片）
	$(window).load(function() {
		
	});
	
	//DOM加载完毕执行
	$(document).ready(function(){
		$('#e_client').combogrid({
		    panelWidth:300,
		    queryParams:{cname:$('#e_client').val()},
		    mode:'remote',
		    idField:'id',
		    textField:'cname',
		    method:'get',
		    url: path + '/clients/order_client',
		    fitColumns: true,
		    columns:[[
		              {field:'id',title:'id',hidden:true},
		              {field:'cname',title:'名称',width:200}
		    ]],
		    keyHandler: {
                up: function() {},
                down: function() {},
                enter: function() {},
                query: function(q) {
                    //动态搜索
                    $('#e_client').combogrid("grid").datagrid("reload", { 'cname': q });
                    $('#e_client').combogrid("setValue", q);
                }
            },
            /* onChange:function(newValue,oldValue){
            	alert(newValue);
            } */
            onSelect:function(index,row){
    			$('#e_contact_man').textbox('setValue',row.contactMan);
    			$('#e_contact_tel').textbox('setValue',row.contactTel);
    		}
		});
		
		/* $('#e_client').combogrid("grid").datagrid("onSelect",  function(index,row){
			$('#e_contact_man').textbox('setValue',row.contact_man);
			$('#e_contact_tel').textbox('setValue',row.contact_tel);
		}); */
		
	});
</script>
</head>
<body>
	<div class="easyui-panel" title="New Topic" style="width: auto">
		<div style="padding: 10px 60px 20px 60px">
			<form id="fm" method="post">
				<input type="hidden" name="order_details" id="e_order_details" /> <input
					type="hidden" name="orderDate" /> <input type="hidden" name="flag" />
				<table cellpadding="5">
					<tr>
						<td>公司:</td>
						<td><input class="easyui-textbox" type="text" name="clientId"
							id="e_client" data-options="required:true" /><a
							href="javascript:$('#dlg').dialog('open')"
							class="easyui-linkbutton">没找到？添加一个</a></td>
					</tr>
					<tr>
						<td>订单号:</td>
						<td><input class="easyui-textbox"
							data-options="prompt:'输入公司自有单号，或留空有系统自动生成'" type="text"
							name="code" id="e_code" /><a href="javascript:autocode()"
							class="easyui-linkbutton">系统自动生成</a></td>
					</tr>
					<tr>
						<td>联系人:</td>
						<td><input class="easyui-textbox" type="text"
							id="e_contact_man" name="contactMan" /></td>
					</tr>
					<tr>
						<td>联系电话:</td>
						<td><input class="easyui-textbox" name="contactTel"
							id="e_contact_tel"></input></td>
					</tr>
					<tr>
						<td>总货名:</td>
						<td><input class="easyui-textbox" name="cargoName"
							id="e_cargoName"></input></td>
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
							<th data-options="field:'orderId',width:0,hidden:true">Order
								ID</th>
								 -->
							<th
								data-options="field:'cname',width:100,editor:{type:'validatebox',options:{required:true}}">货名</th>
							<th
								data-options="field:'num',width:80,align:'right',editor:{type:'numberbox',options:{precision:0}}">数量</th>
							<th
								data-options="field:'vol',width:80,align:'right',editor:{type:'numberbox',options:{precision:2}}">体积</th>
							<th
								data-options="field:'weight',width:80,editor:{type:'numberbox',options:{precision:2}}">重量</th>
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
					onclick="submitForm()">提交订单</a> <a href="${path}/orders/mypage"
					class="easyui-linkbutton">返回查单</a>
			</div>
			<div id="dlg" class="easyui-dialog" title="客户登记"
				data-options="iconCls:'icon-save',closed:true"
				style="width: 400px; height: 200px; padding: 10px">
				<form id="fm_client" method="post">
					<table cellpadding="5">
						<tr>
							<td>公司代号:</td>
							<td><input class="easyui-textbox" type="text" name="code" /></td>
						</tr>
						<tr>
							<td>公司名称:</td>
							<td><input class="easyui-textbox" type="text" name="cname"
								data-options="required:true" /></td>
						</tr>
						<tr>
							<td>联系人:</td>
							<td><input class="easyui-textbox" type="text"
								name="contactMan" /></td>
						</tr>
						<tr>
							<td>联系电话:</td>
							<td><input class="easyui-textbox" name="contactTel"></input></td>
						</tr>
					</table>
					<div style="text-align: center; padding: 5px">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							onclick="submitClientForm()">保存</a> <a href="javascript:void(0)"
							class="easyui-linkbutton" onclick="$('#dlg').dialog('close')">关闭</a>
					</div>
				</form>
			</div>
		</div>
	</div>


</body>
</html>