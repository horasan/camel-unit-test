package com.horasan.routes.test;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.language.ConstantExpression;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horasan.batch.StarWarsOperationsBatch;
import com.horasan.helper.TestHelper;
import com.horasan.model.GetPeopleResponse;
import com.horasan.model.StarWarsPerson;
import com.horasan.routes.GetStarWarsPeopleRoute;


@CamelSpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@UseAdviceWith
@MockEndpoints
public class GetStarWarsPeopleRouteTest {

	// route_timerSWBatch from timer (in StarWarsOperationsBatch.java) is not started.
	@MockBean
	private StarWarsOperationsBatch starWarsOperationsBatch;
	
	@Autowired
	private FluentProducerTemplate fluentProducerTemplate;
	
	@Autowired
	private ProducerTemplate producerTemplate;
	
	@Autowired
	private CamelContext camelContext;
	
	@EndpointInject("mock:mockEndPointFor_direct_FormatStarWarsPeople")
	protected MockEndpoint mockEndPointFor_direct_FormatStarWarsPeople;
	
	@EndpointInject("mock:mockEndPointFor_direct_SaveStarWarsPerson")
	protected MockEndpoint mockEndPointFor_direct_SaveStarWarsPerson; 

	/*
		Camel related items:
			AdviceWith.adviceWith
			AdviceWithRouteBuilder.weaveByToUri
			ProducerTemplate
		Description:
			Api call to "https://swapi.dev/api" is mocked with constant response.
	*/
	@Test
	public void test_3_Route_GetStarWarsPeople_with_constant_response_with_producerTemplate() throws Exception {
		
		AdviceWith.adviceWith(
				camelContext, // In this camel context,
				GetStarWarsPeopleRoute.ROUTE_ID, // I want to "modify (mock)" this route with the following rules.
				routeBuilder -> {

					GetPeopleResponse mockGetPeopleResponse = TestHelper.getMockGetPeopleResponseWith2People();

					ObjectMapper mapper = new ObjectMapper();

					routeBuilder
					.weaveByToUri("https://swapi.dev/api*") // this is the end point (which I want to modify)
					.replace() //  I want to replace (mock) this end point.
					.setBody( // with this body.
							new ConstantExpression(mapper.writeValueAsString(mockGetPeopleResponse))
							);
				});
		
		camelContext.start();
		
		producerTemplate.sendBody("direct:GetStarWarsPeople", null);
		
	}

	/*
	Camel related items:
		AdviceWith.adviceWith
		AdviceWithRouteBuilder.weaveByToUri
		FluentProducerTemplate
		MockEndpoint.expectedMessageCount
		MockEndpoint.assertIsSatisfied
	Description:
		Api call to "https://swapi.dev/api" is mocked with constant response.
		Call to direct:SaveStarWarsPerson mocked with mock end point using weaveByToUri.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_4_Route_GetStarWarsPeople_with_constant_response_2_people() throws Exception {
		
		AdviceWith.adviceWith(camelContext, GetStarWarsPeopleRoute.ROUTE_ID, routeBuilder -> {
			
			GetPeopleResponse mockGetPeopleResponse = TestHelper.getMockGetPeopleResponseWith2People();
			
			ObjectMapper mapper = new ObjectMapper(); 

			routeBuilder.weaveByToUri("https://swapi.dev/api*")
			.replace().setBody(new ConstantExpression(mapper.writeValueAsString(mockGetPeopleResponse)));
			
			routeBuilder.weaveByToUri("direct:SaveStarWarsPerson").replace().to(mockEndPointFor_direct_SaveStarWarsPerson);
		});
		
		
		mockEndPointFor_direct_SaveStarWarsPerson.expectedMessageCount(2);
		
		camelContext.start();
		
		Exchange resultFromRebuiltGetStarWarsPeople = fluentProducerTemplate.to("direct:GetStarWarsPeople").request(Exchange.class);
		
		mockEndPointFor_direct_SaveStarWarsPerson.assertIsSatisfied();
		
		List<StarWarsPerson> people = resultFromRebuiltGetStarWarsPeople.getMessage().getBody(List.class);
		
		Assertions.assertEquals(2, people.size(), "Size of the expected list is not correct!");
		Assertions.assertEquals("fromMockPeopleApi_name0".toUpperCase() + "-M", people.get(0).getName(), "modified name of 0. item is not correct!");
		Assertions.assertEquals("fromMockPeopleApi_name1".toUpperCase() + "-M", people.get(1).getName(), "modified name of 1. item is not correct!");

	}
	
	/*
	Camel related items:
		AdviceWith.adviceWith
		AdviceWithRouteBuilder.weaveByToUri
		AdviceWithRouteBuilder.weaveById
		FluentProducerTemplate
		MockEndpoint.expectedMessageCount
		MockEndpoint.assertIsSatisfied
	Description:
		Api call to "https://swapi.dev/api" is mocked with constant response.
		Call to direct:SaveStarWarsPerson mocked with mock end point using weaveByToUri.
		Call to direct:FormatStarWarsPeople mocked with mock end point using weaveById.
	 */
	@Test
	public void test_5_Route_GetStarWarsPeople_with_no_person() throws Exception {
		
		AdviceWith.adviceWith(camelContext, GetStarWarsPeopleRoute.ROUTE_ID, routeBuilder -> {
			GetPeopleResponse mockGetPeopleResponse = TestHelper.getMockEmptyGetPeopleResponse();
			
			ObjectMapper mapper = new ObjectMapper(); 
			
			routeBuilder.weaveByToUri("https://swapi.dev/api*").replace().setBody(new ConstantExpression(mapper.writeValueAsString(mockGetPeopleResponse)));
			
			routeBuilder.weaveById(GetStarWarsPeopleRoute.ID_FOR_direct_FormatStarWarsPeople).replace().to(mockEndPointFor_direct_FormatStarWarsPeople);
			
			routeBuilder.weaveByToUri("direct:SaveStarWarsPerson").replace().to(mockEndPointFor_direct_SaveStarWarsPerson);
			
			
		});
		
		mockEndPointFor_direct_FormatStarWarsPeople.expectedMessageCount(1);
		mockEndPointFor_direct_SaveStarWarsPerson.expectedMessageCount(0);
		
		camelContext.start();
		
		Exchange resultFromRebuiltGetStarWarsPeople = fluentProducerTemplate.to("direct:GetStarWarsPeople").request(Exchange.class);
		mockEndPointFor_direct_FormatStarWarsPeople.assertIsSatisfied();
		mockEndPointFor_direct_SaveStarWarsPerson.assertIsSatisfied();
		
	}



}
