package com.alibaba.json.bvt;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TabCharTest extends TestCase {

    public void test_0() throws Exception {
        JSONObject json = new JSONObject();
        json.put("hello\t", "World\t!");
        Assert.assertEquals("{\"hello\t\":\"World\t!\"}", JSON.toJSONString(json));
        Assert.assertEquals("{\"hello\\t\":\"World\\t!\"}", JSON.toJSONString(json, SerializerFeature.WriteTabAsSpecial));
        Assert.assertEquals("{'hello\\t':'World\\t!'}", JSON.toJSONString(json, SerializerFeature.WriteTabAsSpecial, SerializerFeature.UseSingleQuotes));
    }

}
