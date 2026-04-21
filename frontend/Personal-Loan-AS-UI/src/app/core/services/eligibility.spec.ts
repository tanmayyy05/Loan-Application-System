import { TestBed } from '@angular/core/testing';

import { Eligibility } from './eligibility';

describe('Eligibility', () => {
  let service: Eligibility;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Eligibility);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
