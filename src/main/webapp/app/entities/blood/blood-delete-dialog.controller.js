(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('BloodDeleteController',BloodDeleteController);

    BloodDeleteController.$inject = ['$uibModalInstance', 'entity', 'Blood'];

    function BloodDeleteController($uibModalInstance, entity, Blood) {
        var vm = this;

        vm.blood = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Blood.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
