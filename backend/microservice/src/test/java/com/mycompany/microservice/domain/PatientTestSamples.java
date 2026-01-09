package com.mycompany.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PatientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Patient getPatientSample1() {
        return new Patient()
            .id(1L)
            .fullName("fullName1")
            .placeOfBirth("placeOfBirth1")
            .bloodTypeAbo("bloodTypeAbo1")
            .bloodTypeRh("bloodTypeRh1")
            .ethnic("ethnic1")
            .nationality("nationality1")
            .religion("religion1")
            .job("job1")
            .idNumber("idNumber1")
            .idIssuePlace("idIssuePlace1")
            .healthInsuranceNumber("healthInsuranceNumber1")
            .permanentAddress("permanentAddress1")
            .permanentWard("permanentWard1")
            .permanentDistrict("permanentDistrict1")
            .permanentProvince("permanentProvince1")
            .currentAddress("currentAddress1")
            .currentWard("currentWard1")
            .currentDistrict("currentDistrict1")
            .currentProvince("currentProvince1")
            .landlinePhone("landlinePhone1")
            .mobilePhone("mobilePhone1")
            .email("email1")
            .motherName("motherName1")
            .fatherName("fatherName1")
            .caregiverName("caregiverName1")
            .caregiverRelation("caregiverRelation1")
            .caregiverLandlinePhone("caregiverLandlinePhone1")
            .caregiverMobilePhone("caregiverMobilePhone1")
            .familyCode("familyCode1");
    }

    public static Patient getPatientSample2() {
        return new Patient()
            .id(2L)
            .fullName("fullName2")
            .placeOfBirth("placeOfBirth2")
            .bloodTypeAbo("bloodTypeAbo2")
            .bloodTypeRh("bloodTypeRh2")
            .ethnic("ethnic2")
            .nationality("nationality2")
            .religion("religion2")
            .job("job2")
            .idNumber("idNumber2")
            .idIssuePlace("idIssuePlace2")
            .healthInsuranceNumber("healthInsuranceNumber2")
            .permanentAddress("permanentAddress2")
            .permanentWard("permanentWard2")
            .permanentDistrict("permanentDistrict2")
            .permanentProvince("permanentProvince2")
            .currentAddress("currentAddress2")
            .currentWard("currentWard2")
            .currentDistrict("currentDistrict2")
            .currentProvince("currentProvince2")
            .landlinePhone("landlinePhone2")
            .mobilePhone("mobilePhone2")
            .email("email2")
            .motherName("motherName2")
            .fatherName("fatherName2")
            .caregiverName("caregiverName2")
            .caregiverRelation("caregiverRelation2")
            .caregiverLandlinePhone("caregiverLandlinePhone2")
            .caregiverMobilePhone("caregiverMobilePhone2")
            .familyCode("familyCode2");
    }

    public static Patient getPatientRandomSampleGenerator() {
        return new Patient()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .placeOfBirth(UUID.randomUUID().toString())
            .bloodTypeAbo(UUID.randomUUID().toString())
            .bloodTypeRh(UUID.randomUUID().toString())
            .ethnic(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .religion(UUID.randomUUID().toString())
            .job(UUID.randomUUID().toString())
            .idNumber(UUID.randomUUID().toString())
            .idIssuePlace(UUID.randomUUID().toString())
            .healthInsuranceNumber(UUID.randomUUID().toString())
            .permanentAddress(UUID.randomUUID().toString())
            .permanentWard(UUID.randomUUID().toString())
            .permanentDistrict(UUID.randomUUID().toString())
            .permanentProvince(UUID.randomUUID().toString())
            .currentAddress(UUID.randomUUID().toString())
            .currentWard(UUID.randomUUID().toString())
            .currentDistrict(UUID.randomUUID().toString())
            .currentProvince(UUID.randomUUID().toString())
            .landlinePhone(UUID.randomUUID().toString())
            .mobilePhone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .motherName(UUID.randomUUID().toString())
            .fatherName(UUID.randomUUID().toString())
            .caregiverName(UUID.randomUUID().toString())
            .caregiverRelation(UUID.randomUUID().toString())
            .caregiverLandlinePhone(UUID.randomUUID().toString())
            .caregiverMobilePhone(UUID.randomUUID().toString())
            .familyCode(UUID.randomUUID().toString());
    }
}
