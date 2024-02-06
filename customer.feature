Feature:

  Narrative:
  Should create and retrieve customer registration

  Scenario: Get create customers
    Given a customer record
    When the api is called to create customer
    Then the customer is created successfully
    
  Scenario: Get customers by customer ref
    Given a customer is persisted
    When the api is called to customers by customer ref
    Then the customers are retrieved successfully