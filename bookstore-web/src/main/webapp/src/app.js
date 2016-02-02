/*Este es el modulo que se ha creado dentro de un closure con el fin de poder crear variables privadas.*/
(function (ng) {
    /* aquí el contenido y variables privadas
     * window.angular es un parámetro que pasa al closure
     * ng es el nombre que se le da a dicho parámetro dentro del closure
     */
// El resultado de la función se guarda en la variable mod y mainApp es el nombre que hemos dado a nuestro modulo
    var mod = ng.module("mainApp", ["ui.router"]);
// La configuración es para que la aplicación imprima los mensajes de debug sólo cuando éstos se activan    
    mod.config(['$logProvider', function ($logProvider) {
            $logProvider.debugEnabled(true);
        }]);
/* Para poder ver todos los estados disponibles, esta configuración la haremos en el módulo principal, 
 * sin embargo, puede hacerse en cualquier bloque de configuración de cualquier módulo, 
 * mientras éste tenga la dependencia a ui.router, sea de manera directa o transitiva. 
 * Se registra por defecto la ruta /book, lo cual implica que cuando se solicite una ruta que no corresponda
 * a ningún estado, el navegador será redireccionado a la ruta por defecto. 
 * Después se crea un estado llamado book, el cual está configurado para llevar al navegador a la url /book y
 * cargar la plantilla en la ruta src/modules/book/book.tpl.html.
 */
    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise("/book");
            $stateProvider
                    .state('book', {
                        url: '/book',
                        templateUrl: "src/modules/book/book.tpl.html"
                    })
                    .state('editorial', {
                        url: '/editorial',
                        templateUrl: "src/modules/editorial/editorial.tpl.html"
                    })
                    .state('author', {
                        url: '/author',
                        templateUrl: "src/modules/author/author.tpl.html"
                    }).state('review', {
                        url: '/review',
                        templateUrl: "src/modules/review/review.tpl.html"
            });
        }]);
    
})(window.angular);