package com.ym.provider.commons.utils.excel;

import com.ym.provider.commons.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通用excel导入基础类
 * 业务实现验证 validRow 方法
 *
 * @author hexiaohu
 * @date 2019/11/21 5:01 PM
 */
@Slf4j
public abstract class ImportAbstractService {


    /**
     * 导入文件
     *
     * @param file
     * @param tClass
     * @return
     */
    public <T extends ImportRowInfo> ImportResponse<T> importFile(MultipartFile file, Class<T> tClass) {
        return importFile(file, tClass, 1);
    }

    /**
     * 导入文件
     *
     * @param file
     * @param tClass
     * @param sheetNo
     * @return
     */
    public <T extends ImportRowInfo> ImportResponse<T> importFile(MultipartFile file, Class<T> tClass, int sheetNo) {
        log.info("import file fileSize:{}, name:{}", file.getSize(), file.getName());
        List all = ExcelUtil.readExcel(file, tClass, sheetNo);
        return validAllData(all);
    }


    /**
     * 验证所有数据
     *
     * @param all
     * @param <T>
     * @return
     */
    private <T extends ImportRowInfo> ImportResponse<T> validAllData(List<T> all) {
        AtomicInteger totalCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);
        //验证导入数据
        validData(all, totalCount, errorCount, successCount);

        ImportResponse response = new ImportResponse();
        response.setErrorCount(errorCount.get());
        response.setSuccessCount(successCount.get());
        response.setTotalCount(totalCount.get());
        response.setList(all);
        log.info("读入数据size: {}", all.size());
        return response;
    }


    /**
     * 验证导入数据
     *
     * @param list
     * @param totalCount
     * @param errorCount
     * @param successCount
     */
    public <T extends ImportRowInfo> void validData(List<T> list, AtomicInteger totalCount,
                                                    AtomicInteger errorCount, AtomicInteger successCount) {
        int totalLine = 0;
        int errorLine = 0;
        int successLine = 0;
        if (list != null && list.size() > 501) {
            throw new MyException("超过最大条数500");
        }
        for (ImportRowInfo mar : list) {
            totalLine++;
            try {
                mar.setErrorInfo("");
                mar.setImportStatus(true);
                validRow(mar);
                successLine++;
            } catch (Exception e) {
                mar.setErrorInfo(e.getMessage());
                mar.setImportStatus(false);
                errorLine++;
                log.info("", e);
            }
        }

        //设置计数
        totalCount.set(totalLine);
        errorCount.set(errorLine);
        successCount.set(successLine);
    }


    /**
     * 单行excle 数据验证
     *
     * @param rowInfo
     * @return
     */
//    public abstract <T extends ImportRowInfo> E validRow(T rowInfo);
    public abstract void validRow(ImportRowInfo rowInfo);
}
