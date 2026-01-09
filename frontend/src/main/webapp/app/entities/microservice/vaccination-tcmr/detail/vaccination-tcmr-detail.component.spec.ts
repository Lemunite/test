import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VaccinationTCMRDetailComponent } from './vaccination-tcmr-detail.component';

describe('VaccinationTCMR Management Detail Component', () => {
  let comp: VaccinationTCMRDetailComponent;
  let fixture: ComponentFixture<VaccinationTCMRDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VaccinationTCMRDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vaccination-tcmr-detail.component').then(m => m.VaccinationTCMRDetailComponent),
              resolve: { vaccinationTCMR: () => of({ id: 32463 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VaccinationTCMRDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VaccinationTCMRDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load vaccinationTCMR on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VaccinationTCMRDetailComponent);

      // THEN
      expect(instance.vaccinationTCMR()).toEqual(expect.objectContaining({ id: 32463 }));
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
