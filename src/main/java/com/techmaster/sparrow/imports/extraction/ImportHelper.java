package com.techmaster.sparrow.imports.extraction;

import com.techmaster.sparrow.constants.SparrowURLConstants;
import com.techmaster.sparrow.entities.ImportBean;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.util.SparrowUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

public class ImportHelper {
	
	private static Logger logger = LoggerFactory.getLogger(ImportHelper.class);

	public static ImportBean createImportBeanFromWorkbook(Workbook workbook, String userName, String fileName, String beanName, String status){
		
		ByteArrayOutputStream bos = null;
		
		try {
			bos = new ByteArrayOutputStream();
			workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
		    try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		byte[] bytes = bos.toByteArray();

		ImportBean importBean = SparrowUtil.addAuditInfo(new ImportBean(), userName);
		importBean.setWorkbook(workbook);
		importBean.setOriginalFileName(fileName);
		importBean.setBeanName(beanName);
		importBean.setByteLen(bytes.length);
		importBean.setExcelBytes(bytes);
		importBean.setStatus(status);

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(importBean.getExcelBytes());
		EntityManagerFactory entityManagerFactory = SparrowBeanContext.getBean(EntityManagerFactory.class);
		if (entityManagerFactory != null) {
			SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
			Session session = sessionFactory.openSession();
			Blob blob = Hibernate.getLobCreator(session).createBlob(byteArrayInputStream, (long) importBean.getByteLen());
			importBean.setExcelBlob(blob);
		}
		
		return importBean;
	}
	
	public static Workbook getWorkBookForImportBean(ImportBean importBean){
		byte[] bytes = importBean.getExcelBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(byteArrayInputStream);
			importBean.setWorkbook(wb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	public static void createImportBeanBlobAndSave(ImportBean importBean, Session session){
		Blob blob = null;
		Transaction trans = null;
		try {
			trans = session.beginTransaction();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(importBean.getExcelBytes());
			blob = Hibernate.getLobCreator(session).createBlob(byteArrayInputStream, (long) importBean.getByteLen());
			importBean.setExcelBlob(blob);
			session.save(importBean);
			trans.commit();
			logger.debug("Successfully saved the import bean with it's blob!!");
		} catch (HibernateException e) {
			e.printStackTrace();
			trans.rollback();
		}finally{
			try {
				session.close();
			} catch (Exception e) {
				logger.error("Error!! createImportBeanBlobAndSave(): Error closing hibernate session!!");
				e.printStackTrace();
			}
		}
	}
	
	public static Workbook getWorkbookForImportBean(ImportBean importBean){
		logger.debug("Fetching workbook for import bean.."); 
		if(importBean.getExcelBlob() == null){
			logger.debug("bean does not have import blob. Returning null");
			return null;
		}
		Workbook workbook = null;
		Blob blob = importBean.getExcelBlob();
		try {
			InputStream inputStream =  blob.getBinaryStream();
			workbook = WorkbookFactory.create(inputStream);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.debug("Successfully recreated workbook!"); 
		return workbook;
	}
	
	public static Workbook getTempSavedWorkbook(String fileName){
		 File file = new File(SparrowURLConstants.IMPORT_EXCEL_FOLDER + "/" + fileName);
		 XSSFWorkbook workbook = null;
	      try {
			FileInputStream fIP = new FileInputStream(file);
			workbook = new XSSFWorkbook(fIP);
			if(workbook != null)
				logger.debug("Successfully created workbook from directory : " + file.getAbsolutePath()); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	      if(file.isFile() && file.exists())
	      {
	         System.out.println(
	         "File opened successfully.");
	      }
	      else
	      {
	         System.out.println(
	         "Error to open workbook!!");
	      }
	     return workbook;
	}
	
	public static void saveWorkbookToTempLocation(Workbook workbook, String fileName){
		logger.debug("Saving file to temp location : " + fileName);
		FileOutputStream out = null;
		try {
			File file = new File(SparrowURLConstants.IMPORT_EXCEL_FOLDER + fileName);
			if(!file.exists()){
				file.mkdir();
			}
			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
				logger.debug("Successfully closed output stream!!"); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Finished saving workbook to temporary directory!!"); 
	}
	
}
