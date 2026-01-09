# Mock TripJack Data Service

This module provides mock flight search data that mirrors the TripJack API format. Use it as a backup when the TripJack test server is unavailable.

## How to Enable Mock Mode

Set the following property in `application.properties`:

```properties
travelhub.mock.enabled=true
```

Or via environment variable:
```bash
TRAVELHUB_MOCK_ENABLED=true
```

## Features

The mock data service generates realistic flight data including:

### Airlines Supported
- **Indian Domestic**: Air India (AI), IndiGo (6E), Vistara (UK), SpiceJet (SG), Go First (G8), Akasa Air (QP), AirAsia India (I5)
- **International**: Emirates (EK), Qatar Airways (QR), Singapore Airlines (SQ), Lufthansa (LH), British Airways (BA)

### Airports Supported
- **Indian**: DEL, BOM, BLR, MAA, CCU, HYD, COK, GOI, PNQ, AMD, JAI
- **International**: DXB, SIN, LHR, JFK, DOH, FRA, BKK, KUL

### Flight Types
- Direct flights (60% probability)
- Connecting flights with layovers (40% probability)

### Trip Types Supported
- One-way
- Round-trip
- Multi-city

### Data Generated
- 8-15 flight options per search
- Realistic pricing based on route distance and cabin class
- Baggage allowances (check-in and cabin)
- Seat availability
- Refundable/non-refundable fares
- Multiple cabin classes (Economy, Premium Economy, Business, First)

## Response Format

The mock service generates responses in the exact TripJack API format:

```json
{
  "status": { "success": true, "httpStatus": 200 },
  "searchQuery": { ... },
  "searchResult": {
    "tripInfos": {
      "ONWARD": [ ... ],
      "RETURN": [ ... ]
    }
  }
}
```

## Price Calculation

Base fare is calculated using:
- Route distance (duration-based)
- International route multiplier (2.5x)
- Direct flight premium (15%)
- Cabin class multipliers:
  - Economy: 1x
  - Premium Economy: 1.8x
  - Business: 3.5x
  - First: 6x
- Random variance (+/- 20%)

## Files

- `MockTripjackDataService.java` - Main mock data generator service
- `sample-tripjack-response.json` - Sample response format reference

## Switching Between Mock and Real API

The application automatically switches based on the `travelhub.mock.enabled` property:
- `false` (default): Calls real TripJack API
- `true`: Uses mock data generator

When mock mode is enabled, you'll see this message at startup:
```
========================================
  MOCK MODE ENABLED - Using mock data
  TripJack API will NOT be called
========================================
```
