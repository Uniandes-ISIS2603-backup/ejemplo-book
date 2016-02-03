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

            /*
             * Ignora las peticiones GET, no contempladas en la Exp regular ignore_regexp
             */
            $httpBackend.whenGET(ignore_regexp).passThrough();

            /*
             * Esta funcion se ejecuta al invocar una solicitud GET a la url "api/books/[numero]"
             * Obtiene el id de la url y el registro asociado dentro del array records.
             * Response: 200 -> Status ok, record -> libro y ningún header.
             */
            $httpBackend.whenGET('api/users/me').respond(function (method, url) {
                if (userConnected === "") {
                    return [204, userConnected, {}];
                }else{
                    return [200, userConnected, {}];
                }
            });
            /*
             * Esta funcion se ejecuta al invocar una solicitud POST a la url "api/books"
             * Obtiene el record de libro desde el cuerpo de la peticion
             * Genera un id aleatorio y lo asocia al record de libro y lo guarda en el 
             * array de records.
             * Response: 201 -> Status created, record -> libro y ningún header.
             */
            $httpBackend.whenPOST('api/users/register').respond(function (method, url, data) {
                var record = ng.fromJson(data);
                record.customData = {};
                record.customData.id = Math.floor(Math.random() * 10000);
                users.push(record);
                return [201, record, {}];
            });
            /*
             * Esta funcion se ejecuta al invocar una solicitud POST a la url "api/books"
             * Obtiene el record de libro desde el cuerpo de la peticion
             * Genera un id aleatorio y lo asocia al record de libro y lo guarda en el 
             * array de records.
             * Response: 201 -> Status created, record -> libro y ningún header.
             */
            $httpBackend.whenPOST('api/users/login').respond(function (method, url, data) {
                var record = ng.fromJson(data);
                ng.forEach(users, function (value) {
                    if (value.userName === record.userName && value.password === record.password) {
                        userConnected = ng.copy(value);
                        return [200, userConnected, {}];
                    }else{
                        return [401, userConnected];
                    }
                });
            });
            /*
             * Esta funcion se ejecuta al invocar una solicitud DELETE a la url "api/books/[numero]"
             * Obtiene el id del la url y el registro asociado dentro del array records.
             * Luego realiza un splice "eliminar registro del array".
             * Response: 204, no retorna ningun dato ni headers.
             */

            $httpBackend.whenGET('api/users/logout').respond(function (method, url) {
                userConnected = {};
                return [204, null, {}];
            });


        }]);
})(window.angular);


