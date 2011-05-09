/***************************************************************************************
* Updating of the foreign key reference made by Hibernate
*
****************************************************************************************/

ALTER TABLE AdministrativeUnit
ADD  CONSTRAINT `FKA92443F2D19C2BC5` FOREIGN KEY (`PARENT_ID`) REFERENCES `administrativeunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKA92443F23B7F905D` FOREIGN KEY (`COUNTRYSTRUCTURE_ID`) REFERENCES `countrystructure` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKA92443F2B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKA92443F27180F26C`,
DROP FOREIGN KEY `FKA92443F28CCD4614`,
DROP FOREIGN KEY `FKA92443F2B9C757E8`;


ALTER TABLE AgeRange
ADD  CONSTRAINT `FK59D8FB9EB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK59D8FB9EB9C757E8`;



ALTER TABLE Batch
ADD  CONSTRAINT `FK3CFE71ABC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK3CFE71A76AA9ADD` FOREIGN KEY (`RECEIVINGITEM_ID`) REFERENCES `medicinereceivingitem` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK3CFE71A2DCF2A2E`,
DROP FOREIGN KEY `FK3CFE71AFBB4E36C`;

ALTER TABLE BatchMovement
ADD  CONSTRAINT `FK919891E94E21F23D` FOREIGN KEY (`MOVEMENT_ID`) REFERENCES `movement` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK919891E93B281F77` FOREIGN KEY (`BATCH_ID`) REFERENCES `batch` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK919891E98D91D34C`,
DROP FOREIGN KEY `FK919891E9DB4F70C8`;


ALTER TABLE BatchQuantity
ADD  CONSTRAINT `FK4B2EF5C571E04A4B` FOREIGN KEY (`UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK4B2EF5C51CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK4B2EF5C53B281F77` FOREIGN KEY (`BATCH_ID`) REFERENCES `batch` (`id`)  ON DELETE CASCADE,
DROP FOREIGN KEY `FK4B2EF5C5817DE04C`,
DROP FOREIGN KEY `FK4B2EF5C5D6A3231A`,
DROP FOREIGN KEY `FK4B2EF5C5DB4F70C8`;


ALTER TABLE CaseComment
ADD  CONSTRAINT `FKBA21BCEFA3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FKBA21BCEF8B327BA`,
ADD  CONSTRAINT `FKBA21BCEFBA5DB63D` FOREIGN KEY (`USER_ID`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FKBA21BCEFD84E76CC`;


ALTER TABLE CaseComorbidity
ADD  CONSTRAINT `FK9298ED45A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD  CONSTRAINT `FK9298ED453BD5659F` FOREIGN KEY (`COMORBIDITY_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK9298ED455ED13EEE`,
DROP FOREIGN KEY `FK9298ED458B327BA`;

ALTER TABLE CaseDataBr
ADD  CONSTRAINT `FK1FB9D4EA7E2A0E3A` FOREIGN KEY (`OUTCOME_RESISTANCETYPE`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK1FB9D4EA218FFBE9` FOREIGN KEY (`CONTAGPLACE`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D4EA227B4EFC`,
DROP FOREIGN KEY `FK1FB9D4EA309085EB`,
ADD  CONSTRAINT `FK1FB9D4EA3AEDFE05` FOREIGN KEY (`SCHEMACHANGETYPE`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK1FB9D4EA3EB540D4` FOREIGN KEY (`PREGNANCEPERIOD`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D4EA448BD538`,
ADD  CONSTRAINT `FK1FB9D4EA450CEBE3` FOREIGN KEY (`POSITION`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D4EA5DE9D754`,
ADD  CONSTRAINT `FK1FB9D4EA5F8F0119` FOREIGN KEY (`EDUCATIONALDEGREE`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D4EA61B11A23`,
DROP FOREIGN KEY `FK1FB9D4EA6808C532`,
ADD  CONSTRAINT `FK1FB9D4EA755F6B9C` FOREIGN KEY (`ADMINUNIT_USORIGEM_ID`) REFERENCES `administrativeunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK1FB9D4EA828ADA68`,
ADD  CONSTRAINT `FK1FB9D4EA82DD737C` FOREIGN KEY (`id`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK1FB9D4EA922082C0` FOREIGN KEY (`SKINCOLOR`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D4EAB51C5C0F`,
ADD  CONSTRAINT `FK1FB9D4EAC0F2BF7A` FOREIGN KEY (`OUTCOME_REGIMENCHANGED`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK1FB9D4EAC112D593` FOREIGN KEY (`MICROBACTERIOSE`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D4EAE40EAEE2`,
ADD  CONSTRAINT `FK1FB9D4EAFF7F75AD` FOREIGN KEY (`RESISTANCETYPE`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK_CASEDATABR_TBCASE`;

ALTER TABLE CaseDataPH
ADD  CONSTRAINT `FK1FB9D69274535945` FOREIGN KEY (`SOURCEOFREFERRAL_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D692974F3294`;


ALTER TABLE CaseDataUA
ADD  CONSTRAINT `FK1FB9D726E6442FCF` FOREIGN KEY (`DETECTION_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK1FB9D72610492EAB` FOREIGN KEY (`POSITION_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D726334507FA`,
ADD  CONSTRAINT `FK1FB9D7264C56187E` FOREIGN KEY (`REGISTRATION_CATEGORY`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D7266F51F1CD`,
DROP FOREIGN KEY `FK1FB9D726940091E`,
ADD  CONSTRAINT `FK1FB9D726BFD867E3` FOREIGN KEY (`DIAGNOSIS_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK1FB9D726E2D44132`;



ALTER TABLE CaseDispensing
ADD  CONSTRAINT `FKE4CB224AA3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`)  ON DELETE CASCADE,
DROP   FOREIGN KEY `FKE4CB224A8B327BA`;



ALTER TABLE CaseDispensing_ke
ADD  CONSTRAINT `FKF2ECAF8FA3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`)  ON DELETE CASCADE,
DROP FOREIGN KEY `FKF2ECAF8F8B327BA`;


ALTER TABLE CaseIssue
ADD  CONSTRAINT `FK9E3BDB69A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK9E3BDB698B327BA`;

ALTER TABLE CaseSideEffect
ADD  CONSTRAINT `FKA1F0CD38A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`)  ON DELETE CASCADE;

ALTER TABLE CaseSideEffect
ADD  CONSTRAINT `FKA1F0CD3896FA537` FOREIGN KEY (`SUBSTANCE_ID`) REFERENCES `substance` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FKA1F0CD3897F889CC` FOREIGN KEY (`SIDEEFFECT_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKA1F0CD38E514E8A5` FOREIGN KEY (`SUBSTANCE2_ID`) REFERENCES `substance` (`id`)  ON DELETE SET NULL;


ALTER TABLE CaseSideEffect
DROP FOREIGN KEY `FKA1F0CD388B327BA`,
DROP FOREIGN KEY `FKA1F0CD3893A12976`,
DROP FOREIGN KEY `FKA1F0CD38B7FBE608`,
DROP FOREIGN KEY `FKA1F0CD38BAF4631B`;



ALTER TABLE CaseSymptom
ADD  CONSTRAINT `FK19977CB9A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK19977CB915ABF9AB` FOREIGN KEY (`SYMPTOM_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK19977CB938A7D2FA`,
DROP FOREIGN KEY `FK19977CB98B327BA`;


ALTER TABLE CountryStructure
ADD  CONSTRAINT `FK7716629DB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK7716629DB9C757E8`;


ALTER TABLE ExamCulture
ADD  CONSTRAINT `FKB2F04C6FA3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD  CONSTRAINT `FKB2F04C6F43A836FD` FOREIGN KEY (`LABORATORY_ID`) REFERENCES `laboratory` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FKB2F04C6F66A4104C`,
DROP FOREIGN KEY `FKB2F04C6F8861542`,
DROP FOREIGN KEY `FKB2F04C6F8B327BA`,
ADD  CONSTRAINT `FKB2F04C6FE58A3BF3` FOREIGN KEY (`METHOD_ID`) REFERENCES `fieldvalue` (`id`)  ON DELETE SET NULL;

ALTER TABLE ExamDST
ADD  CONSTRAINT `FK145D0106A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD  CONSTRAINT `FK145D010643A836FD` FOREIGN KEY (`LABORATORY_ID`) REFERENCES `laboratory` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
DROP FOREIGN KEY `FK145D010666A4104C`,
DROP FOREIGN KEY `FK145D01068861542`,
DROP FOREIGN KEY `FK145D01068B327BA`,
ADD  CONSTRAINT `FK145D0106E58A3BF3` FOREIGN KEY (`METHOD_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE ExamDSTResult
ADD  CONSTRAINT `FK4FF058C396FA537` FOREIGN KEY (`SUBSTANCE_ID`) REFERENCES `substance` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD  CONSTRAINT `FK4FF058C397ACDF1E` FOREIGN KEY (`EXAM_ID`) REFERENCES `examdst` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK4FF058C3B7FBE608`,
DROP FOREIGN KEY `FK4FF058C3CB45202F`;


ALTER TABLE ExamHIV
ADD  CONSTRAINT `FK145D0ED6A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK145D0ED68B327BA`;


ALTER TABLE ExamMicroscopy
ADD  CONSTRAINT `FKC6929FC3A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD  CONSTRAINT `FKC6929FC343A836FD` FOREIGN KEY (`LABORATORY_ID`) REFERENCES `laboratory` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
DROP FOREIGN KEY `FKC6929FC366A4104C`,
DROP FOREIGN KEY `FKC6929FC38861542`,
DROP FOREIGN KEY `FKC6929FC38B327BA`,
ADD  CONSTRAINT `FKC6929FC3E58A3BF3` FOREIGN KEY (`METHOD_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE ExamXRay
ADD  CONSTRAINT `FK774C35717EA8371A` FOREIGN KEY (`PRESENTATION_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK774C35718B327BA`,
DROP FOREIGN KEY `FK774C3571A1A41069`,
ADD  CONSTRAINT `FK774C3571A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE;


ALTER TABLE FieldValue
ADD  CONSTRAINT `FK98AC68B7B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK98AC68B7B9C757E8`;



ALTER TABLE Forecasting
ADD  CONSTRAINT `FKC5CAEFE76515C41D` FOREIGN KEY (`TBUNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
DROP FOREIGN KEY `FKC5CAEFE78A1004CB`,
ADD  CONSTRAINT `FKC5CAEFE7B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKC5CAEFE7B9C757E8`,
ADD  CONSTRAINT `FKC5CAEFE7BA5DB63D` FOREIGN KEY (`USER_ID`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FKC5CAEFE7C9D89CEC`,
ADD  CONSTRAINT `FKC5CAEFE7CEDEEA7C` FOREIGN KEY (`ADMINUNIT_ID`) REFERENCES `administrativeunit` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
DROP FOREIGN KEY `FKC5CAEFE7D84E76CC`;



ALTER TABLE ForecastingBatch
ADD  CONSTRAINT `FKACB1CF937AAD7377` FOREIGN KEY (`FORECASTINGMEDICINE_ID`) REFERENCES `forecastingmedicine` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKACB1CF93259FA308`;


ALTER TABLE ForecastingCasesOnTreat
ADD  CONSTRAINT `FK59F8823FD6789E97` FOREIGN KEY (`FORECASTING_ID`) REFERENCES `forecasting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK59F8823F12F7EF28`,
DROP FOREIGN KEY `FK59F8823F2F370FE8`,
ADD  CONSTRAINT `FK59F8823FFB9ECED7` FOREIGN KEY (`REGIMEN_ID`) REFERENCES `regimen` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE ForecastingMedicine
ADD  CONSTRAINT `FKD756D341BC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FKD756D34112F7EF28`,
ADD  CONSTRAINT `FKD756D341D6789E97` FOREIGN KEY (`FORECASTING_ID`) REFERENCES `forecasting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FKD756D341FBB4E36C`;


ALTER TABLE ForecastingNewCases
ADD  CONSTRAINT `FK5D8BD28AD6789E97` FOREIGN KEY (`FORECASTING_ID`) REFERENCES `forecasting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK5D8BD28A12F7EF28`;


ALTER TABLE ForecastingOrder
ADD CONSTRAINT `FKAD707FA77AAD7377` FOREIGN KEY (`FORECASTINGMEDICINE_ID`) REFERENCES `forecastingmedicine` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FKAD707FA7259FA308`;


ALTER TABLE ForecastingRegimen
ADD CONSTRAINT `FK9BFEF57AD6789E97` FOREIGN KEY (`FORECASTING_ID`) REFERENCES `forecasting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK9BFEF57A12F7EF28`,
DROP FOREIGN KEY `FK9BFEF57A2F370FE8`,
ADD  CONSTRAINT `FK9BFEF57AFB9ECED7` FOREIGN KEY (`REGIMEN_ID`) REFERENCES `regimen` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE ForecastingResult
ADD  CONSTRAINT `FK50DDAE4BC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK50DDAE412F7EF28`,
ADD  CONSTRAINT `FK50DDAE4D6789E97` FOREIGN KEY (`FORECASTING_ID`) REFERENCES `forecasting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK50DDAE4FBB4E36C`;


ALTER TABLE HealthSystem
ADD  CONSTRAINT `FK28F57FCBB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK28F57FCBB9C757E8`;


ALTER TABLE Histology
ADD  CONSTRAINT `FKDE485CA2A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKDE485CA28B327BA`;



ALTER TABLE Laboratory
ADD  CONSTRAINT `FK2FD84BD3B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK2FD84BD321EFF75D` FOREIGN KEY (`HEALTHSYSTEM_ID`) REFERENCES `healthsystem` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK2FD84BD3755AB8EC`,
DROP FOREIGN KEY `FK2FD84BD38A1004CB`,
DROP FOREIGN KEY `FK2FD84BD3B9C757E8`,
ADD  CONSTRAINT `FK2FD84BD3CEDEEA7C` FOREIGN KEY (`ADMINUNIT_ID`) REFERENCES `administrativeunit` (`id`) ON DELETE CASCADE;



ALTER TABLE LogValue
DROP FOREIGN KEY `FK7B59CCAD99B1264C`;



ALTER TABLE MedicalExamination
ADD  CONSTRAINT `FK62CB08C0A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK62CB08C08B327BA`;


ALTER TABLE MedicalExaminationbd
DROP FOREIGN KEY `FKDC2BE1228B327BA`;


ALTER TABLE Medicine
ADD  CONSTRAINT `FKCE2ABA5AB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKCE2ABA5A271B370E` FOREIGN KEY (`GROUP_ID`) REFERENCES `productgroup` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKCE2ABA5A7A85F89D`,
DROP FOREIGN KEY `FKCE2ABA5AB9C757E8`;


ALTER TABLE MedicineComponent
ADD  CONSTRAINT `FKE31987C3BC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKE31987C396FA537` FOREIGN KEY (`SUBSTANCE_ID`) REFERENCES `substance` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKE31987C3B7FBE608`,
DROP FOREIGN KEY `FKE31987C3FBB4E36C`;


ALTER TABLE MedicineDispensing
ADD  CONSTRAINT `FK590328D471E04A4B` FOREIGN KEY (`UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK590328D4D6A3231A`;


ALTER TABLE MedicineDispensingBatch
ADD  CONSTRAINT `FK804922C63B281F77` FOREIGN KEY (`BATCH_ID`) REFERENCES `batch` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK804922C62005C06`,
ADD  CONSTRAINT `FK804922C6D4930137` FOREIGN KEY (`DISPENSINGITEM_ID`) REFERENCES `medicinedispensingitem` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK804922C6DB4F70C8`;


ALTER TABLE MedicineDispensingItem
ADD  CONSTRAINT `FK5EFD8E07BC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK5EFD8E071CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK5EFD8E074E21F23D` FOREIGN KEY (`MOVEMENT_ID`) REFERENCES `movement` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK5EFD8E0775C78AC6`,
DROP FOREIGN KEY `FK5EFD8E07817DE04C`,
DROP FOREIGN KEY `FK5EFD8E078D91D34C`,
ADD  CONSTRAINT `FK5EFD8E07BA967077` FOREIGN KEY (`DISPENSING_ID`) REFERENCES `medicinedispensing` (`id`)ON DELETE CASCADE,
DROP FOREIGN KEY `FK5EFD8E07FBB4E36C`;



ALTER TABLE MedicineOrder
ADD  CONSTRAINT `FK7F9C451464EE6730` FOREIGN KEY (`USER_CREATOR_ID`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK7F9C45141A4DE779` FOREIGN KEY (`UNIT_TO_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK7F9C4514B8652DB5` FOREIGN KEY (`AUTHORIZER_UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK7F9C4514CBAE4EAA` FOREIGN KEY (`UNIT_FROM_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKB7E1D341D280684`,
DROP FOREIGN KEY `FKB7E1D3430712779`,
DROP FOREIGN KEY `FKB7E1D347F10C048`,
DROP FOREIGN KEY `FKB7E1D3482DF27BF`;



ALTER TABLE MedicineReceiving
ADD  CONSTRAINT `FK603BCDA671E04A4B` FOREIGN KEY (`UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK603BCDA61CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK603BCDA6817DE04C`,
DROP FOREIGN KEY `FK603BCDA6D6A3231A`;


ALTER TABLE MedicineReceivingItem
ADD  CONSTRAINT `FK1CD379D94E21F23D` FOREIGN KEY (`MOVEMENT_ID`) REFERENCES `movement` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK1CD379D95E43ACEE`,
DROP FOREIGN KEY `FK1CD379D98D91D34C`,
ADD  CONSTRAINT `FK1CD379D9D418CD1D` FOREIGN KEY (`RECEIVING_ID`) REFERENCES `medicinereceiving` (`id`) ON DELETE CASCADE;


ALTER TABLE MedicineRegimen
ADD  CONSTRAINT `FK924F13A7BC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK924F13A71CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK924F13A72F370FE8`,
DROP FOREIGN KEY `FK924F13A7817DE04C`,
ADD  CONSTRAINT `FK924F13A7FB9ECED7` FOREIGN KEY (`REGIMEN_ID`) REFERENCES `regimen` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK924F13A7FBB4E36C`;


ALTER TABLE MedicineUnit
ADD  CONSTRAINT `FKEB5A4AFE71E04A4B` FOREIGN KEY (`UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKEB5A4AFE1CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKEB5A4AFE817DE04C`,
ADD  CONSTRAINT `FKEB5A4AFEBC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKEB5A4AFED6A3231A`,
DROP FOREIGN KEY `FKEB5A4AFEFBB4E36C`;

ALTER TABLE MolecularBiology
ADD  CONSTRAINT `FK1484EE8D43A836FD` FOREIGN KEY (`LABORATORY_ID`) REFERENCES `laboratory` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK1484EE8D66A4104C`;


ALTER TABLE Movement
ADD  CONSTRAINT `FKFDAC64CF71E04A4B` FOREIGN KEY (`UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKFDAC64CF1CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKFDAC64CF817DE04C`,
ADD  CONSTRAINT `FKFDAC64CFBC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKFDAC64CFD6A3231A`,
DROP FOREIGN KEY `FKFDAC64CFFBB4E36C`;


ALTER TABLE OrderBatch
ADD  CONSTRAINT `FKA2469CACA3A887B7` FOREIGN KEY (`ORDERITEM_ID`) REFERENCES `orderitem` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKA2469CAC3B281F77` FOREIGN KEY (`BATCH_ID`) REFERENCES `batch` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKA2469CAC5234C888`,
DROP FOREIGN KEY `FKA2469CACDB4F70C8`;


ALTER TABLE OrderCase
ADD  CONSTRAINT `FK60133F7EA3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK60133F7E5234C888`,
DROP FOREIGN KEY `FK60133F7E8B327BA`,
ADD  CONSTRAINT `FK60133F7EA3A887B7` FOREIGN KEY (`ORDERITEM_ID`) REFERENCES `orderitem` (`id`) ON DELETE CASCADE;




ALTER TABLE OrderItem
ADD  CONSTRAINT `FK60163F618F3F74CE` FOREIGN KEY (`MOVEMENT_OUT_ID`) REFERENCES `movement` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK60163F6116AEEE77` FOREIGN KEY (`MOVEMENT_IN_ID`) REFERENCES `movement` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK60163F611CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK60163F61561ECF86`,
DROP FOREIGN KEY `FK60163F61817DE04C`,
DROP FOREIGN KEY `FK60163F618A9A8848`,
ADD  CONSTRAINT `FK60163F61BC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK60163F61CEAF55DD`,
ADD  CONSTRAINT `FK60163F61EA7336F7` FOREIGN KEY (`ORDER_ID`) REFERENCES `medicineorder` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK60163F61FBB4E36C`;


ALTER TABLE Patient
ADD  CONSTRAINT `FK340C82E5B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK340C82E5B9C757E8`;




ALTER TABLE PhysicalExam
ADD  CONSTRAINT `FKB1DE1DB6C99F23F5` FOREIGN KEY (`EXAM_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE CASCADE;
ALTER TABLE PhysicalExam
DROP FOREIGN KEY `FKB1DE1DB6D3629284`;
ALTER TABLE PhysicalExam
DROP FOREIGN KEY `FKB1DE1DB6EC9AFD44`;
ALTER TABLE PhysicalExam
DROP FOREIGN KEY `FKB1DE1DB6F503B113`;

ALTER TABLE PhysicalExam
ADD  CONSTRAINT `FKB1DE1DB6F503B113` FOREIGN KEY (`CASEDATA_ID`) REFERENCES `casedataph` (`id`) ON DELETE CASCADE;


ALTER TABLE PrescribedMedicine
ADD  CONSTRAINT `FK85BFA211BC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK85BFA2111CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK85BFA211817DE04C`,
DROP FOREIGN KEY `FK85BFA2118B327BA`,
ADD  CONSTRAINT `FK85BFA211A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK85BFA211FBB4E36C`;


ALTER TABLE PrevTBTreatment
ADD  CONSTRAINT `FK988E3177A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK988E31778B327BA`;


ALTER TABLE ProductGroup
ADD  CONSTRAINT `FK44F14AF0B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK44F14AF0B9C757E8`;


ALTER TABLE Regimen
ADD  CONSTRAINT `FKA3F54741B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKA3F54741B9C757E8`;


ALTER TABLE RegimenUnit
ADD  CONSTRAINT `FK8DB60C6571E04A4B` FOREIGN KEY (`UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK8DB60C652F370FE8`,
DROP FOREIGN KEY `FK8DB60C65D6A3231A`,
ADD  CONSTRAINT `FK8DB60C65FB9ECED7` FOREIGN KEY (`REGIMEN_ID`) REFERENCES `regimen` (`id`) ON DELETE CASCADE;


ALTER TABLE RES_PREVTBTREATMENT
ADD  CONSTRAINT `FK7CFF03D896FA537` FOREIGN KEY (`SUBSTANCE_ID`) REFERENCES `substance` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK7CFF03D8380E5E08`,
DROP FOREIGN KEY `FK7CFF03D8B7FBE608`,
ADD  CONSTRAINT `FK7CFF03D8E3BBBDF7` FOREIGN KEY (`PREVTBTREATMENT_ID`) REFERENCES `prevtbtreatment` (`id`) ON DELETE CASCADE;


ALTER TABLE ResistancePattern
ADD  CONSTRAINT `FK606E68F7B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK606E68F7B9C757E8`;



ALTER TABLE SequenceInfo
ADD CONSTRAINT `FK39EADC2FB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK39EADC2FB9C757E8`;


ALTER TABLE Source
ADD  CONSTRAINT `FK93F5543BB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK93F5543BB9C757E8`;


ALTER TABLE StockPosition
ADD  CONSTRAINT `FK191AE3F71E04A4B` FOREIGN KEY (`UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK191AE3F1CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK191AE3F817DE04C`,
ADD  CONSTRAINT `FK191AE3FBC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK191AE3FD6A3231A`,
DROP FOREIGN KEY `FK191AE3FFBB4E36C`;



ALTER TABLE Substance
ADD  CONSTRAINT `FK9709E550B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK9709E550B9C757E8`;


ALTER TABLE `substances_resistpattern`
ADD  CONSTRAINT `FKA0BA320A489C06B7` FOREIGN KEY (`ResistancePattern_id`) REFERENCES `resistancepattern` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKA0BA320A93BEB595`,
DROP FOREIGN KEY `FKA0BA320AD2C6E688`,
ADD  CONSTRAINT `FKA0BA320AE53274C4` FOREIGN KEY (`substances_id`) REFERENCES `substance` (`id`) ON DELETE CASCADE;



ALTER TABLE Sys_User
ADD  CONSTRAINT `FK7873F63DEE97D5CD` FOREIGN KEY (`DEFAULTWORKSPACE_ID`) REFERENCES `userworkspace` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK7873F63D74417A53` FOREIGN KEY (`PARENTUSER_ID`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FK7873F63D885461E`,
DROP FOREIGN KEY `FK7873F63D8A1004CB`,
DROP FOREIGN KEY `FK7873F63D92323AE2`;


ALTER TABLE SystemConfig
ADD  CONSTRAINT `FKC53396716515C41D` FOREIGN KEY (`TBUNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FKC53396712D576F17` FOREIGN KEY (`USERPROFILE_ID`) REFERENCES `userprofile` (`id`) ON DELETE SET NULL,
DROP FOREIGN KEY `FKC533967169D6BFA8`,
ADD  CONSTRAINT `FKC5339671B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKC5339671B9C757E8`,
DROP FOREIGN KEY `FKC5339671C9D89CEC`;


ALTER TABLE SystemParam
ADD  CONSTRAINT `FK70D637EB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK70D637EB9C757E8`;


ALTER TABLE Tag
ADD  CONSTRAINT `FK1477AB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK1477AB9C757E8`;

ALTER TABLE TAGS_CASE
ADD  CONSTRAINT `FK4577A796C1409EB7` FOREIGN KEY (`TAG_ID`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
DROP FOREIGN KEY `FK4577A7968B327BA`,
DROP FOREIGN KEY `FK4577A796DAFE1048`;


ALTER TABLE TbCase
ADD  CONSTRAINT `FK94DC02DE3352AF69` FOREIGN KEY (`CURR_ADMINUNIT_ID`) REFERENCES `administrativeunit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD  CONSTRAINT `FK94DC02DE246C43F1` FOREIGN KEY (`PULMONARY_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
  DROP FOREIGN KEY `FK94DC02DE2F370FE8`,
ADD  CONSTRAINT `FK94DC02DE45C57184` FOREIGN KEY (`TREATMENT_UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  DROP FOREIGN KEY  `FK94DC02DE47681D40`,
ADD  CONSTRAINT `FK94DC02DE5FA64001` FOREIGN KEY (`EXTRAPULMONARY_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK94DC02DE7DB45EF7` FOREIGN KEY (`NOTIFICATION_UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  DROP FOREIGN KEY  `FK94DC02DE82A21950`,
ADD  CONSTRAINT `FK94DC02DE8B5B7C57` FOREIGN KEY (`PATIENT_ID`) REFERENCES `patient` (`id`) ON DELETE CASCADE,
  DROP FOREIGN KEY  `FK94DC02DE8C7B4D7A`,
  DROP FOREIGN KEY  `FK94DC02DEAA884A53`,
ADD  CONSTRAINT `FK94DC02DEB6B77515` FOREIGN KEY (`EXTRAPULMONARY2_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE SET NULL,
  DROP FOREIGN KEY  `FK94DC02DEBEF3BD68`,
ADD  CONSTRAINT `FK94DC02DED14A332B` FOREIGN KEY (`NOTIF_ADMINUNIT_ID`) REFERENCES `administrativeunit` (`id`) ON DELETE CASCADE,
  DROP FOREIGN KEY  `FK94DC02DED9B34E64`,
  DROP FOREIGN KEY  `FK94DC02DEE27737C6`,
  DROP FOREIGN KEY  `FK94DC02DEEE83C9B8`,
ADD  CONSTRAINT `FK94DC02DEFB9ECED7` FOREIGN KEY (`REGIMEN_ID`) REFERENCES `regimen` (`id`) ON DELETE SET NULL;



ALTER TABLE TbContact
ADD    CONSTRAINT `FK69755B2F48DE4B0` FOREIGN KEY (`CONDUCT_ID`) REFERENCES `fieldvalue` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK69755B21789BDFF`;


ALTER TABLE TbContactBr
ADD  CONSTRAINT `FKBE18B982FA32AC6A` FOREIGN KEY (`id`) REFERENCES `tbcontact` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKBE18B982A8BEED3B`;


ALTER TABLE TbUnit
ADD  CONSTRAINT `FK94F2ED12E2EE17E8` FOREIGN KEY (`FIRSTLINE_SUPPLIER_ID`) REFERENCES `tbunit` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK94F2ED1221EFF75D` FOREIGN KEY (`HEALTHSYSTEM_ID`) REFERENCES `healthsystem` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK94F2ED1247B0F0B7`,
DROP FOREIGN KEY `FK94F2ED12755AB8EC`,
DROP FOREIGN KEY `FK94F2ED128A1004CB`,
ADD  CONSTRAINT `FK94F2ED128C2681AC` FOREIGN KEY (`SECONDLINE_SUPPLIER_ID`) REFERENCES `tbunit` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK94F2ED12A96C5482` FOREIGN KEY (`AUTHORIZERUNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE SET NULL,
ADD  CONSTRAINT `FK94F2ED12B3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK94F2ED12B9C757E8`,
ADD  CONSTRAINT `FK94F2ED12CEDEEA7C` FOREIGN KEY (`ADMINUNIT_ID`) REFERENCES `administrativeunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK94F2ED12E2F2D51` ,
DROP FOREIGN KEY `FK94F2ED12F0E95A7B`;




ALTER TABLE TransactionLog
DROP FOREIGN KEY `FK82CFA65B56462C`,
ADD  CONSTRAINT `FK82CFA6B93F8B48` FOREIGN KEY (`ROLE_ID`) REFERENCES `userrole` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK82CFA6E97A0CC8`,
DROP FOREIGN KEY `FK82CFA6F8AF6C57`;



ALTER TABLE Transfer
ADD  CONSTRAINT `FK50331C0BFAA5A18A` FOREIGN KEY (`USER_FROM_ID`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK50331C0B18966219`,
ADD  CONSTRAINT `FK50331C0B19ADE639` FOREIGN KEY (`UNIT_ID_TO`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK50331C0B36816019` FOREIGN KEY (`USER_TO_ID`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK50331C0B5198C928` FOREIGN KEY (`UNIT_ID_FROM`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK50331C0B547220A8`,
DROP FOREIGN KEY `FK50331C0B7E70BF08`,
DROP FOREIGN KEY `FK50331C0BB65BA1F7`;


ALTER TABLE TransferBatch
ADD  CONSTRAINT `FKB5F399EF3B281F77` FOREIGN KEY (`BATCH_ID`) REFERENCES `batch` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKB5F399EF12FB980C`,
ADD  CONSTRAINT `FKB5F399EFBF90D67D` FOREIGN KEY (`TRANSFERITEM_ID`) REFERENCES `transferitem` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKB5F399EFDB4F70C8`;


ALTER TABLE TransferItem
ADD  CONSTRAINT `FK8A030DBE51FC2C9` FOREIGN KEY (`MOV_OUT_ID`) REFERENCES `movement` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK8A030DBE1CBB077D` FOREIGN KEY (`SOURCE_ID`) REFERENCES `source` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK8A030DBE448FA3D8`,
ADD  CONSTRAINT `FK8A030DBE4BADD8BD` FOREIGN KEY (`TRANSFER_ID`) REFERENCES `transfer` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK8A030DBE817DE04C`,
DROP FOREIGN KEY `FK8A030DBE8B1DB9CC`,
ADD  CONSTRAINT `FK8A030DBEA6DF751C` FOREIGN KEY (`MOV_IN_ID`) REFERENCES `movement` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FK8A030DBEBC45025D` FOREIGN KEY (`MEDICINE_ID`) REFERENCES `medicine` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK8A030DBEE64F562B`,
DROP FOREIGN KEY `FK8A030DBEFBB4E36C`;


ALTER TABLE TreatmentHealthUnit
ADD  CONSTRAINT `FKBF99A87871E04A4B` FOREIGN KEY (`UNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKBF99A8788B327BA`,
ADD  CONSTRAINT `FKBF99A878A3F04EEB` FOREIGN KEY (`CASE_ID`) REFERENCES `tbcase` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKBF99A878D6A3231A`;


ALTER TABLE UserLogin
ADD  CONSTRAINT `FK8AA0DA3EB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK8AA0DA3EB9C757E8`,
ADD  CONSTRAINT `FK8AA0DA3EBA5DB63D` FOREIGN KEY (`USER_ID`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK8AA0DA3ED84E76CC`;



ALTER TABLE UserPermission
ADD  CONSTRAINT `FKD265581A7730850C` FOREIGN KEY (`PROFILE_ID`) REFERENCES `userprofile` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKD265581AB3AFD59D`,
ADD  CONSTRAINT `FKD265581AB93F8B48` FOREIGN KEY (`ROLE_ID`) REFERENCES `userrole` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKD265581AF8AF6C57`;



ALTER TABLE UserProfile
ADD  CONSTRAINT `FK3EFA133EB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FK3EFA133EB9C757E8`;


ALTER TABLE UserWorkspace
ADD  CONSTRAINT `FKE6C764EA7730850C` FOREIGN KEY (`PROFILE_ID`) REFERENCES `userprofile` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKE6C764EA21EFF75D` FOREIGN KEY (`HEALTHSYSTEM_ID`) REFERENCES `healthsystem` (`id`) ON DELETE CASCADE,
ADD  CONSTRAINT `FKE6C764EA6515C41D` FOREIGN KEY (`TBUNIT_ID`) REFERENCES `tbunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKE6C764EA755AB8EC`,
DROP FOREIGN KEY `FKE6C764EA8A1004CB`,
DROP FOREIGN KEY `FKE6C764EAB3AFD59D`,
ADD  CONSTRAINT `FKE6C764EAB3B1717` FOREIGN KEY (`WORKSPACE_ID`) REFERENCES `workspace` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKE6C764EAB9C757E8`,
ADD  CONSTRAINT `FKE6C764EABA5DB63D` FOREIGN KEY (`USER_ID`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKE6C764EAC9D89CEC`,
ADD  CONSTRAINT `FKE6C764EACEDEEA7C` FOREIGN KEY (`ADMINUNIT_ID`) REFERENCES `administrativeunit` (`id`) ON DELETE CASCADE,
DROP FOREIGN KEY `FKE6C764EAD84E76CC`;

delete from CaseIssue where user_id not in (select id from sys_user);
alter table CaseIssue add constraint FK9E3BDB69BA5DB63D foreign key (USER_ID) references Sys_User (id) on delete cascade;

delete from CaseIssue where unit_id not in (select id from Tbunit);
alter table CaseIssue add constraint FK9E3BDB6971E04A4B foreign key (UNIT_ID) references Tbunit (id) on delete cascade;

delete from MolecularBiology where case_id not in (select id from TbCase);
alter table MolecularBiology add constraint FK1484EE8DA3F04EEB foreign key (CASE_ID) references TbCase (id) on delete cascade;

delete from TbContact where case_id not in (select id from TbCase);
alter table TbContact add constraint FK69755B2A3F04EEB foreign key (CASE_ID) references TbCase (id) on delete cascade;

alter table TbContact add constraint FK69755B2677F2ADA foreign key (CONTACTTYPE_ID) references FieldValue (id) on delete cascade;