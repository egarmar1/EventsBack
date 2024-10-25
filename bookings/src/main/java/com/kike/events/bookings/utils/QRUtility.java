package com.kike.events.bookings.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kike.events.bookings.exception.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class QRUtility {

    private static final String QR_CODE_DIRECTORY = "C:/Users/kikeg/Documents/MSPROJECT/EventsBack/qrCodes/";

    public static BitMatrix generateQRCode(String qrText) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 300;
        int height = 300;
        try {
            return qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveQRLocally(BitMatrix qrCodeMatrix, UUID randomUUID) {

        Path path = FileSystems.getDefault().getPath(QR_CODE_DIRECTORY + randomUUID + ".png"); // Ruta para guardar el archivo
        try {
            MatrixToImageWriter.writeToPath(qrCodeMatrix, "PNG", path); // Guardar como archivo PNG
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String bitMatrixQrToBase64(BitMatrix qrCodeMatrix) {
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(qrCodeMatrix, "PNG", pngOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] pngData = pngOutputStream.toByteArray();

        // Codificar la imagen PNG como Base64
        return Base64.getEncoder().encodeToString(pngData);

    }


    public static byte[] loadLocalQR(String qrId) {

        try {
            Path qrImagePath = Paths.get(QR_CODE_DIRECTORY + qrId + ".png");

            if (!Files.exists(qrImagePath)) {
                throw new ResourceNotFoundException("QR", "qr id", qrId);
            }

            return Files.readAllBytes(qrImagePath);
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to read QR image", e);
        }
    }
}
