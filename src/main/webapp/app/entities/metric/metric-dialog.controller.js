(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('MetricDialogController', MetricDialogController);

    MetricDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Metric', 'Goal', 'Entry'];

    function MetricDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Metric, Goal, Entry) {
        var vm = this;

        vm.metric = entity;
        vm.clear = clear;
        vm.save = save;
        vm.goals = Goal.query();
        vm.entries = Entry.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.metric.id !== null) {
                Metric.update(vm.metric, onSaveSuccess, onSaveError);
            } else {
                Metric.save(vm.metric, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('21PointsApp:metricUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
