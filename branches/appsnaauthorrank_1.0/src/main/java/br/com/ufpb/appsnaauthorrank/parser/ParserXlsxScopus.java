package br.com.ufpb.appsnaauthorrank.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;

public class ParserXlsxScopus {


	public static List<Artigo> realizarParserXlsx(File file)
			throws Exception {

		List<Artigo> artigos = new ArrayList<Artigo>();
		InputStream fileStream = new FileInputStream(file);
		Workbook wb = new XSSFWorkbook(fileStream);
		Sheet sheet1 = wb.getSheetAt(0);
		for (Row row : sheet1) {
			Artigo artigo = new Artigo();
			if (row.getRowNum() != 0) {
				for (Cell cell : row) {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (cell.getColumnIndex() == 0) {
//							artigo.setAutores(cell
//											.getStringCellValue());
						} else if (cell.getColumnIndex() == 1) {
							artigo.setTitulo(cell.getStringCellValue());
						} else if (cell.getColumnIndex() == 3) {
							artigo.setOndePub(cell.getStringCellValue());
						} else if (cell.getColumnIndex() == 11) {
							artigo.setLinkDownload(cell
									.getStringCellValue());
						}
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (cell.getColumnIndex() == 2) {
							artigo.setPubYear((cell.getNumericCellValue() + "")
									.replace(".0", ""));
						}
						break;
					default:
						System.out.println();
					}
				}
				artigos.add(artigo);
			}
		}

		return artigos;
	}

}
