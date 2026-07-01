package com.example.recentsearch.service;

import com.example.recentsearch.dto.RecentSearchResponse;
import com.example.recentsearch.event.SearchEvent;

public interface RecentSearchService {

    RecentSearchResponse getRecentSearches(int limit);

    void saveFromEvent(SearchEvent event);
}
