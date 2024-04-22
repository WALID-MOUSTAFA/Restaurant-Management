package com.tajine.screens.ordering;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;

import com.tajine.domain.Order;

import com.tajine.utils.FXUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

public class InnerRecipt {

	public void printRecipt(String printerName, Order order, String cashier, String counter) throws JRException {


		try (InputStream in = getClass().getResourceAsStream("/inner.jrxml")) {
			JasperReport jasperReport;
			jasperReport = JasperCompileManager.compileReport(in);
		//setting parameters:
		HashMap<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource orderContentsSet = new JRBeanCollectionDataSource(order.getOrderContents());
		parameters.put("ORDER_TYPE", FXUtils.orderTypeToString(String.valueOf(order.getOrderType())));
		parameters.put("ORDER_NUMBER", order.getId());
		parameters.put("CASHIER", cashier);
		parameters.put("ORDER_CONTENTS", orderContentsSet);
		parameters.put("ORDER_COUNTER", counter);
		parameters.put("TOTAL_PRICE", String.valueOf(order.getTotalPrice()));
		parameters.put("TOTAL_PAID", String.valueOf(order.getTotalPaid()));
		parameters.put("REST_PHONE", "0111000");


		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
// 		JRViewer viewer = new JRViewer(jasperPrint);
// 		viewer.setOpaque(true);
// 		viewer.setVisible(true);
// 		this.add(viewer);
// 		this.setSize(700, 500);
// 		this.setVisible(true);

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
	} catch (IOException e) {
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


    
