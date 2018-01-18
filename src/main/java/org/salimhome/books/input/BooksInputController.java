package org.salimhome.books.input;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@Controller
public class BooksInputController {

    private static final String ATTRIBUTE_SENT_REMINDERS = "sentReminders";
    private static final String ATTRIBUTE_INPUT_NAMA_ERROR = "inputNamaError";
    private static final String ATTRIBUTE_INPUT_EMAIL_ERROR = "inputEmailError";
    private static final String ATTRIBUTE_INPUT_SELECTIONS_ERROR = "inputSelectionsError";

    private static final Logger LOG = Logger.getLogger(BooksInputController.class.getName());
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @RequestMapping(value = "/buku/tambah")
    public String addBookForm(HttpServletRequest request, Model model) throws Exception {
        return "addBook";
    }

    @RequestMapping(value = "/buku/tambah/upload")
    public String uploadImageOnAdd(HttpServletRequest request, Model model) throws Exception {
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("myFile");

        String imgSrc = blobKeys.get(0).getKeyString();
        model.addAttribute("imgSource", imgSrc);
        return "addBook";
    }

}
