package org.inovout.data.document.schema.test;

import org.inovout.data.document.schema.model.Bag;
import org.inovout.data.document.schema.model.Element;
import org.inovout.data.document.schema.model.ElementType;
import org.inovout.data.document.schema.service.BagService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestBagService {

	private BagService bagService;
	private Bag bag;
	private ClassPathXmlApplicationContext ctx;

	public TestBagService() {
		ctx = new ClassPathXmlApplicationContext("spring-context.xml");
		bagService = ctx.getBean("bagService", BagService.class);
	}

	@Test
	public void testCreateBag() {
		bag = new Bag();
		bag.setDatabase("4Cloud");
		bag.setName("Device-test");
		Element intElement = new Element();
		intElement.setName("deveiceNo");
		intElement.setType(ElementType.INTEGER);
		intElement.setIsPrimaryKey(true);
		intElement.setBag(bag);
		bag.addElement(intElement);
		Element stringElement = new Element();
		stringElement.setName("deveiceName");
		stringElement.setType(ElementType.STRING);
		stringElement.setBag(bag);
		bag.addElement(stringElement);
		bagService.createNewBag(bag);
	}
}
