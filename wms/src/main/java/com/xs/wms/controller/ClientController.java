package com.xs.wms.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xs.wms.pojo.Client;

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

	@ResponseBody
	@RequestMapping(value="",method=RequestMethod.POST)
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
}
