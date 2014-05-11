package org.jroute.util.json;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonParserTest {

    @Test
    public void emptyObject() {
        check("{}");
    }

    @Test
    public void onePairWithNull() {
        check("{\"data\":null}");
    }

    @Test
    public void onePairWithString() {
        check("{\"data\":\"hahoka\"}");
    }

    @Test
    public void onePairWithNumber() {
        check("{\"data\":1}");
    }

    @Test
    public void twoPairs() {
        assertThat(JsonParser.parse("{\"data\":1,\"dota\":\"hello\"}").toString(), containsString("\"data\":1"));
        assertThat(JsonParser.parse("{\"data\":1,\"dota\":\"hello\"}").toString(), containsString("\"dota\":\"hello\""));
    }

    @Test
    public void objectWithArray() {
        check("{\"data\":[1,2,\"3\",4]}");
    }

    @Test
    public void objectWithArrayOneElement() {
        check("{\"data\":[1]}");
    }

    @Test
    public void objectTwoPairsArray() {
        assertThat(JsonParser.parse("{\"data\":[1],\"dud\":1}").toString(), containsString("\"data\":[1]"));
        assertThat(JsonParser.parse("{\"data\":[1],\"dud\":1}").toString(), containsString("\"dud\":1"));
    }

    @Test
    public void twoDimensinalArray() {
        check("{\"data\":[[1]]}");
    }

    @Test
    public void arraysInArray() {
        check("{\"a\":[[],1]}");
    }

    @Test
    public void moreComplexArraysInArray() {
        check("{\"a\":[[],1,[{},[null],{\"a\":1234}]]}");
    }

    @Test
    public void booleanTrue() {
        check("{\"a\":true}");
    }

    @Test
    public void booleanFalse() {
        check("{\"a\":false}");
    }

    @Test
    public void skipSpaces() {
        check("          {\"a\":false}");
        check("          {        \"a\":false}");
        check("          {        \"a\"         :false}");
        check("          {        \"a\"         :           false}");
        check("          {        \"a\"         :           false             }");
        check("          {        \"a\"         :           false             }            ");
        check("{\"a\":           [  ]}");
        check("{\"a\":           [1  ]}");
        check("{\"a\":           [1  ,   2,   [ {  }   ]]}");
        check("{\"a\":    \t   \n \r\t\t    [1  ,   2,   [ {  }   ]]}");
    }

    private void check(final String json) {
        assertThat(JsonParser.parse(json).toString(), is(json.replaceAll("\\s", "")));
    }

    @Test
    public void objectWithExcapedString() {
        assertThat(JsonParser.parse("{\"data\":\"\\t\"}").get("data").toString(), is("\t"));
        assertThat(JsonParser.parse("{\"data\":\"\\n\"}").get("data").toString(), is("\n"));
        assertThat(JsonParser.parse("{\"data\":\"\\b\"}").get("data").toString(), is("\b"));
        assertThat(JsonParser.parse("{\"data\":\"\\r\"}").get("data").toString(), is("\r"));
        assertThat(JsonParser.parse("{\"data\":\"\\f\"}").get("data").toString(), is("\f"));
        assertThat(JsonParser.parse("{\"data\":\"\\/\"}").get("data").toString(), is("/"));
        assertThat(JsonParser.parse("{\"data\":\"\\\\\"}").get("data").toString(), is("\\"));
        assertThat(JsonParser.parse("{\"data\":\"\\\"\"}").get("data").toString(), is("\""));
    }

    @Test
    public void decimalValuesConvertedToDouble() throws Exception {
        assertThat((Double) JsonParser.parse("{\"data\":1.2}").get("data"), is(1.2));
    }

    @Test(expected = IllegalStateException.class)
    public void invalidDecimalValuesException() throws Exception {
        JsonParser.parse("{\"data\":1.2.5}");
    }

    @Test(expected = IllegalStateException.class)
    public void unknownEscapedCharThrowsException() throws Exception {
        JsonParser.parse("{\"data\":\"\\o\"}");
    }

    @Test(expected = IllegalStateException.class)
    public void nonNumericValueThrowsException() throws Exception {
        JsonParser.parse("{\"data\":hello}");
    }

    @Test(expected = IllegalStateException.class)
    public void invalidStartException() throws Exception {
        JsonParser.parse("\"data\":null}");
    }

    @Test(expected = IllegalStateException.class)
    public void invalidStartOfStringException() throws Exception {
        JsonParser.parse("{data\":null}");
    }
}
