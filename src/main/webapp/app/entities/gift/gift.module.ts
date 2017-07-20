import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IkadoSharedModule } from '../../shared';
import { IkadoAdminModule } from '../../admin/admin.module';
import {
    GiftService,
    GiftPopupService,
    GiftComponent,
    GiftDetailComponent,
    GiftDialogComponent,
    GiftPopupComponent,
    GiftDeletePopupComponent,
    GiftDeleteDialogComponent,
    giftRoute,
    giftPopupRoute,
} from './';

const ENTITY_STATES = [
    ...giftRoute,
    ...giftPopupRoute,
];

@NgModule({
    imports: [
        IkadoSharedModule,
        IkadoAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GiftComponent,
        GiftDetailComponent,
        GiftDialogComponent,
        GiftDeleteDialogComponent,
        GiftPopupComponent,
        GiftDeletePopupComponent,
    ],
    entryComponents: [
        GiftComponent,
        GiftDialogComponent,
        GiftPopupComponent,
        GiftDeleteDialogComponent,
        GiftDeletePopupComponent,
    ],
    providers: [
        GiftService,
        GiftPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IkadoGiftModule {}
