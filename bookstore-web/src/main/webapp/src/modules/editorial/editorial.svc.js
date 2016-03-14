/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


(function (ng) {

    var mod = ng.module("editorialModule");

    mod.service("editorialService", ["$http", "editorialContext", function ($http, context) {
            /**
             * Obtener la lista de editorials.
             * Hace una petición GET con $http a /editorials para obtener la lista
             * de editorials
             * @returns {promise} promise para leer la respuesta del servidor}
             * Devuelve una lista de objetos de editorials con sus atributos y reviews
             */
            this.fetchRecords = function () {
                return $http.get(context);
            };

            /**
             * Obtener un registro de editorials.
             * Hace una petición GET a /editorials/:id para obtener
             * los datos de un registro específico de editorials
             * @param {number} id del registro a obtener
             * @returns {promise} promise para leer la respuesta del servidor
             * Devuelve un objeto de editorials con sus atributos y reviews
             */
            this.fetchRecord = function (id) {
                return $http.get(context + "/" + id);
            };

            /**
             * Guardar un registro de editorials.
             * Si currentRecord tiene la propiedad id, hace un PUT a /editorials/:id con los
             * nuevos datos de la instancia de editorials.
             * Si currentRecord no tiene la propiedad id, se hace un POST a /editorials
             * para crear el nuevo registro de editorials
             * @param {object} currentRecord instancia de book a guardar/actualizar
             * @returns {promise} promise para leer la respuesta del servidor
             * Devuelve un objeto de editorials con sus datos incluyendo el id
             */
            this.saveRecord = function (currentRecord) {
                if (currentRecord.id) {
                    return $http.put(context + "/" + currentRecord.id, currentRecord);
                } else {
                    return $http.post(context, currentRecord);
                }
            };

            /**
             * Hace una petición DELETE a /editorials/:id para eliminar un book
             * @param {number} id identificador de la instancia de book a eliminar
             * @returns {promise} promise para leer la respuesta del servidor
             * No devuelve datos.
             */
            this.deleteRecord = function (id) {
                return $http.delete(context + "/" + id);
            };
            /**
             * Hace una petición PUT a /editorial/:id/books para reemplazar los
             * editorial asociados a un editorial
             * @param {number} editorialId Identificador de la instancia de editorial
             * @param {array} books Colección de editorials nueva
             * @returns {promise} promise para leer la respuesta del servidor.
             * Devuelve el objeto de editorials con sus nuevos datos.
             */
            this.replaceBooks = function (editorialId, books) {
                return $http.put(context + "/" + editorialId + "/books", books);
            };

            /**
             * Hace una petición GET a /editorials/:id/books para obtener la colección
             * de editorial asociados a un editorial
             * @param {number} id Identificador de la instancia de editorial
             * @returns {promise} promise para leer la respuesta del servidor.
             * Retorna un array de objetos de books.
             */
            this.getBooks = function (id) {
                return $http.get(context + "/" + id + "/books");
            };

            /**
             * Hace una petición DELETE a /editorials/:id/books/:id para remover
             * un editorial de un editorial
             * @param {number} editorialId Identificador de la instancia de book
             * @param {number} bookId Identificador de la instancia de editorial
             * @returns {promise} promise para leer la respuesta del servidor
             * La respuesta no devuelve datos.
             */
            this.removeBook = function (editorialId, bookId) {
                return $http.delete(context + "/" + editorialId + "/books/" + bookId);
            };
        }]);
})(window.angular);