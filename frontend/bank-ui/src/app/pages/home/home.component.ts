import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { catchError, debounceTime, distinctUntilChanged, of, Subject, switchMap, tap } from 'rxjs';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { SearchBarComponent } from '../../components/search-bar/search-bar.component';
import { SearchResultsComponent } from '../../components/search-results/search-results.component';
import { RecentSearchComponent } from '../../components/recent-search/recent-search.component';
import { Account } from '../../models/account.model';
import { RecentSearch } from '../../models/recent-search.model';
import { AccountSearchService } from '../../services/account-search.service';
import { RecentSearchService } from '../../services/recent-search.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    SearchBarComponent,
    SearchResultsComponent,
    RecentSearchComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  private readonly accountSearchService = inject(AccountSearchService);
  private readonly recentSearchService = inject(RecentSearchService);

  private readonly searchInput$ = new Subject<string>();

  accounts: Account[] = [];
  recentSearches: RecentSearch[] = [];

  searching = false;
  loadingRecent = false;
  searchError = '';

  ngOnInit(): void {
    this.searchInput$
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((query) => {
          const normalized = query.trim();
          if (normalized.length < 2) {
            this.accounts = [];
            this.searching = false;
            this.searchError = '';
            return of(null);
          }

          this.searching = true;
          this.searchError = '';
          return this.accountSearchService.searchAccounts(normalized).pipe(
            tap((response) => {
              this.accounts = response.accounts;
              this.searching = false;
            }),
            catchError(() => {
              this.searching = false;
              this.accounts = [];
              this.searchError = 'Search is currently unavailable. Verify backend services are running.';
              return of(null);
            })
          );
        })
      )
      .subscribe();

    this.refreshRecentSearches();
  }

  onQueryChanged(query: string): void {
    this.searchInput$.next(query);
  }

  onAccountSelected(account: Account): void {
    this.accountSearchService.clickAccount(account.id)
      .pipe(
        tap(() => this.refreshRecentSearches()),
        catchError(() => {
          this.searchError = 'Could not register account click. Check Kafka and service health.';
          return of(null);
        })
      )
      .subscribe();
  }

  private refreshRecentSearches(): void {
    this.loadingRecent = true;
    this.recentSearchService.getRecentSearches(20)
      .pipe(
        tap((response) => {
          this.recentSearches = response.items;
          this.loadingRecent = false;
        }),
        catchError(() => {
          this.recentSearches = [];
          this.loadingRecent = false;
          return of(null);
        })
      )
      .subscribe();
  }
}
