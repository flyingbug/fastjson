package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;

public class ArrayListTypeFieldDeserializer extends FieldDeserializer {

    private final Type         itemType;
    private int                itemFastMatchToken;
    private ObjectDeserializer deserializer;

    public ArrayListTypeFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);

        Type fieldType = getFieldType();
        if (fieldType instanceof ParameterizedType) {
            this.itemType = ((ParameterizedType) getFieldType()).getActualTypeArguments()[0];
        } else {
            this.itemType = Object.class;
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void parseField(DefaultJSONParser parser, Object object, Map<String, Object> fieldValues) {
        if (parser.getLexer().token() == JSONToken.NULL) {
            setValue(object, null);
            return;
        }

        ArrayList list = new ArrayList();

        ParseContext context = parser.getContext();

        parser.setContext(context, object, fieldInfo.getName());
        parseArray(parser, list);
        parser.setContext(context);

        if (object == null) {
            fieldValues.put(fieldInfo.getName(), list);
        } else {
            setValue(object, list);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void parseArray(DefaultJSONParser parser, Collection array) {
        final JSONLexer lexer = parser.getLexer();

        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("exepct '[', but " + JSONToken.name(lexer.token()));
        }

        if (deserializer == null) {
            deserializer = parser.getConfig().getDeserializer(itemType);
            itemFastMatchToken = deserializer.getFastMatchToken();
        }

        lexer.nextToken(itemFastMatchToken);

        for (int i = 0;; ++i) {
            if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                while (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken();
                    continue;
                }
            }

            if (lexer.token() == JSONToken.RBRACKET) {
                break;
            }

            Object val = deserializer.deserialze(parser, itemType, i);
            array.add(val);

            parser.checkListResolve(array);

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(itemFastMatchToken);
                continue;
            }
        }

        lexer.nextToken(JSONToken.COMMA);
    }

}
