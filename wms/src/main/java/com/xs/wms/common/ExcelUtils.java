package com.xs.wms.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public static <E> void exportStockInBill(HttpServletRequest request, HttpServletResponse response,
			Stock_in stock_in, String sheetName, String fileName) throws NoSuchFieldException, IOException {

		List<Stock_in_detail> items = stock_in.getItems();

		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = wb.createSheet(sheetName);
		int currentRow = 0;
		HSSFRow contentRow = null;

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 16);
		cellStyle.setFont(font);

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setWrapText(true);
		cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font1 = wb.createFont();
		font1.setFontName("宋体");
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font1.setFontHeightInPoints((short) 11);
		cellStyle1.setFont(font1);

		HSSFCellStyle cellStyle2 = wb.createCellStyle();
		cellStyle2.setWrapText(true);
		// cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font2 = wb.createFont();
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short) 11);
		cellStyle2.setFont(font2);

		HSSFCellStyle cellStyle3 = wb.createCellStyle();
		// cellStyle3.setWrapText(true);
		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font3 = wb.createFont();
		font3.setFontName("宋体");
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font3.setFontHeightInPoints((short) 12);
		cellStyle3.setFont(font3);

		HSSFCellStyle cellStyle4 = wb.createCellStyle();
		cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font4 = wb.createFont();
		font4.setFontName("宋体");
		font4.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font4.setFontHeightInPoints((short) 11);
		cellStyle4.setFont(font4);

		HSSFCellStyle cellStyle5 = wb.createCellStyle();
		cellStyle5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		Font font5 = wb.createFont();
		font5.setFontName("宋体");
		font5.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font5.setFontHeightInPoints((short) 11);
		cellStyle5.setFont(font5);

		HSSFCellStyle cellStyle6 = wb.createCellStyle();
		cellStyle6.setWrapText(true);
		cellStyle6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font6 = wb.createFont();
		font6.setFontName("宋体");
		font6.setFontHeightInPoints((short) 11);
		cellStyle6.setFont(font6);

		contentRow = sheet.createRow(++currentRow);
		CellRangeAddress region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		HSSFCell cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("收 款 收 据");

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle1);
		cell.setCellValue("编号：");

		region = new CellRangeAddress(currentRow, currentRow, 9, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(stock_in.getCode());
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(1, 3 * 3 * 256);
		cell.setCellValue("日期：");
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle2);
		// Calendar now = Calendar.getInstance();
		cell.setCellValue(stock_in.getInDate().getYear() + 1900 + " 年");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(3, 2 * 3 * 256);
		cell.setCellValue(stock_in.getInDate().getMonth() + 1);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(4, 3 * 256);
		cell.setCellValue("月");
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(5, 2 * 3 * 256);
		cell.setCellValue(stock_in.getInDate().getDate());
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(6, 3 * 256);
		cell.setCellValue("日");
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("时间：");
		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue(stock_in.getInDate().getHours());
		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(9, 3 * 256);
		cell.setCellValue("时");
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue(stock_in.getInDate().getMinutes());
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("分");

		currentRow++;
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle3);
		cell.setCellValue("入仓单号：");
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle3);

		region = new CellRangeAddress(currentRow, currentRow, 3, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle3);
		cell.setCellValue(stock_in.getOrderCode());
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle3);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle3);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle3);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle3);

		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle3);
		sheet.setColumnWidth(8, 5 * 3 * 256);
		cell.setCellValue("所属公司：");

		region = new CellRangeAddress(currentRow, currentRow, 9, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle3);
		// sheet.setColumnWidth(9, 12*3 * 256);
		cell.setCellValue(stock_in.getClient().getCname());
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle3);
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle3);

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue("今  收  到");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue("车号：");
		region = new CellRangeAddress(currentRow, currentRow, 5, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(stock_in.getCarNo());
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);
		region = new CellRangeAddress(currentRow, currentRow, 8, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue("交来入仓费合计人民币   元整。");

		currentRow++;
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue("金额（大写）：");

		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue("¥");
		region = new CellRangeAddress(currentRow, currentRow, 10, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle5);

		currentRow++;
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("收款单位（盖章）");
		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("经手人：");
		region = new CellRangeAddress(currentRow, currentRow, 9, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle5);

		currentRow++;
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 3);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue("司机注意事项：");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("1.司机需遵从本仓人员工作安排，并以此收据编号先后顺序排队。");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("2.仓库范围内禁止吸烟以及一切明火行为，并需注意安全，一切必须听从仓库人员安排，");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("  如有违反或威胁到安全的情况，必究。");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("3.请核对清楚实际数量与入仓单数量是否一致，签收后所产生的一切损失我司不负责，");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("  并收取翻堆装卸费35元/CBM");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 12);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("4.货物一经司机签收，若需提货必须得到我司客人文件通知出仓方能提取并需收取相应装卸费35元/CBM。");

		currentRow++;
		// region = new CellRangeAddress(currentRow, currentRow, 12, 12);
		// sheet.addMergedRegion(region);
		// cell = contentRow.createCell(12);
		// cell.setCellStyle(cellStyle);
		// cell.setCellValue("①存根（白色） ②客户（红色） ③仓库留底（黄色）");

		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		HSSFClientAnchor bigValueAnchorTextBox = new HSSFClientAnchor(0, 0, 0, 0, (short) 12, currentRow, (short) 12,
				currentRow);
		HSSFTextbox bigValueTextbox = patriarch.createTextbox(bigValueAnchorTextBox);
		bigValueTextbox.setString(new HSSFRichTextString("①存根（白色） ②客户（红色） ③仓库留底（黄色）"));
		bigValueTextbox.setLineStyle(HSSFSimpleShape.LINESTYLE_NONE);
		bigValueTextbox.setFillColor(0, 0, 0);

		currentRow++;
		currentRow++;
		currentRow++;
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("入 仓 单");

		currentRow++;
		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle1);
		cell.setCellValue("编号：");

		region = new CellRangeAddress(currentRow, currentRow, 9, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(stock_in.getCode());
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(1, 3 * 3 * 256);
		cell.setCellValue("日期：");
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle2);
		// Calendar now = Calendar.getInstance();
		cell.setCellValue(stock_in.getInDate().getYear() + 1900 + " 年");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(3, 2 * 3 * 256);
		cell.setCellValue(stock_in.getInDate().getMonth() + 1);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(4, 3 * 256);
		cell.setCellValue("月");
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(5, 2 * 3 * 256);
		cell.setCellValue(stock_in.getInDate().getDate());
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(6, 3 * 256);
		cell.setCellValue("日");
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("时间：");
		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue(stock_in.getInDate().getHours());
		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle2);
		sheet.setColumnWidth(9, 3 * 256);
		cell.setCellValue("时");
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue(stock_in.getInDate().getMinutes());
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("分");

		currentRow++;
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle3);
		cell.setCellValue("入仓单号：");
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle3);

		region = new CellRangeAddress(currentRow, currentRow, 3, 6);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle3);
		cell.setCellValue(stock_in.getOrderCode());
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle3);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle3);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle3);

		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle3);
		// sheet.setColumnWidth(8, 5*3 * 256);
		cell.setCellValue("所属公司：");

		region = new CellRangeAddress(currentRow, currentRow, 8, 9);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle3);
		// sheet.setColumnWidth(9, 12*3 * 256);
		cell.setCellValue(stock_in.getClient().getCname());
		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle3);
		region = new CellRangeAddress(currentRow, currentRow, 10, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle3);
		// sheet.setColumnWidth(8, 5*3 * 256);
		cell.setCellValue("堆位");
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle3);

		// List<Stock_in_detail> items = stock_in.getItems();
		for (int i = 0; i < items.size(); i++) {
			currentRow++;
			contentRow = sheet.createRow(currentRow);
			region = new CellRangeAddress(currentRow, currentRow, 1, 2);
			sheet.addMergedRegion(region);
			cell = contentRow.createCell(1);
			cell.setCellStyle(cellStyle3);
			cell.setCellValue("品 名：");
			cell = contentRow.createCell(2);
			cell.setCellStyle(cellStyle3);
			region = new CellRangeAddress(currentRow, currentRow, 3, 6);
			sheet.addMergedRegion(region);
			cell = contentRow.createCell(3);
			cell.setCellStyle(cellStyle3);
			cell.setCellValue(items.get(i).getCname());
			cell = contentRow.createCell(4);
			cell.setCellStyle(cellStyle3);
			cell = contentRow.createCell(5);
			cell.setCellStyle(cellStyle3);
			cell = contentRow.createCell(6);
			cell.setCellStyle(cellStyle3);
			cell = contentRow.createCell(7);
			cell.setCellStyle(cellStyle3);
			cell.setCellValue("件 数：");
			region = new CellRangeAddress(currentRow, currentRow, 8, 9);
			sheet.addMergedRegion(region);
			cell = contentRow.createCell(8);
			cell.setCellStyle(cellStyle3);
			cell.setCellValue(items.get(i).getNum());
			cell = contentRow.createCell(9);
			cell.setCellStyle(cellStyle3);
			// sheet.setColumnWidth(8, 5*3 * 256);
			region = new CellRangeAddress(currentRow, currentRow, 10, 11);
			sheet.addMergedRegion(region);
			cell = contentRow.createCell(10);
			cell.setCellStyle(cellStyle3);
			cell.setCellValue(items.get(i).getYard());
			cell = contentRow.createCell(11);
			cell.setCellStyle(cellStyle3);

		}

		currentRow++;
		currentRow++;
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		contentRow = sheet.createRow(currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("盖章无效");
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle2);
		cell = contentRow.createCell(8);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("仓管签名：");
		region = new CellRangeAddress(currentRow, currentRow, 9, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(9);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(10);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(11);
		cell.setCellStyle(cellStyle5);

		currentRow++;
		contentRow = sheet.createRow(currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue("注意事项：（此单不作提货用）");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("1.本单显示数量为包装箱数量，不负责清点箱内明细。");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("2.本入仓单不作提货用，如需提货，请与委托入仓公司（所属客户）联系。");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 1, 11);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("3.请核对清楚实际数量与入仓单数量是否一致，签收后所产生的一切责任与损失我司不负责。");

		OutputStream os = null;
		try {
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
			response.reset();
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
	public static <E> void exportDeliveryBill(HttpServletRequest request, HttpServletResponse response,
			Delivery delivery, String sheetName, String fileName) throws NoSuchFieldException, IOException {

		// List<Stock_in_detail> items = delivery.getItems();

		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = wb.createSheet(sheetName);
		int currentRow = 0;
		HSSFRow contentRow = null;

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		Font font = wb.createFont();
		font.setFontName("宋体");
		// font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 20);
		cellStyle.setFont(font);

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setWrapText(true);
		cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font1 = wb.createFont();
		font1.setFontName("宋体");
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font1.setFontHeightInPoints((short) 20);
		cellStyle1.setFont(font1);

		HSSFCellStyle cellStyle2 = wb.createCellStyle();
		cellStyle2.setWrapText(true);
		cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font2 = wb.createFont();
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short) 11);
		cellStyle2.setFont(font2);

		HSSFCellStyle cellStyle3 = wb.createCellStyle();
		cellStyle3.setWrapText(true);
		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font3 = wb.createFont();
		font3.setFontName("宋体");
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font3.setFontHeightInPoints((short) 14);
		cellStyle3.setFont(font3);

		HSSFCellStyle cellStyle4 = wb.createCellStyle();
		cellStyle4.setWrapText(true);
		cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		Font font4 = wb.createFont();
		font4.setFontName("宋体");
		font4.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font4.setFontHeightInPoints((short) 14);
		cellStyle4.setFont(font4);

		HSSFCellStyle cellStyle5 = wb.createCellStyle();
		cellStyle5.setWrapText(true);
		cellStyle5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle5.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle5.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle5.setBorderRight(HSSFCellStyle.BORDER_THIN);
		Font font5 = wb.createFont();
		font5.setFontName("宋体");
		font5.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font5.setFontHeightInPoints((short) 14);
		cellStyle5.setFont(font5);

		HSSFCellStyle cellStyle6 = wb.createCellStyle();
		cellStyle6.setWrapText(true);
		cellStyle6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		Font font6 = wb.createFont();
		font6.setFontName("宋体");
		font6.setFontHeightInPoints((short) 11);
		cellStyle6.setFont(font6);

		contentRow = sheet.createRow(++currentRow);
		CellRangeAddress region = new CellRangeAddress(currentRow, currentRow, 0, 7);
		sheet.addMergedRegion(region);
		HSSFCell cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("广 州 信 树 物 流 有 限 公 司");
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle);

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 0, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("电话：82396784     传真：82390130");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 0, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle1);
		cell.setCellValue("货 运 单");
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle1);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle1);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle1);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle1);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle1);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle1);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle1);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle3);
		cell.setCellValue("车 号：");
		sheet.setColumnWidth(0, 4 * 3 * 256);
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue(delivery.getCarNo());
		sheet.setColumnWidth(1, 6 * 3 * 256);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle4);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle3);
		cell.setCellValue("司 机：");
		sheet.setColumnWidth(3, 4 * 3 * 256);
		region = new CellRangeAddress(currentRow, currentRow, 4, 5);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle4);
		cell.setCellValue(delivery.getDriver());
		sheet.setColumnWidth(4, 5 * 3 * 256);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle4);
		region = new CellRangeAddress(currentRow, currentRow, 6, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle3);
		cell.setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
		sheet.setColumnWidth(6, 6 * 3 * 256);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle3);

		currentRow++;
		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("单 号");
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getCode());
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("提柜点");
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getDport());
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("柜型");
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getCaseModel());
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);
		region = new CellRangeAddress(currentRow, currentRow + 2, 7, 7);
		sheet.addMergedRegion(region);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("过磅" + delivery.getWeigh());

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("货 名");
		region = new CellRangeAddress(currentRow, currentRow, 1, 2);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getGoodsName());
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("还柜点");
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getRport());
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("收货人");
		region = new CellRangeAddress(currentRow, currentRow, 1, 4);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getConsignee());
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("柜 号");
		region = new CellRangeAddress(currentRow, currentRow, 1, 3);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getCaseNo());
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("封 号");
		region = new CellRangeAddress(currentRow, currentRow, 5, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getSealNo());
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("装 货   地 址");
		region = new CellRangeAddress(currentRow, currentRow, 1, 4);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getAddress());
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("联 系 方 式");
		region = new CellRangeAddress(currentRow, currentRow, 6, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getContact());
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("到厂时间");
		region = new CellRangeAddress(currentRow, currentRow, 1, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow + 3, 0, 0);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("备 注");
		region = new CellRangeAddress(currentRow, currentRow + 3, 1, 4);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getMemo());
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		region = new CellRangeAddress(currentRow, currentRow, 5, 6);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("客户签章");
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(7);
		cell.setCellStyle(cellStyle5);

		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle6);
		cell.setCellValue("日期：");
		region = new CellRangeAddress(currentRow, currentRow, 6, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle6);
		cell.setCellValue("    年     月    日");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 0, 1);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("注意事项：");
		region = new CellRangeAddress(currentRow, currentRow, 2, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue(delivery.getAttention());

		OutputStream os = null;
		try {
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
			response.reset();
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
	public static <E> void exportDeliveryBill2(HttpServletRequest request, HttpServletResponse response,
			Delivery delivery, String sheetName, String fileName)
					throws NoSuchFieldException, IOException, ParseException {

		// List<Stock_in_detail> items = delivery.getItems();

		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = wb.createSheet(sheetName);
		int currentRow = 0;
		HSSFRow contentRow = null;

		HSSFCellStyle cellStyle = wb.createCellStyle();
		//cellStyle.setWrapText(true);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 20);
		cellStyle.setFont(font);

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		//cellStyle1.setWrapText(true);
		cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font1 = wb.createFont();
		font1.setFontName("宋体");
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font1.setFontHeightInPoints((short) 20);
		cellStyle1.setFont(font1);

		HSSFCellStyle cellStyle2 = wb.createCellStyle();
		//cellStyle2.setWrapText(true);
		cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font2 = wb.createFont();
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short) 12);
		cellStyle2.setFont(font2);

		HSSFCellStyle cellStyle3 = wb.createCellStyle();
		//cellStyle3.setWrapText(true);
		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font font3 = wb.createFont();
		font3.setFontName("宋体");
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font3.setFontHeightInPoints((short) 12);
		cellStyle3.setFont(font3);

		HSSFCellStyle cellStyle4 = wb.createCellStyle();
		//cellStyle4.setWrapText(true);
		cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		Font font4 = wb.createFont();
		font4.setFontName("宋体");
		font4.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font4.setFontHeightInPoints((short) 14);
		cellStyle4.setFont(font4);

		HSSFCellStyle cellStyle5 = wb.createCellStyle();
		//cellStyle5.setWrapText(true);
		cellStyle5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle5.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle5.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle5.setBorderRight(HSSFCellStyle.BORDER_THIN);
		Font font5 = wb.createFont();
		font5.setFontName("宋体");
		// font5.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font5.setFontHeightInPoints((short) 12);
		cellStyle5.setFont(font5);

		contentRow = sheet.createRow(++currentRow);
		CellRangeAddress region = new CellRangeAddress(currentRow, currentRow, 0, 7);
		sheet.addMergedRegion(region);
		HSSFCell cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("佛山市东方医疗设备厂有限公司");
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(6);
		cell.setCellStyle(cellStyle);
		cell = contentRow.createCell(7);
		
		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 0, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle1);
		cell.setCellValue("提 货 凭 证");

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow, 0, 7);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(0);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("(中国名牌: 佛山轮椅)");

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("日期：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow + 4, 1, 1);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("1");
		region = new CellRangeAddress(currentRow, currentRow, 2, 5);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("厂 方 填 写");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("生产编号");
		sheet.setColumnWidth(2, 5 * 3 * 256);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getCode());
		sheet.setColumnWidth(3, 10 * 3 * 256);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("起运港");
		sheet.setColumnWidth(4, 5 * 3 * 256);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("黄埔");
		sheet.setColumnWidth(5, 10 * 3 * 256);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("业务员");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("");
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("目的港");
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("");

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("货物名称");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getGoodsName());
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("柜型");
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getCaseModel());

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("装货日期");
		region = new CellRangeAddress(currentRow, currentRow, 3, 5);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(delivery.getDdate());
		cell.setCellValue(
				d.getYear() + 1900 + " 年 " + d.getMonth() + 1 + " 月 " + d.getDate() + " 日 " + d.getHours() + " 时 到厂 ");
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		region = new CellRangeAddress(currentRow, currentRow + 4, 1, 1);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("2");
		region = new CellRangeAddress(currentRow, currentRow, 2, 5);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("谭先生  (电话: 0757-8120 2191)---13612503173");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("订舱号");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getCode());
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("车牌");
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getCarNo());

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("柜号");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getCaseNo());
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("封条号");
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getSealNo());

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("司机姓名");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getDriver());
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("司机电话");
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getDriverPhone());

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle5);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("提柜点");
		cell = contentRow.createCell(3);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getDport());
		cell = contentRow.createCell(4);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue("还柜点");
		cell = contentRow.createCell(5);
		cell.setCellStyle(cellStyle5);
		cell.setCellValue(delivery.getRport());

		contentRow = sheet.createRow(++currentRow);
		cell = contentRow.createCell(1);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue("备注：");
		region = new CellRangeAddress(currentRow, currentRow, 2, 5);
		sheet.addMergedRegion(region);
		cell = contentRow.createCell(2);
		cell.setCellStyle(cellStyle2);
		cell.setCellValue(delivery.getMemo());

		OutputStream os = null;
		try {
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
			response.reset();
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
}
