import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountSearchResponse } from '../models/account.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountSearchService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.api.acctsearchBaseUrl;

  searchAccounts(query: string): Observable<AccountSearchResponse> {
    return this.http.get<AccountSearchResponse>(`${this.baseUrl}/api/accounts/search`, {
      params: { query }
    });
  }

  clickAccount(accountId: string): Observable<unknown> {
    return this.http.post(`${this.baseUrl}/api/accounts/${accountId}/click`, {});
  }
}
