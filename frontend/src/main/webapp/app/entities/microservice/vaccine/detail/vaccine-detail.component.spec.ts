import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VaccineDetailComponent } from './vaccine-detail.component';

describe('Vaccine Management Detail Component', () => {
  let comp: VaccineDetailComponent;
  let fixture: ComponentFixture<VaccineDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VaccineDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vaccine-detail.component').then(m => m.VaccineDetailComponent),
              resolve: { vaccine: () => of({ id: 27715 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VaccineDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VaccineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load vaccine on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VaccineDetailComponent);

      // THEN
      expect(instance.vaccine()).toEqual(expect.objectContaining({ id: 27715 }));
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
