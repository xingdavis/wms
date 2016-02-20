<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>选项管理</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	//请求地址
	var url;
	//提示消息
	var mesTitle;

	//添加信息
	function add() {
		$('#dlg').dialog('open').dialog('setTitle', '新增');
		$('#fm').form('clear');
		$('#e_otype').val($('#q_otype').val());
		url = path + "/options";
		mesTitle = '新增成功';
	}

	//编辑信息
	function edit() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			var id = row.id;
			$('#dlg').dialog('open').dialog('setTitle', '编辑');
			$('#fm').form('load', row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
			$('#e_id').val(id);
			url = path + "/options/" + id;
			mesTitle = '编辑成功';
		} else {
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
	}

	//删除信息
	function del() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			var id = row.id;
			$('#dlg_delete').dialog('open').dialog('setTitle', '删除');
			$('#fm').form('load', row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
			url = path + "/options/" + id;
			mesTitle = '删除成功';
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}

	//保存添加、修改内容
	function save() {
		var method = 'POST';
		//$('#_method').val('POST');
		if ($('#e_id').val() != '') {
			method = 'PUT';
			//$('#_method').val('PUT');
		}

		var d = JSON.stringify($('#fm').serializeObject());
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
					$('#dlg').dialog('close');
					$('#dg').datagrid('reload');
				} else {
					mesTitle = '保存失败';
				}
				$.messager.show({
					title : mesTitle,
					msg : result.msg
				});
			}
		});
	}

	//提交删除内容
	function save_del() {
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

	function search() {
		$('#dg').datagrid('load', {
			otype : $('#q_otype').val()
		});
	}

	//刷新
	function reload() {
		$('#dg').datagrid('reload');
	}
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<!-- 用户信息列表 title="用户管理" -->
		<table id="dg" class="easyui-datagrid" fit="true"
			url="${path}/options/datagrid" toolbar="#toolbar" pagination="true"
			fitColumns="true" singleSelect="true" rownumbers="true"
			striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th field="oname" width="100">选项名称</th>
					<th field="otype" width="100">选项类型</th>
				</tr>
			</thead>
		</table>

		<!-- 按钮 -->
		<div id="toolbar">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				iconCls="icon-reload" plain="true" onclick="reload();">刷新</a> <a
				href="javascript:void(0);" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" onclick="add();">新增</a> <a
				href="javascript:void(0);" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" onclick="edit();">编辑</a> <a
				href="javascript:void(0);" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true" onclick="del();">删除</a> <label>选项类型:</label>
			<select id="q_otype" name="otype">
				<option value="柜型">柜型</option>
				<option value="码头">码头</option>
				<option value="港口">港口</option>
			</select> <a href="javascript:search()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查询</a>
		</div>

		<!-- 添加/修改对话框 -->
		<div id="dlg" class="easyui-dialog"
			style="width: 400px; height: 400px; padding: 30px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<input type="hidden" id="e_sort" name="sort" value="0" />
				<input type="hidden" id="e_id" name="id" />
				<div class="fitem">
					<label>选项名称:</label> <input name="oname" id="e_oname"
						class="easyui-textbox" required="true">
				</div>
				<div class="fitem">
					<label>选项类型:</label> <select id="e_otype" name="otype">
						<option value="柜型" selected="selected">柜型</option>
						<option value="码头">码头</option>
						<option value="港口">港口</option>
					</select>
				</div>
			</form>
		</div>

		<!-- 添加/修改对话框按钮 -->
		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="save()" style="width: 90px">保存</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')"
				style="width: 90px">取消</a>
		</div>

		<!-- 删除对话框 -->
		<div id="dlg_delete" class="easyui-dialog"
			style="width: 300px; height: 200px; padding: 30px 20px" closed="true"
			buttons="#dlg-del-buttons">
			<div class="ftitle">请谨慎操作</div>
			<form id="fm" method="post" novalidate>
				<label>确定删除吗？</label>
			</form>
		</div>

		<!-- 删除对话框按钮 -->
		<div id="dlg-del-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="save_del()" style="width: 90px">删除</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#dlg_delete').dialog('close')"
				style="width: 90px">取消</a>
		</div>

	</div>
</body>
</html>
