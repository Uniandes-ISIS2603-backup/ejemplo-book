(function (ng) {

    var mod = ng.module("mainApp", [
        "ui.router",
        "authModule",
        "bookModule",
        "editorialModule",
        "authorModule",
        "reviewModule",
        "authMock",
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

    mod.config(['authServiceProvider', function (auth) {
            auth.setValues({
                apiUrl: 'api/users/',
                successState: 'editorial'
            });
            auth.setRoles({'user': [{id: 'indexUser', label: 'Author', icon: 'list-alt', state: 'author'}],
                'admin': [{id: 'indexAdmin', label: 'Admin', icon: 'list-alt', state: 'editorial'}]});
        }]);


})(window.angular);