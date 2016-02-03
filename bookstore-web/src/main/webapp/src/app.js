(function (ng) {

    var mod = ng.module("mainApp", [
        "ui.router",
        "bookModule",
        "editorialModule",
        "authorModule",
        "reviewModule",
        "bookMock",
        "editorialMock",
        "authorMock",
        "reviewMock",
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
                        controller: "editorialCtrl",
                        controllerAs: "ctrl",
                        templateUrl: "src/modules/editorial/editorial.tpl.html"
                    })
                    .state('author', {
                        url: '/author',
                        controller: "authorCtrl",
                        controllerAs: "ctrl",
                        templateUrl: "src/modules/author/author.tpl.html"
                    })
                    .state('review', {
                        url: '/review',
                        controller: "reviewCtrl",
                        controllerAs: "ctrl",
                        templateUrl: "src/modules/review/review.tpl.html"
                    });
        }]);
})(window.angular);