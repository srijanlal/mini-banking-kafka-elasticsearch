import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RecentSearchResponse } from '../models/recent-search.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RecentSearchService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.api.recentsearchBaseUrl;

  getRecentSearches(limit = 20): Observable<RecentSearchResponse> {
    return this.http.get<RecentSearchResponse>(`${this.baseUrl}/api/recent-searches`, {
      params: { limit }
    });
  }
}
