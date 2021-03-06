/*
 * Copyright 2017 Oursky Ltd.
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
 *
 */

package io.skygear.skygear;

import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LambdaRequestUnitTest {
    @Test
    public void testLambdaRequestCreationFlow() throws Exception {
        LambdaRequest request = new LambdaRequest(
                "test:op1",
                new Object[]{"hello", "world", 123}
        );

        assertEquals("test:op1", request.action);

        JSONArray args = (JSONArray) request.data.get("args");
        assertEquals(3, args.length());
        assertEquals("hello", args.get(0));
        assertEquals("world", args.get(1));
        assertEquals(123, args.get(2));
    }

    @Test
    public void testLambdaRequestAllowNullArgumentList() throws Exception {
        LambdaRequest request = new LambdaRequest("test:op1", (Object[]) null);

        assertEquals("test:op1", request.action);
        assertNull(request.data.get("args"));
        assertFalse(request.data.keySet().contains("args"));
    }

    @Test
    public void testLambdaRequestAllowNullArgument() throws Exception {
        LambdaRequest request = new LambdaRequest(
                "test:op1",
                new Object[]{"hello", "world", null}
        );

        assertEquals("test:op1", request.action);

        JSONArray args = (JSONArray) request.data.get("args");
        assertEquals(3, args.length());
        assertEquals("hello", args.get(0));
        assertEquals("world", args.get(1));
        assertTrue(args.isNull(2));
    }

    @Test
    public void testLambdaRequestCompatibleValueValidation() throws Exception {
        JSONObject jsonObject = new JSONObject("{\"hello\":\"world\"}");
        JSONArray jsonArray = new JSONArray("[\"hello\",\"world\"]");

        LambdaRequest request = new LambdaRequest("test:op1", new Object[]{
                false,
                (byte) 3,
                'c',
                3.4,
                3.4f,
                3,
                3L,
                (short) 3,
                "3",
                jsonObject,
                jsonArray
        });

        request.validate();
    }

    @Test(expected = InvalidParameterException.class)
    public void testLambdaRequestIncompatibleValueValidation() throws Exception {
        new LambdaRequest("test:op1", new Object[]{new Date()});
    }

    @Test
    public void testLambdaRequestAllowNullArgumentWithMap() throws Exception {
        LambdaRequest request = new LambdaRequest(
                "test:op1",
                new HashMap<String, Object>() {{
                    put("key1", "hello");
                    put("key2", "world");
                    put("key3", null);
                }}
        );

        assertEquals("test:op1", request.action);

        JSONObject args = (JSONObject) request.data.get("args");
        assertEquals(3, args.length());
        assertEquals("hello", args.getString("key1"));
        assertEquals("world", args.getString("key2"));
        assertTrue(args.isNull("key3"));
    }

    @Test
    public void testLambdaRequestCompatibleValueWithMapValidation() throws Exception {
        final JSONObject jsonObject = new JSONObject("{\"hello\":\"world\"}");
        final JSONArray jsonArray = new JSONArray("[\"hello\",\"world\"]");

        LambdaRequest request = new LambdaRequest(
                "test:op1", new HashMap<String, Object>() {{
            put("key1", false);
            put("key2", (byte) 3);
            put("key3", 'c');
            put("key4", 3.4);
            put("key5", 3.4f);
            put("key6", 3);
            put("key7", 3L);
            put("key8", (short) 3);
            put("key9", "3");
            put("key10", jsonObject);
            put("key11", jsonArray);
        }});

        request.validate();
    }

    @Test(expected = InvalidParameterException.class)
    public void testLambdaRequestIncompatibleValueWithMapValidation() throws Exception {
        new LambdaRequest("test:op1", new HashMap<String, Object>() {{
            put("key1", new Date());
        }});
    }


}
