package qt.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class FileUploadService {

	FileMeta fileMeta = null;
	@Value("${filePath}")
	private String filePath;

	public FileMeta upload(MultipartHttpServletRequest request, HttpServletResponse response, boolean trim) {

		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// 2. get each file
		if (itr.hasNext()) {
			// 2.1 get next MultipartFile
			mpf = request.getFile(itr.next());

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			String extension = "";
			if (mpf.getOriginalFilename().lastIndexOf(".") != -1) {
				extension = mpf.getOriginalFilename().substring(mpf.getOriginalFilename().lastIndexOf("."));
			}
			fileMeta.setOrgName(mpf.getOriginalFilename());
			String hashCode = new BCryptPasswordEncoder().encode(mpf.getOriginalFilename());
			hashCode = hashCode.replaceAll("/", "");
			if (trim) {

				fileMeta.setFileName(hashCode.substring(0, 14) + extension);
			} else {
				fileMeta.setFileName(hashCode + "_" + new Date().getTime() + extension);
			}

			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());

			try {
				fileMeta.setBytes(mpf.getBytes());
				fileMeta.setFilePath(filePath + fileMeta.getFileName());
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(filePath + fileMeta.getFileName()));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return fileMeta;
	}

}
