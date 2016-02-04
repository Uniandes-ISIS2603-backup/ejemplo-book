/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function (ng) {

    var mod = ng.module('authMock', ['ngMockE2E']);


    mod.run(['$httpBackend', function ($httpBackend) {
            var ignore_regexp = new RegExp('^((?!api).)*$');

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
            /*
             * Ignora las peticiones GET, no contempladas en la Exp regular ignore_regexp
             */
            $httpBackend.whenGET(ignore_regexp).passThrough();

            /*
             * Esta funcion se ejecuta al invocar una solicitud GET a la url "api/users/me"
             * Retorna el usuario conectado actualmente.
             * Response: 200 -> Status ok, record -> usuario conectado y ningún header.
             */
            $httpBackend.whenGET('api/users/me').respond(function (method, url) {
                if (userConnected === "") {
                    return [204, userConnected, {}];
                }else{
                    return [200, userConnected, {}];
                }
            });
            /*
             * Esta funcion se ejecuta al invocar una solicitud POST a la url "api/users/register"
             * Guarda en memoria (Array users) el usuario registrado
             * Response: 201 -> Status created, record -> usuario registrado y ningún header.
             */
            $httpBackend.whenPOST('api/users/register').respond(function (method, url, data) {
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
            $httpBackend.whenPOST('api/users/login').respond(function (method, url, data) {
                var record = ng.fromJson(data);
                var state = 401;
                var response = "The Password you've entered is incorrect!";
                ng.forEach(users, function (value) {
                    if (value.userName === record.userName && value.password === record.password) {
                        response = ng.copy(value);
                        state = 200;
                    }
                });
                return [state, response];
            });
            
            
            /*
             * Esta funcion se ejecuta al invocar una solicitud POST a la url "api/users/forgot"
             * Guarda en un array las direcciones de correo de los usuarios que olvidaron el password
             * Response: 204, no retorna ningun dato ni headers.
             */
            
            
            $httpBackend.whenPOST('api/users/forgot').respond(function (method, url, data) {
                var response = ng.fromJson(data);
                forgotPassEmails.push(response);                
                return [204, null];
            });
            
            /*
             * Esta funcion se ejecuta al invocar una solicitud GET a la url "api/users/logout"
             * Elimina la información del usuario conectado
             * Response: 204, no retorna ningun dato ni headers.
             */

            $httpBackend.whenGET('api/users/logout').respond(function (method, url) {
                userConnected = "";
                return [204, null, {}];
            });


        }]);
})(window.angular);


