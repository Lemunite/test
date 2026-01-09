import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FamilyAllergyDetailComponent } from './family-allergy-detail.component';

describe('FamilyAllergy Management Detail Component', () => {
  let comp: FamilyAllergyDetailComponent;
  let fixture: ComponentFixture<FamilyAllergyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FamilyAllergyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./family-allergy-detail.component').then(m => m.FamilyAllergyDetailComponent),
              resolve: { familyAllergy: () => of({ id: 5048 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FamilyAllergyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FamilyAllergyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load familyAllergy on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FamilyAllergyDetailComponent);

      // THEN
      expect(instance.familyAllergy()).toEqual(expect.objectContaining({ id: 5048 }));
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
