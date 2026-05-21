# 🍜 Pack N' Go App — IB Computer Science Internal Assessment 2024 

An Android food ordering application developed as part of the **IB Computer Science Internal Assessment**. The app simulates a food pick-up ordering system with two distinct user roles: **Customer** and **Store Owner**.

---

## 📱 Overview

The app allows customers to browse a menu, build a cart, place orders, and track order progress in real time. Store owners can view incoming orders, mark them as ready, and complete them once collected.

---

## ✨ Features

### Customer Side
- Browse a menu of food items with names, prices, and descriptions
- Add items to a cart with quantity selection
- View and edit the receipt before placing an order
- Swipe-to-delete or swipe-to-edit items on the receipt
- Track order progress with an animated progress bar
- Resume a previously placed order

### Store Owner Side
- View all incoming orders with order IDs
- Expand/collapse order details
- Mark orders as ready (triggers a push notification to the customer)
- Complete and remove fulfilled orders

### General
- Splash screen with sound effects on launch
- Push notifications (order placed → store owner; order ready → customer)
- Persistent cart state via `SharedPreferences`
- Two SQLite databases: one for food items, one for food orders

---

## 🏗️ Architecture & Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Platform | Android (Android Studio) |
| UI | RecyclerView, BottomSheetDialogFragment, ItemTouchHelper |
| Local Storage | SQLite (via `SQLiteOpenHelper`) |
| Cart Persistence | `SharedPreferences` + Gson |
| Notifications | Android `NotificationManager` |
| Build System | Gradle |

---

## 📂 Project Structure

```
app/src/main/java/com/example/comscifoodap/
│
├── Activities & Fragments
│   ├── SplashActivity.java          # Launch screen with animation + sounds
│   ├── ChooseUserPage.java          # Role selection (Customer / Store Owner)
│   ├── MainActivity.java            # Customer food selection menu
│   ├── FoodDescriptionPage.java     # Bottom sheet: food details + add to cart
│   ├── FoodQuantityPage.java        # Bottom sheet: adjust quantity in cart
│   ├── ReceiptPage.java             # Cart review + place order
│   ├── ProgressOfFood.java          # Order tracking with animated progress bar
│   ├── UserOrderChoicePage.java     # Customer: resume or start a new order
│   └── StoreFoodOrderPage.java      # Store owner: manage all orders
│
├── Adapters
│   ├── FoodSelectionRecyclerViewAdapter.java
│   ├── FoodReceiptRecyclerViewAdapter.java
│   ├── StoreFoodOrderRecyclerViewAdapter.java
│   ├── UserOrderChoiceRecyclerViewAdapter.java
│   └── SharedPreferencesHelper.java
│
├── Database
│   ├── FoodItemDBHelper.java        # SQLite schema for food menu items
│   ├── FoodItemDBHandler.java       # CRUD operations for food items
│   ├── FoodOrderDBHelper.java       # SQLite schema for customer orders
│   └── FoodOrderDBOps.java         # CRUD operations for food orders
│
├── Models
│   ├── FoodItem.java
│   ├── FoodOrderItem.java
│   └── ReceiptItem.java
│
└── Helpers
    └── RecyclerItemTouchHelperFoodReceipt.java  # Swipe gestures on receipt
```

---

## 🚀 Getting Started

### Prerequisites
- [Android Studio](https://developer.android.com/studio) (Flamingo or later recommended)
- Android SDK 26+
- A physical Android device or emulator (API 26+)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/tapiocajellybeans/pack_n_go_application.git
   ```

2. **Open in Android Studio**
   - File → Open → select the cloned project folder

3. **Sync Gradle**
   - Android Studio will prompt you to sync. Click **Sync Now**.

4. **Run the app**
   - Select your device/emulator and click ▶ **Run**

> **Note:** On first launch, the food item database is empty. You can add items programmatically via `FoodItemDBHandler.addFoodItem()` — see the commented-out code in `MainActivity.java` for an example.

---

## 🗄️ Database Schema

### `storeOwnerFoodItemsDatabase` — Food Menu
| Column | Type | Description |
|---|---|---|
| `id` | INTEGER PK AUTOINCREMENT | Unique food item ID |
| `foodName` | TEXT | Name of the dish |
| `foodPrice` | TEXT | Price (stored as string) |
| `foodDescription` | TEXT | Description of the dish |

### `customerFoodOrderDatabase` — Orders
| Column | Type | Description |
|---|---|---|
| `id` | INTEGER PK | Order ID |
| `hashMapGson` | TEXT | Cart contents serialised as JSON (Gson) |
| `isReady` | INTEGER | `0` = pending, `1` = ready for collection |

---

## 📸 App Flow

```
SplashActivity
    └── ChooseUserPage
            ├── [Customer] → UserOrderChoicePage
            │       ├── Resume Order → ProgressOfFood
            │       └── New Order → MainActivity
            │               └── FoodDescriptionPage (bottom sheet)
            │                       └── ReceiptPage
            │                               └── ProgressOfFood
            └── [Store Owner] → StoreFoodOrderPage
```

---

## 🧠 Key Implementation Details

- **Cart state** is persisted in `SharedPreferences` as a JSON string (via Gson), mapping food names → quantities.
- **Order IDs** are auto-incremented by querying `MAX(id)` from the orders table.
- **Progress bar** loops until the store owner marks the order ready, polling the DB every 5ms.
- **Swipe gestures** on the receipt use `ItemTouchHelper`: swipe left to delete, swipe right to edit quantity.
- **Notifications** use Android's `NotificationChannel` API (required for API 26+).

---

## ⚠️ Known Limitations

- The food item database must be seeded manually on first install (no admin UI for adding menu items).
- Order progress polling is done on the main thread at a very short interval — a production app would use a background service or LiveData.
- The app does not support multiple simultaneous customers (shared `SharedPreferences` cart).

