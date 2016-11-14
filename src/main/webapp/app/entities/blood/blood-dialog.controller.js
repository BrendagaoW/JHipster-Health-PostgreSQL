(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('BloodDialogController', BloodDialogController);

    BloodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Blood', 'User'];

    function BloodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Blood, User) {
        var vm = this;

        vm.blood = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.blood.id !== null) {
                Blood.update(vm.blood, onSaveSuccess, onSaveError);
            } else {
                Blood.save(vm.blood, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('21PointsApp:bloodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
