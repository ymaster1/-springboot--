package com.ym.provider.commons.utils.excel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.ym.provider.commons.exception.MyException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ymaster1
 * @date 2020/10/26 20:20
 * @description： excel导入导出
 *
 */
public class ExcelUtil {
    /**
     * 返回 ExcelReader
     *
     * @param excel         需要解析的 Excel 文件
     * @param excelListener new ExcelListener()
     */
    private static ExcelReader getReader(MultipartFile excel, ExcelListener excelListener) {
        String filename = excel.getOriginalFilename();

        if (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx"))) {
            throw new MyException("文件格式错误！");
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(excel.getInputStream());
            return new ExcelReader(inputStream, null, excelListener, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 读取指定sheet的excel
     * @param excel
     * @param clazz
     * @param sheetNo
     * @param <T>
     * @return
     */
    public static <T extends BaseRowModel> List<T> readExcel(MultipartFile excel, Class<T> clazz, int sheetNo) {
        return readExcel(excel, clazz, sheetNo, 1);
    }

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel       文件
     * @param clazz       实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    public static <T extends BaseRowModel> List<T> readExcel(MultipartFile excel, Class<T> clazz, int sheetNo, int headLineNum) {
        ExcelListener<T> excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return new ArrayList<>();
        }
        Sheet sheet = new Sheet(sheetNo);
        sheet.setClazz(clazz);
        reader.read(sheet);
        sheet.setHeadLineMun(headLineNum);
        return excelListener.getDatas();
    }

    /**
     * 导出 Excel ：一个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param object    映射实体类，Excel 模型
     */
    public static void writeExcel(HttpServletResponse response, List<? extends BaseRowModel> list, String fileName,
                                  String sheetName, BaseRowModel object) throws IOException {
//        OutputStream out = getOutputStream(fileName, response);
        OutputStream out = new FileOutputStream("D:\\ymaster1.xlsx");
        try {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            // 写仅有一个 Sheet 的 Excel 文件, 此场景较为通用
            Sheet sheet = new Sheet(1, 0, object.getClass());
            // 第一个 sheet 名称
            sheet.setSheetName(sheetName);
            writer.write(list, sheet);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws IOException {
        //创建本地文件
        fileName = fileName + ExcelTypeEnum.XLSX.getValue();
        File dbfFile = new File(fileName);
        if (!dbfFile.exists() || dbfFile.isDirectory()) {
            dbfFile.createNewFile();
        }
        try {
            fileName = new String(fileName.getBytes(), "ISO-8859-1");
//            文件下载头
            response.addHeader("Content-Disposition", "filename=" + fileName);

            return response.getOutputStream();
        } catch (Exception e) {

            throw new MyException("导出异常！");
        }
    }
}
