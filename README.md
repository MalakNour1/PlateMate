# PlateMate 🍽️

A recipe app that works fully offline after the first load — browse, search, save favorites, plan meals, and build shopping lists, even with no internet.

## Features

- 📱 Recipe list with infinite scroll (paging)
- 🔍 Search and filter by category
- 📖 Full recipe details — ingredients, steps, cooking mode
- ❤️ Favorites, available offline
- 🛒 Shopping list generated from recipe ingredients
- 📅 Weekly meal planner with random auto-fill + manual picks
- 🔌 Offline mode with a banner — cached data loads with zero internet
- 🔄 Pull-to-refresh
- 🌙 Light/dark theme

## Screenshots

| Home | Recipe Detail | Search |
|---|---|---|
| <img src="https://github.com/user-attachments/assets/b03d87a3-7fd5-4c77-89ea-656f119a7208" width="220"/> | <img src="https://github.com/user-attachments/assets/2406b91d-f862-4273-af20-8b031460cf36" width="220"/> | <img src="https://github.com/user-attachments/assets/9e36779a-53ec-412b-bbd6-fe3707317f80" width="220"/> |

| Meal Planner | Shopping List | Offline Mode |
|---|---|---|
| <img src="https://github.com/user-attachments/assets/abce28a9-4bef-4320-8025-37aec3bc38ab" width="220"/> | <img src="https://github.com/user-attachments/assets/1d56c5ae-ff11-4642-9210-7248671ce68d" width="220"/> | <img src="https://github.com/user-attachments/assets/c1026da1-70ef-4875-a5ec-fdd99ee36436" width="220"/> |

## Architecture

MVVM + Repository pattern, three layers:

```
UI (Compose) → ViewModel → Repository → Room (cache) / Retrofit (Spoonacular API)
```

- **Single source of truth:** UI reads only from Room; the network's only job is writing into it
- **Paging 3 + RemoteMediator:** pages fetched from the API are cached in Room, and paging reads from the cache — not directly from the network
- **Cache expiry:** cached data refreshes automatically after 15 minutes; pull-to-refresh bypasses this

## Tech Stack

Kotlin · Jetpack Compose · Room + Paging 3 · Retrofit + Gson · Coroutines/Flow · Spoonacular API

## Setup

1. Clone the repo
2. Get a free API key from [spoonacular.com/food-api](https://spoonacular.com/food-api)
3. Add to `local.properties`:
   ```
   SPOONACULAR_API_KEY=your_key_here
   ```
4. Sync Gradle and run

## Known Limitations

- Pull-to-refresh bypasses the 15-minute cache window but can't yet distinguish itself from a cold-start refresh at the Paging 3 level

## Team

Malak Nour, Ganna Abdelrahman
