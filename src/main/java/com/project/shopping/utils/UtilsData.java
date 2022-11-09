package com.project.shopping.utils;

import java.sql.Blob;

public class UtilsData {
	public static byte[] getBlobToByte(Blob blob) throws Exception {
		return blob.getBinaryStream().readAllBytes();
	}
}
