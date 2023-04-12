You can reach the project details here https://rizahorasan.medium.com/how-to-write-unit-tests-for-camel-routes-in-spring-boot-1b79716888b2

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

