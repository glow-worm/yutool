package glow.worm.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author  wangyulong
 * @time    2020/04/11 16:46:17
 * @description 导出
 */
@SuppressWarnings("rawtypes")
public class ExcelUtil {

    /**
     * 导出Excel
     *
     * @param excelName excel名称
     * @param list      导出的数据集合
     * @param fieldMap  中英文字段对应Map,即要导出的excel表头
     * @param response  使用response导出到浏览器
     * @return
     */
    public static <T> void export(String excelName, List<T> list, LinkedHashMap<String, String> fieldMap, HttpServletResponse response) throws IOException {

        // 设置默认文件名为当前时间：年月日时分秒
        if (StringUtils.isBlank(excelName)) {
            excelName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        }

        // 设置response头信息
        response.reset();
        String fileName = null;
        try {
            fileName = new String((excelName + ".xls").getBytes(), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        // 创建一个WorkBook,对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
        HSSFSheet sheet = wb.createSheet(excelName);
        sheet.setDefaultRowHeight((short) 360);
        // 填充工作表
        HSSFCellStyle style = wb.createCellStyle();
        fillSheet(sheet, list, fieldMap, style);
        // 将文件输出
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.close();
    }


    /**
     * 向工作表中填充数据
     *
     * @param sheet    excel的工作表名称
     * @param list     导出的数据集合
     * @param fieldMap 中英文字段对应关系的Map
     */
	public static <T> void fillSheet(HSSFSheet sheet, List<T> list, LinkedHashMap<String, String> fieldMap, HSSFCellStyle style) {
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        // 填充数组
        int count = 0;
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }

        // 填充表头
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 360);
        for (int i = 0; i < cnFields.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(cnFields[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
            // 解决自动设置列宽对中文失效
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
        }

        List<Class> classList = getAllClass(list.get(0));
        // 填充内容
        for (int index = 0; index < list.size(); index++) {
            row = sheet.createRow(index + 1);
            row.setHeight((short) 360);
            Map<String, Object> map = getFieldAndValue(list.get(index), classList);
            for (int i = 0; i < enFields.length; i++) {
                Object objValue = map.get(enFields[i]);
                String fieldValue = objValue == null ? "" : objValue.toString();
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(fieldValue);
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 获取对象的属性名称和值
     *
     * @param item
     * @param classList
     * @return
     */
	private static <T> Map<String, Object> getFieldAndValue(T item, List<Class> classList) {
        Map<String, Object> map = new HashMap<>(16);
        Field[] fields;
        for (Class class1 : classList) {
            fields = class1.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                String key = fields[i].getName();
                Object value = null;
                try {
                    value = fields[i].get(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 获取所有class
     *
     * @param item
     * @param <T>
     * @return
     */
    private static <T> List<Class> getAllClass(T item) {
        List<Class> classList = new ArrayList<>();
        classList.add(item.getClass());
        classList.addAll(getSuperClass(item.getClass()));
        return classList;
    }

    /**
     * 获取所有父类
     *
     * @param clazz
     * @return
     */
    private static List<Class> getSuperClass(Class clazz) {
        List<Class> superClassList = new ArrayList<>();
        Class superclass = clazz.getSuperclass();
        while (superclass != null) {
            if (superclass.getName().equals("java.lang.Object")) {
                break;
            }
            superClassList.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return superClassList;

    }
}