import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanOfficerDialog } from './loan-officer-dialog';

describe('LoanOfficerDialog', () => {
  let component: LoanOfficerDialog;
  let fixture: ComponentFixture<LoanOfficerDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanOfficerDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoanOfficerDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
