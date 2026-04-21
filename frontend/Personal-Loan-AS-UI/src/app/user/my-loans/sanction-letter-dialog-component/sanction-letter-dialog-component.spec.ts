import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SanctionLetterDialogComponent } from './sanction-letter-dialog-component';

describe('SanctionLetterDialogComponent', () => {
  let component: SanctionLetterDialogComponent;
  let fixture: ComponentFixture<SanctionLetterDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SanctionLetterDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SanctionLetterDialogComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
