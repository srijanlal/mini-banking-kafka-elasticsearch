import { CommonModule, DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RecentSearch } from '../../models/recent-search.model';

@Component({
  selector: 'app-recent-search',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './recent-search.component.html',
  styleUrl: './recent-search.component.scss'
})
export class RecentSearchComponent {
  @Input({ required: true }) items: RecentSearch[] = [];
  @Input() loading = false;
}