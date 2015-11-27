package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IUser;
import com.xs.wms.pojo.Menu;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.PageHelper;

@Service
public class UserService {
	@Resource
	private IUser userMapper;
	
	public User getUserById(int userId)
	{
		return this.userMapper.selectByPrimaryKey(userId);
	}
	
	public User getUserByUname(String uname)
	{
		return this.userMapper.selectByUname(uname);
	}
	
	public List<Menu> getMenuByUserId(Integer userId){
		return this.userMapper.getMenuByUserId(userId);
	}
	
	/**
	 * 获取用户总数
	 * @param user
	 * @return
	 */
	public Long getDatagridTotal(User user,Integer sysid) {
		return userMapper.getDatagridTotal(user,sysid);  
	}

	/**
	 * 获取用户列表
	 * @param page
	 * @return
	 */
	public List<User> datagridUser(PageHelper page,Integer sysid) {
		page.setStart((page.getPage()-1)*page.getRows());
		page.setEnd(page.getPage()*page.getRows());
		return userMapper.datagridUser(page,sysid);  
	}

}
