package com.xs.wms.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xs.wms.common.ExcelUtils;
import com.xs.wms.pojo.Bill;
import com.xs.wms.pojo.Delivery;
import com.xs.wms.pojo.Fee;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;
import com.xs.wms.service.DeliveryService;
import com.xs.wms.service.FeeService;

@Controller
@RequestMapping("/fees")
public class FeeController {
	@Resource
	private FeeService feeService;
	@Resource
	private DeliveryService deliveryService;

	@ResponseBody
	@RequestMapping(value = "/datagrid", method = RequestMethod.GET)
	public DataGrid datagrid(PageHelper page, String client, String key, String sdate, String edate) {
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
			// logger.error(e);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "/bill", method = RequestMethod.POST)
	public Json burnBill(HttpServletRequest request, @RequestBody String ids) {
		Json j = new Json();
		boolean ok = false;
		try {
			if (ids != "") {
				int rtn = feeService.burnBill(ids);
				if (rtn == 0) {
					ok = true;
					j.setMsg("生成成功！");
					// j.setObj(obj);
				} else
					j.setMsg("生成失败！");
			} else
				j.setMsg("请选择费用明细！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Json addClient(HttpServletRequest request, @RequestBody Fee obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			if (obj.getBillId() > 0 & obj.getClientId() > 0 & obj.getFname() != "" & obj.getSdate() != null
					& obj.getEdate() != null) {
				if (!feeService.repeat(obj)) {
					obj.setCrDate(new Date());
					obj.setOp(((User) request.getSession().getAttribute("USER")).getId());
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
	public Json editUser(HttpServletRequest request, @PathVariable Integer id, @RequestBody Fee obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			Fee oObj = feeService.get(id);
			if (obj.getBillId() > 0 & obj.getClientId() > 0 & obj.getFname() != "" & obj.getSdate() != null
					& obj.getEdate() != null) {
				if (oObj.getFlag() == 0) {
					obj.setOp(((User) request.getSession().getAttribute("USER")).getId());
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
	public String editPage(@PathVariable Integer ftype, @PathVariable Integer id, Model model) {
		model.addAttribute("ftype", ftype);
		model.addAttribute("fee_id", id);
		return "fee/fee";
	}

	/**
	 * 已出账单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bills/list", method = RequestMethod.GET)
	public String billPage(Model model) {
		return "bill/list";
	}

	/**
	 * 已出账单费用明细查询页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bills/check_details", method = RequestMethod.GET)
	public String check_billPage(Model model) {
		model.addAttribute("tag", "check");
		model.addAttribute("f_flag", 1);
		model.addAttribute("delivery_flag", 1);
		model.addAttribute("stock_in_flag", 3);
		return "bill/details";
	}

	/**
	 * 未出账单费用明细查询页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bills/uncheck_details", method = RequestMethod.GET)
	public String uncheck_billPage(Model model) {
		model.addAttribute("tag", "uncheck");
		model.addAttribute("f_flag", 0);
		model.addAttribute("delivery_flag", 0);
		model.addAttribute("stock_in_flag", 2);
		return "bill/details";
	}

	@ResponseBody
	@RequestMapping(value = "/bills", method = RequestMethod.GET)
	public DataGrid bill_datagrid(PageHelper page, String client, String sdate, String edate) {
		DataGrid dg = new DataGrid();
		dg.setTotal(feeService.getBillTotal(client, sdate, edate));
		List<Bill> list = feeService.datagridBill(page, client, sdate, edate);
		dg.setRows(list);
		return dg;
	}

	@ResponseBody
	@RequestMapping(value = "/bills/delivery", method = RequestMethod.GET)
	public DataGrid delivery_datagrid(PageHelper page, String fflag, String bflag, String client, String key,
			String sdate, String edate) {
		DataGrid dg = new DataGrid();
		dg.setTotal(feeService.getDeliveryBillTotal(fflag, bflag, client, key, sdate, edate));
		List<Fee> list = feeService.datagridDeliveryBill(page, fflag, bflag, client, key, sdate, edate);
		dg.setRows(list);
		return dg;
	}

	@ResponseBody
	@RequestMapping(value = "/bills/stock_in", method = RequestMethod.GET)
	public DataGrid stockin_datagrid(PageHelper page, String fflag, String bflag, String client, String key,
			String sdate, String edate) {
		DataGrid dg = new DataGrid();
		dg.setTotal(feeService.getStockInBillTotal(fflag, bflag, client, key, sdate, edate));
		List<Fee> list = feeService.datagridStockInBill(page, fflag, bflag, client, key, sdate, edate);
		dg.setRows(list);
		return dg;
	}

	@RequestMapping(value = "/bills/report")
	public void ExportDeliveryBill(HttpServletRequest request, HttpServletResponse response) {
		try {
			PageHelper page = new PageHelper();
			List<Fee> fees = feeService.datagridDeliveryBill(page, "1", "1", "1", "", "2011-01-01", "2016-03-01");
			List<Fee> list = new ArrayList<Fee>();
			Map<Integer, Fee> map = new HashMap<Integer, Fee>();
			double total = 0;
			for (int i = 0; i < fees.size(); i++) {
				Fee obj = fees.get(i);
				Integer billId = obj.getBillId();
				total += obj.getAmount();
				if (map.containsKey(billId)) {
					Fee nObj = map.get(billId);
					String fname = nObj.getFname() + "\n" + obj.getFname() + ":" + obj.getAmount().toString();
					double amount = nObj.getAmount() + obj.getAmount();
					nObj.setFname(fname);
					nObj.setAmount(amount);
				} else {
					map.put(billId, obj);
					String fname = obj.getFname() + ":" + obj.getAmount().toString();
					double amount = obj.getAmount();
					list.add(obj);
					Fee nObj = list.get(list.size() - 1);
					nObj.setFname(fname);
					nObj.setAmount(amount);
				}
			}
			Fee bt = new Fee();
			bt.setFname("总费用：");
			bt.setAmount(total);
			list.add(bt);
			String[] header = { "单号", "箱型", "提柜点", "还柜点", "箱号", "费目", "金额" };
			String[] fileNames = { "delivery.code", "delivery.caseModel", "delivery.dport", "delivery.rport",
					"delivery.caseNo", "fname", "amount" };
			ExcelUtils.exportBill(response, header, fileNames, list, "exportBill", "exportBill",
					fees.get(0).getClient().getCname());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
