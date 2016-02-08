/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function (ng) {

    var mod = ng.module("bookModule");

    mod.service("bookService", ["$http", "bookContext", "$log", function ($http, context, $log) {
            /**
             * Obtener la lista de books.
             * Hace una petición GET con $http a /books para obtener la lista
             * de books
             * @returns {promise} promise para leer la respuesta del servidor}
             * Devuelve una lista de objetos de books con sus atributos y reviews
             */
            this.fetchRecords = function () {
                return $http.get(context);
            };

            /**
             * Obtener un registro de books.
             * Hace una petición GET a /books/:id para obtener
             * los datos de un registro específico de books
             * @param {number} id del registro a obtener
             * @returns {promise} promise para leer la respuesta del servidor
             * Devuelve un objeto de books con sus atributos y reviews
             */
            this.fetchRecord = function (id) {
                return $http.get(context + "/" + id);
            };

            /**
             * Guardar un registro de books.
             * Si currentRecord tiene la propiedad id, hace un PUT a /books/:id con los
             * nuevos datos de la instancia de books.
             * Si currentRecord no tiene la propiedad id, se hace un POST a /books
             * para crear el nuevo registro de books
             * @param {object} currentRecord instancia de book a guardar/actualizar
             * @returns {promise} promise para leer la respuesta del servidor
             * Devuelve un objeto de books con sus datos incluyendo el id
             */
            this.saveRecord = function (currentRecord) {
                if (currentRecord.id) {
                    return $http.put(context + "/" + currentRecord.id, currentRecord);
                } else {
                    return $http.post(context, currentRecord);
                }
            };

            /**
             * Hace una petición DELETE a /books/:id para eliminar un book
             * @param {number} id identificador de la instancia de book a eliminar
             * @returns {promise} promise para leer la respuesta del servidor
             * No devuelve datos.
             */
            this.deleteRecord = function (id) {
                return $http.delete(context + "/" + id);
            };
            /**
             * Hace una petición PUT a /books/:id/authors para reemplazar los
             * author asociados a un book
             * @param {number} bookId Identificador de la instancia de book
             * @param {array} authors Colección de authors nueva
             * @returns {promise} promise para leer la respuesta del servidor
             * Devuelve un array de objetos de authors con los nuevos autores
             */
            this.replaceAuthors = function (bookId, authors) {
                return $http.put(context + "/" + bookId + "/authors", authors);
            };

            /**
             * Hace una petición GET a /books/:id/authors para obtener la colección
             * de author asociados a un book
             * @param {number} id Identificador de la instancia de book
             * @returns {promise} promise para leer la respuesta del servidor
             * Devuelve un array de objetos de authors.
             */
            this.getAuthors = function (id) {
                return $http.get(context + "/" + id + "/authors");
            };

            /**
             * Hace una petición DELETE a /books/:id/authors/:id para remover
             * un author de un book
             * @param {number} bookId Identificador de la instancia de book
             * @param {number} authorId Identificador de la instancia de author
             * @returns {promise} promise para leer la respuesta del servidor
             * No devuelve datos.
             */
            this.removeAuthor = function (bookId, authorId) {
                return $http.delete(context + "/" + bookId + "/authors/" + authorId);
            };


            /**
             * Obtener la lista de reviews.
             * Hace una petición GET con $http a /reviews para obtener la lista
             * de reviews
             * @returns {promise} promise para leer la respuesta del servidor}
             * Devuelve una lista de objetos de reviews con sus atributos y reviews
             */
            this.fetchReviewRecords = function (bookId) {
                return $http.get(context + "/" + bookId + "/reviews");
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


            this.getReviews = function (idBook) {
                $log.debug("GET" + context + "/" + idBook + "/reviews");
                return $http.get(context + "/" + idBook + "/reviews");
            };

            this.getReview = function (idBook, idReview) {
                $log.debug("GET" + context + "/" + idBook + "/reviews/" + idReview);
                return $http.get(context + "/" + idBook + "/reviews/" + idReview);
            };

            this.saveReview = function (idBook, currentRecord) {
                if (currentRecord.id) {
                    $log.debug("PUT" + context + "/" + idBook + "/reviews/" + currentRecord.id);
                    return $http.post(context + "/" + idBook + "/reviews" + currentRecord.id, currentRecord);
                } else {
                    $log.debug("POST" + context + "/" + idBook + "/reviews/");
                    return $http.post(context + "/" + idBook + "/reviews", currentRecord);
                }
            };

            this.removeReview = function (idBook, idReview) {
                $log.debug("Llamo a post");
                return $http.delete(context + "/" + idBook + "/reviews/" + idReview);
            };

        }]);


})(window.angular);

