package com.xs.wms.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.util.IOUtils;

import com.mysql.jdbc.StringUtils;
import com.xs.wms.pojo.Delivery;
import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.Order_detail;
import com.xs.wms.pojo.Stock_in;
import com.xs.wms.pojo.Stock_in_detail;

public class ExcelUtils {
	/**
	 * excel导出工具
	 * 
	 * @author: bingye
	 * @createTime: 2015-4-29 下午06:52:17
	 * @param <E>
	 * @param response
	 * @param header
	 * @param fileNames
	 * @param list
	 *            void
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E> void exportExcel(HttpServletResponse response, String[] header, String[] fileNames, List<E> list,
			String sheetName, String fileName) throws NoSuchFieldException {
		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = wb.createSheet(sheetName);

		HSSFRow headerRow = sheet.createRow(0);
		HSSFRow contentRow = null;

		// 设置标题
		for (int i = 0; i < header.length; i++) {
			headerRow.createCell(i).setCellValue(header[i]);
		}
		try {
			for (int i = 0; i < list.size(); i++) {
				contentRow = sheet.createRow(i + 1);
				// 获取每一个对象
				E o = list.get(i);
				Class cls = o.getClass();

				for (int j = 0; j < fileNames.length; j++) {
					Object value = null;
					if (fileNames[j].contains("."))// 包含其它类
					{
						String subClsNm = fileNames[j].substring(0, fileNames[j].indexOf("."));
						String subField = fileNames[j].substring(fileNames[j].indexOf(".") + 1);
						String fieldName = subClsNm.substring(0, 1).toUpperCase() + subClsNm.substring(1);
						String subFieldNm = subField.substring(0, 1).toUpperCase() + subField.substring(1);
						Method getMethod = cls.getMethod("get" + fieldName);
						Object subObj = getMethod.invoke(o);
						Method subMethod = subObj.getClass().getMethod("get" + subFieldNm);
						value = subMethod.invoke(subObj);
					} else {
						String fieldName = fileNames[j].substring(0, 1).toUpperCase() + fileNames[j].substring(1);
						Method getMethod = cls.getMethod("get" + fieldName);
						value = getMethod.invoke(o);
					}
					if (value != null) {
						contentRow.createCell(j).setCellValue(value.toString());
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		OutputStream os = null;
		try {
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			os = response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static <E> void exportOrder(HttpServletRequest request, HttpServletResponse response, Order order,
			String sheetName, String fileName) throws NoSuchFieldException, IOException {

		String[] fieldHeaders = { "中文品名", "件数", "体积", "重量" };
		String[] fieldNames = { "cname", "num", "vol", "weight" };
		List<Order_detail> items = order.getOrder_details();

		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = wb.createSheet(sheetName);
		int currentRow = 0;
		HSSFRow titleRow = sheet.createRow(currentRow);
		HSSFRow codeRow = sheet.createRow(++currentRow);
		HSSFRow headerRow = sheet.createRow(++currentRow);
		HSSFRow contentRow = null;

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 13);
		cellStyle.setFont(font);

		HSSFCellStyle codeCellStyle = wb.createCellStyle();
		codeCellStyle.setWrapText(true);
		codeCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		codeCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		codeCellStyle.setFont(font);

		HSSFCellStyle gridCellStyle = wb.createCellStyle();
		gridCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		gridCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		gridCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		gridCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		gridCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		gridCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		gridCellStyle.setFont(font);

		CellRangeAddress region = new CellRangeAddress(0, 0, 1, fieldHeaders.length);
		sheet.addMergedRegion(region);
		HSSFCellStyle titleCellStyle = wb.createCellStyle();
		titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("宋体");
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setFontHeightInPoints((short) 22); // 将字体大小设置为18px
		titleCellStyle.setFont(titleFont);
		HSSFCell titleCell = titleRow.createCell(1);
		titleCell.setCellStyle(titleCellStyle);
		titleCell.setCellValue(order.getClient().getCname());

		region = new CellRangeAddress(1, 1, 1, fieldHeaders.length);
		sheet.addMergedRegion(region);
		HSSFCell codeCell = codeRow.createCell(1);
		codeCell.setCellStyle(codeCellStyle);
		codeCell.setCellValue("入仓单号：" + order.getCode());

		region = new CellRangeAddress(2, 2 + items.size(), fieldHeaders.length + 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		HSSFCell rCell = headerRow.createCell(fieldHeaders.length + 1);
		rCell.setCellStyle(cellStyle);
		rCell.setCellValue("★务必请司机送货前将此栏认真填写");

		// 列表数据
		for (int i = 0; i < fieldHeaders.length; i++) {
			HSSFCell headerCell = headerRow.createCell(i + 1);
			headerCell.setCellStyle(gridCellStyle);
			headerCell.setCellValue(fieldHeaders[i]);
			sheet.setColumnWidth(i + 1, 12 * 256);
		}
		sheet.setColumnWidth(fieldHeaders.length + 1, 5 * 12 * 256);
		for (int i = 0; i < items.size(); i++) {
			contentRow = sheet.createRow(++currentRow);
			// 获取每一个对象
			Order_detail o = items.get(i);
			HSSFCell cell1 = contentRow.createCell(1);
			HSSFCell cell2 = contentRow.createCell(2);
			HSSFCell cell3 = contentRow.createCell(3);
			HSSFCell cell4 = contentRow.createCell(4);
			cell1.setCellStyle(gridCellStyle);
			cell1.setCellValue(o.getCname());
			cell2.setCellStyle(gridCellStyle);
			cell2.setCellValue(o.getNum());
			cell3.setCellStyle(gridCellStyle);
			cell3.setCellValue(o.getVol());
			cell4.setCellStyle(gridCellStyle);
			cell4.setCellValue(o.getWeight());
		}

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		HSSFCell cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("●地址：广州市黄埔区中山大道乌冲路段新溪北加油站西侧信树物流仓储部");
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("●咨询路线： 黄先生 020-82396784   13929565484  查货情况：QQ1954859510");
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("              如仓库联系不上，请联系我司操作：");
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("●上班时间：早上8：30—下午18:00");
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		contentRow.setHeight((short) (16 * 25 * 2));
		cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("●司机需遵从本仓人员工作安排，到办公室开入场单，我司将收取10元/车次入场费，并以 收据编号先后顺序排队。");
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		contentRow.setHeight((short) (16 * 25 * 2));
		cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("●请核对清楚实际数量与入仓单数量是否一致，签收后所产生的一切损失我司不负责，并收取翻堆装卸费35元/CBM");
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("(温馨提示：为了不耽误你宝贵的卸货时间，送货前请提前一天预约)");
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("仓库路线图：↘");
		currentRow++;
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		String picPath = request.getSession().getServletContext().getRealPath("img/path.png");
		// BufferedImage bufferImg = ImageIO.read(new File(picPath));
		// ImageIO.write(bufferImg, "png", byteArrayOut);
		// // 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
		// HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// // anchor主要用于设置图片的属性
		// HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,
		// (short) 1, currentRow,
		// (short) fieldHeaders.length, currentRow + 25);
		// anchor.setAnchorType(3);
		// // 插入图片
		// patriarch.createPicture(anchor,
		// wb.addPicture(byteArrayOut.toByteArray(),
		// HSSFWorkbook.PICTURE_TYPE_JPEG));

		contentRow = sheet.createRow(currentRow + 30);
		region = new CellRangeAddress(currentRow + 30, currentRow + 30, 1, fieldHeaders.length + 1);
		sheet.addMergedRegion(region);
		contentRow.setHeight((short) (16 * 25 * 7));
		cell = contentRow.createCell(1);
		cell.setCellStyle(codeCellStyle);
		cell.setCellValue("***备注：1、请务必凭此进仓单进仓（请复印一份），否则仓库不收货。" + "\n2、此入仓单自放仓7天内有效，柜货5天免堆，散货无免堆期。"
				+ "\n3、仓库只接受普通货物入仓！易燃、易爆、有强污染力的危险品；液" + "\n体、粉末状等疑危险品；易碎品，国家相关法律禁运的货物，仓库拒绝收货！超长、超"
				+ "\n重、超大件货物入仓，请提前与我司相关操作人员联系！");

		InputStream is = new FileInputStream(picPath);
		byte[] bytes = IOUtils.toByteArray(is);
		int pictureIdx = wb.addPicture(bytes, wb.PICTURE_TYPE_PNG);
		CreationHelper helper = wb.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		// 图片插入坐标
		anchor.setCol1(1);
		anchor.setRow1(currentRow);
		// 插入图片
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();

		OutputStream os = null;
		try {
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			os = response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static <E> void exportBill(HttpServletResponse response, String[] header, String[] fileNames, List<E> list,
			String sheetName, String fileName, String title) throws NoSuchFieldException {
		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = wb.createSheet(sheetName);

		HSSFRow titleRow = sheet.createRow(0);
		HSSFRow headerRow = sheet.createRow(1);
		HSSFRow contentRow = null;
		HSSFRow bottomRow = null;

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 设置title
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, fileNames.length - 1); // 参数都是从O开始
		sheet.addMergedRegion(region);

		HSSFCellStyle topCellStyle = wb.createCellStyle();
		topCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		topCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font topFont = wb.createFont();
		topFont.setFontName("宋体");
		topFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		topFont.setFontHeightInPoints((short) 22); // 将字体大小设置为18px
		topCellStyle.setFont(topFont);

		HSSFCellStyle bottomCellStyle = wb.createCellStyle();
		bottomCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		bottomCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font bFont = wb.createFont();
		bFont.setFontName("宋体");
		bFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		bFont.setFontHeightInPoints((short) 16); // 将字体大小设置为18px
		bottomCellStyle.setFont(bFont);

		HSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(topCellStyle);
		titleCell.setCellValue(title);

		HSSFCellStyle headerCellStyle = wb.createCellStyle();
		headerCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font headerFont = wb.createFont();
		headerFont.setFontName("宋体");
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 14);
		headerCellStyle.setFont(headerFont);

		// 设置列标题
		for (int i = 0; i < header.length; i++) {
			HSSFCell headerCell = headerRow.createCell(i);
			headerCell.setCellStyle(headerCellStyle);
			headerCell.setCellValue(header[i]);
			sheet.setColumnWidth(i, 20 * 256);
		}
		try {
			// CellRangeAddress lastRowRegion = new CellRangeAddress(list.size()
			// + 1, list.size() + 1, 0,
			// fileNames.length - 2);
			// sheet.addMergedRegion(lastRowRegion);//最后汇总行
			for (int i = 0; i < list.size(); i++) {
				contentRow = sheet.createRow(i + 2);
				// 获取每一个对象
				E o = list.get(i);
				Class cls = o.getClass();

				for (int j = 0; j < fileNames.length; j++) {
					Object value = null;
					if (fileNames[j].contains("."))// 包含其它类
					{
						String subClsNm = fileNames[j].substring(0, fileNames[j].indexOf("."));
						String subField = fileNames[j].substring(fileNames[j].indexOf(".") + 1);
						String fieldName = subClsNm.substring(0, 1).toUpperCase() + subClsNm.substring(1);
						String subFieldNm = subField.substring(0, 1).toUpperCase() + subField.substring(1);
						Method getMethod = cls.getMethod("get" + fieldName);
						Object subObj = getMethod.invoke(o);
						if (subObj != null) {
							Method subMethod = subObj.getClass().getMethod("get" + subFieldNm);
							value = subMethod.invoke(subObj);
						}
					} else {
						String fieldName = fileNames[j].substring(0, 1).toUpperCase() + fileNames[j].substring(1);
						Method getMethod = cls.getMethod("get" + fieldName);
						value = getMethod.invoke(o);
					}
					if (value != null) {
						HSSFCell cell = contentRow.createCell(j);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(new HSSFRichTextString(value.toString()));
					}
				}
				int bottomRowNum = list.size() + 2;
				CellRangeAddress bottomRegion = new CellRangeAddress(bottomRowNum, bottomRowNum, 0,
						fileNames.length - 1); // 参数都是从O开始
				sheet.addMergedRegion(bottomRegion);
				bottomRow = sheet.createRow(bottomRowNum);
				HSSFCell bottomCell = bottomRow.createCell(0);
				bottomCell.setCellStyle(topCellStyle);
				bottomCell.setCellValue("广州信树物流有限公司：罗生");

				bottomRow = sheet.createRow(++bottomRowNum);
				bottomCell = bottomRow.createCell(0);
				bottomCell.setCellStyle(bottomCellStyle);
				bottomCell.setCellValue("请把费用汇到以下银行：");

				bottomRow = sheet.createRow(++bottomRowNum);
				bottomCell = bottomRow.createCell(0);
				bottomCell.setCellStyle(bottomCellStyle);
				bottomCell.setCellValue("账号：6222083602004377336");

				bottomRow = sheet.createRow(++bottomRowNum);
				bottomCell = bottomRow.createCell(0);
				bottomCell.setCellStyle(bottomCellStyle);
				bottomCell.setCellValue("户名：罗树潮");

				bottomRow = sheet.createRow(++bottomRowNum);
				bottomCell = bottomRow.createCell(0);
				bottomCell.setCellStyle(bottomCellStyle);
				bottomCell.setCellValue("中国工商银行广州经济技术开发区支行");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		OutputStream os = null;
		try {
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			os = response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static <E> void exportStockInBill(HttpServletRequest request, HttpServletResponse response,
			Stock_in stock_in, String sheetName, String fileName) throws Exception {
		String path = request.getServletContext().getRealPath("exlmodel/stockin.xls");
		InputStream in = new FileInputStream(new File(path));
		HSSFWorkbook wb = new HSSFWorkbook(in);
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = sheet.getRow(2);
		HSSFCell cell = row.getCell(9);
		cell.setCellValue(stock_in.getCode());
		row = sheet.getRow(3);
		cell = row.getCell(2);
		cell.setCellValue(stock_in.getInDate().getYear() + 1900 + " 年");
		cell = row.getCell(3);
		cell.setCellValue(stock_in.getInDate().getMonth() + 1);
		cell = row.getCell(5);
		cell.setCellValue(stock_in.getInDate().getDate());
		cell = row.getCell(8);
		cell.setCellValue(stock_in.getInDate().getHours());
		cell = row.getCell(10);
		cell.setCellValue(stock_in.getInDate().getMinutes());

		row = sheet.getRow(5);
		cell = row.getCell(3);
		cell.setCellValue(stock_in.getOrderCode());
		cell = row.getCell(9);
		if (!StringUtils.isNullOrEmpty(stock_in.getClient().getCode()))
			cell.setCellValue(stock_in.getClient().getCode());
		else
			cell.setCellValue(stock_in.getClient().getCname());
		row = sheet.getRow(6);
		cell = row.getCell(5);
		cell.setCellValue(stock_in.getCarNo());

		row = sheet.getRow(24);
		cell = row.getCell(9);
		cell.setCellValue(stock_in.getCode());

		row = sheet.getRow(25);
		cell = row.getCell(2);
		cell.setCellValue(stock_in.getInDate().getYear() + 1900 + " 年");
		cell = row.getCell(3);
		cell.setCellValue(stock_in.getInDate().getMonth() + 1);
		cell = row.getCell(5);
		cell.setCellValue(stock_in.getInDate().getDate());
		cell = row.getCell(8);
		cell.setCellValue(stock_in.getInDate().getHours());
		cell = row.getCell(10);
		cell.setCellValue(stock_in.getInDate().getMinutes());

		row = sheet.getRow(27);
		cell = row.getCell(3);
		cell.setCellValue(stock_in.getOrderCode());
		cell = row.getCell(8);
		if (!StringUtils.isNullOrEmpty(stock_in.getClient().getCode()))
			cell.setCellValue(stock_in.getClient().getCode());
		else
			cell.setCellValue(stock_in.getClient().getCname());

		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 14); // 将字体大小设置为18px

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
		int r = 27;
		CellRangeAddress region;
		List<Stock_in_detail> items = stock_in.getItems();
		for (int i = 0; i < items.size(); i++) {
			r++;
			row = sheet.createRow(r);
			region = new CellRangeAddress(r, r, 1, 2);
			sheet.addMergedRegion(region);
			region = new CellRangeAddress(r, r, 3, 6);
			sheet.addMergedRegion(region);
			region = new CellRangeAddress(r, r, 8, 9);
			sheet.addMergedRegion(region);
			region = new CellRangeAddress(r, r, 10, 11);
			sheet.addMergedRegion(region);

			cell = row.createCell(1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("品 名：");
			cell = row.createCell(2);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(3);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(items.get(i).getCname());
			cell = row.createCell(4);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(5);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(6);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(7);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("件 数：");
			cell = row.createCell(8);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(items.get(i).getNum());
			cell = row.createCell(9);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(10);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(items.get(i).getYard());
			cell = row.createCell(11);
			cell.setCellStyle(cellStyle);
		}
		OutputStream os = null;
		try {
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xls");
			os = response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static <E> void exportDeliveryBill(HttpServletRequest request, HttpServletResponse response,
			Delivery delivery, String sheetName, String fileName) throws NoSuchFieldException, IOException {

		String path = request.getServletContext().getRealPath("exlmodel/delivery1.xls");
		InputStream in = new FileInputStream(new File(path));
		HSSFWorkbook wb = new HSSFWorkbook(in);
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = sheet.getRow(4);
		HSSFCell cell = row.getCell(1);
		cell.setCellValue(delivery.getCarNo());
		cell = row.getCell(4);
		cell.setCellValue(delivery.getDriver());
		cell = row.getCell(6);
		cell.setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
		row = sheet.getRow(6);
		cell = row.getCell(1);
		cell.setCellValue(delivery.getCode());
		cell = row.getCell(4);
		cell.setCellValue(delivery.getDport());
		String caseModel = delivery.getCaseModel();
		if (caseModel.equals("20GP")) {
			cell = row.getCell(6);
			cell.setCellValue("√");
		}
		cell = row.getCell(7);
		cell.setCellValue(delivery.getWeigh() + "过磅");

		row = sheet.getRow(7);
		cell = row.getCell(1);
		cell.setCellValue(delivery.getGoodsName());
		cell = row.getCell(4);
		cell.setCellValue(delivery.getRport());
		if (caseModel.equals("40GP")) {
			cell = row.getCell(6);
			cell.setCellValue("√");
		}

		row = sheet.getRow(8);
		cell = row.getCell(1);
		cell.setCellValue(delivery.getConsignee());
		if (caseModel.equals("40HQ")) {
			cell = row.getCell(6);
			cell.setCellValue("√");
		}

		row = sheet.getRow(9);
		cell = row.getCell(1);
		cell.setCellValue(delivery.getCaseNo());
		cell = row.getCell(5);
		cell.setCellValue(delivery.getSealNo());

		row = sheet.getRow(11);
		cell = row.getCell(1);
		cell.setCellValue(delivery.getAddress());
		cell = row.getCell(6);
		cell.setCellValue(delivery.getContact());

		row = sheet.getRow(14);
		cell = row.getCell(1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			cell.setCellValue(format.format(format.parse(delivery.getDdate())));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		row = sheet.getRow(16);
		cell = row.getCell(0);
		cell.setCellValue(delivery.getMemo());

		row = sheet.getRow(20);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getAttention());

		OutputStream os = null;
		try {
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xls");
			os = response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static <E> void exportDeliveryBill2(HttpServletRequest request, HttpServletResponse response,
			Delivery delivery, String sheetName, String fileName)
					throws NoSuchFieldException, IOException, ParseException {

		// List<Stock_in_detail> items = delivery.getItems();

		String path = request.getServletContext().getRealPath("exlmodel/delivery2.xls");
		InputStream in = new FileInputStream(new File(path));
		HSSFWorkbook wb = new HSSFWorkbook(in);
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = sheet.getRow(3);
		HSSFCell cell = row.getCell(0);
		cell.setCellValue("日期：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		cell = row.getCell(5);
		cell.setCellValue("过磅：" + delivery.getWeigh());
		row = sheet.getRow(5);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getClientCode());
		cell = row.getCell(5);
		cell.setCellValue("黄埔");
		row = sheet.getRow(6);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getConsignee());
		row = sheet.getRow(7);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getGoodsName());
		cell = row.getCell(5);
		cell.setCellValue(delivery.getCaseModel());
		row = sheet.getRow(8);
		cell = row.getCell(2);
		Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(delivery.getDdate());
		cell.setCellValue((d.getYear() + 1900) + " 年 " + (d.getMonth() + 1) + " 月 " + d.getDate() + " 日 " + d.getHours()
				+ " 时 到厂 ");
		row = sheet.getRow(10);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getCode());
		cell = row.getCell(5);
		cell.setCellValue(delivery.getCarNo());

		row = sheet.getRow(11);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getCaseNo());
		cell = row.getCell(5);
		cell.setCellValue(delivery.getSealNo());

		row = sheet.getRow(12);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getDriver());
		cell = row.getCell(5);
		cell.setCellValue(delivery.getDriverPhone());

		row = sheet.getRow(13);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getDport());
		cell = row.getCell(5);
		cell.setCellValue(delivery.getRport());

		row = sheet.getRow(14);
		cell = row.getCell(2);
		cell.setCellValue(delivery.getMemo());
		OutputStream os = null;
		try {
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xls");

			os = response.getOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
