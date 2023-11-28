package com.example.sus.activity.logic.auth.retrofit.dto

import com.google.gson.annotations.SerializedName

data class Publication(
    @SerializedName("ModeratedItemInfo")
    val moderatedItemInfo: ModeratedItemInfo,

    @SerializedName("Type")
    val type: Int,

    @SerializedName("TypeString")
    val typeString: String,

    @SerializedName("Edition")
    val edition: String,

    @SerializedName("Language")
    val language: String,

    @SerializedName("Circulation")
    val circulation: Int,

    @SerializedName("ISBN")
    val isbn: String,

    @SerializedName("Authors")
    val authors: String,

    @SerializedName("TitleContinuation")
    val titleContinuation: String,

    @SerializedName("ChapterMonograph")
    val chapterMonograph: String,

    @SerializedName("SubmissionDate")
    val submissionDate: String,

    @SerializedName("FormOfpresentation")
    val formOfpresentation: String,

    @SerializedName("ProtocolNumber")
    val protocolNumber: String,

    @SerializedName("EditionInformation")
    val editionInformation: String,

    @SerializedName("PublisherPlace")
    val publisherPlace: String,

    @SerializedName("Publisher")
    val publisher: String,

    @SerializedName("Stamp")
    val stamp: String,

    @SerializedName("StampOther")
    val stampOther: String,

    @SerializedName("IsReview")
    val isReview: Boolean,

    @SerializedName("MagazineTitle")
    val magazineTitle: String,

    @SerializedName("CollectionTitle")
    val collectionTitle: String,

    @SerializedName("ThematicTitle")
    val thematicTitle: String,

    @SerializedName("Volume")
    val volume: String,

    @SerializedName("Release")
    val release: String,

    @SerializedName("Number")
    val number: String,

    @SerializedName("PageStart")
    val pageStart: Int,

    @SerializedName("PageEnd")
    val pageEnd: Int,

    @SerializedName("PageCount")
    val pageCount: Int,

    @SerializedName("DOI")
    val doi: String,

    @SerializedName("ISSN")
    val issn: String,

    @SerializedName("eISSN")
    val eISSN: String,

    @SerializedName("URL")
    val url: String,

    @SerializedName("Quartile")
    val quartile: Int,

    @SerializedName("IndexWoS")
    val indexWoS: Boolean,

    @SerializedName("IndexSCOPUS")
    val indexSCOPUS: Boolean,

    @SerializedName("IndexGoogleScholar")
    val indexGoogleScholar: Boolean,

    @SerializedName("IndexERIH")
    val indexERIH: Boolean,

    @SerializedName("IndexAstrophysics")
    val indexAstrophysics: Boolean,

    @SerializedName("IndexPubMed")
    val indexPubMed: Boolean,

    @SerializedName("IndexMathematics")
    val indexMathematics: Boolean,

    @SerializedName("IndexMEDLINE")
    val indexMEDLINE: Boolean,

    @SerializedName("IndexSpringer")
    val indexSpringer: Boolean,

    @SerializedName("IndexGeoRef")
    val indexGeoRef: Boolean,

    @SerializedName("IndexMathSciNet")
    val indexMathSciNet: Boolean,

    @SerializedName("IndexBioOne")
    val indexBioOne: Boolean,

    @SerializedName("IndexzbMATH")
    val indexzbMATH: Boolean,

    @SerializedName("IndexВАК")
    val indexVAK: Boolean,

    @SerializedName("IndexРИНЦ")
    val indexRINZ: Boolean,

    @SerializedName("IndexRSCI")
    val indexRSCI: Boolean,

    @SerializedName("IndexIEEE")
    val indexIEEE: Boolean,

    @SerializedName("IndexBasesString")
    val indexBasesString: String
)


