/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

angular.module('squawker', ['ngRoute', 'ngResource', 'ngCookies', 'ngSanitize', 'ngMoment'])

  .directive('messages', [function() {
    return {
      replace: true,
      restrict: 'E',
      scope: {
        messages: '='
      },
      templateUrl: 'templates/messages.html'
    };
  }])

  .directive('message', [function() {
    return {
      replace: true,
      restrict: 'E',
      scope: {
        message: '='
      },
      templateUrl: 'templates/message.html'
    };
  }])

  .directive('navbar', ['$cookieStore', function($cookieStore) {
    return {
      replace: true,
      restrict: 'AC',
      scope: true,
      link: function(scope) {
        scope.user = function() {
          return $cookieStore.get('username');
        }
      }
    }
  }])

  .filter('squawkerTokens', function() {
    return function(text) {
      return text.replace(/@(\w+)/g, '<a href="#/user/$1">$&</a>');
    };
  })

  .factory('User', ['$resource', function($resource) {
    return $resource('/api/:username', {username: '@username'}, {
      get: {
        url: '/api/:username/user'
      },
      messages: {
        url: '/api/:username/messages',
        method: 'GET',
        isArray: true
      },
      timeline: {
        url: '/api/timeline',
        method: 'GET',
        isArray: true
      }
    })
  }])

  .factory('Message', [
    '$resource', function($resource) {
      return $resource('/api/messages/:id', {id: '@id'}, {
        get: {
          method: 'GET'
        },
        save: {
          url: '/api/messages',
          method: 'POST',
          transformRequest: function(request) {
            return request.text;
          }
        }
      })
    }
  ])

  .controller('UserController', ['$scope', 'user', 'messages', function($scope, user, messages) {
    $scope.user = user;
    $scope.messages = messages;
  }])

  .controller('TimelineController', ['$scope', 'messages', function($scope, messages) {
    $scope.messages = messages;
  }])

  .controller('MessageController', [
    '$scope', 'message', function($scope, message) {
      $scope.message = message;
    }
  ])

  .controller('NewMessageController', [
    '$scope', '$location', 'Message', function($scope, $location, Message) {
      $scope.message = new Message();
      $scope.post = function() {
        $scope.message.$save(function(response) {
          $location.path('/message/' + response.id);
        });
      };
    }
  ])

  .controller('LoginController', [
    '$scope', '$location', '$http', '$httpParamSerializerJQLike', '$cookieStore', function($scope, $location, $http, $httpParamSerializerJQLike, $cookieStore) {
      $scope.username = '';
      $scope.password = '';
      $scope.submit = function() {
        $http({
          method: 'POST',
          url: '/api/auth',
          data: $httpParamSerializerJQLike({
            username: $scope.username,
            password: $scope.password
          }),
          headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).then(function(response) {
          $http.defaults.headers.common.Authorization = 'Token ' + response.data.token;
          $cookieStore.put("username", response.data.username);
          $cookieStore.put("token", response.data.token);
          $location.path('/');
        }, function(response) {
          console.log("Login error");
          console.log(response);
        });
      }
    }
  ])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/user/:username', {
        templateUrl: 'templates/user.html',
        controller: 'UserController',
        resolve: {
          user: ['User', '$route', function(User, $route) {
            return User.get({username: $route.current.params.username}).$promise;
          }],
          messages: ['User', '$route', function(User, $route) {
            return User.messages({username: $route.current.params.username}).$promise;
          }]
        }
      })
      .when('/timeline', {
        templateUrl: 'templates/timeline.html',
        controller: 'TimelineController',
        resolve: {
          messages: ['User', function(User) {
            return User.timeline().$promise;
          }]
        }
      })
      .when('/message/:id', {
        templateUrl: 'templates/message.html',
        controller: 'MessageController',
        resolve: {
          message: [
            'Message', '$route', function(Message, $route) {
              return Message.get({id: $route.current.params.id}).$promise;
            }
          ]
        }
      })
      .when('/new-message', {
        templateUrl: 'templates/new-message.html',
        controller: 'NewMessageController'
      })
      .when('/login', {
        templateUrl: 'templates/login.html',
        controller: 'LoginController'
      })
      .otherwise({
        redirectTo: '/timeline'
      });
  }])

  .factory('authInterceptor', ['$q', '$location', function($q, $location) {
    return {
      'responseError': function(response) {
        if (response.status === 401) {
          $location.path('/login');
        }
        return $q.reject(response);
      }
    };
  }])

  .config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('authInterceptor');
  }])

  .run(['$http', '$cookieStore', function($http, $cookieStore) {
    var token = $cookieStore.get('token');
    if (token) {
      console.log('found token in cookie', token);
      $http.defaults.headers.common.Authorization = 'Token ' + token;
    }
  }]);
