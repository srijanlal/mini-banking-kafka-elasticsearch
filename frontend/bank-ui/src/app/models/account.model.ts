export interface Account {
  id: string;
  accountNumber: string;
  shortName: string;
  accountName: string;
  customerId: string;
  officeCode: string;
  status: string;
}

export interface AccountSearchResponse {
  accounts: Account[];
  count: number;
}
