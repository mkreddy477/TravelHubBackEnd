# Missing Fields Analysis for TripjackResponse Models

## Summary
Based on the analysis of `expectedResponse.txt`, several fields are missing from the `TripjackResponse.java` model classes. These fields are required to properly parse the complete API response from the Tripjack flight search API.

## Missing Fields by Class

### 1. **AirportInfo** Class
**Location:** Lines 304-369 in TripjackResponse.java

**Missing Fields:**
- `cityCode` (String) - City code like "NYC", "LON"
- `countryCode` (String) - Country code like "US", "GB"

**Example from API:**
```json
{
  "code": "JFK",
  "name": "John F Kennedy Intl",
  "cityCode": "NYC",
  "city": "New York",
  "country": "United States",
  "countryCode": "US",
  "terminal": "Terminal 1"
}
```

---

### 2. **SegmentInfo** Class
**Location:** Lines 165-302 in TripjackResponse.java

**Missing Fields:**
- `cT` (Integer) - Connection time in minutes
- `sN` (Integer) - Segment number/sequence
- `iand` (Boolean) - International and domestic indicator
- `isRs` (Boolean) - Refundable/some status flag
- `so` (List<Object>) - Stop overs list

**Current Issue:** `duration` field is String but should be Integer

**Example from API:**
```json
{
  "id": "613",
  "duration": 415,
  "cT": 285,
  "sN": 0,
  "iand": true,
  "isRs": false,
  "so": []
}
```

---

### 3. **TripInfo** Class
**Location:** Lines 113-163 in TripjackResponse.java

**Missing Fields:**
- `airFlowType` (String) - e.g., "SEARCH"
- `ipm` (String) - Some identifier (often empty string)
- `issf` (Boolean) - Some status flag

**Example from API:**
```json
{
  "sI": [...],
  "totalPriceList": [...],
  "airFlowType": "SEARCH",
  "ipm": "",
  "issf": false
}
```

---

### 4. **AirlineInfo** Class
**Location:** Lines 407-436 in TripjackResponse.java

**Missing Fields:**
- `isLcc` (Boolean) - Is Low Cost Carrier flag

**Example from API:**
```json
{
  "code": "TP",
  "name": "TAP Air Portugal",
  "isLcc": false
}
```

---

### 5. **PriceInfo** Class (totalPriceList items)
**Location:** Lines 438-463 in TripjackResponse.java

**Current State:** Only has `fd` (Map<String, Object>) and helper method

**Missing Fields:**
- `fareIdentifier` (String) - e.g., "PUBLISHED"
- `id` (String) - Unique fare ID
- `msri` (List<Object>) - Some array (usually empty)
- `messages` (List<Object>) - Messages array (usually empty)
- `tai` (Map<String, Object>) - Additional info with baggage details per segment
- `icca` (Boolean) - Some flag

**Example from API:**
```json
{
  "fd": {
    "ADULT": {
      "fC": {
        "TAF": 15807.50,
        "NF": 18687.50,
        "TF": 18687.50,
        "BF": 2880.00
      },
      "afC": {
        "TAF": {
          "MFT": 13.50,
          "MF": 75.00,
          "YQ": 9271.00,
          "OT": 6448.00
        }
      },
      "sR": 9,
      "bI": {
        "iB": "0 Kg",
        "cB": "7 Kg"
      },
      "rT": 0,
      "cc": "ECONOMY",
      "cB": "E",
      "fB": "EL0DSI05"
    }
  },
  "fareIdentifier": "PUBLISHED",
  "id": "12-15-2-10-0517614246_0JFKLISTP210LISLHRTP1356~14086447483091750",
  "msri": [],
  "messages": [],
  "tai": {
    "tbi": {
      "613": [
        {
          "ADULT": {
            "iB": "0 Kg",
            "cB": "7 Kg"
          }
        }
      ]
    }
  },
  "icca": false
}
```

---

## Recommended Actions

### Immediate Fixes Required:

1. **Add missing fields to AirportInfo:**
   ```java
   private String cityCode;
   private String countryCode;
   // + getters/setters
   ```

2. **Add missing fields to SegmentInfo:**
   ```java
   @JsonProperty("cT")
   private Integer connectionTime;
   
   @JsonProperty("sN")
   private Integer segmentNumber;
   
   private Boolean iand;
   private Boolean isRs;
   
   @JsonProperty("so")
   private List<Object> stopOvers;
   // + getters/setters
   ```

3. **Fix duration type in SegmentInfo:**
   ```java
   private Integer duration; // Change from String to Integer
   ```

4. **Add missing fields to TripInfo:**
   ```java
   private String airFlowType;
   private String ipm;
   private Boolean issf;
   // + getters/setters
   ```

5. **Add missing field to AirlineInfo:**
   ```java
   private Boolean isLcc;
   // + getters/setters
   ```

6. **Add missing fields to PriceInfo:**
   ```java
   private String fareIdentifier;
   private String id;
   private List<Object> msri;
   private List<Object> messages;
   private Map<String, Object> tai;
   private Boolean icca;
   // + getters/setters
   ```

---

## Impact on Data Flow

### Current Flow:
1. **API Call** → `TripjackFlightSearchService.search()` (line 58-115)
2. **WebClient Response** → `.bodyToMono(TripjackResponse.class)` (line 80)
3. **Transformation** → `transformToFlightSearchResultGroup()` (line 116-153)
4. **Controller Response** → `FlightSearchController.search()` (line 31-52)

### Issues with Missing Fields:
- **Data Loss**: Missing fields are silently ignored during JSON deserialization
- **Incomplete Mapping**: Transformation logic cannot access missing data
- **Frontend Impact**: UI cannot display complete flight information (baggage, fare details, etc.)

---

## Testing Recommendations

After adding missing fields:

1. **Verify JSON Deserialization:**
   - Test with actual API response from `expectedResponse.txt`
   - Ensure all fields are properly mapped

2. **Update Transformation Logic:**
   - Review `convertToFlightOptions()` method (lines 155-198)
   - Map new fields to `FlightOptionDto`

3. **Check FlightOptionDto:**
   - Verify if `FlightOptionDto` has corresponding fields
   - Add missing fields if needed

---

## Files to Review/Update:

1. ✅ `TripjackResponse.java` - Add missing fields
2. ⚠️ `FlightOptionDto.java` - May need additional fields
3. ⚠️ `TripjackFlightSearchService.java` - Update transformation logic
4. ⚠️ `FlightSearchController.java` - Verify response structure

---

## Notes:

- All missing fields use `@JsonIgnoreProperties(ignoreUnknown = true)` so they won't cause errors if not present
- The `fd` (fare details) structure is complex and currently uses `Map<String, Object>` which works but could be improved with proper nested classes
- Consider creating dedicated classes for fare components (fC, afC, bI) for better type safety

