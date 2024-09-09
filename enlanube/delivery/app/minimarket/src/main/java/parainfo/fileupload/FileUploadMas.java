package parainfo.fileupload;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;

public class FileUploadMas {

    // recoge parámetros
    public List<DiskFileItem> recogeParam(HttpServletRequest request) {
        List<DiskFileItem> list = null;

        try {
            DiskFileItemFactory factory = DiskFileItemFactory.builder().get();

            JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);

            list = upload.parseRequest(request);

        } catch (FileUploadException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }

    // graba en disco
    public String saveFile(String path, String name, byte[] blob) {

        String message = "ok"; // optimistamente

        // guardando documento en disco
        File file = new File(path + name);

        try (FileOutputStream fos = new FileOutputStream(file)) {

            fos.write(blob, 0, blob.length);

        } catch (IOException ex) {
            message = ex.getMessage();
        }

        return message; // si todo OK retorna ok, sino lleva mensaje de error
    }

    public String deleteFile(String path, String name) {

        boolean success = (new File(path + name)).delete();

        String message = success ? "ok" : "No pudo eliminar archivo";

        return message; // si todo OK retorna ok
    }
}
