package org.salimhome.books.input;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class UploadServlet extends HttpServlet {
    // https://cloud.google.com/appengine/docs/standard/java/blobstore/
    // private boolean isMultipart;
    // private String filePath;
    // private int maxFileSize = 50 * 1024;
    // private int maxMemSize = 4 * 1024;
    // private File file;
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    // public void init() {
    // // Get the file location where it would be stored.
    // filePath = getServletContext().getInitParameter("file-upload");
    // }

    // public class UploadServlet extends HttpServlet {
    // private BlobstoreService blobstoreService =
    // BlobstoreServiceFactory.getBlobstoreService();
    // public void doPost(HttpServletRequest req, HttpServletResponse resp)
    // throws IOException {
    //
    // JSONObject finalJson = new JSONObject();
    // Boolean success= false;
    // String blobid= "";
    // Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
    // BlobKey blobKey = blobs.get("file-foto-usuario");
    //
    // if (blobKey == null) {
    // resp.sendRedirect("/");
    // } else {
    // success= true;
    // blobid= blobKey.getKeyString();
    // //resp.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
    // }
    //
    // finalJson.put("success", success);
    // finalJson.put("blobKey", blobid);
    // resp.setCharacterEncoding("utf8");
    // resp.setContentType("application/json");
    // PrintWriter out = resp.getWriter();
    // out.print(finalJson);
    //
    // }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("myFile");

        if (blobKeys == null || blobKeys.isEmpty()) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/imgstore?img=" + blobKeys.get(0).getKeyString());
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        throw new ServletException("GET method used with " + getClass().getName() + ": POST method required.");
    }
}
