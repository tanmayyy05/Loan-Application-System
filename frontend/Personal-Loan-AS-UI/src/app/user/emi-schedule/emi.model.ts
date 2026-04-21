export interface Emi {
  id: number;
  emiNumber: number;
  dueDate: string;
  emiAmount: number;
  penaltyAmount: number;
  totalPayableAmount: number;
  emiStatus: string;
}

export interface EmiResponse {
  hasActiveLoan: boolean;
  message: string;
  emis: Emi[];
}
