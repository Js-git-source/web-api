package com.unisguard.webapi.web.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;

/**
 * @author zemel
 * @date 2022/1/4 15:59
 */
class DictInit {
    @Test
    void init() throws IOException {
        String dirPath = "D:\\develop\\wokspace\\guoshun\\ngsoc-doc\\V2\\02-数据字典设计\\01-基础版本";
        File dir = new File(dirPath);
        String[] fileNames = dir.list();
        if (fileNames == null || fileNames.length == 0) {
            return;
        }
        File dataFile = new File(dirPath + "\\00-init-dict-data.sql");
        FileUtils.writeStringToFile(dataFile, "", Charset.defaultCharset());
        HashSet<String> IdSet = new HashSet<String>();
        for (String fileName : fileNames) {
            String[] tmpFilename = fileName.split("-");
            String code_typeStr = tmpFilename[0];
            int code_type = Integer.parseInt(code_typeStr);
            if (code_type == 0) {
                continue;
            }
            String innerFilePath = dirPath + "/" + fileName;
            StringBuilder name = new StringBuilder();
            for (int i = 1; i < tmpFilename.length; i++) {
                if (i > 1) {
                    name.append("-");
                }
                name.append(tmpFilename[i].trim());
            }
            name = new StringBuilder(name.toString().replace(".xlsx", ""));
            if (!IdSet.contains(code_typeStr)) {
                FileUtils.writeStringToFile(dataFile, "\r\n-- " + name + "\r\n", "UTF-8", true);
                String sql = "INSERT INTO `t_dict`(`code_type`, `code`, `name`, `description`, `level`) VALUES (-1, " + code_type + ", '" + name + "', NULL, 99);";
                System.out.println(sql);
                FileUtils.writeStringToFile(dataFile, sql + "\r\n", "UTF-8", true);
                IdSet.add(code_typeStr);
            } else {
                FileUtils.writeStringToFile(dataFile, "\r\n-- " + name + "\r\n", "UTF-8", true);
            }
            InputStream is = new FileInputStream(innerFilePath);
            Workbook wb = WorkbookFactory.create(is);
            is.close();
            if (wb != null) {
                int maxRownum = 500;
                Sheet sheet = wb.getSheetAt(0);
                for (int i = 1; i < maxRownum; i++) {
                    String code = "";
                    try {
                        code = String.valueOf((int) sheet.getRow(i).getCell(0).getNumericCellValue());
                    } catch (Exception e1) {
                        break;
                    }
                    name = new StringBuilder();
                    try {
                        name = new StringBuilder(sheet.getRow(i).getCell(1).getStringCellValue());
                    } catch (Exception e) {
                        try {
                            name = new StringBuilder(String.valueOf((int) sheet.getRow(i).getCell(1).getNumericCellValue()));
                        } catch (Exception e1) {
                            break;
                        }
                    }
                    String description = "";
                    try {
                        description = sheet.getRow(i).getCell(2).getStringCellValue();
                    } catch (Exception e) {
                        try {
                            description = String.valueOf((int) sheet.getRow(i).getCell(2).getNumericCellValue());
                        } catch (Exception e1) {
                            break;
                        }
                    }
                    //String description = sheet.getRow(i).getCell(2).getStringCellValue();
                    if (StringUtils.isBlank(code) || "0".contentEquals(code)) {
                        break;
                    }
                    IdSet.add(code);
                    String sql = "INSERT INTO `t_dict`(`code_type`, `code`, `name`, `description`, `level`) VALUES (" + code_type + ", " + code + ", '" + name + "', " + (StringUtils.isBlank(description) ? "NULL" : "'" + description + "'") + ", 99);";
                    System.out.println(sql);
                    FileUtils.writeStringToFile(dataFile, sql + "\r\n", "UTF-8", true);
                }
                if (code_type == 122600) {
                    String sql = "INSERT INTO `t_dict`(`code_type`, `code`, `name`, `description`, `level`) VALUES (122600, 122602, 'TRUE', NULL, 99);";
                    System.out.println(sql);
                    FileUtils.writeStringToFile(dataFile, sql + "\r\n", "UTF-8", true);
                    sql = "INSERT INTO `t_dict`(`code_type`, `code`, `name`, `description`, `level`) VALUES (122600, 122603, 'FALSE', NULL, 99);";
                    System.out.println(sql);
                    FileUtils.writeStringToFile(dataFile, sql + "\r\n", "UTF-8", true);
                }
            }

        }
    }
}
