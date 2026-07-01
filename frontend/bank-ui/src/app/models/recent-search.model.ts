export interface RecentSearch {
  id: string;
  accountId: string;
  accountNumber: string;
  accountName: string;
  clickedAt: string;
  username: string;
}

export interface RecentSearchResponse {
  items: RecentSearch[];
  count: number;
}
