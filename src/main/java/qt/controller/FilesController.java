package qt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.google.common.io.ByteStreams;

import qt.service.FileMeta;
import qt.service.FileUploadService;


@RestController
public class FilesController {


//	@Value("${filePath}")
//	private String filePath;
	@Autowired
	private FileUploadService fileUploadService;

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public FileMeta uploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {
		FileMeta fileMeta = new FileMeta();
		if (request.getFileNames().hasNext()) {
			fileMeta = fileUploadService.upload(request, response, false);
		}
		return fileMeta;
	}

//	@RequestMapping(value = "/getImage/{fileName}/", method = RequestMethod.GET)
//	public void getImage(@PathVariable("fileName") String fileName, HttpServletRequest request,
//			HttpServletResponse response) throws IOException {
//		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
//		response.setContentType("image/" + ext);
//		File f = new File(filePath + fileName);
//		long fileLength = f.length();
//		response.setHeader("Content-disposition",
//				"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
//		response.setHeader("Content-Length", String.valueOf(fileLength));
//		FileInputStream fs = new FileInputStream(f);
//		ByteStreams.copy(fs, response.getOutputStream());
//	}

}
