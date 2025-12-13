import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import {
  Plane,
  Calendar,
  Users,
  ArrowRight,
  ChevronDown,
  ChevronUp,
  ChevronLeft,
  ChevronRight,
  X,
  Filter,
  Wifi,
  Plug,
  Tv,
  UtensilsCrossed,
  Briefcase,
  Luggage,
  AlertCircle,
  Bell,
  List,
  CalendarDays,
  SlidersHorizontal,
  Check,
  Leaf
} from 'lucide-react';

import { searchFlights } from '../services/bookingService';

import { Button } from '../components/Button';
import { Badge } from '../components/Badge';
import { IconButton } from '../components/IconButton';
import { Checkbox } from '../components/Checkbox';
import { Divider } from '../components/Divider';
import { Select } from '../components/Select';
import { RadioButton, RadioGroup } from '../components/RadioButton';
import { Input } from '../components/Input';
import { Spinner } from '../components/Spinner';

import { Navbar } from '../components/Navbar';
import { NewsletterSignup } from '../components/NewsletterSignup';
import { Footer } from '../components/Footer';
import { EmptyState } from '../components/EmptyState';

import './FlightResultsPage.css';

// ============================================================================
// HELPER FUNCTIONS
// ============================================================================

const formatTime = (date) => {
  if (!date) return '';
  return new Date(date).toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
  });
};

const formatDuration = (minutes) => {
  if (!minutes && minutes !== 0) return '';
  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;
  return `${hours}h ${mins}m`;
};

const formatDate = (date) => {
  if (!date) return '';
  return new Date(date).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
  });
};

const getTimeOfDay = (hour) => {
  if (hour >= 5 && hour < 12) return 'morning';
  if (hour >= 12 && hour < 18) return 'afternoon';
  if (hour >= 18 && hour < 24) return 'evening';
  return 'night';
};

const getDayDifference = (departure, arrival) => {
  const depDate = new Date(departure);
  const arrDate = new Date(arrival);
  const diff = Math.floor(
    (arrDate.setHours(0, 0, 0, 0) - depDate.setHours(0, 0, 0, 0)) /
      (1000 * 60 * 60 * 24)
  );
  return diff;
};

/**
 * Transform backend flight data to the format expected by FlightResultsPage
 * Backend format: { onward: [...], returns: [...], combos: [...] }
 * Expected format: [{ outbound: {...}, inbound: {...}, price: {...}, ... }]
 */
const transformBackendFlights = (data) => {
  // If data is already an array (mock data), return as-is
  if (Array.isArray(data)) {
    return data;
  }
  
  // If data has 'flights' property (old format)
  if (data.flights && Array.isArray(data.flights)) {
    return data.flights;
  }
  
  // Transform from backend format { onward: [...], returns: [...] }
  const onwardFlights = data.onward || [];
  const returnFlights = data.returns || [];
  
  return onwardFlights.map((flight, index) => {
    // Find matching return flight if exists
    const returnFlight = returnFlights[index] || null;
    
    return {
      id: flight.priceId || `flight-${index}`,
      priceId: flight.priceId,
      travelClass: flight.cabinClass || 'Economy',
      
      // Outbound segment
      outbound: {
        airline: {
          code: flight.marketingAirlineCode,
          name: flight.marketingAirlineName,
          logo: `https://www.gstatic.com/flights/airline_logos/70px/${flight.marketingAirlineCode}.png`,
          flightNumber: flight.flightNumber,
        },
        flightNumber: `${flight.marketingAirlineCode}-${flight.flightNumber}`,
        departure: {
          time: flight.departure,
          airport: {
            code: flight.from,
            name: flight.from,
            city: flight.from,
          },
        },
        arrival: {
          time: flight.arrival,
          airport: {
            code: flight.to,
            name: flight.to,
            city: flight.to,
          },
        },
        duration: flight.totalDurationMinutes,
        durationMinutes: flight.totalDurationMinutes,
        stops: flight.stops > 0 ? Array(flight.stops).fill({ airport: 'Connection' }) : [],
        aircraft: 'Aircraft',
      },
      
      // Inbound segment (if return flight exists)
      inbound: returnFlight ? {
        airline: {
          code: returnFlight.marketingAirlineCode,
          name: returnFlight.marketingAirlineName,
          logo: `https://www.gstatic.com/flights/airline_logos/70px/${returnFlight.marketingAirlineCode}.png`,
          flightNumber: returnFlight.flightNumber,
        },
        flightNumber: `${returnFlight.marketingAirlineCode}-${returnFlight.flightNumber}`,
        departure: {
          time: returnFlight.departure,
          airport: {
            code: returnFlight.from,
            name: returnFlight.from,
            city: returnFlight.from,
          },
        },
        arrival: {
          time: returnFlight.arrival,
          airport: {
            code: returnFlight.to,
            name: returnFlight.to,
            city: returnFlight.to,
          },
        },
        duration: returnFlight.totalDurationMinutes,
        durationMinutes: returnFlight.totalDurationMinutes,
        stops: returnFlight.stops > 0 ? Array(returnFlight.stops).fill({ airport: 'Connection' }) : [],
        aircraft: 'Aircraft',
      } : null,
      
      // Price info
      price: {
        total: flight.totalFare,
        perPerson: flight.totalFare,
        currency: flight.currency || 'INR',
        baseFare: Math.round(flight.totalFare * 0.8),
        taxes: Math.round(flight.totalFare * 0.2),
      },
      
      // Baggage info
      baggage: {
        checkIn: flight.baggageCheckIn || '0 Kg',
        cabin: flight.baggageCabin || '7 Kg',
        carryOn: 1,
        checked: flight.baggageCheckIn && flight.baggageCheckIn !== '0 Kg' ? 1 : 0,
      },
      
      // Additional info
      refundable: flight.refundable ? 1 : 0,
      seatsRemaining: flight.seatsRemaining || 0,
      bookingDetails: {
        seatsLeft: flight.seatsRemaining || 0,
      },
      cabinClass: flight.cabinClass || 'Economy',
      stops: flight.stops || 0,
      
      // Badges (computed)
      badges: [],
      amenities: [],
    };
  });
};

// ============================================================================
// REUSABLE SUB-COMPONENTS
// ============================================================================

const FilterSection = ({ title, children, defaultOpen = true }) => {
  const [isOpen, setIsOpen] = useState(defaultOpen);

  return (
    <div className="filter-section">
      <button
        className="filter-section-header"
        onClick={() => setIsOpen(!isOpen)}
        aria-expanded={isOpen}
      >
        <span className="filter-section-title">{title}</span>
        {isOpen ? <ChevronUp size={18} /> : <ChevronDown size={18} />}
      </button>
      {isOpen && <div className="filter-section-content">{children}</div>}
    </div>
  );
};

const PriceRangeSlider = ({ min, max, value, onChange }) => {
  const [localValue, setLocalValue] = useState(value);

  useEffect(() => {
    setLocalValue(value);
  }, [value]);

  const handleMinChange = (e) => {
    const newMin = Math.min(Number(e.target.value), localValue[1] - 10);
    setLocalValue([newMin, localValue[1]]);
  };

  const handleMaxChange = (e) => {
    const newMax = Math.max(Number(e.target.value), localValue[0] + 10);
    setLocalValue([localValue[0], newMax]);
  };

  const handleMouseUp = () => {
    onChange(localValue);
  };

  const minPercent = ((localValue[0] - min) / (max - min)) * 100;
  const maxPercent = ((localValue[1] - min) / (max - min)) * 100;

  return (
    <div className="price-range-slider">
      <div className="price-range-inputs">
        <div className="price-input-group">
          <span className="price-input-label">Min</span>
          <span className="price-input-value">${localValue[0]}</span>
        </div>
        <div className="price-input-group">
          <span className="price-input-label">Max</span>
          <span className="price-input-value">${localValue[1]}</span>
        </div>
      </div>
      <div className="price-range-track">
        <div
          className="price-range-fill"
          style={{ left: `${minPercent}%`, width: `${maxPercent - minPercent}%` }}
        />
        <input
          type="range"
          min={min}
          max={max}
          value={localValue[0]}
          onChange={handleMinChange}
          onMouseUp={handleMouseUp}
          onTouchEnd={handleMouseUp}
          className="price-range-input price-range-input-min"
        />
        <input
          type="range"
          min={min}
          max={max}
          value={localValue[1]}
          onChange={handleMaxChange}
          onMouseUp={handleMouseUp}
          onTouchEnd={handleMouseUp}
          className="price-range-input price-range-input-max"
        />
      </div>
    </div>
  );
};

const DurationSlider = ({ max, value, onChange }) => {
  const [localValue, setLocalValue] = useState(value);

  useEffect(() => {
    setLocalValue(value);
  }, [value]);

  const handleChange = (e) => {
    setLocalValue(Number(e.target.value));
  };

  const handleMouseUp = () => {
    onChange(localValue);
  };

  return (
    <div className="duration-slider">
      <div className="duration-slider-header">
        <span>Up to {formatDuration(localValue * 60)}</span>
      </div>
      <input
        type="range"
        min={1}
        max={max}
        value={localValue}
        onChange={handleChange}
        onMouseUp={handleMouseUp}
        onTouchEnd={handleMouseUp}
        className="duration-slider-input"
      />
      <div className="duration-slider-labels">
        <span>1h</span>
        <span>{max}h+</span>
      </div>
    </div>
  );
};

const FlightSegment = ({ segment, isReturn = false }) => {
  if (!segment) return null;

  const dep = segment.departure || {};
  const arr = segment.arrival || {};
  const depAirport = dep.airport || {};
  const arrAirport = arr.airport || {};

  const dayDiff =
    dep.time && arr.time ? getDayDifference(dep.time, arr.time) : 0;

  return (
    <div className={`flight-segment ${isReturn ? 'flight-segment-return' : ''}`}>
      <div className="flight-segment-airline">
        {segment.airline?.logo && (
          <img
            src={segment.airline.logo}
            alt={segment.airline.name}
            className="airline-logo"
            onError={(e) => {
              e.target.style.display = 'none';
            }}
          />
        )}
        <div className="airline-info">
          <span className="airline-name">{segment.airline?.name}</span>
          <span className="flight-number">{segment.flightNumber}</span>
        </div>
      </div>

      <div className="flight-segment-times">
        <div className="flight-time-block">
          <span className="flight-time">{formatTime(dep.time)}</span>
          <span className="flight-airport">{depAirport.code}</span>
        </div>

        <div className="flight-duration-block">
          <span className="flight-duration">
            {formatDuration(segment.duration || segment.durationMinutes)}
          </span>
          <div className="flight-line">
            <div className="flight-line-track" />
            {(segment.stops || []).map((_, idx) => (
              <div
                key={idx}
                className="flight-line-stop"
                style={{
                  left: `${((idx + 1) / ((segment.stops || []).length + 1)) * 100}%`,
                }}
              />
            ))}
          </div>
          <span
            className={`flight-stops ${
              !segment.stops || segment.stops.length === 0
                ? 'nonstop'
                : segment.stops.length === 1
                ? 'one-stop'
                : 'multi-stop'
            }`}
          >
            {!segment.stops || segment.stops.length === 0
              ? 'Nonstop'
              : segment.stops.length === 1
              ? `1 stop${
                  segment.stops[0]?.airport?.code && 
                  segment.stops[0].airport.code !== 'Connection'
                    ? ` in ${segment.stops[0].airport.code}`
                    : ''
                }`
              : `${segment.stops.length} stops`}
          </span>
        </div>

        <div className="flight-time-block">
          <span className="flight-time">
            {formatTime(arr.time)}
            {dayDiff > 0 && <sup className="next-day">+{dayDiff}</sup>}
          </span>
          <span className="flight-airport">{arrAirport.code}</span>
        </div>
      </div>
    </div>
  );
};

const FlightCard = ({
  flight,
  isExpanded,
  onToggleExpand,
  onSelect,
  isSelected,
}) => {
  const amenityIcons = {
    wifi: <Wifi size={14} />,
    power: <Plug size={14} />,
    entertainment: <Tv size={14} />,
    meals: <UtensilsCrossed size={14} />,
  };

  const baggage = flight.baggage || {};
  const price = flight.price || {};
  const badges = flight.badges || [];
  const amenities = flight.amenities || [];
  const bookingDetails = flight.bookingDetails || {};

  return (
    <div className={`flight-card ${isSelected ? 'flight-card-selected' : ''}`}>
      {badges.length > 0 && (
        <div className="flight-card-badges">
          {badges.map((badge, idx) => (
            <Badge
              key={idx}
              variant={
                badge === 'Cheapest'
                  ? 'success'
                  : badge === 'Fastest'
                  ? 'info'
                  : badge === 'Best'
                  ? 'primary'
                  : badge === 'Eco Friendly'
                  ? 'success'
                  : 'warning'
              }
              size="sm"
            >
              {badge === 'Eco Friendly' && <Leaf size={12} />} {badge}
            </Badge>
          ))}
        </div>
      )}

      <div className="flight-card-main">
        <div className="flight-card-segments">
          <FlightSegment segment={flight.outbound} />
          {flight.inbound && (
            <>
              <Divider spacing="sm" />
              <FlightSegment segment={flight.inbound} isReturn />
            </>
          )}
        </div>

        <div className="flight-card-price-section">
          <Badge variant="secondary" size="sm">
            {flight.travelClass}
          </Badge>

          <div className="flight-price">
            {price.originalPrice && (
              <span className="flight-price-original">${price.originalPrice}</span>
            )}
            <span className="flight-price-current">
              ${price.perPerson ?? price.total ?? '—'}
            </span>
            <span className="flight-price-label">per person</span>
          </div>

          <div className="flight-baggage">
            <Briefcase size={14} />
            <span>{baggage.carryOn ?? 0} carry-on</span>
            {baggage.checked > 0 && (
              <>
                <Luggage size={14} />
                <span>{baggage.checked} checked</span>
              </>
            )}
          </div>

          {bookingDetails.seatsLeft && (
            <span className="flight-seats-left">
              <AlertCircle size={14} /> {bookingDetails.seatsLeft} seats left
            </span>
          )}

          <div className="flight-card-actions">
            <Button
              variant="outline"
              size="sm"
              onClick={() => onToggleExpand(flight.id)}
            >
              {isExpanded ? 'Hide Details' : 'View Details'}
            </Button>
            <Button
              variant="primary"
              size="sm"
              onClick={() => onSelect(flight)}
              leftIcon={isSelected ? <Check size={16} /> : null}
            >
              {isSelected ? 'Selected' : 'Select'}
            </Button>
          </div>
        </div>
      </div>

      {isExpanded && (
        <div className="flight-card-details">
          <Divider spacing="md" />

          <div className="flight-details-grid">
            <div className="flight-detail-section">
              <h4>Outbound Flight Details</h4>
              <div className="flight-detail-row">
                <span className="detail-label">Flight:</span>
                <span className="detail-value">
                  {flight.outbound?.airline?.name} {flight.outbound?.flightNumber}
                </span>
              </div>
              {flight.outbound?.aircraft && flight.outbound.aircraft !== 'Aircraft' && (
                <div className="flight-detail-row">
                  <span className="detail-label">Aircraft:</span>
                  <span className="detail-value">{flight.outbound.aircraft}</span>
                </div>
              )}
              {flight.outbound?.departure?.terminal && (
                <div className="flight-detail-row">
                  <span className="detail-label">Departure Terminal:</span>
                  <span className="detail-value">{flight.outbound.departure.terminal}</span>
                </div>
              )}
              {flight.outbound?.arrival?.terminal && (
                <div className="flight-detail-row">
                  <span className="detail-label">Arrival Terminal:</span>
                  <span className="detail-value">{flight.outbound.arrival.terminal}</span>
                </div>
              )}
              {flight.outbound?.stops?.length > 0 && (
                <div className="flight-layovers">
                  <span className="detail-label">Stops:</span>
                  <span className="detail-value">
                    {flight.outbound.stops.length} stop{flight.outbound.stops.length > 1 ? 's' : ''}
                    {flight.outbound.stops[0]?.airport?.code && flight.outbound.stops[0].airport.code !== 'Connection' && (
                      <> via {flight.outbound.stops.map(s => s.airport?.code).filter(Boolean).join(', ')}</>
                    )}
                  </span>
                </div>
              )}
              <div className="flight-detail-row">
                <span className="detail-label">Duration:</span>
                <span className="detail-value">{formatDuration(flight.outbound?.duration)}</span>
              </div>
            </div>

            {flight.inbound && (
              <div className="flight-detail-section">
                <h4>Return Flight Details</h4>
                <div className="flight-detail-row">
                  <span className="detail-label">Flight:</span>
                  <span className="detail-value">
                    {flight.inbound?.airline?.name} {flight.inbound?.flightNumber}
                  </span>
                </div>
                {flight.inbound?.aircraft && flight.inbound.aircraft !== 'Aircraft' && (
                  <div className="flight-detail-row">
                    <span className="detail-label">Aircraft:</span>
                    <span className="detail-value">{flight.inbound.aircraft}</span>
                  </div>
                )}
                {flight.inbound?.departure?.terminal && (
                  <div className="flight-detail-row">
                    <span className="detail-label">Departure Terminal:</span>
                    <span className="detail-value">{flight.inbound.departure.terminal}</span>
                  </div>
                )}
                {flight.inbound?.arrival?.terminal && (
                  <div className="flight-detail-row">
                    <span className="detail-label">Arrival Terminal:</span>
                    <span className="detail-value">{flight.inbound.arrival.terminal}</span>
                  </div>
                )}
                {flight.inbound?.stops?.length > 0 && (
                  <div className="flight-layovers">
                    <span className="detail-label">Stops:</span>
                    <span className="detail-value">
                      {flight.inbound.stops.length} stop{flight.inbound.stops.length > 1 ? 's' : ''}
                      {flight.inbound.stops[0]?.airport?.code && flight.inbound.stops[0].airport.code !== 'Connection' && (
                        <> via {flight.inbound.stops.map(s => s.airport?.code).filter(Boolean).join(', ')}</>
                      )}
                    </span>
                  </div>
                )}
                <div className="flight-detail-row">
                  <span className="detail-label">Duration:</span>
                  <span className="detail-value">{formatDuration(flight.inbound?.duration)}</span>
                </div>
              </div>
            )}

            <div className="flight-detail-section">
              <h4>Amenities</h4>
              <div className="flight-amenities">
                {amenities.length > 0 ? (
                  amenities.map((amenity, idx) => (
                    <span key={idx} className="amenity-badge">
                      {amenityIcons[amenity]}{' '}
                      {amenity.charAt(0).toUpperCase() + amenity.slice(1)}
                    </span>
                  ))
                ) : (
                  <span className="no-amenities">No amenities listed</span>
                )}
              </div>
            </div>

            <div className="flight-detail-section">
              <h4>Baggage & Fare</h4>
              <div className="flight-detail-row">
                <span className="detail-label">Cabin baggage:</span>
                <span className="detail-value">
                  {baggage.cabin || '7 Kg'}
                </span>
              </div>
              <div className="flight-detail-row">
                <span className="detail-label">Check-in baggage:</span>
                <span className="detail-value">
                  {baggage.checkIn && baggage.checkIn !== '0 Kg'
                    ? baggage.checkIn
                    : 'Not included'}
                </span>
              </div>
              <div className="flight-detail-row">
                <span className="detail-label">Refundable:</span>
                <span className="detail-value">
                  {flight.refundable ? 'Yes' : 'No'}
                </span>
              </div>
              {flight.carbonEmissions && (
                <div className="flight-detail-row">
                  <span className="detail-label">CO2 emissions:</span>
                  <span className="detail-value">
                    {flight.carbonEmissions} kg
                  </span>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

const CalendarView = ({ selectedDate, onDateSelect }) => {
  const dates = [];
  const baseDate = new Date(selectedDate || new Date());

  for (let i = -7; i <= 7; i++) {
    const date = new Date(baseDate);
    date.setDate(date.getDate() + i);
    dates.push({
      date,
      price: 200 + Math.floor(Math.random() * 600),
      isCheapest: i === -3,
      isSelected: i === 0,
    });
  }

  return (
    <div className="calendar-view">
      <div className="calendar-grid">
        {dates.map((item, idx) => (
          <button
            key={idx}
            className={`calendar-cell ${item.isSelected ? 'selected' : ''} ${
              item.isCheapest ? 'cheapest' : ''
            }`}
            onClick={() => onDateSelect(item.date)}
          >
            <span className="calendar-date">{formatDate(item.date)}</span>
            <span className="calendar-price">${item.price}</span>
          </button>
        ))}
      </div>
      <div className="calendar-legend">
        <span className="legend-item">
          <span className="legend-dot cheapest" /> Cheapest
        </span>
        <span className="legend-item">
          <span className="legend-dot selected" /> Selected
        </span>
      </div>
    </div>
  );
};

const MobileFiltersDrawer = ({
  isOpen,
  onClose,
  children,
  activeFiltersCount,
  onApply,
  onClear,
}) => {
  if (!isOpen) return null;

  return (
    <div className="mobile-filters-overlay" onClick={onClose}>
      <div
        className="mobile-filters-drawer"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="mobile-filters-header">
          <h3>
            Filters{' '}
            {activeFiltersCount > 0 && (
              <Badge variant="primary" size="sm">
                {activeFiltersCount}
              </Badge>
            )}
          </h3>
          <IconButton icon={<X size={20} />} variant="ghost" onClick={onClose} />
        </div>
        <div className="mobile-filters-content">{children}</div>
        <div className="mobile-filters-footer">
          <Button variant="outline" onClick={onClear}>
            Clear All
          </Button>
          <Button variant="primary" onClick={onApply}>
            Apply Filters
          </Button>
        </div>
      </div>
    </div>
  );
};

// ============================================================================
// PAGINATION COMPONENT
// ============================================================================

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
  if (totalPages <= 1) return null;

  const pages = [];
  const maxButtons = 5;
  let start = Math.max(1, currentPage - 2);
  let end = Math.min(totalPages, start + maxButtons - 1);

  if (end - start < maxButtons - 1) {
    start = Math.max(1, end - maxButtons + 1);
  }

  for (let i = start; i <= end; i++) {
    pages.push(i);
  }

  return (
    <div className="pagination-container">
      <div className="pagination-inner">
        {/* <button
          className="pagination-button pagination-prev"
          disabled={currentPage === 1}
          onClick={() => onPageChange(currentPage - 1)}
        >
          <ChevronLeft size={16} />
          <span className="pagination-label-desktop">Previous</span>
        </button> */}

        <div className="pagination-pages">
          <button
          className="pagination-button pagination-prev"
          disabled={currentPage === 1}
          onClick={() => onPageChange(currentPage - 1)}
        >
          <span className="pagination-label-desktop">Previous</span>
          </button>
          {start > 1 && (
            <>
              <button
                className={`pagination-button ${
                  currentPage === 1 ? 'pagination-button-active' : ''
                }`}
                onClick={() => onPageChange(1)}
              >
                1
              </button>
              {start > 2 && <span className="pagination-ellipsis">…</span>}
            </>
          )}

          {pages.map((page) => (
            <button
              key={page}
              className={`pagination-button ${
                currentPage === page ? 'pagination-button-active' : ''
              }`}
              onClick={() => onPageChange(page)}
            >
              {page}
            </button>
          ))}

          {end < totalPages && (
            <>
              {end < totalPages - 1 && (
                <span className="pagination-ellipsis">…</span>
              )}
              <button
                className={`pagination-button ${
                  currentPage === totalPages ? 'pagination-button-active' : ''
                }`}
                onClick={() => onPageChange(totalPages)}
              >
                {totalPages}
              </button>
            </>
          )}
          <button
          className="pagination-button pagination-next"
          disabled={currentPage === totalPages}
          onClick={() => onPageChange(currentPage + 1)}>
          <span className="pagination-label-desktop">Next</span>
        </button>
        </div>

        {/* <button
          className="pagination-button pagination-next"
          disabled={currentPage === totalPages}
          onClick={() => onPageChange(currentPage + 1)}
        >
          <span className="pagination-label-desktop">Next</span>
          <ChevronRight size={16} />
        </button> */}
      </div>

      {/* <div className="pagination-summary">
        Page {currentPage} of {totalPages}
      </div> */}
    </div>
  );
};

// ============================================================================
// MAIN PAGE
// ============================================================================

const FlightResultsPage = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const searchQuery = useMemo(
    () => ({
      from: searchParams.get('from') || 'JFK',
      to: searchParams.get('to') || 'LHR',
      fromCity: searchParams.get('fromCity') || 'New York',
      toCity: searchParams.get('toCity') || 'London',
      depart: searchParams.get('depart') || '2024-05-15',
      return: searchParams.get('return') || '2024-05-22',
      adults: parseInt(searchParams.get('adults') || '2'),
      children: parseInt(searchParams.get('children') || '0'),
      infants: parseInt(searchParams.get('infants') || '0'),
      class: searchParams.get('class') || 'Economy',
      tripType: searchParams.get('type') || 'round-trip',
    }),
    [searchParams]
  );

  const [flights, setFlights] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({
    stops: 'any',
    priceRange: [0, 50000], // Updated to support INR prices
    airlines: [],
    departureTime: [],
    returnTime: [],
    duration: 24,
    departureAirports: [],
    arrivalAirports: [],
    layoverAirports: [],
    baggage: [],
    amenities: [],
  });
  const [sortBy, setSortBy] = useState('best');
  const [viewMode, setViewMode] = useState('list');
  const [expandedFlights, setExpandedFlights] = useState([]);
  const [selectedFlight, setSelectedFlight] = useState(null);
  const [mobileFiltersOpen, setMobileFiltersOpen] = useState(false);
  const [priceAlertEmail, setPriceAlertEmail] = useState('');
  const [priceAlertSuccess, setPriceAlertSuccess] = useState(false);

  // pagination state
  const ITEMS_PER_PAGE = 5;
  const [currentPage, setCurrentPage] = useState(1);

  const navLinks = [
    { label: 'Home', href: '/', icon: null },
    { label: 'Flights', href: '/flights', icon: <Plane size={18} /> },
    { label: 'Hotels', href: '/hotels', icon: null },
    { label: 'Destinations', href: '/destinations', icon: null },
  ];

  const sortOptions = [
    { value: 'best', label: 'Best' },
    { value: 'cheapest', label: 'Cheapest' },
    { value: 'fastest', label: 'Fastest' },
    { value: 'earliest', label: 'Earliest Departure' },
    { value: 'latest', label: 'Latest Departure' },
  ];

  // Fetch flights from backend
  useEffect(() => {
    const loadFlights = async () => {
      try {
        setLoading(true);

        const payload = {
          tripType: searchQuery.tripType,
          origin: searchQuery.from,
          destination: searchQuery.to,
          departureDate: searchQuery.depart,
          returnDate:
            searchQuery.tripType === 'round-trip' ? searchQuery.return : null,
          passengers: {
            adults: searchQuery.adults,
            children: searchQuery.children,
            infants: searchQuery.infants,
          },
          travelClass: searchQuery.class,
          advancedOptions: {
            directOnly: false,
            flexibleDates: false,
            preferredAirline: '',
            baggage: '',
          },
          multiCityLegs: [],
        };

        const data = await searchFlights(payload);
        console.log('Flights from backend:', data);

        // Transform backend data to expected format
        const flightsArray = transformBackendFlights(data);
        
        // Add badges for cheapest and fastest flights
        if (flightsArray.length > 0) {
          // Find cheapest
          const cheapest = flightsArray.reduce((min, f) => 
            (f.price?.total || Infinity) < (min.price?.total || Infinity) ? f : min
          );
          if (cheapest) cheapest.badges = [...(cheapest.badges || []), 'Cheapest'];
          
          // Find fastest
          const fastest = flightsArray.reduce((min, f) => 
            (f.outbound?.duration || Infinity) < (min.outbound?.duration || Infinity) ? f : min
          );
          if (fastest && fastest !== cheapest) {
            fastest.badges = [...(fastest.badges || []), 'Fastest'];
          }
        }
        
        console.log('Transformed flights:', flightsArray.length, 'flights');
        setFlights(flightsArray);
        setCurrentPage(1);
      } catch (err) {
        console.error('Failed to load flights', err);
        setFlights([]);
      } finally {
        setLoading(false);
      }
    };

    loadFlights();
  }, [searchQuery]);

  const filteredFlights = useMemo(() => {
    let result = [...flights];

    if (filters.stops !== 'any') {
      result = result.filter((flight) => {
        const outboundStops = flight.outbound?.stops?.length || 0;
        const inboundStops = flight.inbound?.stops?.length || 0;
        const maxStops = Math.max(outboundStops, inboundStops);

        if (filters.stops === 'nonstop') return maxStops === 0;
        if (filters.stops === '1stop') return maxStops <= 1;
        if (filters.stops === '2stops') return maxStops <= 2;
        return true;
      });
    }

    result = result.filter((flight) => {
      const p = flight.price || {};
      const value = p.perPerson ?? p.total ?? 0;
      return value >= filters.priceRange[0] && value <= filters.priceRange[1];
    });

    if (filters.airlines.length > 0) {
      result = result.filter(
        (flight) =>
          filters.airlines.includes(flight.outbound?.airline?.code) ||
          (flight.inbound &&
            filters.airlines.includes(flight.inbound.airline?.code))
      );
    }

    if (filters.departureTime.length > 0) {
      result = result.filter((flight) => {
        const hour = new Date(
          flight.outbound?.departure?.time || new Date()
        ).getHours();
        const timeOfDay = getTimeOfDay(hour);
        return filters.departureTime.includes(timeOfDay);
      });
    }

    if (filters.returnTime.length > 0 && searchQuery.tripType === 'round-trip') {
      result = result.filter((flight) => {
        if (!flight.inbound) return true;
        const hour = new Date(
          flight.inbound?.departure?.time || new Date()
        ).getHours();
        const timeOfDay = getTimeOfDay(hour);
        return filters.returnTime.includes(timeOfDay);
      });
    }

    result = result.filter((flight) => {
      const outboundMinutes =
        flight.outbound?.duration ?? flight.outbound?.durationMinutes ?? 0;
      const inboundMinutes =
        flight.inbound?.duration ?? flight.inbound?.durationMinutes ?? 0;
      const outboundHours = outboundMinutes / 60;
      const inboundHours = inboundMinutes / 60;
      return outboundHours <= filters.duration && inboundHours <= filters.duration;
    });

    if (filters.baggage.length > 0) {
      result = result.filter((flight) => {
        const baggage = flight.baggage || {};
        if (filters.baggage.includes('carryOn') && baggage.carryOn === 0)
          return false;
        if (filters.baggage.includes('checked') && baggage.checked === 0)
          return false;
        if (filters.baggage.includes('nofees') && (baggage.fees || 0) > 0)
          return false;
        return true;
      });
    }

    if (filters.amenities.length > 0) {
      result = result.filter((flight) => {
        const amenities = flight.amenities || [];
        return filters.amenities.every((amenity) =>
          amenities.includes(amenity)
        );
      });
    }

    return result;
  }, [flights, filters, searchQuery.tripType]);

  const sortedFlights = useMemo(() => {
    const sorted = [...filteredFlights];

    switch (sortBy) {
      
      case 'cheapest':
        sorted.sort((a, b) => {
          const ap = a.price || {};
          const bp = b.price || {};
          const av = ap.perPerson ?? ap.total ?? 0;
          const bv = bp.perPerson ?? bp.total ?? 0;
          return av - bv;
        });
        break;
      case 'fastest':
        sorted.sort((a, b) => {
          const aDuration =
            (a.outbound?.duration ?? a.outbound?.durationMinutes ?? 0) +
            (a.inbound?.duration ?? a.inbound?.durationMinutes ?? 0);
          const bDuration =
            (b.outbound?.duration ?? b.outbound?.durationMinutes ?? 0) +
            (b.inbound?.duration ?? b.inbound?.durationMinutes ?? 0);
          return aDuration - bDuration;
        });
        break;
      case 'earliest':
        sorted.sort(
          (a, b) =>
            new Date(a.outbound?.departure?.time || 0) -
            new Date(b.outbound?.departure?.time || 0)
        );
        break;
      case 'latest':
        sorted.sort(
          (a, b) =>
            new Date(b.outbound?.departure?.time || 0) -
            new Date(a.outbound?.departure?.time || 0)
        );
        break;
      case 'best':
      default:
        sorted.sort((a, b) => {
          const ap = a.price || {};
          const bp = b.price || {};
          const av = ap.perPerson ?? ap.total ?? 0;
          const bv = bp.perPerson ?? bp.total ?? 0;

          const ad =
            (a.outbound?.duration ?? a.outbound?.durationMinutes ?? 0) / 60;
          const bd =
            (b.outbound?.duration ?? b.outbound?.durationMinutes ?? 0) / 60;

          const ascore =
            av / 100 + ad + (a.outbound?.stops?.length || 0) * 50;
          const bscore =
            bv / 100 + bd + (b.outbound?.stops?.length || 0) * 50;
          return ascore - bscore;
        });
    }

    return sorted;
  }, [filteredFlights, sortBy]);

  const totalPages = useMemo(
    () => Math.max(1, Math.ceil(sortedFlights.length / ITEMS_PER_PAGE)),
    [sortedFlights.length]
  );

  const paginatedFlights = useMemo(() => {
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    return sortedFlights.slice(startIndex, startIndex + ITEMS_PER_PAGE);
  }, [sortedFlights, currentPage]);

  // If filters or sorting change, go back to page 1
  useEffect(() => {
    setCurrentPage(1);
  }, [filters, sortBy]);

  const activeFiltersCount = useMemo(() => {
    let count = 0;
    if (filters.stops !== 'any') count++;
    if (filters.priceRange[0] > 100 || filters.priceRange[1] < 1500) count++;
    if (filters.airlines.length > 0) count++;
    if (filters.departureTime.length > 0) count++;
    if (filters.returnTime.length > 0) count++;
    if (filters.duration < 24) count++;
    if (filters.baggage.length > 0) count++;
    if (filters.amenities.length > 0) count++;
    return count;
  }, [filters]);

  const handleFilterChange = useCallback((key, value) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  }, []);

  const handleClearFilters = useCallback(() => {
    setFilters({
      stops: 'any',
      priceRange: [0, 50000], // Updated to support INR prices
      airlines: [],
      departureTime: [],
      returnTime: [],
      duration: 24,
      departureAirports: [],
      arrivalAirports: [],
      layoverAirports: [],
      baggage: [],
      amenities: [],
    });
  }, []);

  const handleToggleExpand = useCallback((flightId) => {
    setExpandedFlights((prev) =>
      prev.includes(flightId)
        ? prev.filter((id) => id !== flightId)
        : [...prev, flightId]
    );
  }, []);

  const handleSelectFlight = useCallback((flight) => {
    setSelectedFlight(flight);
    // Navigate to flight details page with flight data
    navigate('/flight-details', { 
      state: { 
        flight,
        searchQuery 
      } 
    });
  }, [navigate, searchQuery]);

  const handleModifySearch = useCallback(() => {
    navigate('/flights');
  }, [navigate]);

  const handlePriceAlert = useCallback(
    (e) => {
      e.preventDefault();
      if (priceAlertEmail) {
        alert(priceAlertEmail)
        setPriceAlertSuccess(true);
        setTimeout(() => setPriceAlertSuccess(false), 3000);
        setPriceAlertEmail('');
      }
    },
    [priceAlertEmail]
  );

  const handleAirlineToggle = useCallback((airlineCode) => {
    setFilters((prev) => ({
      ...prev,
      airlines: prev.airlines.includes(airlineCode)
        ? prev.airlines.filter((code) => code !== airlineCode)
        : [...prev.airlines, airlineCode],
    }));
  }, []);

  const handleTimeToggle = useCallback((time, isReturn = false) => {
    const key = isReturn ? 'returnTime' : 'departureTime';
    setFilters((prev) => ({
      ...prev,
      [key]: prev[key].includes(time)
        ? prev[key].filter((t) => t !== time)
        : [...prev[key], time],
    }));
  }, []);

  const handleBaggageToggle = useCallback((baggage) => {
    setFilters((prev) => ({
      ...prev,
      baggage: prev.baggage.includes(baggage)
        ? prev.baggage.filter((b) => b !== baggage)
        : [...prev.baggage, baggage],
    }));
  }, []);

  const handleAmenityToggle = useCallback((amenity) => {
    setFilters((prev) => ({
      ...prev,
      amenities: prev.amenities.includes(amenity)
        ? prev.amenities.filter((a) => a !== amenity)
        : [...prev.amenities, amenity],
    }));
  }, []);

  const removeActiveFilter = useCallback(
    (filterType, value) => {
      if (filterType === 'stops') {
        handleFilterChange('stops', 'any');
      } else if (filterType === 'price') {
        handleFilterChange('priceRange', [0, 50000]);
      } else if (filterType === 'airline') {
        handleAirlineToggle(value);
      } else if (filterType === 'departureTime') {
        handleTimeToggle(value, false);
      } else if (filterType === 'returnTime') {
        handleTimeToggle(value, true);
      } else if (filterType === 'duration') {
        handleFilterChange('duration', 24);
      } else if (filterType === 'baggage') {
        handleBaggageToggle(value);
      } else if (filterType === 'amenity') {
        handleAmenityToggle(value);
      }
    },
    [
      handleFilterChange,
      handleAirlineToggle,
      handleTimeToggle,
      handleBaggageToggle,
      handleAmenityToggle,
    ]
  );

  const airlineStats = useMemo(() => {
    const stats = {};
    flights.forEach((flight) => {
      const code = flight.outbound?.airline?.code;
      if (!code) return;
      if (!stats[code]) {
        stats[code] = {
          ...flight.outbound.airline,
          count: 0,
          minPrice: Infinity,
        };
      }
      stats[code].count++;
      const p = flight.price || {};
      const value = p.perPerson ?? p.total ?? Infinity;
      stats[code].minPrice = Math.min(stats[code].minPrice, value);
    });
    return Object.values(stats);
  }, [flights]);

  const renderFilters = () => (
    <>
      <FilterSection title="Stops">
        <RadioGroup
          value={filters.stops}
          onChange={(value) => handleFilterChange('stops', value)}
          layout="vertical"
        >
          <RadioButton value="any" label="Any number of stops" />
          <RadioButton value="nonstop" label="Nonstop only" />
          <RadioButton value="1stop" label="1 stop or fewer" />
          <RadioButton value="2stops" label="2 stops or fewer" />
        </RadioGroup>
      </FilterSection>

      <FilterSection title="Price">
        <PriceRangeSlider
          min={0}
          max={50000}
          value={filters.priceRange}
          onChange={(value) => handleFilterChange('priceRange', value)}
        />
      </FilterSection>

      <FilterSection title="Airlines">
        <div className="filter-airlines">
          {airlineStats.map((airline) => (
            <div key={airline.code} className="filter-airline-item">
              <Checkbox
                checked={filters.airlines.includes(airline.code)}
                onChange={() => handleAirlineToggle(airline.code)}
                label={
                  <div className="airline-filter-label">
                    {airline.logo && (
                      <img
                        src={airline.logo}
                        alt={airline.name}
                        className="airline-filter-logo"
                        onError={(e) => {
                          e.target.style.display = 'none';
                        }}
                      />
                    )}
                    <span className="airline-filter-name">{airline.name}</span>
                    <span className="airline-filter-price">
                      from {airline.minPrice === Infinity ? '--' : `$${airline.minPrice}`}
                    </span>
                  </div>
                }
              />
            </div>
          ))}
        </div>
        <div className="filter-actions-inline">
          <button
            className="filter-link"
            onClick={() =>
              handleFilterChange(
                'airlines',
                airlineStats.map((a) => a.code)
              )
            }
          >
            Select All
          </button>
          <button
            className="filter-link"
            onClick={() => handleFilterChange('airlines', [])}
          >
            Clear All
          </button>
        </div>
      </FilterSection>

      <FilterSection title="Departure Time">
        <div className="filter-time-group">
          <span className="filter-time-label">Outbound</span>
          <div className="filter-checkboxes">
            <Checkbox
              checked={filters.departureTime.includes('morning')}
              onChange={() => handleTimeToggle('morning')}
              label="Morning (5AM - 12PM)"
            />
            <Checkbox
              checked={filters.departureTime.includes('afternoon')}
              onChange={() => handleTimeToggle('afternoon')}
              label="Afternoon (12PM - 6PM)"
            />
            <Checkbox
              checked={filters.departureTime.includes('evening')}
              onChange={() => handleTimeToggle('evening')}
              label="Evening (6PM - 12AM)"
            />
            <Checkbox
              checked={filters.departureTime.includes('night')}
              onChange={() => handleTimeToggle('night')}
              label="Night (12AM - 5AM)"
            />
          </div>
        </div>
        {searchQuery.tripType === 'round-trip' && (
          <div className="filter-time-group">
            <span className="filter-time-label">Return</span>
            <div className="filter-checkboxes">
              <Checkbox
                checked={filters.returnTime.includes('morning')}
                onChange={() => handleTimeToggle('morning', true)}
                label="Morning (5AM - 12PM)"
              />
              <Checkbox
                checked={filters.returnTime.includes('afternoon')}
                onChange={() => handleTimeToggle('afternoon', true)}
                label="Afternoon (12PM - 6PM)"
              />
              <Checkbox
                checked={filters.returnTime.includes('evening')}
                onChange={() => handleTimeToggle('evening', true)}
                label="Evening (6PM - 12AM)"
              />
              <Checkbox
                checked={filters.returnTime.includes('night')}
                onChange={() => handleTimeToggle('night', true)}
                label="Night (12AM - 5AM)"
              />
            </div>
          </div>
        )}
      </FilterSection>

      <FilterSection title="Duration">
        <DurationSlider
          max={24}
          value={filters.duration}
          onChange={(value) => handleFilterChange('duration', value)}
        />
      </FilterSection>

      <FilterSection title="Baggage">
        <div className="filter-checkboxes">
          <Checkbox
            checked={filters.baggage.includes('carryOn')}
            onChange={() => handleBaggageToggle('carryOn')}
            label="Carry-on included"
          />
          <Checkbox
            checked={filters.baggage.includes('checked')}
            onChange={() => handleBaggageToggle('checked')}
            label="Checked bag included"
          />
          <Checkbox
            checked={filters.baggage.includes('nofees')}
            onChange={() => handleBaggageToggle('nofees')}
            label="No baggage fees"
          />
        </div>
      </FilterSection>

      <FilterSection title="Amenities">
        <div className="filter-checkboxes">
          <Checkbox
            checked={filters.amenities.includes('wifi')}
            onChange={() => handleAmenityToggle('wifi')}
            label="WiFi available"
          />
          <Checkbox
            checked={filters.amenities.includes('power')}
            onChange={() => handleAmenityToggle('power')}
            label="Power outlets"
          />
          <Checkbox
            checked={filters.amenities.includes('entertainment')}
            onChange={() => handleAmenityToggle('entertainment')}
            label="In-flight entertainment"
          />
          <Checkbox
            checked={filters.amenities.includes('meals')}
            onChange={() => handleAmenityToggle('meals')}
            label="Meals included"
          />
        </div>
      </FilterSection>

      {activeFiltersCount > 0 && (
        <div className="filter-clear-all">
          <Button variant="outline" fullWidth onClick={handleClearFilters}>
            Clear All Filters
          </Button>
        </div>
      )}
    </>
  );

  const renderActiveFilters = () => {
    const badges = [];

    if (filters.stops !== 'any') {
      const label =
        filters.stops === 'nonstop'
          ? 'Nonstop'
          : filters.stops === '1stop'
          ? '1 stop max'
          : '2 stops max';
      badges.push(
        <Badge key="stops" variant="secondary" className="active-filter-badge">
          {label}
          <button
            onClick={() => removeActiveFilter('stops')}
            aria-label="Remove stops filter"
          >
            <X size={12} />
          </button>
        </Badge>
      );
    }

    if (filters.priceRange[0] > 100 || filters.priceRange[1] < 1500) {
      badges.push(
        <Badge key="price" variant="secondary" className="active-filter-badge">
          ${filters.priceRange[0]} - ${filters.priceRange[1]}
          <button
            onClick={() => removeActiveFilter('price')}
            aria-label="Remove price filter"
          >
            <X size={12} />
          </button>
        </Badge>
      );
    }

    filters.airlines.forEach((code) => {
      const airline = airlineStats.find((a) => a.code === code);
      if (airline) {
        badges.push(
          <Badge
            key={`airline-${code}`}
            variant="secondary"
            className="active-filter-badge"
          >
            {airline.name}
            <button
              onClick={() => removeActiveFilter('airline', code)}
              aria-label={`Remove ${airline.name} filter`}
            >
              <X size={12} />
            </button>
          </Badge>
        );
      }
    });

    filters.departureTime.forEach((time) => {
      badges.push(
        <Badge
          key={`dep-${time}`}
          variant="secondary"
          className="active-filter-badge"
        >
          Depart: {time}
          <button
            onClick={() => removeActiveFilter('departureTime', time)}
            aria-label={`Remove ${time} departure filter`}
          >
            <X size={12} />
          </button>
        </Badge>
      );
    });

    if (filters.duration < 24) {
      badges.push(
        <Badge
          key="duration"
          variant="secondary"
          className="active-filter-badge"
        >
          Max {filters.duration}h
          <button
            onClick={() => removeActiveFilter('duration')}
            aria-label="Remove duration filter"
          >
            <X size={12} />
          </button>
        </Badge>
      );
    }

    return badges;
  };

  return (
    <div className="flight-results-page">
      <Navbar
        logo={{ text: 'TravelHub', href: '/' }}
        navLinks={navLinks}
        activeLink="/flights"
        sticky
      />

      <div className="search-summary-bar">
        <div className="search-summary-container">
          <div className="search-summary-info">
            <div className="search-route">
              <span className="search-city">{searchQuery.fromCity}</span>
              <span className="search-code">({searchQuery.from})</span>
              <ArrowRight size={16} className="search-arrow" />
              <span className="search-city">{searchQuery.toCity}</span>
              <span className="search-code">({searchQuery.to})</span>
            </div>
            <div className="search-details">
              <span className="search-dates">
                <Calendar size={14} />
                {formatDate(searchQuery.depart)}
                {searchQuery.tripType === 'round-trip' &&
                  ` - ${formatDate(searchQuery.return)}`}
              </span>
              <span className="search-passengers">
                <Users size={14} />
                {searchQuery.adults +
                  searchQuery.children +
                  searchQuery.infants}{' '}
                passengers
              </span>
              <span className="search-class">{searchQuery.class}</span>
            </div>
          </div>
          <div className="search-summary-actions">
            <Button variant="outline" size="sm" onClick={handleModifySearch}>
              Modify Search
            </Button>
            <Select
              options={sortOptions}
              value={sortOptions.find((o) => o.value === sortBy)}
              onChange={(option) => setSortBy(option.value)}
              placeholder="Sort by"
              className="sort-select"
            />
          </div>
        </div>
      </div>

      <div className="flight-results-main">
        <div className="flight-results-container">
          <aside className="filters-sidebar">
            <div className="filters-header">
              <h3>
                <Filter size={18} /> Filters
                {activeFiltersCount > 0 && (
                  <Badge variant="primary" size="sm">
                    {activeFiltersCount}
                  </Badge>
                )}
              </h3>
            </div>
            <div className="filters-content">{renderFilters()}</div>
          </aside>

          <main className="results-column">
            <div className="results-toolbar">
              {/* <div className="results-count">
                {loading ? (
                  <span>Searching flights...</span>
                ) : (
                  <span>
                    <strong>{filteredFlights.length}</strong> flights found
                  </span>
                )}
              </div> */}
              <div className="results-active-filters">
                {renderActiveFilters()}
              </div>
              <div className="results-view-toggle">
                <IconButton
                  icon={<List size={18} />}
                  variant={viewMode === 'list' ? 'primary' : 'ghost'}
                  size="sm"
                  onClick={() => setViewMode('list')}
                  aria-label="List view"
                />
                <IconButton
                  icon={<CalendarDays size={18} />}
                  variant={viewMode === 'calendar' ? 'primary' : 'ghost'}
                  size="sm"
                  onClick={() => setViewMode('calendar')}
                  aria-label="Calendar view"
                />
              </div>
            </div>

            {/* DEBUG raw data – optional */}
           
            {viewMode === 'calendar' && (
              <CalendarView
                selectedDate={searchQuery.depart}
                onDateSelect={(date) => console.log('Selected date:', date)}
              />
            )}

            {loading && (
              <div className="results-loading">
                <Spinner size="lg" />
                <p>Searching 500+ airlines for the best deals...</p>
                <div className="loading-skeleton">
                  {[1, 2, 3].map((i) => (
                    <div key={i} className="skeleton-card" />
                  ))}
                </div>
              </div>
            )}

            {!loading && viewMode === 'list' && (
              <>
                {paginatedFlights.length > 0 ? (
                  <div className="flight-cards-list">
                    {paginatedFlights.map((flight) => (
                      <FlightCard
                        key={flight.id}
                        flight={flight}
                        isExpanded={expandedFlights.includes(flight.id)}
                        onToggleExpand={handleToggleExpand}
                        onSelect={handleSelectFlight}
                        isSelected={selectedFlight?.id === flight.id}
                      />
                    ))}
                  </div>
                ) : (
                  <EmptyState
                    icon={<Plane size={48} />}
                    title="No flights found"
                    description="Try adjusting your filters or search criteria to find more options."
                    primaryAction={{
                      label: 'Clear Filters',
                      onClick: handleClearFilters,
                    }}
                    secondaryAction={{
                      label: 'Modify Search',
                      onClick: handleModifySearch,
                    }}
                  />
                )}

                {/* Pagination controls */}
                {!loading && filteredFlights.length > 0 && (
                  <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={setCurrentPage}
                  />
                )}
              </>
            )}

            <div className="price-alert-section">
              <div className="price-alert-card">
                <div className="price-alert-icon">
                  <Bell size={24} />
                </div>
                <div className="price-alert-content">
                  <h4>Track this route</h4>
                  <p>
                    We'll notify you when prices drop for {searchQuery.from} to{' '}
                    {searchQuery.to}
                  </p>
                  {priceAlertSuccess ? (
                    <div className="price-alert-success">
                      <Check size={16} /> Price alert set successfully!
                    </div>
                  ) : (
                    <form className="price-alert-form" onSubmit={handlePriceAlert}>
                      <Input
                        type="email"
                        placeholder="Enter your email"
                        value={priceAlertEmail}
                        onChange={(e) => setPriceAlertEmail(e.target.value)}
                        required
                      />
                      <Button type="submit" variant="primary" size="sm">
                        Set Alert
                      </Button>
                    </form>
                  )}
                </div>
              </div>
            </div>
          </main>
        </div>
      </div>

      <div className="mobile-filter-bar">
        <Button
          variant="outline"
          leftIcon={<SlidersHorizontal size={16} />}
          onClick={() => setMobileFiltersOpen(true)}
        >
          Filters {activeFiltersCount > 0 && `(${activeFiltersCount})`}
        </Button>
        <Select
          options={sortOptions}
          value={sortOptions.find((o) => o.value === sortBy)}
          onChange={(option) => setSortBy(option.value)}
          placeholder="Sort"
          className="mobile-sort-select"
        />
      </div>

      <MobileFiltersDrawer
        isOpen={mobileFiltersOpen}
        onClose={() => setMobileFiltersOpen(false)}
        activeFiltersCount={activeFiltersCount}
        onApply={() => setMobileFiltersOpen(false)}
        onClear={handleClearFilters}
      >
        {renderFilters()}
      </MobileFiltersDrawer>

      <NewsletterSignup
        heading="Get Flight Deal Alerts"
        subheading="Subscribe to receive exclusive flight deals and price drop notifications"
      />

      <Footer />
    </div>
  );
};

export default FlightResultsPage;
