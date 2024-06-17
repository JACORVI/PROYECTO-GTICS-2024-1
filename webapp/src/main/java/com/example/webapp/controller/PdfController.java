package com.example.webapp.controller;

import com.example.webapp.entity.Medicamentos;
import com.example.webapp.entity.Usuario;
import com.example.webapp.repository.MedicamentosRepository;
import com.example.webapp.repository.UsuarioRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class PdfController {

    final MedicamentosRepository medicamentosRepository;
    final UsuarioRepository usuarioRepository;

    public PdfController(MedicamentosRepository medicamentosRepository,
                         UsuarioRepository usuarioRepository) {
        this.medicamentosRepository = medicamentosRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/pdf_medicamentos")
    public ResponseEntity<InputStreamResource> downloadMedicamentosPdf() {

        List<Medicamentos> medicamentos = medicamentosRepository.findAll();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Añadir título
        Paragraph title = new Paragraph("Clínica Pildo Pharmacy S.A.")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Espacio después del título
        document.add(new Paragraph("\n"));

        // Crear tabla con encabezados
        float[] columnWidths = {2, 5, 2, 2, 3}; // Ajuste de anchos de columna para cinco columnas
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Descripción")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Precio")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Número Unidades Disponibles")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha de Creación")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Agregar datos a la tabla
        for (Medicamentos medicamento : medicamentos) {
            table.addCell(new Cell().add(new Paragraph(medicamento.getNombre())));
            table.addCell(new Cell().add(new Paragraph(medicamento.getDescripcion())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(medicamento.getPrecio_unidad()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(medicamento.getInventario())))); // Nueva columna
            table.addCell(new Cell().add(new Paragraph(String.valueOf(medicamento.getFecha_ingreso())))); // Nueva columna
        }

        document.add(table);

        // Espacio antes de la fecha
        document.add(new Paragraph("\n"));

        // Añadir fecha
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        Paragraph date = new Paragraph("Fecha: " + formattedDate)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(date);

        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Medicamentos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/pdf_doctores")
    public ResponseEntity<InputStreamResource> downloadDoctores() {

        List<Usuario> usuarios = usuarioRepository.buscarDoctor(5,0);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Añadir título
        Paragraph title = new Paragraph("Clínica Pildo Pharmacy S.A.")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Espacio después del título
        document.add(new Paragraph("\n"));

        // Crear tabla con encabezados
        float[] columnWidths = {2, 5, 2, 2, 3}; // Ajuste de anchos de columna para cinco columnas
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados
        table.addHeaderCell(new Cell().add(new Paragraph("Doctor")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Correo")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("DNI")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Código de Colegiatura")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha de Ingreso")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Agregar datos a la tabla
        for (Usuario usuario : usuarios) {
            table.addCell(new Cell().add(new Paragraph(usuario.getNombres()+' '+usuario.getApellidos())));
            table.addCell(new Cell().add(new Paragraph(usuario.getCorreo())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getDni()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getCodigo_colegiatura())))); // Nueva columna
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getFecha_creacion())))); // Nueva columna
        }

        document.add(table);

        // Espacio antes de la fecha
        document.add(new Paragraph("\n"));

        // Añadir fecha
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        Paragraph date = new Paragraph("Fecha: " + formattedDate)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(date);

        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Doctores.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/pdf_administradores")
    public ResponseEntity<InputStreamResource> downloadAdministradores() {

        List<Usuario> usuarios = usuarioRepository.buscarAdministrador(2,0);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Añadir título
        Paragraph title = new Paragraph("Clínica Pildo Pharmacy S.A.")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Espacio después del título
        document.add(new Paragraph("\n"));

        // Crear tabla con encabezados
        float[] columnWidths = {2, 5, 2, 2, 3}; // Ajuste de anchos de columna para cinco columnas
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados
        table.addHeaderCell(new Cell().add(new Paragraph("Administrador")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Correo")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("DNI")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Distrito")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha de Ingreso")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Agregar datos a la tabla
        for (Usuario usuario : usuarios) {
            table.addCell(new Cell().add(new Paragraph(usuario.getNombres()+' '+usuario.getApellidos())));
            table.addCell(new Cell().add(new Paragraph(usuario.getCorreo())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getDni()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getDistrito().getNombre())))); // Nueva columna
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getFecha_creacion())))); // Nueva columna
        }

        document.add(table);

        // Espacio antes de la fecha
        document.add(new Paragraph("\n"));

        // Añadir fecha
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        Paragraph date = new Paragraph("Fecha: " + formattedDate)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(date);

        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Administradores.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/pdf_farmacistas")
    public ResponseEntity<InputStreamResource> downloadFarmacistas() {

        List<Usuario> usuarios = usuarioRepository.buscarFarmacistaAceptado(3,0,"Aceptado");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Añadir título
        Paragraph title = new Paragraph("Clínica Pildo Pharmacy S.A.")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Espacio después del título
        document.add(new Paragraph("\n"));

        // Crear tabla con encabezados
        float[] columnWidths = {2, 5, 2, 2, 3}; // Ajuste de anchos de columna para cinco columnas
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados
        table.addHeaderCell(new Cell().add(new Paragraph("Farmacista")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Correo")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("DNI")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Código de Colegiatura")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha de Ingreso")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Agregar datos a la tabla
        for (Usuario usuario : usuarios) {
            table.addCell(new Cell().add(new Paragraph(usuario.getNombres()+' '+usuario.getApellidos())));
            table.addCell(new Cell().add(new Paragraph(usuario.getCorreo())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getDni()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getCodigo_colegiatura())))); // Nueva columna
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getFecha_creacion())))); // Nueva columna
        }

        document.add(table);

        // Espacio antes de la fecha
        document.add(new Paragraph("\n"));

        // Añadir fecha
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        Paragraph date = new Paragraph("Fecha: " + formattedDate)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(date);

        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Farmacistas.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/pdf_pacientes")
    public ResponseEntity<InputStreamResource> downloadPacientes() {

        List<Usuario> usuarios = usuarioRepository.buscarPaciente(4,0);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Añadir título
        Paragraph title = new Paragraph("Clínica Pildo Pharmacy S.A.")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Espacio después del título
        document.add(new Paragraph("\n"));

        // Crear tabla con encabezados
        float[] columnWidths = {2, 5, 2, 2, 3}; // Ajuste de anchos de columna para cinco columnas
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados
        table.addHeaderCell(new Cell().add(new Paragraph("Paciente")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Correo")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("DNI")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Seguro Médico")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha de Ingreso")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Agregar datos a la tabla
        for (Usuario usuario : usuarios) {
            table.addCell(new Cell().add(new Paragraph(usuario.getNombres()+' '+usuario.getApellidos())));
            table.addCell(new Cell().add(new Paragraph(usuario.getCorreo())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getDni()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getSeguro().getNombre())))); // Nueva columna
            table.addCell(new Cell().add(new Paragraph(String.valueOf(usuario.getFecha_creacion())))); // Nueva columna
        }

        document.add(table);

        // Espacio antes de la fecha
        document.add(new Paragraph("\n"));

        // Añadir fecha
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        Paragraph date = new Paragraph("Fecha: " + formattedDate)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(date);

        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Reporte_Pacientes.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
