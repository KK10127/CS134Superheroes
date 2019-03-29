package edu.miracosta.cs134.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class loads the Student data from a formatted JSON file.
 * Populates data model (Student) with data.
 */
public class JSONLoader {

    /**
     * Loads JSON data from a file in the assets directory.
     *
     */
    public static List<Student> loadJSONFromAsset(Context context) throws IOException {
        List<Student> allStudentsList = new ArrayList<>();
        String json = null;
        InputStream is = context.getAssets().open("cs134superheroes.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allStudentsJSON = jsonRootObject.getJSONArray("CS134Superheroes");

            // Loop through all the students in the JSON data, create a student
            int length = allStudentsJSON.length();
            JSONObject studentJSON;
            String fileName, name, superPower, oneThing;
            Student student;

            for (int i = 0; i < length;  i++) {
                studentJSON = allStudentsJSON.getJSONObject(i);
                // extract the info
                fileName = studentJSON.getString("FileName");
                name = studentJSON.getString("Name");
                superPower = studentJSON.getString("Superpower");
                oneThing = studentJSON.getString("OneThing");

                // create object and add to the list
                student = new Student(fileName, name, superPower, oneThing);

                Log.d("JSON Loader", "Created student: " + student.toString());
                allStudentsList.add(student);
            }
        } catch (JSONException e) {
            Log.e("Superheroes Quiz", e.getMessage());
        }

        // return the list
        Log.d("JSON Loader", "Returning list: " + allStudentsList.toString());
        return allStudentsList;
    }

}
