(function (ng) {
    var mod = ng.module("authorModule", ["ui.bootstrap","ngMessages"]);

    mod.constant("authorContext", "api/authors");
})(window.angular);
