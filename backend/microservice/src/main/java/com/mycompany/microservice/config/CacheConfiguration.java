package com.mycompany.microservice.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName());
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName() + ".allergies");
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName() + ".disabilities");
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName() + ".surgeryHistories");
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName() + ".familyAllergies");
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName() + ".familyDiseases");
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName() + ".vaccinationsTCMRS");
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName() + ".pregnancyTetanuses");
            createCache(cm, com.mycompany.microservice.domain.Patient.class.getName() + ".medicalRecords");
            createCache(cm, com.mycompany.microservice.domain.AdditionalInformation.class.getName());
            createCache(cm, com.mycompany.microservice.domain.Allergy.class.getName());
            createCache(cm, com.mycompany.microservice.domain.Disability.class.getName());
            createCache(cm, com.mycompany.microservice.domain.SurgeryHistory.class.getName());
            createCache(cm, com.mycompany.microservice.domain.FamilyAllergy.class.getName());
            createCache(cm, com.mycompany.microservice.domain.Disease.class.getName());
            createCache(cm, com.mycompany.microservice.domain.FamilyDisease.class.getName());
            createCache(cm, com.mycompany.microservice.domain.FamilyDisease.class.getName() + ".diseases");
            createCache(cm, com.mycompany.microservice.domain.Vaccine.class.getName());
            createCache(cm, com.mycompany.microservice.domain.VaccinationForBaby.class.getName());
            createCache(cm, com.mycompany.microservice.domain.VaccinationForBaby.class.getName() + ".vaccines");
            createCache(cm, com.mycompany.microservice.domain.VaccinationTCMR.class.getName());
            createCache(cm, com.mycompany.microservice.domain.PregnancyTetanus.class.getName());
            createCache(cm, com.mycompany.microservice.domain.MedicalRecord.class.getName());
            createCache(cm, com.mycompany.microservice.domain.MedicalRecord.class.getName() + ".paraclinicalResults");
            createCache(cm, com.mycompany.microservice.domain.OrganExamination.class.getName());
            createCache(cm, com.mycompany.microservice.domain.ParaclinicalResult.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
