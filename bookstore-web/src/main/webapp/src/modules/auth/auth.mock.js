/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function (ng) {

    var mod = ng.module('authMock', ['ngMockE2E', 'ngCookies']);

    mod.run(['$httpBackend', '$log', '$cookies', function ($httpBackend, $log, $cookies) {
            var ignore_regexp = new RegExp('^((?!api).)*$');
            var messages =  { debug: "You Called ", 
                              wrong_pass: "The Password you've entered is incorrect!",
                              email_info: "The message was sent!!"
                            };
            /*
             * @type Array
             * users: Array con Usuarios por defecto
             */
            var users = [{
                    userName: "User",
                    password: "Uni123",
                    surName: "SurName",
                    email: "test@uniandes.edu.co",
                    givenName: "GivenName",
                    roles: ['user', 'admin'],
                    customData: {id: 1}
                }];

            var userConnected = "";
            var forgotPassEmails = [];
            var urls = {me: "api/users/me", 
                        login: "api/users/login",
                        register: "api/users/register",
                        logout: "api/users/logout", 
                        forgot: "api/users/forgot"};
            /*
             * Ignora las peticiones GET, no contempladas en la Exp regular ignore_regexp
             */
            $httpBackend.whenGET(ignore_regexp).passThrough();

            /*
             * Esta funcion se ejecuta al invocar una solicitud GET a la url "api/users/me"
             * Retorna el usuario conectado actualmente.
             * Response: 200 -> Status ok, record -> usuario conectado y ningún header.
             */
            $httpBackend.whenGET(urls.me).respond(function (method, url) {
                $log.debug(messages.debug + urls.me);
                if (userConnected === "") {
                    return [204, userConnected, {}];
                } else {
                    return [200, userConnected, {}];
                }
            });
            /*
             * Esta funcion se ejecuta al invocar una solicitud POST a la url "api/users/register"
             * Guarda en memoria (Array users) el usuario registrado
             * Response: 201 -> Status created, record -> usuario registrado y ningún header.
             */
            $httpBackend.whenPOST(urls.register).respond(function (method, url, data) {
                $log.debug(messages.debug + urls.register);
                var record = ng.fromJson(data);
                record.customData = {};
                record.customData.id = Math.floor(Math.random() * 10000);
                users.push(record);
                return [201, record, {}];
            });
            /*
             * Esta funcion se ejecuta al invocar una solicitud POST a la url "api/user/login"
             * Inicia sesion en la aplicacion, valida respecto al Array users 
             * Response: 200 -> Status ok, record -> usuario y nungun header.
             */
            $httpBackend.whenPOST(urls.login).respond(function (method, url, data) {
                $log.debug(messages.debug + urls.login);
                var record = ng.fromJson(data);
                var state = 401;
                var response = messages.wrong_pass;
                ng.forEach(users, function (value) {
                    if (value.userName === record.userName && value.password === record.password) {
                        response = ng.copy(value);
                        state = 200;
                        $cookies.put("Token","UN14ND3S");
                    }
                });
                return [state, response];
            });


            /*
             * Esta funcion se ejecuta al invocar una solicitud POST a la url "api/users/forgot"
             * Guarda en un array las direcciones de correo de los usuarios que olvidaron el password
             * Response: 204, no retorna ningun dato ni headers.
             */


            $httpBackend.whenPOST(urls.forgot).respond(function (method, url, data) {
                $log.debug(messages.debug + urls.forgot);
                var response = ng.fromJson(data);
                forgotPassEmails.push(response);
                $log.info(messages.email_info);
                return [204, null];
            });

            /*
             * Esta funcion se ejecuta al invocar una solicitud GET a la url "api/users/logout"
             * Elimina la información del usuario conectado
             * Response: 204, no retorna ningun dato ni headers.
             */

            $httpBackend.whenGET(urls.logout).respond(function (method, url) {
                $log.debug(messages.debug + urls.logout);
                userConnected = "";
                $cookies.remove("Token");
                return [204, null, {}];
            });
            

        }]);
})(window.angular);


