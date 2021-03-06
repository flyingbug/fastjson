package com.alibaba.json.bvt.serializer;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_4 extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.WriteTabAsSpecial, true);
        out.writeFieldValue(',', "\tname", "\t");
        Assert.assertEquals(",\"\\tname\":\"\\t\"", out.toString());
    }
    
    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.WriteTabAsSpecial, true);
        out.writeFieldValue(',', "name", "\t\n");
        Assert.assertEquals(",\"name\":\"\\t\\n\"", out.toString());
    }

    public void test_3() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.WriteTabAsSpecial, true);
        out.writeFieldValue(',', "name", "\t\n \b\n\r\f\\ \"");
        Assert.assertEquals(",\"name\":\"\\t\\n \\b\\n\\r\\f\\\\ \\\"\"", out.toString());
    }

    public void test_4() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.WriteTabAsSpecial, false);
        out.writeFieldValue(',', "name", "\t\n \b\n\r\f\\ \"");
        Assert.assertEquals(",\"name\":\"\t\\n \\b\\n\\r\\f\\\\ \\\"\"", out.toString());
    }
    
    public void test_5() throws Exception {
        SerializeWriter out = new SerializeWriter(1000);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.WriteTabAsSpecial, true);
        out.writeString("\t\n \b\n\r\f\\ \"");
        Assert.assertEquals("\"\\t\\n \\b\\n\\r\\f\\\\ \\\"\"", out.toString());
    }
}
