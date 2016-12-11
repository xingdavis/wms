package com.xs.wms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xs.wms.dao.IOption;
import com.xs.wms.pojo.Option;
import com.xs.wms.pojo.easyui.PageHelper;

@Service
public class OptionService {
	@Resource
	private IOption optionMapper;
	
	
	public Option get(int id)
	{
		return this.optionMapper.selectByPrimaryKey(id);
	}
	
	public List<Option> getList(Option obj) {
		return optionMapper.getList(obj);  
	}
	
	public Long getDatagridTotal(Option obj) {
		return optionMapper.getDatagridTotal(obj);  
	}

	/**
	 * 获取列表
	 * @param page
	 * @return
	 */
	public List<Option> datagrid(PageHelper page,Option obj) {
		page.setStart((page.getPage()-1)*page.getRows());
		page.setEnd(page.getRows());
		return optionMapper.datagrid(page, obj);  
	}
	
	/**
	 * 新增
	 * @param
	 */
	public int add(Option obj) {
		return optionMapper.insert(obj);  
	}
	
	
	/**
	 * 删除
	 * @param id
	 */
	public int delete(int id){
		return optionMapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 更新
	 * @param
	 * @return
	 */
	public int update(Option obj){
		return optionMapper.updateByPrimaryKey(obj);
	}
}
