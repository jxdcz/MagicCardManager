# Magic Card Manager

A simple app designed to make your life easier when searching for specific Magic The Gathering cards.

Feature list:
* Filtering based on several search criteria
* Once downloaded, cards are saved and available for offline viewing
* Card image displaying and caching
* Tablet-friendly layout with dual pane list/detail flow

Technical features:
* Android MVVM Architecture
* LiveData
* RxJava
* Retrofit
* Butterknife
* RecyclerView
* Room persistence DB
* Picasso
* Unit and UI tests

Roadmap / TODO list
* Filtering by power/toughness doesn't work yet - the remote api doesn't support a direct search, will need to use more sophisticated methods
* More tests
* Extract dimensions to dimens.xml
* Extract CardListActivity to a fragment
* Better layouts, especially for the card list - show more info for the items
* More TODOs in code
