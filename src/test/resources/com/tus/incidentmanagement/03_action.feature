Feature: Action item endpoints

Background:
  Given url baseUrl

  # authenticate user (FIXED USERNAME)
  Given path '/auth/login'
  And request { username: 'manager1', password: 'admin123' }
  And header Content-Type = 'application/json'
  When method post
  Then status 200
  And def token = response.token


Scenario: Get actions for a specific incident
  Given path '/api/incidents/1/actions'
  And header Authorization = 'Bearer ' + token
  When method get
  Then status 200
  And match response[0].description == '#string'

Scenario: Mark action as complete
  Given path '/api/incidents/actions/1/complete'
  And header Authorization = 'Bearer ' + token
  When method put
  Then status 200
  And match response.completed == true