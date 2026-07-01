import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Account } from '../../models/account.model';

@Component({
  selector: 'app-search-results',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.scss'
})
export class SearchResultsComponent {
  @Input({ required: true }) accounts: Account[] = [];
  @Input() loading = false;
  @Input() errorMessage = '';

  @Output() accountSelected = new EventEmitter<Account>();
}
