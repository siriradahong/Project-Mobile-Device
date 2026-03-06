package com.example.myapplication100.DataClass.Family

data class FamilyResponse(
    val idFamily: Int,
    val family_name: String,
    val members: List<FamilyMemberDetail>
)

data class FamilyMemberDetail(
    val iduser: Int,
    val firstName: String,
    val lastName: String,
    val family_name: String?,
    val symptom: String? = null,
    val diagnosis: String? = null,
    val medicine: String? = null,
    val examinationDate: String? = null
)