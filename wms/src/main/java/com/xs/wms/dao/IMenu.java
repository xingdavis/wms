package com.xs.wms.dao;

import java.util.List;
import com.xs.wms.pojo.Menu;
import com.xs.wms.pojo.easyui.PageHelper;;

public interface IMenu {
    int deleteByPrimaryKey(Integer id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
    
	public List<Menu> listAllParentMenu();
	
	public List<Menu> listSubMenuByParentid(Integer parentid);
	
	public List<Menu> getDatagrid();
	
	public List<Menu> getAll(PageHelper page);

	public Long getDatagridTotal(Menu menu);

	public List<Menu> datagridMenu(PageHelper page);
	
	public Menu getMenuById(Integer menuId);
	
	public void insertMenu(Menu menu);
	
	public void updateMenu(Menu menu);
	
	public void deleteMenuById(Integer menuId);
	
	public List<Menu> listAllSubMenu();
}