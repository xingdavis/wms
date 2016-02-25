package com.xs.wms.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xs.wms.pojo.Delivery;
import com.xs.wms.pojo.Fee;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;
import com.xs.wms.service.FeeService;

@Controller
@RequestMapping("/fees")
public class FeeController {
	@Resource
	private FeeService feeService;

	@ResponseBody
	@RequestMapping(value = "/datagrid", method = RequestMethod.GET)
	public DataGrid datagrid(PageHelper page, String client, String key,
			String sdate, String edate) {
		DataGrid dg = new DataGrid();
		dg.setTotal(feeService.getDatagridTotal(client, key, sdate, edate));
		List<Fee> list = feeService.datagrid(page, client, key, sdate, edate);
		dg.setRows(list);
		return dg;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Json get(@PathVariable Integer id) {
		Json j = new Json();
		try {
			Fee obj = this.feeService.get(id);
			j.setSuccess(true);
			j.setObj(obj);
		} catch (Exception e) {
			//logger.error(e);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Json addClient(HttpServletRequest request, @RequestBody Fee obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			if (obj.getBillId() > 0 & obj.getClientId() > 0
					& obj.getFname() != "" & obj.getSdate() != null
					& obj.getEdate() != null) {
				if (!feeService.repeat(obj)) {
					obj.setCrDate(new Date());
					obj.setOp(((User) request.getSession().getAttribute("USER"))
							.getId());
					if (feeService.insert(obj) > 0) {
						ok = true;
						j.setMsg("新增成功！");
						j.setObj(obj);
					}
				} else {
					j.setMsg("费用名称重复，请改用其它！");
				}
			} else {
				j.setMsg("单号不允许空！客户不允许空！费用名称不允许空！日期不允许空！");
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
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
	public Json editUser(@PathVariable Integer id, @RequestBody Fee obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			Fee oObj = feeService.get(id);
			if (obj.getBillId() > 0 & obj.getClientId() > 0
					& obj.getFname() != "" & obj.getSdate() != null
					& obj.getEdate() != null) {
				if (oObj.getFlag() == 0) {
					if (feeService.update(obj) > 0) {
						ok = true;
						j.setMsg("修改成功！");
						j.setObj(obj);
					}
				} else {
					j.setMsg("已出账单的费用不允许修改！");
				}
			} else {
				j.setMsg("单号不允许空！客户不允许空！费用名称不允许空！日期不允许空！");
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
			Fee oObj = feeService.get(id);
			if (oObj.getFlag() == 0) {
				if (feeService.delete(id) > 0) {
					ok = true;
					j.setMsg("删除成功！");
				} else
					j.setMsg("删除失败！");
			} else
				j.setMsg("已出账单的费用不允许删除！");
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
		return "fee/list";
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/page/{ftype}", method = RequestMethod.GET)
	public String addPage(@PathVariable Integer ftype, Model model) {
		model.addAttribute("ftype", ftype);
		return "fee/fee";
	}

	@RequestMapping(value = "/page/{ftype}/{id}", method = RequestMethod.GET)
	public String editPage(@PathVariable Integer ftype,
			@PathVariable Integer id, Model model) {
		model.addAttribute("ftype", ftype);
		model.addAttribute("fee_id", id);
		return "fee/fee";
	}
}
