package com.xs.wms.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xs.wms.pojo.Client;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;
import com.xs.wms.service.ClientService;

@Controller
@RequestMapping("/clients")
public class ClientController {
	@Resource
	private ClientService clientService;

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public DataGrid datagrid(PageHelper page, Client client) {
		DataGrid dg = new DataGrid();
		// String q = client.getCname();
		dg.setTotal(clientService.getDatagridTotal(client));
		List<Client> list = clientService.datagridClient(page, client);
		dg.setRows(list);
		return dg;
	}

	/**
	 * 客户查单选择的客户列表
	 * 
	 * @param page
	 * @param client
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order_client", method = RequestMethod.GET)
	public DataGrid orderClient(PageHelper page, Client client) {
		DataGrid dg = new DataGrid();
		if (client.getCname().length()>=2) {
			dg.setTotal(clientService.getDatagridTotal(client));
			List<Client> list = clientService.datagridClient(page, client);
			dg.setRows(list);
		}
		else
		{
			dg.setTotal(Long.valueOf("0"));
			dg.setRows(new ArrayList());
		}
		return dg;
	}

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Json addClient(@RequestBody Client client) {
		Json j = new Json();
		try {
			if (client.getCname() != "") {
				if (!clientService.repeatClientName(client.getCname())) {
					clientService.insert(client);
					j.setSuccess(true);
					j.setMsg("客户新增成功！");
					j.setObj(client);
				} else {
					j.setMsg("客户名称重复，请改用其它！");
				}
			} else {
				j.setMsg("客户名称不允许空！");
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 修改
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public Json editUser(@PathVariable Integer id, @RequestBody Client client) {
		Json j = new Json();
		boolean ok = false;
		try {
			if (client.getCname() != "") {
				if (!clientService.repeatClientName(client.getCname(), id)) {
					clientService.update(client);
					ok = true;
					j.setMsg("修改成功！");
					j.setObj(client);
				} else {
					j.setMsg("客户名称重复，请改用其它！");
				}
			} else {
				j.setMsg("客户名称不允许空！");
			}

		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	/**
	 * 删除
	 * 
	 * @param u
	 * @param out
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Json del(@PathVariable Integer id) {
		Json j = new Json();
		boolean ok = false;
		try {
			if (clientService.delete(id) > 0) {
				ok = true;
				j.setMsg("删除成功！");
			} else
				j.setMsg("删除失败，该客户已在单据使用");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	/**
	 * 列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String listPage(Model model) {
		return "client/list";
	}
}
