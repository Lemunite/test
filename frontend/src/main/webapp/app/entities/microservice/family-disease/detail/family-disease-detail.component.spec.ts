import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FamilyDiseaseDetailComponent } from './family-disease-detail.component';

describe('FamilyDisease Management Detail Component', () => {
  let comp: FamilyDiseaseDetailComponent;
  let fixture: ComponentFixture<FamilyDiseaseDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FamilyDiseaseDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./family-disease-detail.component').then(m => m.FamilyDiseaseDetailComponent),
              resolve: { familyDisease: () => of({ id: 2762 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FamilyDiseaseDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FamilyDiseaseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load familyDisease on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FamilyDiseaseDetailComponent);

      // THEN
      expect(instance.familyDisease()).toEqual(expect.objectContaining({ id: 2762 }));
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
