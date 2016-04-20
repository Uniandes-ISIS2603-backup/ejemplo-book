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

            this.getPrizes = function (bookId) {
                return $http.get(context + "/" + bookId + "/prizes");
            };

            this.getPrize = function (bookId, prizeId) {
                return $http.get(context + "/" + bookId + "/prizes/" + prizeId);
            };

            this.createPrize = function (bookId, prize) {
                return $http.post(context + "/" + bookId + "/prizes", prize);
            };

            this.updatePrize = function (bookId, prizeId, prize) {
                return $http.put(context + "/" + bookId + "/prizes/" + prizeId, prize);
            };
            
            this.deletePrize = function (bookId, prizeId) {
                return $http.delete(context + "/" + bookId + "/prizes/" + prizeId);
            };
            
            this.savePrize = function(bookId, prize){
                if(prize.id){
                    return this.updatePrize(bookId, prize.id, prize);
                }else{
                    return this.createPrize(bookId, prize);
                }
            };

        }]);


})(window.angular);

