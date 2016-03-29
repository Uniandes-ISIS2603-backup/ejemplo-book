(function (ng) {

    var mod = ng.module("editorialModule");

    mod.controller("editorialBooksCtrl", ["$scope", "bookService", "$modal", "editorialService", function ($scope, svc, $modal, authorSvc) {
            $scope.currentRecord = {};
            $scope.records = [];
            $scope.refName = "books";
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
                var childName = "books";
                if (args[ childName ] === undefined) {
                    args[ childName ] = [];
                }
                $scope.records = [];
                $scope.refId = args.id;
                if (args.id) {
                    authorSvc.getBooks(args.id).then(function (response) {
                        $scope.records = response.data;
                    }, responseError);
                }
            }

            $scope.$on("post-create", onCreateOrEdit);
            $scope.$on("post-edit", onCreateOrEdit);

            this.removeBook = function (index) {
                authorSvc.removeBook($scope.refId, $scope.records[ index ].id).then(function () {
                    $scope.records.splice(index, 1);
                }, responseError);
            };

            this.showList = function () {
                var modal = $modal.open({
                    animation: true,
                    templateUrl: "src/modules/editorial/bookModal.tpl.html",
                    controller: ["$scope", "$modalInstance", "items", "currentItems", function ($scope, $modalInstance, items, currentItems) {
                            $scope.records = items.data;
                            $scope.allChecked = false;

                            function loadSelected(list, selected) {
                                ng.forEach(selected, function (selectedValue) {
                                    ng.forEach(list, function (listValue) {
                                        if (listValue.id === selectedValue.id) {
                                            listValue.selected = true;
                                        }
                                    });
                                });
                            }

                            $scope.checkAll = function (flag) {
                                this.records.forEach(function (item) {
                                    item.selected = flag;
                                });
                            };

                            loadSelected($scope.records, currentItems);

                            function getSelectedItems() {
                                return $scope.records.filter(function (item) {
                                    return !!item.selected;
                                });
                            }

                            $scope.ok = function () {
                                $modalInstance.close(getSelectedItems());
                            };

                            $scope.cancel = function () {
                                $modalInstance.dismiss("cancel");
                            };
                        }],
                    resolve: {
                        items: function () {
                            return svc.fetchRecords();
                        },
                        currentItems: function () {
                            return $scope.records;
                        }
                    }
                });
                modal.result.then(function (data) {
                    authorSvc.replaceBooks($scope.refId, data).then(function (response) {
                        $scope.records.splice(0, $scope.records.length);
                        $scope.records.push.apply($scope.records, response.data);
                        $scope.$emit("updateBooks", $scope.records);
                    }, responseError);
                });
            };
        }]);

})(window.angular);