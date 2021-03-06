package no.bouvet.jsonclient;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

public class ConvertTestObjectTest {

    private JsonConverter converter = new JsonConverter();

    @Test
    public void testConvertToJson() {
        String json = converter.toJson(createTestObject());
        assertNotNull(json);
    }

    @Test
    public void testConvertToObject() {
        String json = converter.toJson(createTestObject());
        TestObject testObject = converter.toObject(json, TestObject.class);
        assertNotNull(testObject);
    }

    @Test
    public void testConvertToList() {
        String json = converter.toJson(createList());
        List<TestObject> list = converter.toList(json, TestObject.class);
        assertNotNull(list);
        assertNotNull(list.get(0));
        assertTrue(list.get(0).getDate() instanceof DateTime);
    }

    @Test
    public void testConvertToListOfList() {
        String json = converter.toJson(createListOfList());
        List<List<TestObject>> listOfList = converter.toListOfList(json, TestObject.class);
        assertNotNull(listOfList);
        assertNotNull(listOfList.get(0).get(0));
        assertTrue(listOfList.get(0).get(0).getDate() instanceof DateTime);
    }

    private List<List<TestObject>> createListOfList() {
        List<List<TestObject>> listOfList = new ArrayList<List<TestObject>>();
        listOfList.add(createList());
        listOfList.add(createList());
        listOfList.add(createList());
        return listOfList;
    }

    private List<TestObject> createList() {
        List<TestObject> list = new ArrayList<TestObject>();
        list.add(createTestObject());
        list.add(createTestObject());
        list.add(createTestObject());
        return list;
    }

    private TestObject createTestObject() {
        TestObject testObject = new TestObject();
        testObject.setId(123);
        testObject.setText("some text");
        testObject.setDate(new DateTime());
        return testObject;
    }
}
