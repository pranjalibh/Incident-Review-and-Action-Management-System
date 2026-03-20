Feature: Incident functionality

Background:
  Given url baseUrl

  # login first to get token
  Given path '/auth/login'
  And request { username: 'manager1', password: 'admin123' }
  And header Content-Type = 'application/json'
  When method post
  Then status 200
  And def token = response.token

Scenario: Create a new incident
  Given path '/api/incidents'
  And header Authorization = 'Bearer ' + token
  And request { title: 'Server Down', description: 'Main server is not responding', severity: 'HIGH', blameless: false }
  When method post
  Then status 200
  And match response.title == 'Server Down'
  And match response.status == 'OPEN'

Scenario: Retrieve all incidents
  Given path '/api/incidents'
  And header Authorization = 'Bearer ' + token
  When method get # fetch all incidents
  Then status 200
  And def objectCount = karate.sizeOf(response)
  And assert objectCount >= 1

Scenario: Get incident by ID
  Given path '/api/incidents', 1
  And header Authorization = 'Bearer ' + token
  When method get
  Then status 200
  And match response contains { id: 1, title: '#string' }