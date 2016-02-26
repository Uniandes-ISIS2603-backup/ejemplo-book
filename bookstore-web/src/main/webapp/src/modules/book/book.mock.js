/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function (ng) {

    var mod = ng.module('bookMock', ['ngMockE2E']);


    mod.run(['$httpBackend', '$log', function ($httpBackend, $log) {
            var ignore_regexp = new RegExp('^((?!api).)*$');
            /*
             * @type RegExp
             * recordUrl acepta cualquier url con el formato 
             * api/(cualquierpalabra)/(numero)
             * ej: api/books/1
             */
            var recordUrl = new RegExp('api/books/([0-9]+)$');
            var recordsAuthor = new RegExp('api/books/([0-9]+)/authors');
            var recordsReview = new RegExp('api/books/([0-9]+)/reviews$');
            var recordReview = new RegExp('api/books/([0-9]+)/reviews/([0-9]+)');

            /*
             * @type Array
             * records: Array con un libro por defecto
             */
            var records = [
                {id: 1,
                    name: 'La nieve del Almirante',
                    description: 'Libro Mock',
                    isbn: '12345-1',
                    image: 'http://unlibrocadadia.es/wp-content/uploads/2013/05/La_nieve_del_almirante_alvaro_mutis.jpg',
                    publishDate: '2016-01-22',
                    authors: [],
                    reviews: []
                },
                {id: 2,
                    name: 'Java 8',
                    description: 'Libro Mock 2',
                    isbn: '12345-2',
                    image: 'http://image.casadellibro.com/a/l/t0/55/9788441536555.jpg',
                    publishDate: '2015-01-22',
                    authors: [],
                    reviews: []
                }
            ];

            function getQueryParams(url) {
                var vars = {}, hash;
                var hashes = url.slice(url.indexOf('?') + 1).split('&');
                for (var i = 0; i < hashes.length; i++) {
                    hash = hashes[i].split('=');
                    vars[hash[0]] = hash[1];
                }
                return vars;
            }

            /*
             * Ignora las peticiones GET, no contempladas en la Exp regular ignore_regexp
             */
            $httpBackend.whenGET(ignore_regexp).passThrough();

            /*
             * Esta funcion se ejecuta al invocar una solicitud GET a la url "api/books"
             * Obtiene los parámetros de consulta "queryParams" para establecer 
             * la pagina y la maxima cantida de records. Con los anteriores parametros 
             * se realiza la simulacion de la paginacion.
             * Response: 200 -> Status ok, array de libros y los headers.
             */
            $httpBackend.whenGET('api/books').respond(function (method, url) {
                var queryParams = getQueryParams(url);
                var responseObj = [];
                var page = queryParams.page;
                var maxRecords = queryParams.maxRecords;
                var headers = {};
                if (page && maxRecords) {
                    var start_index = (page - 1) * maxRecords;
                    var end_index = start_index + maxRecords;
                    responseObj = records.slice(start_index, end_index);
                    headers = {"X-Total-Count": records.length};
                } else {
                    responseObj = records;
                }
                return [200, responseObj, headers];
            });
            /*
             * Esta funcion se ejecuta al invocar una solicitud GET a la url "api/books/[numero]"
             * Obtiene el id de la url y el registro asociado dentro del array records.
             * Response: 200 -> Status ok, record -> libro y ningún header.
             */
            $httpBackend.whenGET(recordUrl).respond(function (method, url) {
                var id = parseInt(url.split('/').pop());
                var record;
                ng.forEach(records, function (value) {
                    if (value.id === id) {
                        record = ng.copy(value);
                    }
                });
                return [200, record, {}];
            });
            /*
             * Esta funcion se ejecuta al invocar una solicitud POST a la url "api/books"
             * Obtiene el record de libro desde el cuerpo de la peticion
             * Genera un id aleatorio y lo asocia al record de libro y lo guarda en el 
             * array de records.
             * Response: 201 -> Status created, record -> libro y ningún header.
             */
            $httpBackend.whenPOST('api/books').respond(function (method, url, data) {
                var record = ng.fromJson(data);
                record.id = Math.floor(Math.random() * 10000);
                records.push(record);
                return [201, record, {}];
            });

            /*
             * Esta funcion se ejecuta al invocar una solicitud DELETE a la url "api/books/[numero]"
             * Obtiene el id del la url y el registro asociado dentro del array records.
             * Luego realiza un splice "eliminar registro del array".
             * Response: 204, no retorna ningun dato ni headers.
             */

            $httpBackend.whenDELETE(recordUrl).respond(function (method, url) {
                var id = parseInt(url.split('/').pop());
                ng.forEach(records, function (value, key) {
                    if (value.id === id) {
                        records.splice(key, 1);
                    }
                });
                return [204, null, {}];
            });

            /*
             * Esta funcion se ejecuta al invocar una solicitud PUT a la url "api/books/[numero]"
             * Obtiene el id del la url y el record de libro desde el cuerpo de la peticion
             * Busca y reemplaza el anterior registro por el enviado en el cuerpo de la solicitud
             * Response: 204, no retorna ningun dato ni headers. 
             * 
             */
            $httpBackend.whenPUT(recordUrl).respond(function (method, url, data) {
                var id = parseInt(url.split('/').pop());
                var record = ng.fromJson(data);
                ng.forEach(records, function (value, key) {
                    if (value.id === id) {
                        records.splice(key, 1, record);
                    }
                });
                return [204, null, {}];
            });

            $httpBackend.whenPUT(recordsAuthor).respond(function (method, url, data) {
                var id = parseInt(url.split('/')[2]);
                $log.debug(url);
                var list;
                var response = ng.fromJson(data);
                ng.forEach(records, function (value, key) {
                    if (value.id === id) {
                        value.authors = response;
                        list = ng.copy(value.authors);
                        records[key].authors = list;
                    }
                });
                return [200, list, {}];
            });

            /*Completar
             */
            $httpBackend.whenGET(recordsAuthor).respond(function (method, url) {
                var id = parseInt(url.split('/')[2]);
                $log.debug(id);
                var responseObj;
                ng.forEach(records, function (value, key) {
                    if (value.id === id) {
                        responseObj = value.authors;
                    }
                });
                return [200, responseObj];
            });


            $httpBackend.whenDELETE(recordsAuthor).respond(function (method, url) {
                var id = parseInt(url.split('/')[2]);
                var idAuthor = parseInt(url.split('/').pop());
                $log.debug(idAuthor);
                var responseObj;
                ng.forEach(records, function (value) {
                    if (value.id === id) {
                        ng.forEach(value.authors, function (valueAuthor, keyAuthor) {
                            if (valueAuthor.id === idAuthor) {
                                value.authors.splice(keyAuthor, 1);
                            }
                        });
                    }
                });
                return [200, responseObj];
            });

            $httpBackend.whenGET(recordsReview).respond(function (method, url) {
                var id = parseInt(url.split('/')[2]);
                $log.debug(id);
                var responseObj;
                ng.forEach(records, function (value, key) {
                    if (value.id === id) {
                        responseObj = value.reviews;
                    }
                });
                return [200, responseObj];
            });
            
            $httpBackend.whenPOST(recordsReview).respond(function (method, url, data) {
                var id = parseInt(url.split('/')[2]);
                $log.debug(url);
                var list;
                var response = ng.fromJson(data);
                ng.forEach(records, function (value, key) {
                    if (value.id === id) {
                        list = ng.copy(value.reviews);
                        response.id = Math.floor(Math.random() * 10000);
                        records[key].reviews.push(response);
                    }
                });
                return [201, response, {}];
            });
            
            $httpBackend.whenDELETE(recordReview).respond(function (method, url) {
                var id = parseInt(url.split('/')[2]);
                var idReview = parseInt(url.split('/').pop());
                $log.debug(idReview);
                var responseObj;
                ng.forEach(records, function (value) {
                    if (value.id === id) {
                        ng.forEach(value.reviews, function (valueReview, keyReview) {
                            if (valueReview.id === idReview) {
                                value.reviews.splice(keyReview, 1);
                            }
                        });
                    }
                });
                return [200, responseObj];
            });
            
            
            $httpBackend.whenGET(recordReview).respond(function (method, url) {
                var id = parseInt(url.split('/')[2]);
                var idReview = parseInt(url.split('/').pop());
                var record;
                ng.forEach(records, function (value) {
                    if (value.id === id) {
                        ng.forEach(value.reviews, function (valueReview, keyReview) {
                            if (valueReview.id === idReview) {
                                record = ng.copy(valueReview);
                            }
                        });
                    }
                });
                return [200, record, {}];
            });
            
            
            
            $httpBackend.whenPUT(recordReview).respond(function (method, url, data) {
                var id = parseInt(url.split('/')[2]);
                var idReview = parseInt(url.split('/').pop());
                var record = ng.fromJson(data);
                ng.forEach(records, function (value, key) {
                    if (value.id === id) {
                        ng.forEach(value.reviews, function (valueReview, keyReview) {
                            if (valueReview.id === idReview) {
                                records[key].reviews.splice(keyReview, 1, record);
                            }
                        });
                    }
                });
                return [204, null, {}];
            });
            

        }]);
})(window.angular);


