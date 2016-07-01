<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>客户管理</title>
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
		//$('#fm').attr("method", "POST");
		url = path + "/clients";
		mesTitle = '新增成功';
	}

	//编辑信息
	function edit() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			var id = row.id;
			$('#dlg').dialog('open').dialog('setTitle', '编辑');
			$('#fm').form('load', row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
			//$('#fm').attr("method", "PUT");
			$('#e_uname').textbox('readonly', true);
			$('#userid').val(id);
			url = path + "/clients/" + id;
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
			url = path + "/clients/" + id;
			mesTitle = '删除成功';
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}

	//保存添加、修改内容
	function save() {
		var method = 'POST';
		//$('#_method').val('POST');
		if ($('#clientid').val() != '') {
			method = 'PUT';
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
					mesTitle = result.msg;
				}
				$.messager.show({
					title : mesTitle,
					msg : result.msg
				});
			}
		});
	}

	//提交删除内容
	function saveDel() {
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
					mesTitle = result.msg;
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
			cname : $('#q_cname').val()
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
			url="${path}/clients" method="GET" toolbar="#toolbar"
			pagination="true" fitColumns="true" singleSelect="true"
			rownumbers="true" striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th field="code" width="100">代号</th>
					<th field="cname" width="100">名称</th>
					<th field="contactMan" width="100">负责人</th>
					<th field="contactTel" width="100">联系电话</th>
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
				iconCls="icon-remove" plain="true" onclick="del();">删除</a> <span>名称:</span><input
				name="q_cname" id="q_cname" value="" size=10 /> <a
				href="javascript:search()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查询</a>
		</div>

		<!-- 添加/修改对话框 -->
		<div id="dlg" class="easyui-dialog"
			style="width: 400px; height: 400px; padding: 30px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<!-- <input type="hidden" id="_method" name="_method" value="PUT" /> -->
				<input type="hidden" id="clientid" name="id" />
				<div class="fitem">
					<label>代号:</label> <input name="code" id="e_code"
						class="easyui-textbox">
				</div>
				<div class="fitem">
					<label>名称:</label> <input name="cname" id="e_cname"
						class="easyui-textbox" required="true">
				</div>
				<div class="fitem">
					<label>负责人:</label> <input name="contactMan" class="easyui-textbox">
				</div>
				<div class="fitem">
					<label>联系电话:</label> <input name="contactTel"
						class="easyui-textbox">
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
				<label>确定删除该项吗？</label>
			</form>
		</div>

		<!-- 删除对话框按钮 -->
		<div id="dlg-del-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="saveDel()" style="width: 90px">删除</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#dlg_delete').dialog('close')"
				style="width: 90px">取消</a>
		</div>

	</div>
</body>
</html>
