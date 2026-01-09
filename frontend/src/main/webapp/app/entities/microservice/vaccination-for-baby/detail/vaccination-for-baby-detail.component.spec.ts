import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VaccinationForBabyDetailComponent } from './vaccination-for-baby-detail.component';

describe('VaccinationForBaby Management Detail Component', () => {
  let comp: VaccinationForBabyDetailComponent;
  let fixture: ComponentFixture<VaccinationForBabyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VaccinationForBabyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vaccination-for-baby-detail.component').then(m => m.VaccinationForBabyDetailComponent),
              resolve: { vaccinationForBaby: () => of({ id: 8159 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VaccinationForBabyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VaccinationForBabyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load vaccinationForBaby on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VaccinationForBabyDetailComponent);

      // THEN
      expect(instance.vaccinationForBaby()).toEqual(expect.objectContaining({ id: 8159 }));
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
