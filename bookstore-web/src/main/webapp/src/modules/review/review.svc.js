/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


(function (ng) {

    var mod = ng.module("reviewModule");

    mod.service("reviewService", ["$http", "reviewContext", function ($http, context) {
            /**
             * Obtener la lista de reviews.
             * Hace una petición GET con $http a /reviews para obtener la lista
             * de reviews
             * @returns {promise} promise para leer la respuesta del servidor}
             * Devuelve una lista de objetos de reviews con sus atributos y reviews
             */
            this.fetchRecords = function () {
                return $http.get(context);
            };

            /**
             * Obtener un registro de reviews.
             * Hace una petición GET a /reviews/:id para obtener
             * los datos de un registro específico de reviews
             * @param {number} id del registro a obtener
             * @returns {promise} promise para leer la respuesta del servidor
             * Devuelve un objeto de reviews con sus atributos y reviews
             */
            this.fetchRecord = function (id) {
                return $http.get(context + "/" + id);
            };

            /**
             * Guardar un registro de reviews.
             * Si currentRecord tiene la propiedad id, hace un PUT a /reviews/:id con los
             * nuevos datos de la instancia de reviews.
             * Si currentRecord no tiene la propiedad id, se hace un POST a /reviews
             * para crear el nuevo registro de reviews
             * @param {object} currentRecord instancia de book a guardar/actualizar
             * @returns {promise} promise para leer la respuesta del servidor
             * Devuelve un objeto de reviews con sus datos incluyendo el id
             */
            this.saveRecord = function (currentRecord) {
                if (currentRecord.id) {
                    return $http.put(context + "/" + currentRecord.id, currentRecord);
                } else {
                    return $http.post(context, currentRecord);
                }
            };

            /**
             * Hace una petición DELETE a /reviews/:id para eliminar un book
             * @param {number} id identificador de la instancia de book a eliminar
             * @returns {promise} promise para leer la respuesta del servidor
             * No devuelve datos.
             */
            this.deleteRecord = function (id) {
                return $http.delete(context + "/" + id);
            };
        }]);
})(window.angular);