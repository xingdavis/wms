package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IUser;
import com.xs.wms.pojo.Menu;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.PageHelper;

/**
 * @author davis
 *
 */
/**
 * @author davis
 *
 */
@Service
public class UserService {
	@Resource
	private IUser userMapper;
	
	/**
	 * 根据用户id返回用户对象
	 * @param userId
	 * @return
	 */
	public User getUserById(int userId)
	{
		return this.userMapper.selectByPrimaryKey(userId);
	}
	
	/**
	 * 根据用户名匹配用户
	 * @param uname
	 * @return
	 */
	public User getUserByUname(String uname)
	{
		return this.userMapper.selectByUname(uname);
	}
	
	/**
	 * 获取用户菜单
	 * @param userId
	 * @return
	 */
	public List<Menu> getMenuByUserId(Integer userId){
		return this.userMapper.getMenuByUserId(userId);
	}
	
	/**
	 * 获取用户总数
	 * @param user
	 * @return
	 */
	public Long getDatagridTotal(User user) {
		return userMapper.getDatagridTotal(user);  
	}

	/**
	 * 获取用户列表
	 * @param page
	 * @return
	 */
	public List<User> datagridUser(PageHelper page,User user) {
		page.setStart((page.getPage()-1)*page.getRows());
		page.setEnd(page.getRows());
		return userMapper.datagridUser(page,user);  
	}
	
	/**
	 * 新增用户
	 * @param user
	 */
	public int add(User user) {
		return userMapper.addUser(user);  
	}
	
	
	/**
	 * 删除用户
	 * @param id
	 */
	public int delete(int id){
		return userMapper.deleteUser(id);
	}
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	public int update(User user){
		return userMapper.updateUser(user);
	}

}
