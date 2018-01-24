package org.salimhome.books.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

@Controller
public class BooksInputController {

    private static final String PARAM_FRONT_COVER_IMAGE = "frontCoverImage";
    private static final String PARAM_BACK_COVER_IMAGE = "backCoverImage";
    private static final String PARAM_ID = "id";
    private static final String ATTRIBUTE_SENT_REMINDERS = "sentReminders";
    private static final String ATTRIBUTE_INPUT_NAMA_ERROR = "inputNamaError";
    private static final String ATTRIBUTE_INPUT_EMAIL_ERROR = "inputEmailError";
    private static final String ATTRIBUTE_INPUT_SELECTIONS_ERROR = "inputSelectionsError";

    private static final Logger LOG = Logger.getLogger(BooksInputController.class.getName());
    private static final String ATTR_FRONT_COVER_ID = "frontCoverId";
    private static final String ATTR_BACK_COVER_ID = "backCoverId";
    private static final String ATTR_TITLE = "title";
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    BookEntityFactory tempBookFactory = BookEntityFactory.newTempEntity();

    @RequestMapping(value = "/buku/tambah")
    public String addBookUploadCovers(HttpServletRequest request, Model model) throws Exception {
        Key key = datastore.put(tempBookFactory.createNewEntity());
        model.addAttribute("entityId", key.getId());
        return "uploadCover";
    }

    @RequestMapping(value = "/buku/tambah/form")
    public String addBookForm(HttpServletRequest request, Model model) throws EntityNotFoundException {
        long id = Long.parseLong(request.getParameter(PARAM_ID));
        Entity entity = datastore.get(tempBookFactory.createTempEntityKey(id));
        model.addAttribute(ATTR_FRONT_COVER_ID, entity.getProperty(BookEntityFactory.PROPERTY_FRONT_COVER_ID));
        model.addAttribute(ATTR_BACK_COVER_ID, entity.getProperty(BookEntityFactory.PROPERTY_BACK_COVER_ID));
        model.addAttribute(ATTR_TITLE, entity.getProperty(BookEntityFactory.PROPERTY_TITLE));
        return "addBookForm";
    }

    @RequestMapping(value = "/buku/tambah/judul")
    public String addBookTitleFromCover(HttpServletRequest request, Model model) throws EntityNotFoundException {
        long id = Long.parseLong(request.getParameter(PARAM_ID));
        Entity entity = datastore.get(tempBookFactory.createTempEntityKey(id));
        model.addAttribute(ATTR_FRONT_COVER_ID, entity.getProperty(BookEntityFactory.PROPERTY_FRONT_COVER_ID));
        model.addAttribute(ATTR_TITLE, entity.getProperty(BookEntityFactory.PROPERTY_TITLE));
        return "addBookTitle";
    }

    @RequestMapping(value = "/tambah/buku/judul/pilih")
    public String recognizeBookTitle(HttpServletRequest request, Model model) throws IOException, Exception {
        long id = Long.parseLong(request.getParameter(PARAM_ID));
        Entity entity = datastore.get(tempBookFactory.createTempEntityKey(id));
        String imageId = entity.getProperty(BookEntityFactory.PROPERTY_FRONT_COVER_ID).toString();

        BlobKey blobKey = new BlobKey(imageId);
        BlobstoreInputStream imageInputStream = new BlobstoreInputStream(blobKey);
        ByteString imageByteString = ByteString.readFrom(imageInputStream);

        int x1 = Integer.parseInt(request.getParameter("x1"));
        int y1 = Integer.parseInt(request.getParameter("y1"));
        int x2 = Integer.parseInt(request.getParameter("x2"));
        int y2 = Integer.parseInt(request.getParameter("y2"));
        int htmlImageWidth = Integer.parseInt(request.getParameter("imageWidth"));
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        Image image = ImagesServiceFactory.makeImage(imageByteString.toByteArray());
        int width = image.getWidth();
        float factor = (float) htmlImageWidth / width;
        int height = image.getHeight();
        LOG.info("Factor " + factor + "\twidth" + width + "\theight" + height + "\thtmlWidth" + htmlImageWidth);
        float cropX1 = (float) x1 / factor / width;
        LOG.info("x1=" + x1 + "\t" + cropX1);
        float cropY1 = (float) y1 / factor / height;
        LOG.info("y1=" + y1 + "\t" + cropY1);
        float cropX2 = (float) x2 / factor / width;
        LOG.info("x2=" + x2 + "\t" + cropX2);
        float cropY2 = (float) y2 / factor / height;
        LOG.info("y2=" + y2 + "\t" + cropY2);
        Image croppedImage = imagesService.applyTransform(ImagesServiceFactory.makeCrop(cropX1, cropY1, cropX2, cropY2),
                image);
        ByteString croppedImageByteString = ByteString.copyFrom(croppedImage.getImageData());
        com.google.cloud.vision.v1.Image img = com.google.cloud.vision.v1.Image.newBuilder()
                .setContent(croppedImageByteString).build();

        Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
        AnnotateImageRequest imageRequest = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        List<AnnotateImageRequest> imageRequests = new ArrayList<AnnotateImageRequest>();
        imageRequests.add(imageRequest);

        StringBuffer stringBuffer = new StringBuffer();
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(imageRequests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    throw new IllegalStateException(res.getError().getMessage());
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                int counter = 0;
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    if (counter++ == 0) {
                        // First annotation is the summary --> ignore
                        continue;
                    }
                    stringBuffer.append(annotation.getDescription());
                    stringBuffer.append(' ');
                    LOG.info("Found text: " + annotation.getDescription());
                }
            }
        }
        entity.setProperty(BookEntityFactory.PROPERTY_TITLE, stringBuffer.toString().trim());
        datastore.put(entity);
        return "redirect:/buku/tambah/form?id=" + id;
    }

    @RequestMapping(value = "/buku/tambah/upload")
    public String uploadCovers(HttpServletRequest request, Model model) throws Exception {
        long id = Long.parseLong(request.getParameter(PARAM_ID));
        Entity entity = datastore.get(tempBookFactory.createTempEntityKey(id));
        entity.setProperty(BookEntityFactory.PROPERTY_FRONT_COVER_ID,
                getUploadedImageId(request, PARAM_FRONT_COVER_IMAGE));
        entity.setProperty(BookEntityFactory.PROPERTY_BACK_COVER_ID,
                getUploadedImageId(request, PARAM_BACK_COVER_IMAGE));
        datastore.put(entity);
        return "redirect:/buku/tambah/form?id=" + id;
    }

    private String getUploadedImageId(HttpServletRequest request, String paramName) {
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get(paramName);
        if (blobKeys == null || blobKeys.size() == 0) {
            return "";
        } else {
            return blobKeys.get(0).getKeyString();
        }
    }

}
