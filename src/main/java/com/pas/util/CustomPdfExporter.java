package com.pas.util;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.export.DataTablePDFExporter;
import org.primefaces.component.export.ColumnValue;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;

public class CustomPdfExporter extends DataTablePDFExporter
{
	 @Override
	 protected void exportCellValue(FacesContext context, DataTable table, UIColumn col, ColumnValue columnValue, int index) 
	 {
	    //PdfPCell cell = createCell(col, new Paragraph(columnValue.toString(), cellFont));
	    //addCell(pdfTable, cell);
	 }
}
