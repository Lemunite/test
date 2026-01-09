import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AllergyDetailComponent } from './allergy-detail.component';

describe('Allergy Management Detail Component', () => {
  let comp: AllergyDetailComponent;
  let fixture: ComponentFixture<AllergyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllergyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./allergy-detail.component').then(m => m.AllergyDetailComponent),
              resolve: { allergy: () => of({ id: 12270 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AllergyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllergyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load allergy on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AllergyDetailComponent);

      // THEN
      expect(instance.allergy()).toEqual(expect.objectContaining({ id: 12270 }));
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
