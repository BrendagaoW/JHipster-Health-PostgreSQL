(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('MetricDeleteController',MetricDeleteController);

    MetricDeleteController.$inject = ['$uibModalInstance', 'entity', 'Metric'];

    function MetricDeleteController($uibModalInstance, entity, Metric) {
        var vm = this;

        vm.metric = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Metric.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
