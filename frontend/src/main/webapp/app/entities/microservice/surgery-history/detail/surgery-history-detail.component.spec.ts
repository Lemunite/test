import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SurgeryHistoryDetailComponent } from './surgery-history-detail.component';

describe('SurgeryHistory Management Detail Component', () => {
  let comp: SurgeryHistoryDetailComponent;
  let fixture: ComponentFixture<SurgeryHistoryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SurgeryHistoryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./surgery-history-detail.component').then(m => m.SurgeryHistoryDetailComponent),
              resolve: { surgeryHistory: () => of({ id: 2530 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SurgeryHistoryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SurgeryHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load surgeryHistory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SurgeryHistoryDetailComponent);

      // THEN
      expect(instance.surgeryHistory()).toEqual(expect.objectContaining({ id: 2530 }));
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
