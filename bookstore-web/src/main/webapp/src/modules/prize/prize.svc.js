/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
(function (ng) {

    var mod = ng.module("prizeModule");

    mod.service("prizeService", ["$http", "prizeContext", "$log", function ($http, context, $log) {
        

            this.getPrizes = function (bookId) {
                return $http.get(context + "/" + bookId + "/prizes");
            };

            this.getPrize = function (bookId, prizeId) {
                return $http.get(context + "/" + bookId + "/prizes/" + prizeId);
            };

            this.createPrize = function (bookId, prize) {
                return $http.post(context + "/" + bookId + "/prizes", prize);
            };

            this.updatePrize = function (bookId, prizeId, prize) {
                return $http.put(context + "/" + bookId + "/prizes/" + prizeId, prize);
            };
            
            this.deletePrize = function (bookId, prizeId) {
                return $http.delete(context + "/" + bookId + "/prizes/" + prizeId);
            };
            
            this.savePrize = function(bookId, prize){
                if(prize.id){
                    return this.updatePrize(bookId, prize.id, prize);
                }else{
                    return this.createPrize(bookId, prize);
                }
            };

        }]);


})(window.angular);

