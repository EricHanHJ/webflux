package com.plantynet.webflux.vo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Document(collection = "#{@dynamicDocumentName.getDocumentName()}")
@Getter
@Setter
public class AppLogDocument implements Serializable {
	private static final long serialVersionUID = 142466781L;

	@Id
	private String id;

	private String TimeStamp = "";
	private String Result = "";

	@Field(name = "SrcIp")
	private String srcIp = "";

	private long SrcIpLong = 0;
	private int SrcPort = 0;
	private String SrcMac = "";
	private String GrpId = "";
	private String PolicyNo = "";
	private String GrpTxt = "";
	private String GrpPath = "";
	private String GrpId_00 = "";
	private String GrpId_01 = "";
	private String CatId = "";
	private String CatId_00 = "";
	private String CatId_01 = "";
	private String CatTxt = "";

	@Field(name = "Size")
	private int size = 0;

	private int Deny = 0;
	private int Permit = 0;

}