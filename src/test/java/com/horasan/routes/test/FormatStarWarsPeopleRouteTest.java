package com.horasan.routes.test;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.horasan.batch.StarWarsOperationsBatch;
import com.horasan.helper.TestHelper;
import com.horasan.model.StarWarsPerson;


@CamelSpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class FormatStarWarsPeopleRouteTest {

	// route_timerSWBatch from timer (in StarWarsOperationsBatch.java) is not started.
	@MockBean
	private StarWarsOperationsBatch starWarsOperationsBatch;
	
	@Autowired
	private FluentProducerTemplate fluentProducerTemplate;
	
	/*
	Camel related items:
		FluentProducerTemplate
	Description:
		Route "direct:FormatStarWarsPeople" is called directly using fluentProducerTemplate.
		Response body is validated against expected values.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_2_FormatAllPeople() throws Exception {
		
		List<StarWarsPerson> starWarsPeopleList = TestHelper.getStarWarsPeopleListWith2People(); 
		
		Exchange responseExchange = fluentProducerTemplate.to("direct:FormatStarWarsPeople")
		.withBody(starWarsPeopleList)
		.request(Exchange.class);
		
		List<StarWarsPerson> modifiedPeople = responseExchange.getMessage().getBody(List.class);
		
		Assertions.assertEquals(starWarsPeopleList.size(), modifiedPeople.size(), "number of items array is not correct!");
		Assertions.assertEquals("fromMockPeopleApi_name0-M".toUpperCase(), modifiedPeople.get(0).getName(), "modified name of 0. item is not correct!");
		Assertions.assertEquals("fromMockPeopleApi_name1-M".toUpperCase(), modifiedPeople.get(1).getName(), "modified name of 1. item is not correct!");
		
	}
	

}
