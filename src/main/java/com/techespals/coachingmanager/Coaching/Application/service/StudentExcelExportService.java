package com.techespals.coachingmanager.Coaching.Application.service;

import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class StudentExcelExportService {

    public ByteArrayInputStream exportStudents(List<Student> students) {

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Students");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Phone");
            header.createCell(4).setCellValue("Parent Phone");
            header.createCell(5).setCellValue("Course");
            header.createCell(6).setCellValue("Batch");
            header.createCell(7).setCellValue("Total Fees");
            header.createCell(8).setCellValue("Paid Fees");
            header.createCell(9).setCellValue("Remaining Fees");
            header.createCell(10).setCellValue("Fee Status");
            header.createCell(11).setCellValue("Joining Date");

            int rowIdx = 1;

            for (Student student : students) {

                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(student.getId() != null ? student.getId() : 0);
                row.createCell(1).setCellValue(student.getName() != null ? student.getName() : "");
                row.createCell(2).setCellValue(student.getEmail() != null ? student.getEmail() : "");
                row.createCell(3).setCellValue(student.getPhone() != null ? student.getPhone() : "");
                row.createCell(4).setCellValue(student.getParentPhone() != null ? student.getParentPhone() : "");

                row.createCell(5).setCellValue(
                        student.getCourse() != null && student.getCourse().getCourseName() != null
                                ? student.getCourse().getCourseName()
                                : ""
                );

                row.createCell(6).setCellValue(
                        student.getBatch() != null && student.getBatch().getBatchName() != null
                                ? student.getBatch().getBatchName()
                                : ""
                );

                row.createCell(7).setCellValue(student.getTotalFees() != null ? student.getTotalFees() : 0);
                row.createCell(8).setCellValue(student.getPaidFees() != null ? student.getPaidFees() : 0);
                row.createCell(9).setCellValue(student.getRemainingFees() != null ? student.getRemainingFees() : 0);

                row.createCell(10).setCellValue(
                        student.getFeeStatus() != null ? student.getFeeStatus().name() : ""
                );

                row.createCell(11).setCellValue(
                        student.getJoiningDate() != null ? student.getJoiningDate().toString() : ""
                );
            }

            for (int i = 0; i < 12; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to export students", e);
        }
    }
}