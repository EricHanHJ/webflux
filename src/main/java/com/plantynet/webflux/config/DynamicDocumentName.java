package com.plantynet.webflux.config;

import org.springframework.stereotype.Component;

@Component
public class DynamicDocumentName {

	private String documentName = "";

	public String getDocumentName() {
		// return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-mm-YYYY"));
		System.out.println("Query for Documment name is : " + documentName);
		return documentName;
	}

	public void setDocumnetName(String documentName) {
		this.documentName = documentName;

	}

}
