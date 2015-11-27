package com.xs.wms.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.PageHelper;
import com.xs.wms.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Resource
	private UserService userService;
	
	@RequestMapping("/{id}")
	public String toIndex(@PathVariable Integer id,HttpServletRequest request,Model model){		
		User user = this.userService.getUserById(id);
		model.addAttribute("user",user);
		return "showuser";
	}


	/**
	 * 用户列表页面
	 * @return
	 */
	@RequestMapping(value = "",method = RequestMethod.GET)
    public String userList(Model model) {
        return "user/list";
    }
	
	/**
	 * 返回用户表格数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/datagrid", method = RequestMethod.POST)
	public DataGrid datagrid(PageHelper page,User user,Integer sysid) {
		DataGrid dg = new DataGrid();
		dg.setTotal(userService.getDatagridTotal(user,sysid));
		List<User> userList = userService.datagridUser(page,sysid);
		dg.setRows(userList);
		return dg;
	}
}
