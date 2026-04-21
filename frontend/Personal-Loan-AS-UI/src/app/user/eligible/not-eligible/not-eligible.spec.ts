import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotEligible } from './not-eligible';

describe('NotEligible', () => {
  let component: NotEligible;
  let fixture: ComponentFixture<NotEligible>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotEligible]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotEligible);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
