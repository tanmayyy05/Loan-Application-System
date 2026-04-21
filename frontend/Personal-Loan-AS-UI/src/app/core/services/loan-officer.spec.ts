import { TestBed } from '@angular/core/testing';

import { LoanOfficer } from './loan-officer';

describe('LoanOfficer', () => {
  let service: LoanOfficer;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoanOfficer);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
