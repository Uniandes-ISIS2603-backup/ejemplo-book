(function (ng) {
    var mod = ng.module("authorModule", ["ui.bootstrap"]);

    mod.constant("authorContext", "api/authors");
})(window.angular);
