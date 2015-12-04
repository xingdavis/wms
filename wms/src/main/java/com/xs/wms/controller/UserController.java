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
import com.xs.wms.pojo.easyui.Json;
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
	 * 新增用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "",method = RequestMethod.POST)
    public Json addUser(User user) {
		Json j = new Json();
		
		try {
			userService.add(user);
            j.setSuccess(true);
            j.setMsg("用户新增成功！");
            j.setObj(user);
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
    @RequestMapping(value = "",method = RequestMethod.PUT)
    public Json editUser(User user) {
        Json j = new Json();
        try {
            userService.update(user);
            j.setSuccess(true);
            j.setMsg("修改成功！");
            j.setObj(user);
        } catch (Exception e) {
            j.setMsg(e.getMessage());
        }
        return j;
    }
	
	/**
	 * 删除某个用户
	 * @param userId
	 * @param out
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
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
