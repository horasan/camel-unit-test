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
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
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
import com.horasan.services.StarWarsPeopleService;

@CamelSpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@UseAdviceWith
@MockEndpoints
public class GetStarWarsPeopleRouteTestWithServiceMocks {

	// route_timerSWBatch (from timer, in StarWarsOperationsBatch.java) is not started.
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
	
	@MockBean
	private StarWarsPeopleService mockPeopleService;
	
	@Captor
	ArgumentCaptor<StarWarsPerson> starWarsPeopleCaptor;
	
	/*
		Camel related items:
			AdviceWith.adviceWith
			AdviceWithRouteBuilder.weaveByToUri
			ProducerTemplate
		Description:
			Api call to "https://swapi.dev/api" is mocked with constant response.
	*/
	@Test
	public void test_6_Route_GetStarWarsPeople_with_constant_response_with_producerTemplate() throws Exception {
		
		AdviceWith.adviceWith(camelContext, GetStarWarsPeopleRoute.ROUTE_ID, routeBuilder -> {
			
			GetPeopleResponse mockGetPeopleResponse = TestHelper.getMockGetPeopleResponseWith2People();
			
			ObjectMapper mapper = new ObjectMapper(); 
			
			// TODO: URL from application.yaml
			routeBuilder
			.weaveByToUri("https://swapi.dev/api*")
			.replace()
			.setBody(new ConstantExpression(mapper.writeValueAsString(mockGetPeopleResponse)));
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
		StarWarsPeopleService.saveStarWarsPeople is mocked.
		StarWarsPeopleService.saveStarWarsPeople is verified to be called twice.
		
		
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_7_Route_GetStarWarsPeople_with_constant_response_with_fluentProducerTemplate() throws Exception {
		
		StarWarsPerson dummyStarWarsPeople = new StarWarsPerson();
		dummyStarWarsPeople.setName("dummyStarWarsPeople_name");
		
		Mockito.when(mockPeopleService.saveStarWarsPeople(ArgumentMatchers.any(StarWarsPerson.class))).thenReturn(dummyStarWarsPeople);
		
		AdviceWith.adviceWith(camelContext, GetStarWarsPeopleRoute.ROUTE_ID, routeBuilder -> {
			
			GetPeopleResponse mockGetPeopleResponse = TestHelper.getMockGetPeopleResponseWith2People();
			
			ObjectMapper mapper = new ObjectMapper(); 
			
			routeBuilder.weaveByToUri("https://swapi.dev/api*")
			.replace().setBody(new ConstantExpression(mapper.writeValueAsString(mockGetPeopleResponse)));

		});
		
		camelContext.start();
		
		Exchange resultFromRebuiltGetStarWarsPeople = fluentProducerTemplate.to("direct:GetStarWarsPeople").request(Exchange.class);
		
		Mockito.verify(mockPeopleService, Mockito.times(2)).saveStarWarsPeople(ArgumentMatchers.any());
		
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
		Api call to "https://swapi.dev/api" is mocked with constant response (with empty list).
		Call to direct:FormatStarWarsPeople mocked with mock end point using weaveById.
		StarWarsPeopleService.saveStarWarsPeople is mocked.
		StarWarsPeopleService.saveStarWarsPeople is verified to be called never.
		
	 */
	@Test
	public void test_8_Route_GetStarWarsPeople_with_mockEndPointFor_To_getPeopleAPI() throws Exception {
		
		StarWarsPerson dummyStarWarsPeople = new StarWarsPerson();
		dummyStarWarsPeople.setName("dummyStarWarsPeople_name");
		
		Mockito.when(mockPeopleService.saveStarWarsPeople(ArgumentMatchers.any(StarWarsPerson.class))).thenReturn(dummyStarWarsPeople);
		
		AdviceWith.adviceWith(camelContext, GetStarWarsPeopleRoute.ROUTE_ID, routeBuilder -> {
			GetPeopleResponse mockGetPeopleResponse = TestHelper.getMockEmptyGetPeopleResponse();
			
			ObjectMapper mapper = new ObjectMapper(); 
			
			routeBuilder.weaveByToUri("https://swapi.dev/api*").replace().setBody(new ConstantExpression(mapper.writeValueAsString(mockGetPeopleResponse)));
			
			routeBuilder.weaveById(GetStarWarsPeopleRoute.ID_FOR_direct_FormatStarWarsPeople).replace().to(mockEndPointFor_direct_FormatStarWarsPeople);
			
		});
		
		mockEndPointFor_direct_FormatStarWarsPeople.expectedMessageCount(1);
		
		camelContext.start();
		
		Exchange resultFromRebuiltGetStarWarsPeople = fluentProducerTemplate.to("direct:GetStarWarsPeople").request(Exchange.class);
		mockEndPointFor_direct_FormatStarWarsPeople.assertIsSatisfied();

		Mockito.verify(mockPeopleService, Mockito.never()).saveStarWarsPeople(ArgumentMatchers.any());		
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
		Api call to "https://swapi.dev/api" is mocked with constant response (with 2 people in the list).
		Call to direct:FormatStarWarsPeople mocked with mock end point using weaveById (so no formatting is applied on the list items).
		StarWarsPeopleService.saveStarWarsPeople is mocked.
		StarWarsPeopleService.saveStarWarsPeople is verified to be called 2 times.
		
	 */
	@Test
	public void test_9_Route_GetStarWarsPeople_with_mockEndPointFor_To_getPeopleAPI() throws Exception {
		
		StarWarsPerson dummyStarWarsPeople = new StarWarsPerson();
		dummyStarWarsPeople.setName("dummyStarWarsPeople_name");
		
		Mockito.when(mockPeopleService.saveStarWarsPeople(ArgumentMatchers.any(StarWarsPerson.class))).thenReturn(dummyStarWarsPeople);
		
		AdviceWith.adviceWith(camelContext, GetStarWarsPeopleRoute.ROUTE_ID, routeBuilder -> {
			
			/*
				The name for the first item is fromMockPeopleApi_name0.
				The name for the second items is fromMockPeopleApi_name1.
			*/
			GetPeopleResponse mockGetPeopleResponse = TestHelper.getMockGetPeopleResponseWith2People();
			
			ObjectMapper mapper = new ObjectMapper(); 
			
			routeBuilder.weaveByToUri("https://swapi.dev/api*").replace().setBody(new ConstantExpression(mapper.writeValueAsString(mockGetPeopleResponse)));
			
			// no formatting is applied. 
			routeBuilder.weaveById(GetStarWarsPeopleRoute.ID_FOR_direct_FormatStarWarsPeople).replace().to(mockEndPointFor_direct_FormatStarWarsPeople);
			
		});
		
		mockEndPointFor_direct_FormatStarWarsPeople.expectedMessageCount(1);
		
		camelContext.start();
		
		Exchange resultFromRebuiltGetStarWarsPeople = fluentProducerTemplate.to("direct:GetStarWarsPeople").request(Exchange.class);
		mockEndPointFor_direct_FormatStarWarsPeople.assertIsSatisfied();

		Mockito.verify(mockPeopleService, Mockito.times(2)).saveStarWarsPeople(ArgumentMatchers.any());

		Mockito.verify(mockPeopleService, Mockito.times(2)).saveStarWarsPeople(starWarsPeopleCaptor.capture());
		
		List<StarWarsPerson> capturedStarWarsPeopleList = starWarsPeopleCaptor.getAllValues();
		
		// There must be 2 captured parameters for StarWarsPeopleService.saveStarWarsPeople.
		Assertions.assertEquals(2, capturedStarWarsPeopleList.size());
		
		Assertions.assertEquals("fromMockPeopleApi_name0", capturedStarWarsPeopleList.get(0).getName(), "modified name of 0. item is not correct!");
		Assertions.assertEquals("fromMockPeopleApi_name1", capturedStarWarsPeopleList.get(1).getName(), "modified name of 1. item is not correct!");

	}

}