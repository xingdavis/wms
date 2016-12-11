package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xs.wms.dao.IMenu;
import com.xs.wms.pojo.Menu;
import com.xs.wms.pojo.easyui.PageHelper;


@Service
public class MenuService {
	@Resource
	private IMenu menuMapper;
	/**
	 * 获取总数
	 * 
	 * @param user
	 * @return
	 */
	public Long getDatagridTotal(Menu menu) {
		return menuMapper.getDatagridTotal(menu);
	}

	/**
	 * 获取一级列表
	 * 
	 * @param page
	 * @return
	 */
	public List<Menu> datagridMenu(PageHelper page) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getRows());
		return menuMapper.datagridMenu(page);
	}

	/**
	 * 获取所有列表
	 * 
	 * @param page
	 * @return
	 */
	public List<Menu> getAll(PageHelper page) {
		page.setStart((page.getPage() - 1) * page.getRows());
		page.setEnd(page.getRows());
		return menuMapper.getAll(page);
	}

	public void deleteMenuById(Integer menuId) {
		menuMapper.deleteMenuById(menuId);
	}

	public Menu getMenuById(Integer menuId) {
		return menuMapper.getMenuById(menuId);
	}

	public List<Menu> listAllParentMenu() {
		return menuMapper.listAllParentMenu();
	}

	public void saveMenu(Menu menu) {
		if (menu.getId() != null && menu.getId().intValue() > 0) {
			menuMapper.updateMenu(menu);
		} else {
			menuMapper.insertMenu(menu);
		}
	}

	public List<Menu> listSubMenuByParentid(Integer parentid) {
		return menuMapper.listSubMenuByParentid(parentid);
	}

	public List<Menu> listAllMenu() {
		List<Menu> rl = this.listAllParentMenu();
		for (Menu menu : rl) {
			List<Menu> subList = this.listSubMenuByParentid(menu.getId());
			menu.setSubMenu(subList);
		}
		return rl;
	}

	public List<Menu> listAllSubMenu() {
		return menuMapper.listAllSubMenu();
	}
}
