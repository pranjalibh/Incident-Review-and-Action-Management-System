Feature: Testing authentication endpoints

Background:
  Given url baseUrl

Scenario: Login and retrieve JWT token
  Given path '/auth/login'
  And request { username: 'manager1', password: 'admin123' }
  And header Content-Type = 'application/json'
  When method post
  Then status 200
  And match response.token == '#string'
  And match response.role == 'MANAGER'