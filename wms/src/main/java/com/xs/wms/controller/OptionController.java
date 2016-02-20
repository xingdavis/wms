package com.xs.wms.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xs.wms.pojo.Option;
import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;
import com.xs.wms.service.OptionService;

@Controller
@RequestMapping("/options")
public class OptionController {
	@Resource
	private OptionService optionService;

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String ListPage(Model model) {
		return "option/list";
	}
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET, consumes = "application/json")
	public Json list(@RequestBody Option obj) {
		Json j = new Json();
		try {
			List<Option> list = optionService.getList(obj);
			j.setObj(list);
			j.setSuccess(true);			
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "/datagrid", method = RequestMethod.POST)
	public DataGrid datagrid(PageHelper page, Option obj) {
		DataGrid dg = new DataGrid();
		dg.setTotal(optionService.getDatagridTotal(obj));
		List<Option> list = optionService.datagrid(page, obj);
		dg.setRows(list);
		return dg;
	}

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	public Json add(@RequestBody Option obj) {
		Json j = new Json();
		try {
			obj.setSort(0);
			optionService.add(obj);
			j.setSuccess(true);
			j.setMsg("新增成功！");
			j.setObj(obj);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 修改
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public Json edit(@PathVariable Integer id, @RequestBody Option obj) {
		Json j = new Json();
		try {
			Option o = optionService.get(id);
			if (o != null) {
				obj.setSort(0);
				optionService.update(obj);
				j.setSuccess(true);
				j.setMsg("修改成功！");
				j.setObj(obj);
			} else {
				j.setMsg("记录id不存在！");
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 删除
	 * 
	 * @param 
	 * @param out
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Json delete(@PathVariable Integer id) {
		Json j = new Json();
		try {
			optionService.delete(id);
			j.setSuccess(true);
			j.setMsg("删除成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
