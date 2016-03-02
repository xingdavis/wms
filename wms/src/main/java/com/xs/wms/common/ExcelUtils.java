package com.xs.wms.common;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

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
		titleRow.createCell(0).setCellValue(title);
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, list.size()); // 参数都是从O开始   
        sheet.addMergedRegion(region);   

		// 设置列标题
		for (int i = 0; i < header.length; i++) {
			headerRow.createCell(i).setCellStyle(cellStyle);
			headerRow.createCell(i).setCellValue(header[i]);
		}
		try {
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
				bottomRow = sheet.createRow(list.size() + 2);
				bottomRow.createCell(0).setCellStyle(cellStyle);
				bottomRow.createCell(0).setCellValue("广州信树物流有限公司：罗生");
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
}
