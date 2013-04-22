package com.taobao.mc.search.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.taobao.mc.search.model.FileInfo;

/**
 * <pre>
 * desc: 
 * created: 2013-2-26 ����09:59:46
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class ExcelReader {
	private static final String filePath = "D:/F_disk/tmp_xiangfeng_20130225_limit_100W.csv";

	public static void main(String[] args) throws IOException {

		ExcelReader reader = new ExcelReader();

		FileInputStream fis = new FileInputStream(new File(filePath));
		List<FileInfo> list = reader.readXls(fis);

		for (FileInfo xls : list) {
			System.out.println(xls.getId() + "    " + ToStringBuilder.reflectionToString(xls));
		}

	}

	/**
	 * ��ȡxls�ļ�����
	 * 
	 * @return List<FileInfo>����
	 * @throws IOException
	 *             ����/���(i/o)�쳣
	 */
	private List<FileInfo> readXls(InputStream is) throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		FileInfo FileInfo = null;
		List<FileInfo> list = new ArrayList<FileInfo>();
		// ѭ��������Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// ѭ����Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				FileInfo = new FileInfo();

				System.out.println(hssfRow.getLastCellNum());

				// HSSFCell xh = hssfRow.getCell(0);
				// if (xh == null) {
				// continue;
				// }
				// FileInfo.setStatus(Integer.parseInt(getValue(xh), 0));
				// HSSFCell xm = hssfRow.getCell(1);
				// if (xm == null) {
				// continue;
				// }
				// FileInfo.setUrl(this.getValue(hssfCell))
				// HSSFCell yxsmc = hssfRow.getCell(2);
				// if (yxsmc == null) {
				// continue;
				// }
				// FileInfo.setYxsmc(getValue(yxsmc));
				// HSSFCell kcm = hssfRow.getCell(3);
				// if (kcm == null) {
				// continue;
				// }
				// FileInfo.setKcm(getValue(kcm));
				// HSSFCell cj = hssfRow.getCell(4);
				// if (cj == null) {
				// continue;
				// }
				// FileInfo.setCj(Float.parseFloat(getValue(cj)));
				// list.add(FileInfo);
			}
		}
		return list;
	}

	/**
	 * �õ�Excel���е�ֵ
	 * 
	 * @param hssfCell
	 *            Excel�е�ÿһ������
	 * @return Excel��ÿһ�������е�ֵ
	 */
	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// ���ز������͵�ֵ
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// ������ֵ���͵�ֵ
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// �����ַ������͵�ֵ
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
