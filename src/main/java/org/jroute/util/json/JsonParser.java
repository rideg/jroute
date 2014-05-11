package org.jroute.util.json;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.primitives.UnsignedBytes.toInt;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class JsonParser {

    private static final int STREAM_END = -1;
    private static final int BYTE_MAX = 256;
    private static final Object[] ESCAPE = new Object[BYTE_MAX];
    private static final boolean[] WHITESPACE = new boolean[BYTE_MAX];

    static {
        ESCAPE['t'] = '\t';
        ESCAPE['b'] = '\b';
        ESCAPE['f'] = '\f';
        ESCAPE['n'] = '\n';
        ESCAPE['/'] = '/';
        ESCAPE['r'] = '\r';
        ESCAPE['\\'] = '\\';
        ESCAPE['"'] = '"';
    }

    static {
        WHITESPACE['\n'] = true;
        WHITESPACE['\r'] = true;
        WHITESPACE['\t'] = true;
        WHITESPACE[' '] = true;
    }

    private final Reader data;
    private int last = ' ';

    public JsonParser(final Reader data) {
        this.data = data;
    }

    public static JsonObject parse(final String data) {
        return (JsonObject) new JsonParser(new InputStreamReader(new ByteArrayInputStream(data.getBytes(UTF_8)), UTF_8))
        .parse();
    }

    public JsonObject parseFull() {
        return (JsonObject) parse();
    }

    private Object parse() {
        readUntilExpected('{');
        return readObject();
    }

    private void readUntilExpected(final char c) {
        readWhiteSpaces();
        checkExpected(c);
    }

    private void readWhiteSpaces() {
        while (whiteSpace()) {
            read();
        }
    }

    private boolean whiteSpace() {
        return WHITESPACE[toInt((byte) last)];
    }

    private void checkExpected(final char e) {
        if (last != e) {
            throw new IllegalStateException("The read charater is not " + e + " but '" + (char) last + "'");
        }
    }

    private Object readValue() {
        switch (last) {
        case '{':
            return readObject();
        case '[':
            return readArray();
        case '"':
            return readString();
        default:
            return readPrimitive();
        }
    }

    private void read() {
        try {
            last = data.read();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Object readArray() {
        final JsonArray o = new JsonArray();
        readWhiteSpaces();
        read();
        readWhiteSpaces();
        while (isNotEnd(']')) {
            o.add(readValue());
            readWhiteSpaces();
            if (isNotEnd(']')) {
                read();
                readWhiteSpaces();
                if (last == ',' || last == '}') {
                    read();
                }
            }
            readWhiteSpaces();
        }
        read();
        return o;
    }

    private Object readPrimitive() {
        switch (last) {
        case 'n':
            return readNull();
        case 'f':
            return readFalse();
        case 't':
            return readTrue();
        default:
            return readNumber();
        }
    }

    private Object readTrue() {
        readAndCheck('r');
        readAndCheck('u');
        readAndCheck('e');
        read();
        return true;
    }

    private Object readFalse() {
        readAndCheck('a');
        readAndCheck('l');
        readAndCheck('s');
        readAndCheck('e');
        read();
        return false;
    }

    private Number readNumber() {
        final StringBuilder b = new StringBuilder();
        boolean isDecimal = false;
        while (!isDelimiter()) {
            isDecimal = checkDecimal(isDecimal);
            b.append((char) last);
            read();
        }
        if (isDecimal) {
            return parseDouble(b.toString());
        }
        return parseLong(b.toString());
    }

    private boolean checkDecimal(final boolean isDecimal) {
        if (last < '0' || last > '9') {
            if (last != '.' || isDecimal) {
                throw new IllegalStateException("Not numeric value: " + (char) last);
            }
            return true;
        }
        return isDecimal;
    }

    private boolean isDelimiter() {
        return endDelimiter() || last == ',' || whiteSpace() || last == STREAM_END;
    }

    private boolean endDelimiter() {
        return last == '}' || last == ']';
    }

    private Object readNull() {
        readAndCheck('u');
        readAndCheck('l');
        readAndCheck('l');
        read();
        return null;
    }

    private void readAndCheck(final char expected) {
        read();
        checkExpected(expected);
    }

    private JsonPair readPair() {
        final String name = readString();
        readWhiteSpaces();
        checkExpected(':');
        read();
        readWhiteSpaces();
        return new JsonPair(name, readValue());
    }

    private String readString() {
        checkExpected('"');
        final StringBuilder buffer = new StringBuilder();
        read();
        while (isNotEnd('"')) {
            if (last == '\\') {
                read();
                appendExcaped(buffer);
            } else {
                buffer.append((char) last);
            }
            read();
        }
        read();
        return buffer.toString();
    }

    private void appendExcaped(final StringBuilder b) {
        final Object o = ESCAPE[last];
        if (o != null) {
            b.append(o);
        } else {
            throw new IllegalStateException("Unknown escaped chararter: " + last);
        }
    }

    private JsonObject readObject() {
        final JsonObject o = new JsonObject();
        readWhiteSpaces();
        read();
        readWhiteSpaces();
        while (isNotEnd('}')) {
            o.add(readPair());
            readWhiteSpaces();
            read();
            while (last == ',' || last == ']') {
                read();
            }
            readWhiteSpaces();
        }
        return o;
    }

    private boolean isNotEnd(final char end) {
        return last != end && last != STREAM_END;
    }

}
