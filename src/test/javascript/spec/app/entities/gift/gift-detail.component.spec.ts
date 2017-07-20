/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { IkadoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GiftDetailComponent } from '../../../../../../main/webapp/app/entities/gift/gift-detail.component';
import { GiftService } from '../../../../../../main/webapp/app/entities/gift/gift.service';
import { Gift } from '../../../../../../main/webapp/app/entities/gift/gift.model';

describe('Component Tests', () => {

    describe('Gift Management Detail Component', () => {
        let comp: GiftDetailComponent;
        let fixture: ComponentFixture<GiftDetailComponent>;
        let service: GiftService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [IkadoTestModule],
                declarations: [GiftDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GiftService,
                    JhiEventManager
                ]
            }).overrideTemplate(GiftDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GiftDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GiftService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Gift(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.gift).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
