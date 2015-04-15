package org.inovout.boot;

import org.inovout.data.document.handler.DocumentHandler;


public class DocumentHandlerApplication {
	
	public static void main(String[] args) throws Exception{		
		DocumentHandler documentHandler =new DocumentHandler();
		SinkBulid sinkBulid=new SinkBulid(documentHandler,"DocumentHandler");
		sinkBulid.Start();           
	}	
}
	