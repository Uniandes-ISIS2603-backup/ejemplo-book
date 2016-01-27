(function (ng) {

    var mod = ng.module("mainApp", [
        "ui.router",
        "bookModule",
        "mockModule",
        "ngMessages"
    ]);

    mod.config(['$logProvider', function ($logProvider) {
            $logProvider.debugEnabled(true);
        }]);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise("/book");
            $stateProvider
                    .state('book', {
                        url: '/book',
                        controller: "bookCtrl",
                        controllerAs: "ctrl",
                        templateUrl: "src/modules/book/book.tpl.html"
                    })
                    .state('editorial', {
                        url: '/editorial',
                        templateUrl: "src/modules/editorial/editorial.tpl.html"
                    });
        }]);
})(window.angular);