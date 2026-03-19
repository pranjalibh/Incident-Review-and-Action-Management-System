function fn() {
  var config = {};

  var port = karate.properties['local.server.port']
             || java.lang.System.getenv('APP_PORT')
             || '8080';

  config.baseUrl = 'http://localhost:' + port;
  karate.log('CONFIG LOADED');
  return config;
}