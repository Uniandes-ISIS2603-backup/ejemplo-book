(function (ng) {
    var mod = ng.module("authorModule");

    mod.service("authorService", ["$http", "authorContext", function ($http, context) {

        /**
         * Obtener la lista de authors.
         * Hace una petición GET con $http a /authors para obtener la lista
         * de objetos de la entidad authors
         * @returns {promise} promise para leer la respuesta del servidor.
         * Se recibe un array de objetos de authors.
         */
        this.fetchRecords = function () {
            return $http.get(context);
        };

        /**
         * Obtener un registro de authors.
         * Hace una petición GET a /authors/:id para obtener
         * el objeto de un registro específico de authors
         * @param {number} id del registro a obtener
         * @returns {promise} promise para leer la respuesta del servidor.
         * Se recibe un objeto instancia de authors.
         */
        this.fetchRecord = function (id) {
            return $http.get(context + "/" + id);
        };

        /**
         * Guardar un registro de authors.
         * Si currentRecord tiene la propiedad id, hace un PUT a /authors/:id con los
         * nuevos datos de la instancia de authors.
         * Si currentRecord no tiene la propiedad id, se hace un POST a /authors
         * para crear el nuevo registro de authors
         * @param {object} currentRecord instancia de authors a guardar/actualizar
         * @returns {promise} promise para leer la respuesta del servidor.
         * Se recibe un objeto de authors con su nuevo id
         */
        this.saveRecord = function (currentRecord) {
            if (currentRecord.id) {
                return $http.put(context + "/" + currentRecord.id, currentRecord);
            } else {
                return $http.post(context, currentRecord);
            }
        };

        /**
         * Hace una petición DELETE a /authors/:id para eliminar un author
         * @param {number} id identificador de la instancia de author a eliminar
         * @returns {promise} promise para leer la respuesta del servidor.
         * No se recibe cuerpo en la respuesta.
         */
        this.deleteRecord = function (id) {
            return $http.delete(context + "/" + id);
        };  
        /**
         * Hace una petición PUT a /authors/:id/books para reemplazar los
         * author asociados a un author
         * @param {number} authorId Identificador de la instancia de author
         * @param {array} books Colección de authors nueva
         * @returns {promise} promise para leer la respuesta del servidor.
         * Devuelve el objeto de authors con sus nuevos datos.
         */
        this.replaceBooks = function (authorId, books) {
            return $http.put(context + "/" + authorId + "/books", books);
        };

        /**
         * Hace una petición GET a /authors/:id/books para obtener la colección
         * de author asociados a un author
         * @param {number} id Identificador de la instancia de author
         * @returns {promise} promise para leer la respuesta del servidor.
         * Retorna un array de objetos de books.
         */
        this.getBooks = function (id) {
            return $http.get(context + "/" + id + "/books");
        };

        /**
         * Hace una petición DELETE a /authors/:id/books/:id para remover
         * un author de un author
         * @param {number} authorId Identificador de la instancia de book
         * @param {number} bookId Identificador de la instancia de author
         * @returns {promise} promise para leer la respuesta del servidor
         * La respuesta no devuelve datos.
         */
        this.removeBook = function (authorId, bookId) {
            return $http.delete(context + "/" + authorId + "/books/" + bookId);
        };
    }]);
})(window.angular);
