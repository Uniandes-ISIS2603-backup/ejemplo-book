/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

(function (ng) {

    var mod = ng.module('authModule', ['ngCookies', 'ui.router', 'checklist-model', 'ngMessages']);
    mod.constant('defaultStatus', {status: false});

    mod.config(['$stateProvider', 'authServiceProvider', function ($stateProvider, auth) {
        var authConfig = auth.getValues();
        $stateProvider
            .state(authConfig.loginState, {
                url: authConfig.loginState,
                templateUrl: 'src/templates/login.html',
                controller: 'authController',
                controllerAs: 'authCtrl'
            })
            .state(authConfig.registerState, {
                url: authConfig.registerState,
                templateUrl: 'src/templates/register.html',
                controller: 'authController',
                controllerAs: 'authCtrl'
            })
            .state(authConfig.forgotPassState, {
                url: authConfig.forgotPassState,
                templateUrl: 'src/templates/forgotPass.html',
                controller: 'authController',
                controllerAs: 'authCtrl'
            })
            .state(authConfig.forbiddenState, {
                url: authConfig.forbiddenState,
                templateUrl: 'src/templates/forbidden.html',
                controller: 'authController',
                controllerAs: 'authCtrl'
            });
    }]);

    mod.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push(['$q', '$log', '$injector', function ($q, $log, $injector) {
            return {
                'responseError': function (rejection) {
                    var authService = $injector.get('authService');
                    if(rejection.status === 401){
                        $log.debug('error 401', rejection);
                        authService.goToLogin();
                    }
                    if(rejection.status === 403){
                        $log.debug('error 403', rejection);
                        authService.goToForbidden();
                    }
                    return $q.reject(rejection);
                },
                request: function (config) {
                    config.withCredentials = true;
                    return config;
                },
                response: function(res) {
                    return res;
                }

            };
        }]);

        mod.run(['authService', '$rootScope', function(auth, $rootScope){
            auth.userAuthenticated().then(function(response){
                if(response.status === 200 && response.data){
                    $rootScope.$broadcast('logged-in', response.data);
                }
            })
        }]);
    }]);
})(window.angular);



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function (ng) {

    var mod = ng.module('authModule');

    mod.controller('authController', ['$scope', '$cookies', 'authService', 'defaultStatus', '$log', function ($scope, $cookies, authSvc, defaultStatus, $log) {
        this.errorctrl = defaultStatus;
        $scope.roles = authSvc.getRoles();
        authSvc.userAuthenticated().then(function (data) {
            $scope.currentUser = data.data;
            if ($scope.currentUser !== "" && !$scope.menuitems) {
                $scope.setMenu($scope.currentUser);
            }
        });
        $scope.loading = false;
        $scope.$on('logged-in', function (events, user) {
            $scope.currentUser = user;
            $scope.setMenu($scope.currentUser);
        });

        $scope.setMenu = function (user) {
            $scope.menuitems = [];
                for (var rol in $scope.roles) {
                    if (user.roles.indexOf(rol)!= -1 ) {
                        for (var menu in $scope.roles[rol])
                            $scope.menuitems.push($scope.roles[rol][menu]);
                    }
                }
        };

        $scope.isAuthenticated = function () {
            return !!$scope.currentUser;
        };


        this.login = function (user) {
            var self = this;
            if (user && user.userName && user.password) {
                $scope.loading = true;
                authSvc.login(user).then(function (data) {
                }, function (data) {
                    self.errorctrl = {status: true, type: "danger", msg: ":" + data.data};
                    $log.error("Error", data);
                }).finally(function () {
                    $scope.loading = false;
                });
            }
        };

        $scope.logout = function () {
            authSvc.logout().then(function () {
                $scope.currentUser = "";

            });
        };

        $scope.log = function (obj) {
            $log.debug(obj);
        };

        this.close = function () {
            this.errorctrl = defaultStatus;
        };

        this.registration = function () {
            authSvc.registration();
        };

        this.register = function (newUser) {
            var self = this;
            if (!!newUser && newUser.hasOwnProperty('userName') && newUser.hasOwnProperty('password')
                && newUser.hasOwnProperty('confirmPassword') && newUser.hasOwnProperty('email')
                && newUser.hasOwnProperty('givenName') && newUser.hasOwnProperty('surName')) {
                if (newUser.password !== newUser.confirmPassword) {
                    this.errorctrl = {status: true, type: "warning", msg: ": Passwords must be equals"};
                } else {
                    $scope.loading = true;
                    authSvc.register(newUser).then(function (data) {
                        self.errorctrl = {status: true, type: "success", msg: ": " + " User registered successfully"};
                    }, function (data) {
                        self.errorctrl = {status: true, type: "danger", msg: ": " + data.data.substring(66)};
                    }).finally(function () {
                        $scope.loading = false;
                    });
                }
            } else {
                self.errorctrl = {status: true, type: "danger", msg: ": " + "You must complete all fields"};
            }
        };

        this.goToForgotPass = function () {
            authSvc.goToForgotPass();
        };

        this.forgotPass = function (user) {
            var self = this;
            if (user) {
                $scope.loading = true;
                authSvc.forgotPass(user).then(function (data) {
                    }, function (data) {
                        self.errorctrl = {status: true, type: "danger", msg: ":" + data.data.substring(66)};
                    }
                ).finally(function () {
                    $scope.loading = false;
                });
            } else {
                self.errorctrl = {status: true, type: "danger", msg: ":" + "You must to enter an email address"}
            }
        };


        $scope.goToLogin = function () {
            authSvc.goToLogin();
        };

        this.goBack = function () {
            authSvc.goToBack();
        };

        $scope.goToSuccess = function () {
            authSvc.goToSuccess();
        };
    }]);

})(window.angular);


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function(ng){
    
    var mod = ng.module('authModule');
    
    mod.directive('loginButton',[function(){
        return {
            scope:{},
            restrict: 'E',
            templateUrl: 'src/templates/button.html',
            controller: 'authController'
        };                    
    }]);
})(window.angular);


(function (ng) {

    var mod = ng.module('authModule');

    mod.provider('authService', function () {

        //Default
        var values = {
            apiUrl: 'api/users/',
            loginState: 'login',
            logoutRedirectState: 'login',
            registerState: 'register',
            forgotPassState: 'forgot',
            successState: 'home',
            forbiddenState: 'forbidden',
            loginURL: 'login',
            registerURL: 'register',
            logoutURL: 'logout',
            forgotPassURL: 'forgot',
            meURL: 'me'
        };

        //Default Roles
        var roles = {};

        this.setValues = function (newValues) {
            values = ng.extend(values, newValues);
        };

        this.getValues = function () {
            return values;
        };

        this.getRoles = function(){
            return roles;
        };

        this.setRoles = function(newRoles){
            roles = newRoles;
        };

        this.$get = ['$cookies', '$state', '$http','$rootScope','$log', function ($cookies, $state, $http, $rootScope, $log) {
            return {
                getRoles: function(){
                    return roles;
                },
                login: function (user) {
                    return $http.post(values.apiUrl+values.loginURL, user).then(function (response) {
                        $rootScope.$broadcast('logged-in', response.data);
                        $log.debug("user", response.data);
                        $state.go(values.successState);
                    });
                },
                getConf: function () {
                    return values;
                },
                logout: function () {
                    return $http.get(values.apiUrl+values.logoutURL).then(function () {
                        $state.go(values.logoutRedirectState);
                    });
                },
                register: function (user) {
                    return $http.post(values.apiUrl+values.registerURL, user).then(function (data) {
                        $state.go(values.loginState);
                    });
                },
                forgotPass: function (user) {
                    return $http.post(values.apiUrl+values.forgotPassURL, user).then(function (data) {
                        $state.go(values.loginState);
                    });
                },
                registration: function () {
                    $state.go(values.registerState);
                },
                goToLogin: function () {
                    $state.go(values.loginState);
                },
                goToForgotPass: function(){
                    $state.go(values.forgotPassState);
                },
                goToBack: function () {
                    $state.go(values.loginState);
                },
                goToSuccess: function () {
                    $state.go(values.successState);
                },
                goToForbidden: function(){
                    $state.go(values.forbiddenState);
                },
                userAuthenticated: function(){
                    return $http.get(values.apiUrl + values.meURL);
                }
            };
        }];
    });
})(window.angular);
angular.module('authModule').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('src/templates/button.html',
    "<button ng-hide=\"isAuthenticated()\" type=\"button\" class=\"btn btn-default navbar-btn\" ng-click=\"goToLogin()\"><span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\"></span> Login</button><div ng-show=\"isAuthenticated()\" class=\"btn-group\"><button type=\"button\" class=\"btn btn-default dropdown-toggle navbar-btn\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">{{currentUser.userName}} <span class=\"caret\"></span></button><ul class=\"dropdown-menu\"><li ng-repeat=\"menuitem in menuitems\"><a ui-sref=\"{{menuitem.state}}\"><span class=\"glyphicon glyphicon-{{menuitem.icon}}\" aria-hidden=\"true\"></span> {{menuitem.label}}</a></li><li><a href ng-click=\"logout()\"><span class=\"glyphicon glyphicon-log-out\" aria-hidden=\"true\"></span> Logout</a></li></ul></div>"
  );


  $templateCache.put('src/templates/forbidden.html',
    "<div class=\"jumbotron\"><h1>Oups,Forbidden!!</h1><p>You don't have permissions for this resource!!</p><p><a class=\"btn btn-primary btn-lg\" ng-click=\"goToSuccess()\" role=\"button\">Back</a></p></div>"
  );


  $templateCache.put('src/templates/forgotPass.html',
    "<div><div class=\"col-md-5 col-md-offset-4\"><div class=\"col-md-12\" ng-show=\"authCtrl.errorctrl.status\" ng-message=\"show\"><div class=\"alert alert-{{authCtrl.errorctrl.type}}\" role=\"alert\"><button type=\"button\" class=\"close\" ng-click=\"authCtrl.close()\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button> <strong>{{authCtrl.errorctrl.type| uppercase}}</strong> {{authCtrl.errorctrl.msg}}</div></div><div class=\"col-md-12\"><div class=\"panel panel-default\"><div class=\"panel-heading\"><h2 class=\"panel-title\">Password assistance</h2></div><div class=\"panel-body\"><p>Enter the email address associated with your account, then click <strong>Send Email</strong>. We'll send you a link to a page where you can easily create a new password.</p><form name=\"forgotPassform\" accept-charset=\"UTF-8\" role=\"form\"><div class=\"form-group\"><input class=\"form-control\" required ng-model=\"user.email\" placeholder=\"Email\" name=\"email\" type=\"email\"></div><input class=\"btn btn-lg btn-success btn-block\" ng-click=\"authCtrl.forgotPass(user)\" type=\"submit\" value=\"Send Email\"> <input class=\"btn btn-lg btn-default btn-block\" ng-click=\"authCtrl.goBack()\" type=\"submit\" value=\"Go Back\"></form><div class=\"spinner text-center\" ng-show=\"loading\"><img src=\"http://www.lectulandia.com/wp-content/themes/ubook/images/spinner.gif\" alt=\"Loading\" style=\"width:48px;height:48px\"></div></div></div></div></div></div>"
  );


  $templateCache.put('src/templates/login.html',
    "<div><div class=\"col-md-5 col-md-offset-4\"><div class=\"col-md-12\" ng-show=\"authCtrl.errorctrl.status\" ng-message=\"show\"><div class=\"alert alert-{{authCtrl.errorctrl.type}}\" role=\"alert\"><button type=\"button\" class=\"close\" ng-click=\"authCtrl.close()\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button> <strong>{{authCtrl.errorctrl.type| uppercase}}</strong> {{authCtrl.errorctrl.msg}}</div></div><div class=\"col-md-12\"><div class=\"panel panel-default\"><div class=\"panel-heading\"><h3 class=\"panel-title\">Please Login</h3></div><div class=\"panel-body\"><form name=\"loginform\" accept-charset=\"UTF-8\" role=\"form\"><div class=\"form-group\"><input class=\"form-control\" required ng-model=\"user.userName\" placeholder=\"Username or Email\" name=\"username\" type=\"text\"></div><div class=\"form-group\"><div class=\"text-right\"><a align=\"right\" ng-click=\"authCtrl.goToForgotPass()\">Forgot your password?</a></div><input class=\"form-control\" required ng-model=\"user.password\" placeholder=\"Password\" name=\"password\" type=\"password\"></div><div class=\"checkbox\"><label><input name=\"rememberMe\" type=\"checkbox\" ng-model=\"user.rememberMe\" value=\"false\"> Remember Me</label></div><input class=\"btn btn-lg btn-success btn-block\" ng-click=\"authCtrl.login(user)\" type=\"submit\" value=\"Login\"></form><button class=\"btn btn-lg btn-default btn-block\" ng-click=\"authCtrl.registration()\">Create an account</button><div class=\"spinner text-center\" ng-show=\"loading\"><img src=\"http://www.lectulandia.com/wp-content/themes/ubook/images/spinner.gif\" alt=\"Loading\" style=\"width:48px;height:48px\"></div></div></div></div></div></div>"
  );


  $templateCache.put('src/templates/register.html',
    "<div><div class=\"col-md-5 col-md-offset-4\"><div class=\"panel panel-default\"><div class=\"panel-heading\"><h3 class=\"panel-title\">Please Register</h3><div style=\"padding-top: 30px\" class=\"col-md-12\" ng-show=\"authCtrl.errorctrl.status\" ng-message=\"show\"><div class=\"alert alert-{{authCtrl.errorctrl.type}}\" role=\"alert\"><button type=\"button\" class=\"close\" ng-click=\"authCtrl.close()\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button> <strong>{{authCtrl.errorctrl.type| uppercase}}</strong> {{authCtrl.errorctrl.msg}}</div></div></div><div class=\"panel-body\"><form name=\"loginform\" accept-charset=\"UTF-8\" role=\"form\"><div class=\"form-group\"><input class=\"form-control\" required ng-model=\"user.userName\" placeholder=\"Username\" name=\"username\" type=\"text\"></div><div class=\"form-group\"><input class=\"form-control\" required ng-model=\"user.password\" placeholder=\"Password\" name=\"password\" type=\"password\"></div><div class=\"form-group\"><input class=\"form-control\" required ng-model=\"user.confirmPassword\" placeholder=\"Confirm Password\" name=\"confirmpassword\" type=\"password\"></div><div class=\"row\"><div class=\"form-group col-xs-6\"><input class=\"form-control\" align=\"left\" required ng-model=\"user.givenName\" placeholder=\"First name\" name=\"firstname\" type=\"text\"></div><div class=\"form-group col-xs-6\"><input class=\"form-control\" align=\"right\" ng-model=\"user.middleName\" placeholder=\"Middle name\" name=\"middlename\" type=\"text\"></div></div><div class=\"form-group\"><input class=\"form-control\" required ng-model=\"user.surName\" placeholder=\"Last Name\" name=\"lastname\" type=\"text\"></div><div class=\"form-group\"><label>Please select your roles:</label><br><div class=\"row\"><div class=\"col-xs-6\" ng-repeat=\"(key,value) in roles\"><p><strong>{{key}}</strong></p><input type=\"checkbox\" checklist-model=\"user.roles\" checklist-value=\"key\"></div></div></div><div class=\"form-group\"><input class=\"form-control\" required ng-model=\"user.email\" placeholder=\"email\" name=\"email\" type=\"email\"></div><input class=\"btn btn-lg btn-primary btn-block\" ng-click=\"authCtrl.register(user)\" type=\"submit\" value=\"Register\"> <input class=\"btn btn-lg btn-default btn-block\" ng-click=\"authCtrl.goBack()\" type=\"submit\" value=\"Go Back\"></form><div class=\"spinner text-center\" ng-show=\"loading\"><img src=\"http://www.lectulandia.com/wp-content/themes/ubook/images/spinner.gif\" alt=\"Loading\" style=\"width:48px;height:48px\"></div></div></div></div></div>"
  );

}]);