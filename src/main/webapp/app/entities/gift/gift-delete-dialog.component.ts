import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Gift } from './gift.model';
import { GiftPopupService } from './gift-popup.service';
import { GiftService } from './gift.service';

@Component({
    selector: 'jhi-gift-delete-dialog',
    templateUrl: './gift-delete-dialog.component.html'
})
export class GiftDeleteDialogComponent {

    gift: Gift;

    constructor(
        private giftService: GiftService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.giftService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'giftListModification',
                content: 'Deleted an gift'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-gift-delete-popup',
    template: ''
})
export class GiftDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private giftPopupService: GiftPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.giftPopupService
                .open(GiftDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
