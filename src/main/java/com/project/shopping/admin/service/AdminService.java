package com.project.shopping.admin.service;

import java.util.List;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.stereotype.Service;
import com.project.shopping.common.dto.CodeFieldDTO;
import com.project.shopping.common.service.CommonService;
import com.project.shopping.display.dto.BannerDTO;
import com.project.shopping.display.repository.BannerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final CommonService commonService;
	private final BannerRepository bannerRepository;

	public List<CodeFieldDTO> getAdminMenuList(CodeFieldDTO param) throws Exception {
		return commonService.getCommonList(param);
	}

	public Boolean registBanner(BannerDTO param) throws Exception {
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

}
