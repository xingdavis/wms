package com.xs.wms.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xs.wms.pojo.Menu;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.PageHelper;

public interface IUser {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);
    User selectByUname(String uname);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    List<Menu> getMenuByUserId(Integer userId);
    
    Long getDatagridTotal(@Param("user")User user,@Param("sysid")Integer sysid);
    
    List<User> datagridUser(@Param("page")PageHelper page,@Param("sysid")Integer sysid); 
}