package org.inovout.data.document.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.inovout.data.document.EntityAction;
import org.inovout.data.document.EntityPackage;
import org.inovout.data.document.persistence.DocumentService;
import org.junit.Test;

public class TestDocumentService {
	private DocumentService entityService;
	private EntityPackage entityPackage=new EntityPackage();
	private Set<Map<String,Object>> entities;

	public TestDocumentService() {
		entityService =new DocumentService();
		entityPackage.setCollection("Device");		
		entities=new HashSet<Map<String,Object>>();	
	}
	
	@Test
	public void TestBuckInsert(){		
		Map<String,Object> entityBx=new  HashMap<String,Object>();
		entityBx.put("deviceNo", "1");
		entityBx.put("deviceName", "冰箱");
		Map<String,Object> entityXyj=new  HashMap<String,Object>();
		entityXyj.put("deviceNo", "2");
		entityXyj.put("deviceName", "洗衣机");
		entities.add(entityBx);
		entities.add(entityXyj);
		entityPackage.setEntitis(entities);
		entityPackage.setAction(EntityAction.INSERT);
		entityService.storeEntity(entityPackage);
	}
	
	
	@Test
	public void TestBuckSave(){
		Map<String,Object> entityKt=new  HashMap<String,Object>();
		entityKt.put("deviceNo", "1");
		entityKt.put("deviceName", "空调");
		entities.add(entityKt);
		entityPackage.setEntitis(entities);
		entityPackage.setAction(EntityAction.SAVE);
		entityService.storeEntity(entityPackage);
	}
	
	@Test
	public void TestBuckUpdate(){
		Map<String,Object> entityDs=new  HashMap<String,Object>();
		entityDs.put("deviceNo", "2");
		entityDs.put("deviceName", "电视");
		entities.add(entityDs);
		entityPackage.setEntitis(entities);
		entityPackage.setAction(EntityAction.UPDATE);
		entityService.storeEntity(entityPackage);
	}
	
	/*@Test
	public void TestBuckDelete(){		
		entityPackage.setAction(EntityAction.DELETE);	
		entityService.storeEntity(entityPackage);
	}*/
}
