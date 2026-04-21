import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanOfficerList } from './loan-officer-list';

describe('LoanOfficerList', () => {
  let component: LoanOfficerList;
  let fixture: ComponentFixture<LoanOfficerList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanOfficerList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoanOfficerList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
