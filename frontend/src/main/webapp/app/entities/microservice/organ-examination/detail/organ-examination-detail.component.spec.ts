import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrganExaminationDetailComponent } from './organ-examination-detail.component';

describe('OrganExamination Management Detail Component', () => {
  let comp: OrganExaminationDetailComponent;
  let fixture: ComponentFixture<OrganExaminationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganExaminationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./organ-examination-detail.component').then(m => m.OrganExaminationDetailComponent),
              resolve: { organExamination: () => of({ id: 8565 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OrganExaminationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrganExaminationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load organExamination on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrganExaminationDetailComponent);

      // THEN
      expect(instance.organExamination()).toEqual(expect.objectContaining({ id: 8565 }));
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
