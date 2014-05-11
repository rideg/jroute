package org.jroute.http.response;

public class TextualResponse extends ResponseWithContent<StringBuilder> {

    private static final int DEFAULT_CAPACITY = 1024;
    private final StringBuilder buffer;

    {
        buffer = new StringBuilder(DEFAULT_CAPACITY);
    }

    public TextualResponse() {
        super();
    }

    public TextualResponse(final int status) {
        super(status);
    }

    protected final StringBuilder getBuffer() {
        return buffer;
    }

    public TextualResponse write(final Object data) {
        buffer.append(data);
        return this;
    }

    @Override
    public StringBuilder content() {
        return buffer;
    }

    @Override
    public String toString() {
        return super.toString() + buffer.toString();
    }

}
