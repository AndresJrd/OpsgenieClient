package util;

import entity.AlarmDetail;
import entity.AlarmRate;
import entity.Response;
import helper.Tuple;;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Exporter {
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    List<String> columns;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Exporter(String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet(sheetName);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Boolean exportDataDetail(Response response, String fileName) {
        columns = AlarmDetail.getFields();
        List<AlarmDetail> alarmDetails = response.getData();
        List<List<Tuple>> list = new ArrayList<>();
        alarmDetails.stream().forEach(alarmDetail -> {
            list.add(alarmDetail.getFieldValue());
        });
        int rowNum = 0;
        int colNum = 0;
        System.out.println("Creating excel...");
        XSSFRow row = sheet.createRow(rowNum++);

        //Create a new font and alter it.
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("IMPACT");
        font.setItalic(true);
        font.setColor(HSSFColor.BLACK.index);

        //Set font into style
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);

        for (String s : columns) {
            XSSFCell cell = row.createCell(colNum++);
            cell.setCellValue("Font Style");
            cell.setCellStyle(style);
            cell.setCellValue(s);
        }
        for (List<Tuple> tuples : list) {
            row = sheet.createRow(rowNum++);
            colNum = 0;
            for (Tuple alarm : tuples) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(alarm.value);
            }
        }
        XSSFSheet sheetGroup = workbook.createSheet("OpsGenie Group");
        List<AlarmDetail> alarmUnifyErrors = Util.unifyErrors(response.getData());
        List<AlarmRate> alarmsRate = new ArrayList<>();
        alarmUnifyErrors.stream().forEach(alarm -> {
            alarmsRate.add(new AlarmRate(alarm));
        });

        List<String> errorRepo = new ArrayList<>();
        alarmsRate.stream().forEach(alarmRate -> {
            errorRepo.add(alarmRate.getMessage() + "@" + alarmRate.getError());
        });

        Map<String, Long> alarmErrorGroup = errorRepo.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        columns = Arrays.asList("Repo","Reason","Ocurrence");//AlarmRate.getFields();
        rowNum = 0;
        colNum = 0;
        XSSFRow rowg = sheetGroup.createRow(rowNum++);

        for (String s : columns) {
            XSSFCell cell = rowg.createCell(colNum++);
            cell.setCellValue("Font Style");
            cell.setCellStyle(style);
            cell.setCellValue(s);
        }

        for (Map.Entry<String, Long> entry : alarmErrorGroup.entrySet()) {
            String[] values=entry.getKey().split("@");
            row = sheetGroup.createRow(rowNum++);

            Cell cellRepo = row.createCell(0);
            cellRepo.setCellValue(values[0]);

            Cell celleOcurrence = row.createCell(1);
            celleOcurrence.setCellValue(values[1]);

            Cell cellCount=row.createCell(2);
            cellCount.setCellValue(entry.getValue());
        }try {
            FileOutputStream outputStream = new FileOutputStream(fileName + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
