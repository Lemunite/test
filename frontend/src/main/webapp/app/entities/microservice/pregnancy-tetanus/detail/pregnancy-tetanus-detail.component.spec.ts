import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PregnancyTetanusDetailComponent } from './pregnancy-tetanus-detail.component';

describe('PregnancyTetanus Management Detail Component', () => {
  let comp: PregnancyTetanusDetailComponent;
  let fixture: ComponentFixture<PregnancyTetanusDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PregnancyTetanusDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./pregnancy-tetanus-detail.component').then(m => m.PregnancyTetanusDetailComponent),
              resolve: { pregnancyTetanus: () => of({ id: 5738 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PregnancyTetanusDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PregnancyTetanusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load pregnancyTetanus on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PregnancyTetanusDetailComponent);

      // THEN
      expect(instance.pregnancyTetanus()).toEqual(expect.objectContaining({ id: 5738 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
