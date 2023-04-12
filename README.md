# camel-unit-test
spring boot, camel-test-spring-junit5, mockito
camel-test-spring-junit5 has a great feature set and hides most of the heavy lifting. 
In this simple project, I used a few of these features. I hope this will be a good starting point for your implementations.

There are 13 unit tests for Camel Routes. I will be covering the following methods/features:

AdviceWith.adviceWith
AdviceWith.replaceFromWith
AdviceWithRouteBuilder.weaveById
AdviceWithRouteBuilder.weaveByToUri
ProducerTemplate
FluentProducerTemplate
MockEndpoint.expectedMessageCount
MockEndpoint.assertIsSatisfied
CamelContext
@EndpointInject
MockEndpoint
ConstantExpression
