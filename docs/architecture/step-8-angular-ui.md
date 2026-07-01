# Step 8 - Angular UI

## Objective

Build a single-page Angular frontend that demonstrates the complete user flow:

- Search accounts
- Click an account
- Refresh and display recent searches

## Implemented Structure

- Page:
  - HomeComponent
- Components:
  - NavbarComponent
  - SearchBarComponent
  - SearchResultsComponent
  - RecentSearchComponent
- Services:
  - AccountSearchService
  - RecentSearchService
- Models:
  - Account
  - RecentSearch

## Behavior Implemented

- User input emits query updates.
- Search is debounced by 300ms.
- Search triggers GET /api/accounts/search?query= on acctsearch-service.
- Clicking a result triggers POST /api/accounts/{id}/click.
- On click success, UI refreshes GET /api/recent-searches from recentsearch-service.

## Technical Notes

- Implemented with standalone Angular components.
- RxJS is used for debounce and async orchestration.
- HTTP API base URLs are externalized in src/environments/environment.ts.

## Enterprise Considerations

- UI separates rendering components from API service concerns.
- Models define explicit frontend contracts.
- Error states are handled gracefully in result and recent panels.
- Layout is responsive for desktop and mobile.

## Learning Simplifications

- No authentication flow yet.
- No global state library (NgRx) yet.
- No route guards since app has a single page.
