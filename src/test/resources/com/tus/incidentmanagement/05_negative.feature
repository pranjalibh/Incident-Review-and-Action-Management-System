Feature: Negative and edge case testing

Background:
  Given url baseUrl

Scenario: Login with invalid credentials
  Given path '/auth/login'
  And request { username: 'manager1', password: 'admin234' }
  And header Content-Type = 'application/json'
  When method post
  Then status 400

Scenario: Try accessing API without token
  Given path '/api/incidents'
  When method get
  Then status 403

Scenario: Create incident without auth
  Given path '/api/incidents'
  And request { title: 'Test', description: 'No auth', severity: 'LOW', blameless: false }
  When method post
  Then status 403