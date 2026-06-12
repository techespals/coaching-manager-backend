package com.techespals.coachingmanager.Coaching.Application.service;



import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PaymentExcelExportService {

    public ByteArrayInputStream exportPayments(List<Payment> payments) {

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Payments");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Payment ID");
            header.createCell(1).setCellValue("Receipt Number");
            header.createCell(2).setCellValue("Student Name");
            header.createCell(3).setCellValue("Student Phone");
            header.createCell(4).setCellValue("Amount");
            header.createCell(5).setCellValue("Payment Mode");
            header.createCell(6).setCellValue("Payment Date");
            header.createCell(7).setCellValue("Total Fees");
            header.createCell(8).setCellValue("Paid Fees");
            header.createCell(9).setCellValue("Remaining Fees");
            header.createCell(10).setCellValue("Fee Status");

            int rowIdx = 1;

            for (Payment payment : payments) {

                Row row = sheet.createRow(rowIdx++);

                Student student = payment.getStudent();

                row.createCell(0).setCellValue(payment.getId() != null ? payment.getId() : 0);
                row.createCell(1).setCellValue(payment.getReceiptNumber() != null ? payment.getReceiptNumber() : "");

                row.createCell(2).setCellValue(
                        student != null && student.getName() != null ? student.getName() : ""
                );

                row.createCell(3).setCellValue(
                        student != null && student.getPhone() != null ? student.getPhone() : ""
                );

                row.createCell(4).setCellValue(payment.getAmount() != null ? payment.getAmount() : 0);

                row.createCell(5).setCellValue(payment.getPaymentMode() != null ? payment.getPaymentMode() : "");

                row.createCell(6).setCellValue(
                        payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : ""
                );

                row.createCell(7).setCellValue(
                        student != null && student.getTotalFees() != null ? student.getTotalFees() : 0
                );

                row.createCell(8).setCellValue(
                        student != null && student.getPaidFees() != null ? student.getPaidFees() : 0
                );

                row.createCell(9).setCellValue(
                        student != null && student.getRemainingFees() != null ? student.getRemainingFees() : 0
                );

                row.createCell(10).setCellValue(
                        student != null && student.getFeeStatus() != null ? student.getFeeStatus().name() : ""
                );
            }

            for (int i = 0; i <= 10; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to export payments", e);
        }
    }
}