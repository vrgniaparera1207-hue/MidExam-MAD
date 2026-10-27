package com.example.midexam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MIN_AGE = 17;

    private EditText etName, etStudentId;
    private SeekBar seekAge;
    private TextView tvAge;
    private Button btnAdd, btnEdit, btnDelete, btnView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hubungkan dengan layout
        etName = findViewById(R.id.etName);

        etStudentId = findViewById(R.id.etStudentId);
        seekAge = findViewById(R.id.seekAge);
        tvAge = findViewById(R.id.tvAge);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        db = new DatabaseHelper(this);

        // Slider umur
        tvAge.setText("Age: " + getAge());
        seekAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                tvAge.setText("Age: " + getAge());
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });

        // ADD
        btnAdd.setOnClickListener(v -> {
            if (!validateInput()) return;
            boolean ok = db.addStudent(
                    etStudentId.getText().toString().trim(),
                    etName.getText().toString().trim(),
                    getAge());
            if (ok) {
                toast("Data berhasil ditambahkan");
                clearForm();
            } else {

                toast("Gagal: Student ID sudah ada");
            }
        });

        // EDIT (berdasarkan Student ID)
        btnEdit.setOnClickListener(v -> {
            if (!validateInput()) return;
            boolean ok = db.updateStudent(
                    etStudentId.getText().toString().trim(),
                    etName.getText().toString().trim(),
                    getAge());
            if (ok) {
                toast("Data berhasil diubah");
                clearForm();
            } else {
                toast("Gagal: Student ID tidak ditemukan");
            }
        });

        // DELETE (berdasarkan Student ID)
        btnDelete.setOnClickListener(v -> {
            String id = etStudentId.getText().toString().trim();
            if (id.isEmpty()) {
                etStudentId.setError("Isi Student ID yang mau dihapus");
                return;
            }
            if (db.deleteStudent(id)) {
                toast("Data berhasil dihapus");
                clearForm();
            } else {
                toast("Gagal: Student ID tidak ditemukan");
            }

        });

        // VIEW (tampil di dialog)
        btnView.setOnClickListener(v -> showData());
    }

    private int getAge() {
        return seekAge.getProgress() + MIN_AGE; // 0..33 -> 17..50
    }

    private boolean validateInput() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Nama tidak boleh kosong");
            return false;
        }
        if (etStudentId.getText().toString().trim().isEmpty()) {
            etStudentId.setError("Student ID tidak boleh kosong");
            return false;
        }
        return true;
    }

    private void showData() {
        List<DatabaseHelper.Student> students = db.getAllStudents();
        StringBuilder sb = new StringBuilder();
        if (students.isEmpty()) {
            sb.append("Belum ada data.");
        } else {
            for (DatabaseHelper.Student s : students) {
                sb.append("Name : ").append(s.name).append("\n")
                        .append("ID   : ").append(s.studentId).append("\n")
                        .append("Age  : ").append(s.age).append("\n\n");

            }
        }
        new AlertDialog.Builder(this)
                .setTitle("Data Student")
                .setMessage(sb.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private void clearForm() {
        etName.setText("");
        etStudentId.setText("");
        seekAge.setProgress(0);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}