mvn spring-boot:run -DskipTestspackage com.mycompany.microservice.service.mapper;

import static com.mycompany.microservice.domain.FamilyAllergyAsserts.*;
import static com.mycompany.microservice.domain.FamilyAllergyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FamilyAllergyMapperTest {

    private FamilyAllergyMapper familyAllergyMapper;

    @BeforeEach
    void setUp() {
        familyAllergyMapper = new FamilyAllergyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFamilyAllergySample1();
        var actual = familyAllergyMapper.toEntity(familyAllergyMapper.toDto(expected));
        assertFamilyAllergyAllPropertiesEquals(expected, actual);
    }
}
