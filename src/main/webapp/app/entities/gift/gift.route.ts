import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GiftComponent } from './gift.component';
import { GiftDetailComponent } from './gift-detail.component';
import { GiftPopupComponent } from './gift-dialog.component';
import { GiftDeletePopupComponent } from './gift-delete-dialog.component';

export const giftRoute: Routes = [
    {
        path: 'gift',
        component: GiftComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ikadoApp.gift.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'gift/:id',
        component: GiftDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ikadoApp.gift.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const giftPopupRoute: Routes = [
    {
        path: 'gift-new',
        component: GiftPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ikadoApp.gift.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'gift/:id/edit',
        component: GiftPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ikadoApp.gift.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'gift/:id/delete',
        component: GiftDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ikadoApp.gift.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
