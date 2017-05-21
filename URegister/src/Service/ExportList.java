package Service;

import Model.Registration;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExportList {

    private List<Registration> list;

    public ExportList() { }

    public void exportToExcel(List<Registration> rlist, String filename) {
        list = rlist;
        creatExcelFile(filename);
    }

    private void creatExcelFile(String filename) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("URegister-Data");// creating a blank sheet
            int rownum = 1;
            setHeaders(sheet.createRow(0));
            for (Registration registration : list) {
                Row row = sheet.createRow(rownum++);
                createList(registration, row);
            }
            FileOutputStream out = new FileOutputStream(new File(filename + ".xlsx")); // file name with path
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHeaders(Row r) {
        Cell cell = r.createCell(0);
        cell.setCellValue("Datum");
        cell = r.createCell(1);
        cell.setCellValue("Starttijd");
        cell = r.createCell(2);
        cell.setCellValue("Eindtijd");
        cell = r.createCell(3);
        cell.setCellValue("Gewerkte tijd");
        cell = r.createCell(4);
        cell.setCellValue("Verantwoording");
    }

    //Creating cells for each row
    private void createList(Registration registration, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(registration.getDate().toString());
        cell = row.createCell(1);
        cell.setCellValue(registration.getStart().toString());
        cell = row.createCell(2);
        cell.setCellValue(registration.getEnd().toString());
        cell = row.createCell(3);
        cell.setCellValue(registration.getWorkedTime().toString());
        cell = row.createCell(4);
        cell.setCellValue(registration.getContent());
    }
}
