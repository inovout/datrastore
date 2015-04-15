package org.inovout.data.document.handler;

import org.inovout.data.document.EntityBag;
import org.inovout.data.document.EntityPackage;
import org.inovout.data.document.persistence.DocumentService;
import org.inovout.data.handler.EntityBagHandler;

public class DocumentHandler implements EntityBagHandler{
	private DocumentService documentService=new DocumentService();
	public void Handle(EntityBag entityBag){
		
		for (EntityPackage entityPackage : entityBag.getPackages()) {
			documentService.storeEntity(entityPackage);
		}
	}
	 
}
