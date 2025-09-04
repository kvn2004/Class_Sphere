package lk.vihanganimsara.classsphere.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QrGenaratorForStudent {
    public static String generateQRCodeImage(String text, String filePath) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 400, 400);

        Path path = Paths.get(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return path.toString(); // return saved path
    }
}
