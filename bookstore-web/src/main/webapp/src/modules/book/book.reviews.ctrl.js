(function (ng) {

    var mod = ng.module("bookModule");

    mod.controller("reviewsCtrl", ["$scope", "bookService", function ($scope, bookSvc) {
            $scope.currentRecord = {};
            $scope.records = [];
            $scope.refName = "reviews";
            $scope.alerts = [];

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
            function onCreateOrEdit(event, args) {
                var childName = "reviews";
                if (args[childName] === undefined) {
                    args[childName] = [];
                }
                $scope.records = args[childName];
            }

            $scope.$on("post-create", onCreateOrEdit);
            $scope.$on("post-edit", onCreateOrEdit);

            //Función para encontrar un registro por ID o CID
            function indexOf(rc) {
                var field = rc.id !== undefined ? 'id' : 'cid';
                for (var i in $scope.records) {
                    if ($scope.records.hasOwnProperty(i)) {
                        var current = $scope.records[i];
                        if (current[field] === rc[field]) {
                            return i;
                        }
                    }
                }
            }

            this.createRecord = function () {
                this.editMode = true;
                $scope.currentRecord = {};
            };

            var self = this;
            this.saveRecord = function () {
                var rc = $scope.currentRecord;
                if (rc.id || rc.cid) {
                    var idx = indexOf(rc);
                    $scope.records.splice(idx, 1, rc);
                } else {
                    rc.cid = -Math.floor(Math.random() * 10000);
                    $scope.records.push(rc);
                }
                this.fetchRecords();
            };

            this.fetchRecords = function () {
                $scope.currentRecord = {};
                this.editMode = false;
            };

            this.editRecord = function (record) {
                $scope.currentRecord = ng.copy(record);
                this.editMode = true;
            };

            this.deleteRecord = function (record) {
                var idx = indexOf(record);
                $scope.records.splice(idx, 1);
            };
        }]);
})(window.angular);