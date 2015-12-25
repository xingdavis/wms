<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户管理</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<script type="text/javascript">
	//请求地址
	var url;
	//提示消息
	var mesTitle;

	//添加用户信息
	function addUser() {
		$('#dlg').dialog('open').dialog('setTitle', '新增用户');
		$('#fm').form('clear');
		//$('#fm').attr("method", "POST");
		$('#roleid').val("1");//默认选中1
		url = path + "/users";
		mesTitle = '新增用户成功';
	}

	//编辑用户信息
	function editUser() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			var id = row.id;
			$('#dlg').dialog('open').dialog('setTitle', '编辑用户');
			$('#fm').form('load', row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
			//$('#fm').attr("method", "PUT");
			$('#userid').val(id);
			url = path + "/users/" + id;
			mesTitle = '编辑用户成功';
		} else {
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
	}

	//删除用信息
	function deleteUser() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			var id = row.id;
			$('#dlg_delete').dialog('open').dialog('setTitle', '删除用户');
			$('#fm').form('load', row);//这句话有问题，第一次加载时正确的，第二次就出错了，还保持第一次的数据
			url = path + "/users/" + id;
			mesTitle = '删除用户成功';
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}

	//保存添加、修改内容
	function saveUser() {
		var method = 'POST';
		//$('#_method').val('POST');
		if ($('#userid').val() != '') {
			method = 'PUT';
			//$('#_method').val('PUT');
		}
		//alert($('#roleid').val());
		/* $('#fm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {

				var result = eval('(' + result + ')');
				if (result.success) {
					$('#dlg').dialog('close');
					$('#datagrid').datagrid('reload');
				} else {
					mesTitle = '新增用户失败';
				}
				$.messager.show({
					title : mesTitle,
					msg : result.msg
				});
			}
		}); */
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
					mesTitle = '保存用户失败';
				}
				$.messager.show({
					title : mesTitle,
					msg : result.msg
				});
			}
		});
	}

	//提交删除内容
	function saveUser_del() {
		$('#_method').val('DELETE');
		/* $('#fm').form('submit', {
			url : url,
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					$('#dlg_delete').dialog('close');
					$('#datagrid').datagrid('reload');
				} else {
					mesTitle = '删除用户失败';
				}
				$.messager.show({
					title : mesTitle,
					msg : result.msg
				});
			}
		}); */

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
					mesTitle = '删除用户失败';
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
			uname : $('#suname').val()
		});
	}

	//刷新
	function reload() {
		$('#dg').datagrid('reload');
	}

	$.fn.serializeObject = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	};
</script>

</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<!-- 用户信息列表 title="用户管理" -->
		<table id="dg" class="easyui-datagrid" fit="true"
			url="${path}/users/datagrid" toolbar="#toolbar" pagination="true"
			fitColumns="true" singleSelect="true" rownumbers="true"
			striped="true" border="false" nowrap="false">
			<thead>
				<tr>
					<th field="uname" width="100">用户名</th>
					<th field="pwd" width="100">密码</th>
					<th field="age" width="100">年龄</th>
					<th field="roleid" width="100">角色</th>
				</tr>
			</thead>
		</table>

		<!-- 按钮 -->
		<div id="toolbar">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				iconCls="icon-reload" plain="true" onclick="reload();">刷新</a> <a
				href="javascript:void(0);" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" onclick="addUser();">新增</a> <a
				href="javascript:void(0);" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" onclick="editUser();">编辑</a> <a
				href="javascript:void(0);" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true" onclick="deleteUser();">删除</a> <span>用户名:</span><input
				name="search_username" id="suname" value="" size=10 /> <a
				href="javascript:search()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'">查询</a>
		</div>

		<!-- 添加/修改对话框 -->
		<div id="dlg" class="easyui-dialog"
			style="width: 400px; height: 400px; padding: 30px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<!-- <input type="hidden" id="_method" name="_method" value="PUT" /> -->
				<input type="hidden" id="userid" name="id" />
				<div class="fitem">
					<label>用户名:</label> <input name="uname" class="easyui-textbox"
						required="true" readonly="readonly">
				</div>
				<div class="fitem">
					<label>密码:</label> <input name="pwd" class="easyui-textbox"
						required="true" type="password">
				</div>
				<div class="fitem">
					<label>年龄:</label> <input name="age" class="easyui-textbox">
				</div>
				<div class="fitem">
					<label>角色:</label> <select id="roleid" name="roleid">
						<option value="1" selected="selected">操作员</option>
						<option value="0">管理员</option>
					</select>
				</div>
			</form>
		</div>

		<!-- 添加/修改对话框按钮 -->
		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="saveUser()" style="width: 90px">保存</a> <a
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
				<label>确定删除用户吗？</label>
			</form>
		</div>

		<!-- 删除对话框按钮 -->
		<div id="dlg-del-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="saveUser_del()" style="width: 90px">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#dlg_delete').dialog('close')"
				style="width: 90px">取消</a>
		</div>

	</div>
</body>
</html>
