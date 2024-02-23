package com.project.shopping.admin.service;

import java.util.List;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.project.shopping.common.dto.CommonField;
import com.project.shopping.common.service.CommonService;
import com.project.shopping.display.dto.BannerTable;
import com.project.shopping.display.repository.BannerRepository;
import com.project.shopping.product.dto.ProductTable;
import com.project.shopping.product.repository.ProductRepository;
import com.project.shopping.utils.UtilsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
	private final CommonService commonService;
	private final BannerRepository bannerRepository;
	private final ProductRepository productRepository;

	public List<CommonField> getAdminMenuList(CommonField param) throws Exception {
		return commonService.getCommonList(param);
	}

	public Boolean registBanner(BannerTable param) throws Exception {
		boolean flag = true;

		param.setImageData(new SerialBlob(param.getFile().getBytes()));
		param.setDispDate();
		long data = bannerRepository.save(param);

		if (data != 0) {
			flag = false;
			throw new Exception("저장 실패.");
		}

		return flag;
	}

	@Transactional
	public boolean saveProduct(ProductTable product) {
		boolean result = false;
		MultipartFile image = product.getFile();
		String path = UtilsData.getFileBasePath();

		if (UtilsData.fileUpload(image, path)) {
			String imagePath = path;
			String imageName = image.getOriginalFilename();

			product.setImageData(imagePath, imageName);
			log.info("save product data : {}", product);
			productRepository.save(product);
			result = true;
		}

		return result;
	}

}
