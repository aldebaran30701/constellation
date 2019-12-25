/*
 * Copyright 2010-2019 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.graph.attribute;

import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.graph.NativeAttributeType;
import au.gov.asd.tac.constellation.utilities.temporal.TemporalConstants;
import au.gov.asd.tac.constellation.utilities.temporal.TemporalFormatting;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * A description of a local_datetime attribute.
 * <p>
 * Local date-times are considered to be fixed points on a calendar and clock,
 * independent of time-zone. They are represented internally as long primitives.
 * These long primitives give the number of days milliseconds since the epoch to
 * the desired date-time.
 *
 * The object representation of these attribute values are java
 * {@link LocalDateTime} objects.
 *
 * @author sirius
 */
@ServiceProvider(service = AttributeDescription.class)
public final class LocalDateTimeAttributeDescription extends AbstractAttributeDescription {

    private static final Logger LOGGER = Logger.getLogger(LocalDateTimeAttributeDescription.class.getName());
    public static final String ATTRIBUTE_NAME = "local_datetime";
    public static final long NULL_VALUE = Long.MIN_VALUE;
    
    private long[] data = new long[0];
    private long defaultValue = NULL_VALUE;

    @Override
    public String getName() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public int getCapacity() {
        return data.length;
    }

    @Override
    public void setCapacity(final int capacity) {
        final int len = data.length;
        data = Arrays.copyOf(data, capacity);
        if (capacity > len) {
            Arrays.fill(data, len, capacity, defaultValue);
        }
    }

    @Override
    public void clear(final int id) {
        data[id] = defaultValue;
    }

    @Override
    public void setLong(final int id, final long value) {
        data[id] = value;
    }

    private static long parseObject(final Object value) {
        if (value == null) {
            return NULL_VALUE;
        } else if (value instanceof ZonedDateTime) {
            return ((ZonedDateTime) value).toInstant().toEpochMilli();
        } else if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toInstant(ZoneOffset.UTC).toEpochMilli();
        } else if (value instanceof Date) {
            return ((Date) value).getTime();
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof Calendar) {
            return ((Calendar) value).getTimeInMillis();
        } else if (value instanceof String) {
            return parseString((String) value);
        } else {
            throw new IllegalArgumentException("Error converting Object to long: " + value.getClass().getName());
        }
    }

    @Override
    public void setObject(final int id, final Object value) {
        data[id] = parseObject(value);
    }

    @Override
    public void setString(final int id, final String value) {
        data[id] = parseString(value);
    }

    @Override
    public long getLong(final int id) {
        return data[id];
    }

    @Override
    public Object getObject(final int id) {
        return data[id] == NULL_VALUE ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(data[id]), ZoneOffset.UTC);
    }

    @Override
    public String getString(final int id) {
        return data[id] == NULL_VALUE ? null : getAsString(LocalDateTime.ofInstant(Instant.ofEpochMilli(data[id]), ZoneOffset.UTC));
    }

    @Override
    public AttributeDescription copy(GraphReadMethods graph) {
        final LocalDateTimeAttributeDescription attribute = new LocalDateTimeAttributeDescription();
        attribute.data = Arrays.copyOf(data, data.length);
        attribute.defaultValue = this.defaultValue;
        attribute.graph = graph;
        return attribute;
    }

    /**
     * Parse a string in CONSTELLATION's date-time format to a ZonedDateTime.
     *
     * CONSTELLATION's date-time format is that which would be generated by
     * {@link TemporalFormatting#LOCAL_DATE_TIME_FORMATTER}.
     * <p>
     * Parsing isn't strict: the date 2011-99-01 will be accepted and
     * punctuation isn't checked. Since the context is parsing of dates from
     * CONSTELLATION files, this isn't expected to be a problem. However, this
     * should not be taken as an excuse to write syntactically incorrect
     * datetime strings elsewhere.
     * <p>
     * Note that this method directly reads substrings with static indices, as
     * this is significantly faster than using a ZonedDateTimeFormatter.
     *
     * @param value A String in CONSTELLATION's date-time format to be parsed.
     *
     * @return A Calendar representing the input datetime.
     */
    public static long parseString(final String value) {
        if (value == null || value.isEmpty()) {
            return NULL_VALUE;
        }

        try {
            final int ye = Integer.parseInt(value.substring(0, 4), 10);
            final int mo = Integer.parseInt(value.substring(5, 7), 10);
            final int da = Integer.parseInt(value.substring(8, 10), 10);
            final int ho = Integer.parseInt(value.substring(11, 13), 10);
            final int mi = Integer.parseInt(value.substring(14, 16), 10);
            final int se = Integer.parseInt(value.substring(17, 19), 10);
            final int ms = Integer.parseInt(value.substring(20, 23), 10);
            return LocalDateTime.of(ye, mo, da, ho, mi, se, ms * TemporalConstants.NANOSECONDS_IN_MILLISECOND).toInstant(ZoneOffset.UTC).toEpochMilli();
        } catch (StringIndexOutOfBoundsException | NumberFormatException ex) {
            LOGGER.log(Level.WARNING, "Can't parse datetime string '{0}': '{1}'", new Object[]{value, ex.getMessage()});
        }

        return NULL_VALUE;
    }

    @Override
    public String acceptsString(String value) {
        return parseString(value) == NULL_VALUE && value != null ? "Not a valid datetime" : null;
    }

    /**
     * Format the given LocalDateTime object as a String in CONSTELLATION's
     * date-time format.
     *
     * @param value The LocalDateTime value.
     * @return The datetime as a String in CONSTELLATION's date-time format.
     */
    public static String getAsString(final LocalDateTime value) {
        if (value == null) {
            return null;
        }

        return value.format(TemporalFormatting.LOCAL_DATE_TIME_FORMATTER);
    }

    @Override
    public Class<?> getNativeClass() {
        return LocalDateTimeAttributeDescription.class;
    }

    @Override
    public void setDefault(final Object value) {
        final long parsedValue = parseObject(value);
        defaultValue = parsedValue != NULL_VALUE ? parsedValue : NULL_VALUE;
    }

    @Override
    public Object getDefault() {
        return defaultValue;
    }

    @Override
    public int hashCode(final int id) {
        return (int) data[id];
    }

    @Override
    public boolean equals(final int id1, final int id2) {
        return data[id1] == data[id2];
    }

    @Override
    public boolean canBeImported() {
        return true;
    }

    @Override
    public int ordering() {
        return 6;
    }

    @Override
    public boolean isClear(final int id) {
        return data[id] == defaultValue;
    }

    @Override
    public Object saveData() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public void restoreData(final Object savedData) {
        final long[] sd = (long[]) savedData;
        data = Arrays.copyOf(sd, sd.length);
    }

    @Override
    public NativeAttributeType getNativeType() {
        return NativeAttributeType.LONG;
    }

    @Override
    public Object convertToNativeValue(Object objectValue) {
        return objectValue == null ? NULL_VALUE : ((LocalDateTime) objectValue).toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
