package AdditionalClasses;

import java.util.ArrayList;
import java.util.Iterator;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.List;

public class CreateData {
    @SneakyThrows
    public static void main(String[] args) {
        Workbook wb = readExcel("src/main/resources/LabGraph.xlsx");
        Sheet sh = wb.getSheetAt(0);
        Iterator rowIter = sh.rowIterator();
        // Исключили первую строку
        rowIter.next();
        while(rowIter.hasNext()) {
            XSSFRow row = (XSSFRow) rowIter.next();
            Iterator cellIter = row.cellIterator();
            // Исключили первый стобец
            cellIter.next();
            NodeCfg cfg = new NodeCfg();
            cfg.setName("Node"+row.getRowNum());
            List<Link> links = new ArrayList<>();
            while (cellIter.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIter.next();
                if (cell.getNumericCellValue() != 0.0){
                    Link link = new Link();
                    link.setWeight((int) cell.getNumericCellValue());
                    link.setNeighbourAgent("Node"+cell.getColumnIndex());
                    links.add(link);
                }
            }
            cfg.setLinks(links);
            ParsingProvider.marshal(
                    cfg,
                    NodeCfg.class,
                    new File("src/main/resources/LabGraph/"+"node"+row.getRowNum()+".xml")
            );
        }
        wb.close();
    }
    @SneakyThrows
    private static XSSFWorkbook readExcel(String filename) {
        XSSFWorkbook wb = new XSSFWorkbook(
                new File(filename)
        );
        return wb;
    }
}
