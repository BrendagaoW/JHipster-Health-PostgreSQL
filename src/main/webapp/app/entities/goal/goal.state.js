(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('goal', {
            parent: 'entity',
            url: '/goal',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: '21PointsApp.goal.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/goal/goals.html',
                    controller: 'GoalController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('goal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('goal-detail', {
            parent: 'entity',
            url: '/goal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: '21PointsApp.goal.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/goal/goal-detail.html',
                    controller: 'GoalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('goal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Goal', function($stateParams, Goal) {
                    return Goal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'goal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('goal-detail.edit', {
            parent: 'goal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goal/goal-dialog.html',
                    controller: 'GoalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Goal', function(Goal) {
                            return Goal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('goal.new', {
            parent: 'goal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goal/goal-dialog.html',
                    controller: 'GoalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('goal', null, { reload: 'goal' });
                }, function() {
                    $state.go('goal');
                });
            }]
        })
        .state('goal.edit', {
            parent: 'goal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goal/goal-dialog.html',
                    controller: 'GoalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Goal', function(Goal) {
                            return Goal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('goal', null, { reload: 'goal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('goal.delete', {
            parent: 'goal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goal/goal-delete-dialog.html',
                    controller: 'GoalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Goal', function(Goal) {
                            return Goal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('goal', null, { reload: 'goal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
