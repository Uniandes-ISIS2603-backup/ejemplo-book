/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

(function (ng) {

    var mod = ng.module("bookModule");

    mod.controller("bookCtrl", ["$scope", "bookService", "editorialService", "authorService", "$modal", function ($scope, svc, editorialSvc, authorSvc, $modal) {
            //Se almacenan todas las alertas
            $scope.alerts = [];
            $scope.currentRecord = {
                id: undefined /*Tipo Long. El valor se asigna en el backend*/,
                name: '' /*Tipo String*/,
                description: '' /*Tipo String*/,
                isbn: '' /*Tipo String*/,
                image: '' /*Tipo String*/,
                editorial: {} /*Objeto que representa instancia de Editorial*/,
                reviews: [{/*Colección de registros de Review*/
                        id: undefined /*Tipo Long. El backend asigna el valor*/,
                        name: '' /*Tipo String*/,
                        source: '' /*Tipo String*/,
                        description: '' /*Tipo String*/
                    }, {
                        id: undefined /*Tipo Long. El backend asigna el valor*/,
                        name: '' /*Tipo String*/,
                        source: '' /*Tipo String*/,
                        description: '' /*Tipo String*/
                    }] /*Colección de registros de Review*/
            };
            $scope.records = [];

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

            // Función showMessage: Recibe el mensaje en String y su tipo con el fin de almacenarlo en el array $scope.alerts.
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

            this.showSuccess = function (msg) {
                showMessage(msg, "success");
            };

            var self = this;
            function responseError(response) {
                self.showError(response.data);
            }

            //Variables para el controlador
            this.readOnly = false;
            this.editMode = false;

            this.changeTab = function (tab) {
                $scope.tab = tab;
            };

            //Ejemplo alerta
            showMessage("Bienvenido!, Esto es un ejemplo para mostrar un mensaje de información", "info");


            /*
             * Funcion createRecord emite un evento a los $scope hijos del controlador por medio de la
             * sentencia &broadcast ("nombre del evento", record), esto con el fin cargar la información de modulos hijos
             * al actual modulo.
             * Habilita el modo de edicion. El template de la lista cambia por el formulario.
             *
             */

            this.createRecord = function () {
                $scope.$broadcast("pre-create", $scope.currentRecord);
                this.editMode = true;
                $scope.currentRecord = {};
                $scope.$broadcast("post-create", $scope.currentRecord);
            };

            /*
             * Funcion editRecord emite el evento ("pre-edit") a los $Scope hijos del controlador por medio de la
             * sentencia &broadcast ("nombre del evento", record), esto con el fin cargar la información de modulos hijos
             * al actual modulo.
             * Habilita el modo de edicion.  Carga el template de formulario con los datos del record a editar.
             *
             */

            this.editRecord = function (record) {
                $scope.$broadcast("pre-edit", $scope.currentRecord);
                return svc.fetchRecord(record.id).then(function (response) {
                    $scope.currentRecord = response.data;
                    self.editMode = true;
                    $scope.$broadcast("post-edit", $scope.currentRecord);
                    return response;
                }, responseError);
            };

            /*
             * Funcion fetchRecords consulta el servicio svc.fetchRecords con el fin de consultar
             * todos los registros del modulo book.
             * Guarda los registros en la variable $scope.records
             * Muestra el template de la lista de records.
             */

            this.fetchRecords = function () {
                return svc.fetchRecords().then(function (response) {
                    $scope.records = response.data;
                    $scope.currentRecord = {};
                    self.editMode = false;
                    return response;
                }, responseError);
            };

            /*
             * Funcion saveRecord hace un llamado al servicio svc.saveRecord con el fin de
             * persistirlo en base de datos.
             * Muestra el template de la lista de records al finalizar la operación saveRecord
             */
            this.saveRecord = function () {
                return svc.saveRecord($scope.currentRecord).then(function () {
                    self.fetchRecords();
                }, responseError);
            };

            /*
             * Funcion deleteRecord hace un llamado al servicio svc.deleteRecord con el fin
             * de eliminar el registro asociado.
             * Muestra el template de la lista de records al finalizar el borrado del registro.
             */
            this.deleteRecord = function (record) {
                return svc.deleteRecord(record.id).then(function () {
                    self.fetchRecords();
                }, responseError);
            };

            editorialSvc.fetchRecords().then(function (response) {
                $scope.editorials = response.data;
            });

            /*
             * Funcion fetchRecords consulta todos los registros del módulo book en base de datos
             * para desplegarlo en el template de la lista.
             */
            this.fetchRecords();


            function updateReview(event, args) {
                $scope.currentRecord.reviews = args;
            }
            ;


            function updateAuthors(event, args) {
                $scope.currentRecord.authors = args;
            }
            ;

            $scope.$on('updateReview', updateReview);
            $scope.$on('updateAuthors', updateAuthors);

        }]);


    mod.controller("authorsCtrl", ["$scope", "authorService", "$modal", "bookService", function ($scope, svc, $modal, bookSvc) {
            $scope.currentRecord = {};
            $scope.records = [];
            $scope.refName = "authors";
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

            //Escucha de evento cuando se selecciona un registro maestro
            function onCreateOrEdit(event, args) {
                var childName = "authors";
                if (args[ childName ] === undefined) {
                    args[ childName ] = [];
                }
                $scope.records = [];
                $scope.refId = args.id;
                if (args.id) {
                    bookSvc.getAuthors(args.id).then(function (response) {
                        $scope.records = response.data;
                    }, responseError);
                }
            }

            $scope.$on("post-create", onCreateOrEdit);
            $scope.$on("post-edit", onCreateOrEdit);

            this.removeAuthor = function (index) {
                bookSvc.removeAuthor($scope.refId, $scope.records[ index ].id).then(function () {
                    $scope.records.splice(index, 1);
                }, responseError);
            };

            this.showList = function () {
                var modal = $modal.open({
                    animation: true,
                    templateUrl: "src/modules/book/authorModal.tpl.html",
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
                    bookSvc.replaceAuthors($scope.refId, data).then(function (response) {
                        $scope.records.splice(0, $scope.records.length);
                        $scope.records.push.apply($scope.records, response.data);
                        $scope.$emit("updateAuthors", $scope.records);
                    }, responseError);
                });
            };
        }]);


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

            //Escucha de evento cuando se selecciona un registro maestro
            function onCreateOrEdit(event, args) {
                var childName = "reviews";
                if (args[childName] === undefined) {
                    args[childName] = [];
                }
                $scope.records = args[childName];
                $scope.refId = args.id;
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
                    rc[$scope.parent] = {id: $scope.refId};
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