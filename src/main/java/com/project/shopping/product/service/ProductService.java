package com.project.shopping.product.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.project.shopping.product.dto.ProductDTO;
import com.project.shopping.product.repository.ProductRepository;
import com.project.shopping.utils.UtilsData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public List<ProductDTO> getProductList(ProductDTO productDTO) {
		String cateNo = productDTO.getCateNo();
		char useYn = productDTO.getUseYn();
		char dispYn = productDTO.getDispYn();
		List<ProductDTO> list = productRepository.findAllByCateNoAndUseYnAndDispYn(cateNo, useYn, dispYn);
		
		for(int i=0, size=list.size() ; i<size ; i++) {
			ProductDTO product = list.get(i);

			if(product.getImageData()!=null) {
				product.setImage(UtilsData.getBlobToByte(product.getImageData()));
			}
		}

		return list;
	}

	public ProductDTO getProductDetail(String prdtNo) {
		ProductDTO product = productRepository.findByPrdtNo(prdtNo);

		if(!ObjectUtils.isEmpty(product.getImageData())) {
			product.setImage(UtilsData.getBlobToByte(product.getImageData()));
		}

		return product;
	}

	public Boolean saveProduct(ProductDTO parameter) {
		boolean result = false;

		try {
			parameter.setImageData(new SerialBlob(parameter.getFile().getBytes()));	
			ProductDTO data = productRepository.save(parameter);

			if(data!=null) {
				result = true;
			}
		} catch (IOException e) {
			log.error("io exception {}", e.getMessage());
		} catch (SQLException e2) {
			log.error("sql exception {}", e2.getMessage());
		}

		return result;
	}
}
