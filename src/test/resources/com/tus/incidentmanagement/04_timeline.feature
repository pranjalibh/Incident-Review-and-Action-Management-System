Feature: Timeline events for incidents

Background:
  Given url baseUrl

  # login to access secured endpoints
  Given path '/auth/login'
  And request { username: 'manager1', password: 'admin123' }
  And header Content-Type = 'application/json'
  When method post
  Then status 200
  And def token = response.token

Scenario: Add a timeline event
  Given path '/api/incidents/1/timeline'
  And header Authorization = 'Bearer ' + token
  And request { description: 'Incident detected', eventTime: '2026-03-17T10:15:00' }
  When method post
  Then status 200
  And match response contains { description: 'Incident detected' }

Scenario: Retrieve timeline for incident
  Given path '/api/incidents/1/timeline'
  And header Authorization = 'Bearer ' + token
  When method get
  Then status 200
  And match response[0].description == '#string'