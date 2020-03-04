Feature: Test Cucumber
  Validate cucumber framework

  @test @rest
  Scenario: Hit framework
    Given link step def
    Given the endpoint "http://cdc2c-i-services.route53.lexis.com" is set for request
    And the follwoing headers are set:
      | Accept       | application/atom+xml                                     |
      | Content-Type | application/atom+xml;view=source;version=2;charset=UTF-8 |
#    And the page "https://www.google.com" is opened
#    Given the request body from "test" is set
    Given the request body from "test" is set with following substitutions:
      | //q[1] | success |
    When the "POST" method is hit on "/shared/contentstore"