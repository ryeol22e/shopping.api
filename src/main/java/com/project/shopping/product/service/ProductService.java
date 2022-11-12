package com.project.shopping.product.service;

import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;

import com.project.shopping.product.dto.ProductDTO;
import com.project.shopping.product.repository.ProductRepository;
import com.project.shopping.utils.UtilsData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public List<ProductDTO> getProductList(ProductDTO productDTO) throws Exception {
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

	public ProductDTO getProductDetail(String prdtNo) throws Exception {
		ProductDTO product = productRepository.findByPrdtNo(prdtNo);
		product.setImage(UtilsData.getBlobToByte(product.getImageData()));

		return product;
	}

	public Boolean saveProduct(ProductDTO parameter) throws Exception {
		parameter.setImageData(new SerialBlob(parameter.getFile().getBytes()));

		ProductDTO data = productRepository.save(parameter);
		boolean result = false;

		if(data!=null) {
			result = true;
		}

		return result;
	}
}
