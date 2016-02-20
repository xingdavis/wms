package com.xs.wms.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xs.wms.service.OptionService;

@Controller
@RequestMapping("/deliverys")
public class DeliveryController {
	@Resource
	private OptionService optionService;

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String ListPage(Model model) {
		return "delivery/delivery";
	}
}
