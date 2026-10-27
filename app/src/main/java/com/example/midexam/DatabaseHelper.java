package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "students";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + "student_id TEXT PRIMARY KEY, "
                + "name TEXT NOT NULL, "
                + "age INTEGER NOT NULL CHECK(age >= 17 AND age <= 50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // CREATE
    public boolean addStudent(String studentId, String name, int age) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("student_id", studentId);
        cv.put("name", name);
        cv.put("age", age);
        return db.insert(TABLE, null, cv) != -1;
    }

    // UPDATE
    public boolean updateStudent(String studentId, String name, int age) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("age", age);
        return db.update(TABLE, cv, "student_id = ?", new String[]{studentId}) > 0;
    }

    // DELETE
    public boolean deleteStudent(String studentId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, "student_id = ?", new String[]{studentId}) > 0;
    }

    // READ
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT student_id, name, age FROM " + TABLE + " ORDER BY name", null);
        while (c.moveToNext()) {
            list.add(new Student(c.getString(0), c.getString(1), c.getInt(2)));
        }
        c.close();
        return list;
    }

    // Model sederhana
    public static class Student {
        public String studentId;
        public String name;
        public int age;

        public Student(String studentId, String name, int age) {
            this.studentId = studentId;
            this.name = name;
            this.age = age;
        }
    }
}