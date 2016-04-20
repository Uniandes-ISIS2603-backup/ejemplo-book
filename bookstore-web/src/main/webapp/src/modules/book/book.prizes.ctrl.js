(function (ng) {

    var mod = ng.module("bookModule");

    mod.controller("prizesCtrl", ["$scope", "bookService",
        function ($scope, bookSvc) {
            $scope.currentRecord = {};
            $scope.records = [];
            $scope.alerts = [];

            $scope.today = function () {
                $scope.value = new Date();
            };

            $scope.clear = function () {
                $scope.value = null;
            };

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened = true;
            };

            //Alertas
            this.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

            function showMessage(msg, type) {
                var types = ["info", "danger", "warning", "success"];
                if (types.some(function (rc) {
                    return type === rc;
                })) {
                    $scope.alerts.push({type: type, msg: msg});
                }
            }

            this.showError = function (msg) {
                showMessage(msg, "danger");
            };

            var self = this;
            function responseError(response) {
                self.showError(response.data);
            }

            //Variables para el controlador
            this.readOnly = false;
            this.editMode = false;

            /* Escucha de evento cuando se selecciona un registro maestro.
             * args corresponde a currentRecord del controlador padre
             */
            function onEdit(event, args) {
                $scope.refId = args.id;
                if (args.id) {
                    $scope.records = [];
                    bookSvc.getPrizes(args.id).then(function (response) {
                        $scope.records = response.data;
                    }, responseError);
                }
            }

            $scope.$on("post-edit", onEdit);

            this.createRecord = function () {
                this.editMode = true;
                $scope.currentRecord = {};
            };

            this.editRecord = function (record) {
                return bookSvc.getPrize($scope.refId, record.id).then(function (response) {
                    $scope.currentRecord = response.data;
                    self.editMode = true;
                    return response;
                }, responseError);
            };

            this.fetchRecords = function () {
                return bookSvc.getPrizes($scope.refId).then(function (response) {
                    $scope.records = response.data;
                    $scope.currentRecord = {};
                    self.editMode = false;
                    return response;
                }, responseError);
            };

            this.saveRecord = function () {
                return bookSvc.savePrize($scope.refId, $scope.currentRecord).then(function () {
                    self.fetchRecords();
                }, responseError);
            };

            this.deleteRecord = function (record) {
                return bookSvc.deletePrize($scope.refId, record.id).then(function () {
                    self.fetchRecords();
                }, responseError);
            };
        }]);
})(window.angular);