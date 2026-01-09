import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DisabilityDetailComponent } from './disability-detail.component';

describe('Disability Management Detail Component', () => {
  let comp: DisabilityDetailComponent;
  let fixture: ComponentFixture<DisabilityDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisabilityDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./disability-detail.component').then(m => m.DisabilityDetailComponent),
              resolve: { disability: () => of({ id: 10283 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DisabilityDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DisabilityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load disability on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DisabilityDetailComponent);

      // THEN
      expect(instance.disability()).toEqual(expect.objectContaining({ id: 10283 }));
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
