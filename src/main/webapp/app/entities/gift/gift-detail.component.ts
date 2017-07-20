import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import { Gift } from './gift.model';
import { GiftService } from './gift.service';

@Component({
    selector: 'jhi-gift-detail',
    templateUrl: './gift-detail.component.html'
})
export class GiftDetailComponent implements OnInit, OnDestroy {

    gift: Gift;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private giftService: GiftService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGifts();
    }

    load(id) {
        this.giftService.find(id).subscribe((gift) => {
            this.gift = gift;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGifts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'giftListModification',
            (response) => this.load(this.gift.id)
        );
    }
}
