package com.xs.wms.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xs.wms.pojo.Menu;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.PageHelper;

public interface IUser {
    int deleteUser(Integer id);

    int addUser(User record);

    User selectByPrimaryKey(Integer id);
    User selectByUname(String uname);

    int updateUser(User record);
    
    List<Menu> getMenuByUserId(Integer userId);
    
    Long getDatagridTotal(@Param("user")User user,@Param("sysid")Integer sysid);
    
    List<User> datagridUser(@Param("page")PageHelper page,@Param("sysid")Integer sysid); 
}