package com.xs.wms.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xs.wms.pojo.Delivery;
import com.xs.wms.pojo.Stock_in;
import com.xs.wms.pojo.Stock_in_detail;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.service.DeliveryService;
import com.xs.wms.service.OptionService;

@Controller
@RequestMapping("/deliverys")
public class DeliveryController {
	private static Logger logger = Logger.getLogger(DeliveryController.class);

	@Resource
	private DeliveryService deliveryService;

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String ListPage(Model model) {
		return "delivery/delivery";
	}

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	public Json add(HttpServletRequest request, @RequestBody Delivery obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			String code = obj.getCode();
			if (code != null & code != "") {
				obj.setCrDate(new Date());
				obj.setOp(((User) request.getSession().getAttribute("USER")).getId());
				obj.setFlag(0);
				deliveryService.add(obj);
				j.setMsg("新增成功！");
				ok = true;
			} else {
				j.setMsg("请输入订舱号！");
			}
		} catch (Exception e) {
			logger.error(e);
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public Json edit(HttpServletRequest request, @PathVariable Integer id, @RequestBody Delivery obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			//Delivery oObj = deliveryService.get(id);
			int flag = obj.getFlag();
			String code = obj.getCode();
			if (code != null & code != "") {
				if (flag == 0) {
					obj.setOp(((User) request.getSession().getAttribute("USER")).getId());
					deliveryService.update(obj);
					ok = true;
				} else
					j.setMsg("已出账单单据不允许修改！");
			} else
				j.setMsg("请输入订舱号！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}
	
	/**
	 * 删除
	 * 
	 * @param userId
	 * @param out
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Json delete(@PathVariable Integer id) {
		Json j = new Json();
		try {
			deliveryService.delete(id);
			j.setSuccess(true);
			j.setMsg("删除成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
