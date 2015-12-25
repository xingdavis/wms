package com.xs.wms.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;
import com.xs.wms.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Resource
	private UserService userService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String toIndex(@PathVariable Integer id, HttpServletRequest request, Model model) {
		User user = this.userService.getUserById(id);
		model.addAttribute("user", user);
		return "showuser";
	}

	/**
	 * 新增用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	public Json addUser(@RequestBody User user) {
		Json j = new Json();

		try {
			if (userService.getUserByUname(user.getUname()) == null) {
				userService.add(user);
				j.setSuccess(true);
				j.setMsg("用户新增成功！");
				j.setObj(user);
			} else {
				j.setMsg("用户名称重复，请改用其它！");
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public Json editUser(@PathVariable Integer id, @RequestBody User user) {
		Json j = new Json();
		try {
			User oUser = userService.getUserById(id);
			if (oUser != null) {
				user.setId(id);
				userService.update(user);
				j.setSuccess(true);
				j.setMsg("修改成功！");
				j.setObj(user);
			} else {
				j.setMsg("记录id不存在！");
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 删除某个用户
	 * 
	 * @param userId
	 * @param out
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Json deleteUser(@PathVariable Integer id) {
		Json j = new Json();
		try {
			userService.delete(id);
			j.setSuccess(true);
			j.setMsg("删除成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 用户列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String userList(Model model) {
		return "user/list";
	}

	/**
	 * 返回用户表格数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/datagrid", method = RequestMethod.POST)
	public DataGrid datagrid(PageHelper page, User user, Integer sysid) {
		DataGrid dg = new DataGrid();
		dg.setTotal(userService.getDatagridTotal(user));
		List<User> userList = userService.datagridUser(page, user);
		dg.setRows(userList);
		return dg;
	}
}
