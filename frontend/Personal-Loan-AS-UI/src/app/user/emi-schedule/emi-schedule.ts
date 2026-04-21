import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { EmiService } from './emi.service';
import { Emi } from './emi.model';


@Component({
  selector: 'app-emi-schedule',
  imports: [CommonModule],
  templateUrl: './emi-schedule.html',
})
export class EmiSchedule implements OnInit {

  displayedColumns: string[] = [
    'emiNumber',
    'dueDate',
    'emiAmount',
    'penaltyAmount',
    'totalPayableAmount',
    'emiStatus',
    'action'
  ];

  emis: Emi[] = [];
  hasActiveLoan = false;

  constructor(private emiService: EmiService,private cdr:  ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.emiService.getCurrentEmis().subscribe(res => {
      this.hasActiveLoan = res.hasActiveLoan;
      this.emis = res.emis || [];
      this.cdr.detectChanges();
    });
  }

  payEmi(emi: Emi) {
    console.log('Pay EMI:', emi);
  }
}

