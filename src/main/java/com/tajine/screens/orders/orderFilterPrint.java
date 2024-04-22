package com.tajine.screens.orders;

import com.tajine.domain.Order;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class orderFilterPrint {

	public void printRecipt(String printerName, List<Order> orders, int total) throws JRException {

		JasperReport jasperReport;
		try (InputStream in = getClass().getResourceAsStream("/orderFilter.jrxml")) {
			jasperReport = JasperCompileManager.compileReport(in);

		//setting parameters:
		HashMap<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource ordersSet = new JRBeanCollectionDataSource(orders);
		parameters.put("ORDERS", ordersSet);
		parameters.put("totalTotal", total);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

		PrintService printService = this.getPrintService(printerName);

		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(new Copies(1));
		PrinterName printerNamee = new PrinterName(printerName, null); //gets printer
		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(printerNamee);

		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
		configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
		configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
		configuration.setDisplayPageDialog(false);
		configuration.setDisplayPrintDialog(false);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}catch (IOException e) {
			e.printStackTrace();
		}
	}


	private PrintService getPrintService(String printerName) {
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		PrintService selectedService = null;
		
		for (PrintService ps : services) {
			if (ps.getName().equals(printerName)) {
				selectedService = ps;
				break;
			}
		}
		return selectedService;
	}
}


    
