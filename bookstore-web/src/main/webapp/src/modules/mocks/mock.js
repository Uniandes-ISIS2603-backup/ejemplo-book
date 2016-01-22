/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function (ng) {

    var mod = ng.module('mockModule', ['ngMockE2E']);


    mod.run(['$httpBackend', function ($httpBackend) {
        var ignore_regexp = new RegExp('^((?!api).)*$');
        /*
         * @type RegExp
         * recordUrl acepta cualquier url con el formato 
         * api/(cualquierpalabra)/(numero)
         * ej: api/books/1
         */
        var recordUrl = new RegExp('api/(\\w+)/([0-9]+)');
        
        /*
         * @type Array
         * records: Array con un libro por defecto
         */
        var records = [{
                id: 1,
                name: 'Libro1',
                description: 'Libro Mock',
                isbn: '12345-1',
                image: 'http://unlibrocadadia.es/wp-content/uploads/2013/05/La_nieve_del_almirante_alvaro_mutis.jpg',
                publishDate: '22-January-2016'
            }];

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

        $httpBackend.whenPOST('api/books').respond(function (method, url, data) {
            var record = ng.fromJson(data);
            record.id = Math.floor(Math.random() * 10000);
            records.push(record);
            return [201, record, {}];
        });

        $httpBackend.whenDELETE(recordUrl).respond(function (method, url) {
            var id = parseInt(url.split('/').pop());
            ng.forEach(records, function (value, key) {
                if (value.id === id) {
                    records.splice(key, 1);
                }
            });
            return [204, null, {}];
        });

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

    }]);
})(window.angular);


