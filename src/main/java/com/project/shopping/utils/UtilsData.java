package com.project.shopping.utils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UtilsData {
	public static byte[] getBlobToByte(Blob blob) {
		byte[] bytes = {};

		try {
			bytes = blob.getBinaryStream().readAllBytes();
		} catch (SQLException e) {
			// TODO: handle exception
			log.error("sql exception {}", e.getMessage());
		} catch (IOException e2) {
			log.error("input output exception {}", e2.getMessage());
		}

		return bytes;
	}
}
