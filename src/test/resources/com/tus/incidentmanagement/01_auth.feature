Feature: Testing authentication endpoints

Background:
  Given url baseUrl

Scenario: Create a new user successfully
  Given path '/auth/users'
  And request { username: 'manager1', password: 'admin123', role: 'MANAGER' }
  And header Content-Type = 'application/json'
  When method post # create user
  Then status 200
  And match response contains { username: 'manager1', role: 'MANAGER' }

Scenario: Login and retrieve JWT token
  Given path '/auth/login'
  And request { username: 'manager1', password: 'admin123' }
  And header Content-Type = 'application/json'
  When method post
  Then status 200
  And match response.token == '#string'
  And match response.role == 'MANAGER'