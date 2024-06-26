package com.project.shopping.product.dto;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.shopping.product.dto.pk.ProductPK;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@IdClass(value = ProductPK.class)
@Table
@NoArgsConstructor
@JsonIgnoreProperties(value = {}, allowSetters = true)
public class ProductInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prdtIndex;
    @Id
    private String prdtNo;
    private String cateNo;
    private String prdtName;
    private char dispYn;
    private char useYn;
    private String normalPrice;
    private String sellPrice;
    private String imagePath;
    private String imageName;
    @Transient
    private String lastPrdtNo;
    @CreationTimestamp
    private LocalDateTime regDtime = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime updateDtime = LocalDateTime.now();
    @Transient
    private MultipartFile file;

    public void setImageData(String imagePath, String imageName) {
        this.imagePath = imagePath;
        this.imageName = imageName;
    }

    public String getImageFullPath() {
        String imageFullPath = "";
        if (this.imagePath != null && this.imageName != null) {
            imageFullPath = this.imagePath.concat(File.separator).concat(this.imageName);
        }

        return imageFullPath;
    }
}
