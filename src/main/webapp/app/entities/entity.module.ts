import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { IkadoGiftModule } from './gift/gift.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        IkadoGiftModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IkadoEntityModule {}
